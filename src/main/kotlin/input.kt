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
        if(!entry.action.conditions(e.x, e.y)) continue
        canvas = entry.canvas
        entry.action.execute()
      }
    }
  }

  override fun mousePressed(e: MouseEvent) {
    for(button in buttons) {
      if(!button.correspondsTo(e)) continue
      for(entry in button.draggingActions) {
        if(!entry.canvas.hasPoint(e.x, e.y)) continue
        if(!entry.action.conditions(e.x, e.y)) continue
        canvas = entry.canvas
        currentDraggingAction = entry.action
        entry.action.pressed(e.x, e.y)
        return
      }
    }
  }

  override fun mouseDragged(e: MouseEvent) {
    currentDraggingAction?.dragged(e.x, e.y)
  }

  override fun mouseReleased(e: MouseEvent) {
    currentDraggingAction?.released(e.x, e.y)
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
        if(!entry.action.conditions(e.x, e.y)) continue
        canvas = entry.canvas
        entry.action.execute()
      }
    }
  }

  override fun keyTyped(e: KeyEvent) {
    val point = MouseInfo.getPointerInfo().location
    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      for(entry in key.actions) {
        if(!entry.canvas.hasPoint(point.x, point.y)) continue
        if(!entry.action.conditions(point.x, point.y)) continue
        canvas = entry.canvas
        entry.action.execute()
      }
    }
  }

  override fun keyPressed(e: KeyEvent) {
    val point = MouseInfo.getPointerInfo().location
    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      for(entry in key.draggingActions) {
        canvas = entry.canvas
        if(!entry.canvas.hasPoint(point.x, point.y)) continue
        if(!entry.action.conditions(point.x, point.y)) continue
        currentDraggingAction = entry.action
        entry.action.pressed(point.x, point.y)
      }
    }

  }

  override fun keyReleased(e: KeyEvent) {
    val point = MouseInfo.getPointerInfo().location
    currentDraggingAction?.released(point.x, point.y)
    currentDraggingAction = null
  }
}
