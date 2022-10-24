package mod.dragging

import Shape
import java.awt.Graphics2D
import java.awt.event.MouseEvent

object selectSprites: createRectangle(), Drawing {
  private val selection = Shape(0.0, 0.0, 0.0, 0.0)

  override fun pressed(x: Int, y: Int) {
    shape = selection
    selection.width = 0.0
    selection.height = 0.0
    super.pressed(x, y)
  }

  override fun released(x: Int, y: Int) {
    for(shape in shapes) {
      if(selection.overlaps(shape)) selectedShapes.add(shape)
    }
  }

  override fun draw(g2d: Graphics2D) {
    for(shape in selectedShapes) {
      shape.drawSelection(g2d)
    }
  }
}