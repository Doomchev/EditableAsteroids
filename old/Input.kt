import java.awt.MouseInfo
import java.awt.Point
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.util.LinkedList


abstract class Button(var action: Action) {
  companion object {
    val all = LinkedList<Button>()
  }

  init {
    action.triggers.add(this);
  }

  fun hasCode(code: Int): Boolean {
    return false
  }

  open fun hasNumber(number: Int, times: Int): Boolean {
    return false
  }
}

class KeyboardKey(private val code: Int, action: Action) {
  fun hasNumber(number: Int): Boolean {
    return this.code == code
  }
}

class MouseButton(private val number: Int, val times: Int, action: Action)
  : Button(action) {
  override fun hasNumber(number: Int, times: Int): Boolean {
    return this.number == number && this.times == times
  }
}

class Listener: KeyListener, MouseListener, MouseMotionListener {
  companion object {
    var currentDraggingAction: Action? = null
  }

  override fun keyTyped(e: KeyEvent?) {
    for(button in Button.all) {
      if(e != null && button.hasCode(e.keyCode)) {
        button.action.execute()
      }
    }
  }

  override fun keyPressed(e: KeyEvent?) {
    for(button in Button.all) {
      val action = button.action
      if(e != null && button.hasCode(e.keyCode)
          && action.conditions()) {
        currentDraggingAction = action
        val point: Point? = MouseInfo.getPointerInfo().location
        action.start(point!!.x, point.y)
      }
    }
  }

  override fun keyReleased(e: KeyEvent?) {
    for(button in Button.all) {
      val action = button.action
      if(e != null && action == currentDraggingAction) {
        action.stop()
        currentDraggingAction = null
      }
    }
  }

  override fun mouseClicked(e: MouseEvent?) {
    for(button in Button.all) {
      if(e != null && button.hasNumber(e.button, e.clickCount)) {
        button.action.execute()
      }
    }
  }

  override fun mousePressed(e: MouseEvent?) {
    for(button in Button.all) {
      val action = button.action
      if(e != null && button.hasNumber(e.button, 1)
        && action.conditions()) {
        action.start(e.x, e.y)
      }
    }
  }

  override fun mouseReleased(e: MouseEvent?) {
    for(button in Button.all) {
      val action = button.action
      if(e != null && action == currentDraggingAction) {
        action.stop()
        currentDraggingAction = null
      }
    }
  }

  override fun mouseDragged(e: MouseEvent?) {
    mouseMoved(e)
  }

  override fun mouseMoved(e: MouseEvent?) {
    if(e != null && currentDraggingAction != null) {
      currentDraggingAction?.dragging(e.x, e.y)
    }
  }

  override fun mouseEntered(e: MouseEvent?) {
  }

  override fun mouseExited(e: MouseEvent?) {
  }
}

