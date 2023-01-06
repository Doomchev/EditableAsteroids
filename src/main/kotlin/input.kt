import mod.Element
import java.awt.Graphics2D
import java.awt.event.*
import java.util.*
import kotlin.math.abs

abstract class Pushable(val project: Project): Element {
  init {
    buttons.add(this)
  }

  open fun correspondsTo(e: MouseEvent): Boolean = false
  open fun correspondsTo(e: MouseWheelEvent): Boolean = false
  open fun correspondsTo(e: KeyEvent): Boolean = false

  class DraggingEntry(val canvas: Canvas, val action: DraggingAction)

  val draggingActions = mutableListOf<DraggingEntry>()
  fun add(canvas: Canvas, action: DraggingAction) {
    draggingActions.add(DraggingEntry(canvas, action))
  }

  val onClickActions = mutableListOf<ActionEntry>()
  fun addOnClick(canvas: Canvas, action: Action) {
    onClickActions.add(ActionEntry(canvas, action))
  }

  val onPressActions = mutableListOf<ActionEntry>()
  fun addOnPress(canvas: Canvas, action: Action) {
    onPressActions.add(ActionEntry(canvas, action))
  }

  val onUnpressActions = mutableListOf<ActionEntry>()
  fun addOnUnpress(canvas: Canvas, action: Action) {
    onUnpressActions.add(ActionEntry(canvas, action))
  }
}

object keySerializer: ElementSerializer {
  override fun fromNode(node: Node): Element {
    val key = Key(node.getInt("code"), user)
    node.getField("onClick", key.onClickActions)
    node.getField("onPress", key.onPressActions)
    node.getField("onUnpress", key.onUnpressActions)
    return key
  }
}

val buttons = mutableListOf<Pushable>()
class Key(private var code: Int, project: Project): Pushable(project) {
  override fun correspondsTo(e: KeyEvent): Boolean {
    return e.keyChar.code == code || e.keyCode == code
  }

  override fun toNode(node: Node) {
    node.setInt("code", code)
    node.setField("onClick", onClickActions)
    node.setField("onPress", onPressActions)
    node.setField("onUnpress", onUnpressActions)
  }

  override fun toString(): String {
    return "клавишу ${Char(code)} ($code)"
  }
}

object mouseButtonSerializer: ElementSerializer {
  override fun fromNode(node: Node): Element {
    val key = Key(node.getInt("code"), user)
    node.getField("onClick", key.onClickActions)
    node.getField("onPress", key.onPressActions)
    node.getField("onUnpress", key.onPressActions)
    return key
  }
}

class MouseButton(private var button: Int, project: Project): Pushable(project) {
  override fun correspondsTo(e: MouseEvent): Boolean {
    return e.button == button
  }

  override fun toNode(node: Node) {
    node.setInt("button", button)
  }

  override fun toString(): String {
    return "кнопку мыши $button"
  }
}

object mouseWheelUpSerializer: ElementSerializer {
  override fun fromNode(node: Node): Element = mouseWheelUp(user)
}

class mouseWheelUp(project: Project): Pushable(project) {
  override fun correspondsTo(e: MouseWheelEvent): Boolean {
    return e.wheelRotation < 0
  }

  override fun toNode(node: Node) {
  }

  override fun toString(): String {
    return "колесо вверх"
  }
}

object mouseWheelDownSerializer: ElementSerializer {
  override fun fromNode(node: Node): Element = mouseWheelDown(user)
}

class mouseWheelDown(project: Project): Pushable(project) {
  override fun correspondsTo(e: MouseWheelEvent): Boolean {
    return e.wheelRotation > 0
  }

  override fun toNode(node: Node) {
  }

  override fun toString(): String {
    return "колесо вниз"
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
      onClick(button.onClickActions)
    }
  }

  private fun onClick(entries: MutableList<ActionEntry>) {
    for(entry in entries) {
      if(!entry.canvas.active || !entry.canvas.hasMouse()) continue
      currentCanvas = entry.canvas
      if(!entry.action.conditions()) continue
      entry.action.execute()
    }
  }

  private const val minDraggingDistance = 3
  private var pressedEvent: MouseEvent? = null

  override fun mousePressed(e: MouseEvent) {
    updateMouse(e.x, e.y)
    pressedEvent = e
  }

  private var currentDraggingAction: DraggingAction? = null
  private var currentDraggingCanvas: Canvas? = null

  override fun mouseDragged(e: MouseEvent) {
    updateMouse(e.x, e.y)
    if(currentDraggingAction == null) {
      if(abs(e.x - pressedEvent!!.x) < minDraggingDistance
        && abs(e.y - pressedEvent!!.y) < minDraggingDistance) {
        return
      }

      for(button in buttons) {
        if(!button.correspondsTo(pressedEvent!!)) continue
        onDragStart(button.draggingActions)
      }
      return
    }

    currentCanvas = currentDraggingCanvas!!
    currentDraggingAction!!.dragged()
  }

  private fun onDragStart(entries: MutableList<Pushable.DraggingEntry>) {
    for(entry in entries) {
      if(!entry.canvas.hasMouse()) continue
      currentCanvas = entry.canvas
      updateMouse(pressedEvent!!.x, pressedEvent!!.y)
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
        onClick(button.onClickActions)
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
    updateMouse(e.x, e.y)
    if(currentDraggingAction == null) return
    currentCanvas = currentDraggingCanvas!!
    currentDraggingAction!!.dragged()
  }

  override fun mouseWheelMoved(e: MouseWheelEvent) {
    for(wheel in buttons) {
      if(!wheel.correspondsTo(e)) continue
      onClick(wheel.onClickActions)
    }
  }

  override fun keyTyped(e: KeyEvent) {
    if(e.keyChar.code == 96) {
      world.toggle()
      assets.toggle()
      properties.toggle()
      return
    }

    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      onClick(key.onClickActions)
    }
  }

  private val keysPressed = mutableListOf<KeyEntry>()
  class KeyEntry(val key: Pushable, val canvas: Canvas, var remove: Boolean = false)

  override fun keyPressed(e: KeyEvent) {
    for(keyEntry in keysPressed) {
      if(keyEntry.key.correspondsTo(e)) return
    }

    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      for(entry in key.onPressActions) {
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
        for(actionEntry in entry.key.onPressActions) {
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
    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      for(entry in key.onUnpressActions) {
        if(!entry.canvas.active || !entry.canvas.hasMouse()) continue
        currentCanvas = entry.canvas
        if(!entry.action.conditions()) continue
        entry.action.execute()
      }
    }
    for(keyEntry in keysPressed) {
      if(!keyEntry.key.correspondsTo(e)) continue
      keyEntry.remove = true
    }
    if(currentDraggingAction == null) return
    currentCanvas = currentDraggingCanvas!!
    currentDraggingAction!!.released()
    currentDraggingAction = null
  }

  fun draw(g:Graphics2D, canvas: Canvas) {
    if(currentDraggingCanvas == canvas && currentDraggingAction != null) {
      currentDraggingAction!!.drawWhileDragging(g)
    }
  }
}
