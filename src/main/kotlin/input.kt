import java.awt.Graphics2D
import java.awt.event.MouseEvent
import java.awt.event.MouseEvent.*
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.min

abstract class DraggingAction {
  abstract fun conditions(x: Int, y: Int, button: Int): Boolean
  abstract fun mousePressed(x: Int, y: Int, button: Int)
  abstract fun mouseDragged(x: Int, y: Int)
  abstract fun mouseReleased(x: Int, y: Int)
  abstract fun draw(g2d: Graphics2D)
}

val draggingActions = LinkedList<DraggingAction>()
var currentDraggingAction: DraggingAction? = null

object listener: MouseListener, MouseMotionListener {
  override fun mouseClicked(e: MouseEvent?) {
  }

  override fun mousePressed(e: MouseEvent) {
    for(action in draggingActions) {
      if(action.conditions(e.x, e.y, e.button)) {
        currentDraggingAction = action
        action.mousePressed(e.x, e.y, e.button)
        return
      }
    }
  }

  override fun mouseDragged(e: MouseEvent) {
    currentDraggingAction!!.mouseDragged(e.x, e.y)
  }

  override fun mouseReleased(e: MouseEvent) {
    currentDraggingAction!!.mouseReleased(e.x, e.y)
    currentDraggingAction = null
  }

  override fun mouseEntered(e: MouseEvent) {
  }

  override fun mouseExited(e: MouseEvent) {
  }

  override fun mouseMoved(e: MouseEvent) {
  }
}