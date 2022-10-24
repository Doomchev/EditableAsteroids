package mod.drawing

import mod.dragging.Drawing
import mod.dragging.shapes
import java.awt.Graphics2D
import java.io.File
import javax.imageio.ImageIO

object drawShapes: Drawing {
  val image = ImageIO.read(File("img.png"))

  override fun draw(g2d: Graphics2D) {
    for(shape in shapes) {
      shape.draw(g2d, image)
    }
  }
}