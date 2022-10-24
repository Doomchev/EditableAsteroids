import java.awt.MouseInfo
import java.awt.event.*
import java.util.*

abstract class Pushable {
  val draggingActions = LinkedList<DraggingAction>()
  val actions = LinkedList<Action>()

  init {
    buttons.add(this)
  }

  open fun correspondsTo(e: MouseEvent): Boolean = false
  open fun correspondsTo(e: MouseWheelEvent): Boolean = false
  open fun correspondsTo(e: KeyEvent): Boolean = false

  fun add(action: DraggingAction) {
    draggingActions.add(action)
  }

  fun add(action: Action) {
    actions.add(action)
  }
}

val buttons = LinkedList<Pushable>()
class Key(var code: Int): Pushable() {

  override fun correspondsTo(e: KeyEvent): Boolean {
    return e.keyCode == code
  }
}

class MouseButton(var button: Int): Pushable() {
  override fun correspondsTo(e: MouseEvent): Boolean {
    return e.button == button
  }
}

object mouseWheelUp: Pushable() {
  var oldAmount = 0

  override fun correspondsTo(e: MouseWheelEvent): Boolean {
    return e.scrollAmount < oldAmount
  }
}

object mouseWheelDown: Pushable() {
  var oldAmount = 0

  override fun correspondsTo(e: MouseWheelEvent): Boolean {
    return e.scrollAmount > oldAmount
  }
}

object listener: MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
  override fun mouseClicked(e: MouseEvent) {
    for(button in buttons) {
      if(!button.correspondsTo(e)) continue
      for(action in button.actions) {
        if(action.conditions(e.x, e.y)) {
          action.execute()
        }
      }
    }
  }

  override fun mousePressed(e: MouseEvent) {
    for(button in buttons) {
      if(!button.correspondsTo(e)) continue
      for(action in button.draggingActions) {
        if(action.conditions(e.x, e.y)) {
          currentDraggingAction = action
          action.pressed(e.x, e.y)
          return
        }
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
      for(action in wheel.actions) {
        if(action.conditions(e.x, e.y)) {
          action.execute()
        }
      }
    }
  }

  override fun keyTyped(e: KeyEvent) {
    val point = MouseInfo.getPointerInfo().location
    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      for(action in key.actions) {
        if(action.conditions(point.x, point.y)) {
          action.execute()
        }
      }
    }
  }

  override fun keyPressed(e: KeyEvent) {
    val point = MouseInfo.getPointerInfo().location
    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      for(action in key.draggingActions) {
        if(action.conditions(point.x, point.y)) {
          currentDraggingAction = action
          action.pressed(point.x, point.y)
          return
        }
      }
    }

  }

  override fun keyReleased(e: KeyEvent) {
    val point = MouseInfo.getPointerInfo().location
    currentDraggingAction?.released(point.x, point.y)
    currentDraggingAction = null
  }
}
