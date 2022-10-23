import java.awt.MouseInfo
import java.awt.event.*
import java.util.*

abstract class Pushable {
  val draggingActions = LinkedList<DraggingAction>()
  fun pressed(e: MouseListener): Boolean = false
  fun pressed(e: MouseWheelListener): Boolean = false
  fun pressed(e: KeyListener): Boolean = false
}

val keys = LinkedList<Key>()
class Key(var code: Int): Pushable() {
  init {
    keys.add(this)
  }
  fun pressed(e: KeyEvent): Boolean {
    return e.keyCode == code
  }

  fun add(action: DraggingAction) {
    draggingActions.add(action)
  }
}

val mouseButtons = LinkedList<MouseButton>()
class MouseButton(var button: Int): Pushable() {
  init {
    mouseButtons.add(this)
  }
  fun pressed(e: MouseEvent): Boolean {
    return e.button == button
  }

  fun add(action: DraggingAction) {
    draggingActions.add(action)
  }
}

object listener: MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
  override fun mouseClicked(e: MouseEvent?) {
  }

  override fun mousePressed(e: MouseEvent) {
    for(button in mouseButtons) {
      if(!button.pressed(e)) continue
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
    currentDraggingAction?.dragged(e.x, e.y)
  }

  override fun mouseWheelMoved(e: MouseWheelEvent) {
  }

  override fun keyTyped(e: KeyEvent) {

  }

  override fun keyPressed(e: KeyEvent) {
    val point = MouseInfo.getPointerInfo().location
    for(key in keys) {
      if(!key.pressed(e)) continue
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