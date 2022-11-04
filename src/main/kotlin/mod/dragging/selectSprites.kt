package mod.dragging

import Shape
import java.awt.Graphics
import java.awt.Graphics2D

object selectSprites: createRectangle(), Drawing {
  private val selection = Shape(0.0, 0.0, 0.0, 0.0)

  override fun pressed(x: Double, y: Double) {
    shape = selection
    selection.width = 0.0
    selection.height = 0.0
    pressed(x, y, false)
  }

  override fun dragged(x: Double, y: Double) {
    dragged(x, y, false)
  }

  override fun released(x: Double, y: Double) {
    for(shape in shapes) {
      if(selection.overlaps(shape)) selectedShapes.add(shape)
    }
  }

  override fun draw(g: Graphics2D) {
    for(shape in selectedShapes) {
      shape.drawSelection(g)
    }
  }
}