package mod.dragging

import Shape
import image
import xToScreen
import yToScreen
import java.awt.Graphics2D
import java.util.*

abstract class Module {
  abstract fun draw(g2d: Graphics2D)
}

val allModules = LinkedList<Module>()

val shapes = LinkedList<Shape>()

object shapesDrawing: Module() {
  override fun draw(g2d: Graphics2D) {
    for(shape in shapes) {
      shape.draw(g2d, image)
    }
  }
}

val selectedShapes = LinkedList<Shape>()

object selectedShapesDrawing: Module() {
  override fun draw(g2d: Graphics2D) {
    for(shape in selectedShapes) {
      shape.drawSelection(g2d)
    }
  }
}

object shapeResizerDrawing: Module() {
  val cursorSize = 5
  val cursorDistance = 2

  override fun draw(g2d: Graphics2D) {
    if(selectedShapes.size != 1) return
    val shape = selectedShapes.first
    for(xx in -1 .. 1) {
      val x = when(xx) {
        -1 -> xToScreen(shape.leftX).toInt() - cursorDistance - cursorSize
        0 -> xToScreen(shape.centerX).toInt() - cursorSize / 2
        else -> xToScreen(shape.rightX).toInt() + cursorDistance
      }.toInt()
      for(yy in -1 .. 1) {
        if(xx == 0 && yy == 0) continue
        val y = when(yy) {
          -1 -> yToScreen(shape.topY).toInt() - cursorDistance - cursorSize
          0 -> yToScreen(shape.centerY).toInt() - cursorSize / 2
          else -> yToScreen(shape.bottomY).toInt() + cursorDistance
        }
        g2d.fillRect(x, y, cursorSize, cursorSize)
      }
    }
  }
}

