import java.awt.Graphics
import java.awt.Graphics2D

interface DraggingAction {
  fun conditions(x: Double, y: Double): Boolean = true
  fun pressed(x: Double, y: Double)
  fun dragged(x: Double, y: Double)
  fun released(x: Double, y: Double)
  fun drawWhileDragging(g: Graphics2D)
}

var currentDraggingAction: DraggingAction? = null
var currentDraggingCanvas: Canvas? = null