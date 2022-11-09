import java.awt.Graphics2D
import java.awt.event.*
import java.util.*
import kotlin.math.abs

abstract class Pushable {
  init {
    buttons.add(this)
  }

  open fun correspondsTo(e: MouseEvent): Boolean = false
  open fun correspondsTo(e: MouseWheelEvent): Boolean = false
  open fun correspondsTo(e: KeyEvent): Boolean = false

  class DraggingEntry(val canvas: Canvas, val action: DraggingAction)
  val draggingActions = LinkedList<DraggingEntry>()
  fun add(canvas: Canvas, action: DraggingAction) {
    draggingActions.add(DraggingEntry(canvas, action))
  }

  class ActionEntry(val canvas: Canvas, val action: Action)
  val actions = LinkedList<ActionEntry>()
  fun add(canvas: Canvas, action: Action) {
    actions.add(ActionEntry(canvas, action))
  }
}

val buttons = LinkedList<Pushable>()
class Key(var code: Int): Pushable() {
  override fun correspondsTo(e: KeyEvent): Boolean {
    return e.keyChar.code == code
  }
}

class MouseButton(var button: Int): Pushable() {
  override fun correspondsTo(e: MouseEvent): Boolean {
    return e.button == button
  }
}

object mouseWheelUp: Pushable() {
  override fun correspondsTo(e: MouseWheelEvent): Boolean {
    return e.wheelRotation < 0
  }
}

object mouseWheelDown: Pushable() {
  override fun correspondsTo(e: MouseWheelEvent): Boolean {
    return e.wheelRotation > 0
  }
}

var mousefx = 0.0
var mousefy = 0.0
var mousesx = 0
var mousesy = 0

object listener: MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
  private fun updateMouse(x: Int, y: Int) {
    mousesx = x
    mousesy = y
    mousefx = xFromScreen(mousesx)
    mousefy = yFromScreen(mousesy)
  }

  override fun mouseClicked(e: MouseEvent) {
    updateMouse(e.x, e.y)
    for(button in buttons) {
      if(!button.correspondsTo(e)) continue
      onClick(button.actions)
    }
  }

  private fun onClick(entries: LinkedList<Pushable.ActionEntry>) {
    for(entry in entries) {
      if(!entry.canvas.hasMouse()) continue
      currentCanvas = entry.canvas
      if(!entry.action.conditions()) continue
      entry.action.execute()
    }
  }

  val minDraggingDistance = 3
  var pressedEvent: MouseEvent? = null

  override fun mousePressed(e: MouseEvent) {
    pressedEvent = e
  }

  var currentDraggingAction: DraggingAction? = null
  var currentDraggingCanvas: Canvas? = null

  override fun mouseDragged(e: MouseEvent) {
    updateMouse(e.x, e.y)
    if(currentDraggingAction == null) {
      if(abs(e.x - pressedEvent!!.x) < minDraggingDistance
        && abs(e.y - pressedEvent!!.y) < minDraggingDistance) {
        return
      }

      updateMouse(pressedEvent!!.x, pressedEvent!!.y)

      for(button in buttons) {
        if(!button.correspondsTo(pressedEvent!!)) continue
        onDragStart(button.draggingActions)
      }
      return
    }

    currentCanvas = currentDraggingCanvas!!
    currentDraggingAction!!.dragged()
  }

  private fun onDragStart(entries: LinkedList<Pushable.DraggingEntry>) {
    for(entry in entries) {
      if(!entry.canvas.hasMouse()) continue
      currentCanvas = entry.canvas
      if(!entry.action.conditions()) continue
      currentDraggingCanvas = entry.canvas
      currentDraggingAction = entry.action
      entry.action.pressed()
      return
    }
  }

  override fun mouseReleased(e: MouseEvent) {
    updateMouse(e.x, e.y)
    if(currentDraggingAction == null) {
      for(button in buttons) {
        if(!button.correspondsTo(e)) continue
        onClick(button.actions)
        break
      }
      return
    }

    currentCanvas = currentDraggingCanvas!!
    currentDraggingAction!!.released()
    currentDraggingAction = null
  }

  override fun mouseEntered(e: MouseEvent) {
  }

  override fun mouseExited(e: MouseEvent) {
  }

  override fun mouseMoved(e: MouseEvent) {
    if(currentDraggingAction == null) return
    updateMouse(e.x, e.y)
    currentCanvas = currentDraggingCanvas!!
    currentDraggingAction?.dragged()
  }

  override fun mouseWheelMoved(e: MouseWheelEvent) {
    for(wheel in buttons) {
      if(!wheel.correspondsTo(e)) continue
      onClick(wheel.actions)
    }
  }

  override fun keyTyped(e: KeyEvent) {
    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      onClick(key.actions)
    }
  }

  val keysPressed = LinkedList<KeyEntry>()
  class KeyEntry(val key: Pushable, val canvas: Canvas, var remove: Boolean = false)

  override fun keyPressed(e: KeyEvent) {
    for(keyEntry in keysPressed) {
      if(keyEntry.key.correspondsTo(e)) return
    }

    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      for(entry in key.actions) {
        if(!entry.canvas.hasMouse()) continue
        keysPressed.add(KeyEntry(key, entry.canvas))
      }
      onDragStart(key.draggingActions)
    }
  }

  fun onKeyDown() {
    val it = keysPressed.iterator()
    while(it.hasNext()) {
      val entry = it.next()
      if(entry.remove) {
        it.remove()
      } else {
        for(actionEntry in entry.key.actions) {
          if(!actionEntry.action.conditions()) {
            it.remove()
            continue
          }
          actionEntry.action.execute()
        }
      }
    }
  }

  override fun keyReleased(e: KeyEvent) {
    for(keyEntry in keysPressed) {
      if(!keyEntry.key.correspondsTo(e)) continue
      keyEntry.remove = true
    }
    if(currentDraggingCanvas == null) return
    currentCanvas = currentDraggingCanvas!!
    currentDraggingAction?.released()
    currentDraggingAction = null
  }

  fun draw(g:Graphics2D, canvas: Canvas) {
    if(currentDraggingCanvas == canvas && currentDraggingAction != null) {
      currentDraggingAction!!.drawWhileDragging(g)
    }
  }
}
