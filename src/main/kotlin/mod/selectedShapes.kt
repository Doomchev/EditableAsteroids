package mod.dragging

import Shape
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

interface Drawing {
  fun draw(g2d: Graphics2D)
}

val selectedShapes = LinkedList<Shape>()
val shapes = LinkedList<Shape>()





