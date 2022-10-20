import java.awt.event.MouseEvent
import java.awt.event.MouseEvent.*
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.min

val shapes = LinkedList<Shape>()
val selectedShapes = LinkedList<Shape>()
var startingX:Double = 0.0
var startingY:Double = 0.0
val selection:Shape = Shape(0.0, 0.0, 0.0, 0.0)
var currentShape: Shape? = null
var oldx: Double = 0.0
var oldy: Double = 0.0

object listener: MouseListener, MouseMotionListener {
  override fun mouseClicked(e: MouseEvent?) {
  }

  override fun mousePressed(e: MouseEvent) {
    startingX = xFromScreen(e.x)
    startingY = yFromScreen(e.y)
    oldx = startingX
    oldy = startingY
    if(e.button == BUTTON3) {
      val shape = Shape(0.0, 0.0, 0.0, 0.0)
      shapes.add(shape)
      currentShape = shape
    } else {
      val fx = xFromScreen(e.x)
      val fy = yFromScreen(e.y)
      for(shape in selectedShapes.descendingIterator()) {
        if(shape.collidesWithPoint(fx, fy)) return
      }
      selectedShapes.clear()
      for(shape in shapes.descendingIterator()) {
        if(shape.collidesWithPoint(fx, fy)) {
          selectedShapes.add(shape)
          return
        }
      }
      currentShape = selection
      selection.width = 0.0
      selection.height = 0.0
    }
  }

  override fun mouseReleased(e: MouseEvent) {
    if(currentShape == selection) {
      for(shape in shapes) {
        if(selection.overlaps(shape)) selectedShapes.add(shape)
      }
      currentShape = null
    } else if(currentShape != null) {
      selectedShapes.clear()
      selectedShapes.add(currentShape!!)
      currentShape = null
    }
  }

  override fun mouseEntered(e: MouseEvent?) {
  }

  override fun mouseExited(e: MouseEvent?) {
  }

  override fun mouseDragged(e: MouseEvent) {
    val fx = xFromScreen(e.x)
    val fy = yFromScreen(e.y)
    val dx = fx - oldx
    val dy = fy - oldy

    if(currentShape != null) {
      currentShape!!.width = abs(startingX - fx)
      currentShape!!.height = abs(startingY - fy)
      currentShape!!.leftX = min(startingX, fx)
      currentShape!!.topY = min(startingY, fy)
    } else {
      for(shape in selectedShapes) {
        shape.leftX += dx
        shape.topY += dy
      }
    }
    oldx = fx
    oldy = fy
  }

  override fun mouseMoved(e: MouseEvent?) {
  }
}