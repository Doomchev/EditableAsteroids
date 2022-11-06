import java.awt.MouseInfo
import java.awt.event.*
import java.util.*

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

object listener: MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
  override fun mouseClicked(e: MouseEvent) {
    for(button in buttons) {
      if(!button.correspondsTo(e)) continue
      for(entry in button.actions) {
        if(!entry.canvas.hasPoint(e.x, e.y)) continue
        currentCanvas = entry.canvas
        val fx = xFromScreen(e.x)
        val fy = yFromScreen(e.y)
        if(!entry.action.conditions(fx, fy)) continue
        entry.action.execute(fx, fy)
      }
    }
  }

  override fun mousePressed(e: MouseEvent) {
    for(button in buttons) {
      if(!button.correspondsTo(e)) continue
      for(entry in button.draggingActions) {
        if(!entry.canvas.hasPoint(e.x, e.y)) continue
        currentCanvas = entry.canvas
        val fx = xFromScreen(e.x)
        val fy = yFromScreen(e.y)
        if(!entry.action.conditions(fx, fy)) continue
        currentDraggingCanvas = entry.canvas
        currentDraggingAction = entry.action
        entry.action.pressed(fx, fy)
        return
      }
    }
  }

  override fun mouseDragged(e: MouseEvent) {
    if(currentDraggingCanvas == null) return
    currentCanvas = currentDraggingCanvas!!
    val fx = xFromScreen(e.x)
    val fy = yFromScreen(e.y)
    currentDraggingAction?.dragged(fx, fy)
  }

  override fun mouseReleased(e: MouseEvent) {
    if(currentDraggingCanvas == null) return
    currentCanvas = currentDraggingCanvas!!
    val fx = xFromScreen(e.x)
    val fy = yFromScreen(e.y)
    currentDraggingAction?.released(fx, fy)
    currentDraggingAction = null
  }

  override fun mouseEntered(e: MouseEvent) {
  }

  override fun mouseExited(e: MouseEvent) {
  }

  override fun mouseMoved(e: MouseEvent) {
    if(currentDraggingCanvas == null) return
    currentCanvas = currentDraggingCanvas!!
    val fx = xFromScreen(e.x)
    val fy = yFromScreen(e.y)
    currentDraggingAction?.dragged(fx, fy)
  }

  override fun mouseWheelMoved(e: MouseWheelEvent) {
    for(wheel in buttons) {
      if(!wheel.correspondsTo(e)) continue
      for(entry in wheel.actions) {
        if(!entry.canvas.hasPoint(e.x, e.y)) continue
        currentCanvas = entry.canvas
        val fx = xFromScreen(e.x)
        val fy = yFromScreen(e.y)
        if(!entry.action.conditions(fx, fy)) continue
        entry.action.execute(fx, fy)
      }
    }
  }

  override fun keyTyped(e: KeyEvent) {
    val point = MouseInfo.getPointerInfo().location
    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      for(entry in key.actions) {
        if(!entry.canvas.hasPoint(point.x, point.y)) continue
        currentCanvas = entry.canvas
        val fx = xFromScreen(point.x)
        val fy = yFromScreen(point.y)
        if(!entry.action.conditions(fx, fy)) continue
        entry.action.execute(fx, fy)
      }
    }
  }

  var keyPressed: Pushable? = null

  override fun keyPressed(e: KeyEvent) {
    val point = MouseInfo.getPointerInfo().location
    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      keyPressed = key
      for(entry in key.draggingActions) {
        if(!entry.canvas.hasPoint(point.x, point.y)) continue
        currentCanvas = entry.canvas
        val fx = xFromScreen(point.x)
        val fy = yFromScreen(point.y)
        if(!entry.action.conditions(fx, fy)) continue
        currentDraggingAction = entry.action
        currentDraggingCanvas = entry.canvas
        entry.action.pressed(fx, fy)
      }
    }
  }

  fun onKeyDown() {
    if(keyPressed == null) return
    for(entry in keyPressed!!.actions) {
      entry.action.onButtonDown()
    }
  }

  override fun keyReleased(e: KeyEvent) {
    keyPressed = null
    if(currentDraggingCanvas == null) return
    currentCanvas = currentDraggingCanvas!!
    val point = MouseInfo.getPointerInfo().location
    val fx = xFromScreen(point.x)
    val fy = yFromScreen(point.y)
    currentDraggingAction?.released(fx, fy)
    currentDraggingAction = null
  }
}
