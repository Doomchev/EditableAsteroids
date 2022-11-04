package mod.dragging

import Shape
import java.awt.Graphics
import java.awt.Graphics2D
import java.util.*

interface Drawing {
  fun draw(g: Graphics2D)
}

val selectedShapes = LinkedList<Shape>()
val shapes = LinkedList<Shape>()





