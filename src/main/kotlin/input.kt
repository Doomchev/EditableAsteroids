import java.awt.SystemColor.window
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

val shapes = LinkedList<Shape>()
var currentShape: Shape? = null
var startingX:Double = 0.0
var startingY:Double = 0.0

object listener: MouseListener, MouseMotionListener {
  override fun mouseClicked(e: MouseEvent?) {
  }

  override fun mousePressed(e: MouseEvent?) {
    currentShape = Shape(0.0, 0.0, 0.0, 0.0)
    startingX = xFromScreen(e!!.x)
    startingY = yFromScreen(e!!.y)
    shapes.add(currentShape!!)
  }

  override fun mouseReleased(e: MouseEvent?) {
  }

  override fun mouseEntered(e: MouseEvent?) {
  }

  override fun mouseExited(e: MouseEvent?) {
  }

  override fun mouseDragged(e: MouseEvent?) {
    if(currentShape == null) return
    val fx: Double = xFromScreen(e!!.x)
    val fy: Double = yFromScreen(e!!.y)
    currentShape!!.width = abs(startingX - fx)
    currentShape!!.height = abs(startingY - fy)
    currentShape!!.leftX = min(startingX, fx)
    currentShape!!.topY = min(startingY, fy)
  }

  override fun mouseMoved(e: MouseEvent?) {
  }
}