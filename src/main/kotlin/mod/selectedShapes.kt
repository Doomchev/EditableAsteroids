package mod.dragging

import Shape
import image
import java.awt.Graphics2D
import java.util.*

abstract class Module {
  abstract fun draw(g2d: Graphics2D)
}

val allModules = LinkedList<Module>()

val shapes = LinkedList<Shape>()

object shapesModule: Module() {
  override fun draw(g2d: Graphics2D) {
    for(shape in shapes) {
      shape.draw(g2d, image)
    }
  }
}

val selectedShapes = LinkedList<Shape>()

object selectedShapesModule: Module() {
  override fun draw(g2d: Graphics2D) {
    for(shape in selectedShapes) {
      shape.drawSelection(g2d)
    }
  }
}