import java.awt.Graphics2D
import java.awt.event.KeyListener
import java.awt.event.MouseListener
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import java.util.*

interface DraggingAction {
  fun conditions(x: Int, y: Int): Boolean
  fun pressed(x: Int, y: Int)
  fun dragged(x: Int, y: Int)
  fun released(x: Int, y: Int)
  fun drawWhileDragging(g2d: Graphics2D)
}

var currentDraggingAction: DraggingAction? = null