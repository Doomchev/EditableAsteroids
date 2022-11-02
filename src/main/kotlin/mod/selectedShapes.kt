package mod.dragging

import Shape
import java.awt.Graphics2D
import java.util.*

interface Drawing {
  fun draw(g2d: Graphics2D)
}

val selectedShapes = LinkedList<Shape>()
val shapes = LinkedList<Shape>()



