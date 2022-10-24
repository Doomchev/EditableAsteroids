import java.awt.Graphics2D

interface DraggingAction {
  fun conditions(x: Int, y: Int): Boolean = true
  fun pressed(x: Int, y: Int)
  fun dragged(x: Int, y: Int)
  fun released(x: Int, y: Int)
  fun drawWhileDragging(g2d: Graphics2D)
}

var currentDraggingAction: DraggingAction? = null