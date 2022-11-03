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
    return e.keyCode == code || e.keyChar.code == code
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
        val sx = e.x - entry.canvas.viewport.leftX
        val sy = e.y - entry.canvas.viewport.topY
        if(!entry.action.conditions(sx, sy)) continue
        currentCanvas = entry.canvas
        entry.action.execute(sx, sy)
      }
    }
  }

  override fun mousePressed(e: MouseEvent) {
    for(button in buttons) {
      if(!button.correspondsTo(e)) continue
      for(entry in button.draggingActions) {
        if(!entry.canvas.hasPoint(e.x, e.y)) continue
        val sx = e.x - entry.canvas.viewport.leftX
        val sy = e.y - entry.canvas.viewport.topY
        if(!entry.action.conditions(sx, sy)) continue
        currentCanvas = entry.canvas
        currentDraggingCanvas = entry.canvas
        currentDraggingAction = entry.action
        entry.action.pressed(sx, sy)
        return
      }
    }
  }

  override fun mouseDragged(e: MouseEvent) {
    val sx = e.x - currentCanvas.viewport.leftX
    val sy = e.y - currentCanvas.viewport.topY
    currentDraggingAction?.dragged(sx, sy)
  }

  override fun mouseReleased(e: MouseEvent) {
    val sx = e.x - currentCanvas.viewport.leftX
    val sy = e.y - currentCanvas.viewport.topY
    currentDraggingAction?.released(sx, sy)
    currentDraggingAction = null
  }

  override fun mouseEntered(e: MouseEvent) {
  }

  override fun mouseExited(e: MouseEvent) {
  }

  override fun mouseMoved(e: MouseEvent) {
    val point = MouseInfo.getPointerInfo().location
    currentDraggingAction?.dragged(point.x, point.y)
  }

  override fun mouseWheelMoved(e: MouseWheelEvent) {
    for(wheel in buttons) {
      if(!wheel.correspondsTo(e)) continue
      for(entry in wheel.actions) {
        if(!entry.canvas.hasPoint(e.x, e.y)) continue
        val sx = e.x - entry.canvas.viewport.leftX
        val sy = e.y - entry.canvas.viewport.topY
        if(!entry.action.conditions(sx, sy)) continue
        currentCanvas = entry.canvas
        entry.action.execute(sx, sy)
      }
    }
  }

  override fun keyTyped(e: KeyEvent) {
    val point = MouseInfo.getPointerInfo().location
    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      for(entry in key.actions) {
        if(!entry.canvas.hasPoint(point.x, point.y)) continue
        val sx = point.x - entry.canvas.viewport.leftX
        val sy = point.y - entry.canvas.viewport.topY
        if(!entry.action.conditions(sx, sy)) continue
        currentCanvas = entry.canvas
        entry.action.execute(sx, sy)
      }
    }
  }

  override fun keyPressed(e: KeyEvent) {
    val point = MouseInfo.getPointerInfo().location
    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      for(entry in key.draggingActions) {
        if(!entry.canvas.hasPoint(point.x, point.y)) continue
        val sx = point.x - entry.canvas.viewport.leftX
        val sy = point.y - entry.canvas.viewport.topY
        if(!entry.action.conditions(sx, sy)) continue
        currentCanvas = entry.canvas
        currentDraggingAction = entry.action
        currentDraggingCanvas = entry.canvas
        entry.action.pressed(sx, sy)
      }
    }

  }

  override fun keyReleased(e: KeyEvent) {
    val point = MouseInfo.getPointerInfo().location
    val sx = point.x - currentDraggingCanvas!!.viewport.leftX
    val sy = point.y - currentDraggingCanvas!!.viewport.topY
    currentDraggingAction?.released(sx, sy)
    currentDraggingAction = null
  }
}
