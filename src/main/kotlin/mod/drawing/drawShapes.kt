package mod.drawing

import mod.dragging.Drawing
import mod.dragging.shapes
import java.awt.Graphics
import java.awt.Graphics2D

object drawShapes: Drawing {
  override fun draw(g: Graphics2D) {
    for(shape in shapes) {
      shape.draw(g)
    }
  }
}