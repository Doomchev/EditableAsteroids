package modules

import Shape
import selectedShapes
import shapes
import java.awt.event.MouseEvent

object selectSprites: createRectangle() {
  private val selection = Shape(0.0, 0.0, 0.0, 0.0)

  override fun conditions(x: Int, y: Int, button: Int): Boolean {
    return button == MouseEvent.BUTTON1
  }

  override fun mousePressed(x: Int, y: Int, button: Int) {
    shape = selection
    selection.width = 0.0
    selection.height = 0.0
    super.mousePressed(x, y, button)
  }

  override fun mouseReleased(x: Int, y: Int) {
    for(shape in shapes) {
      if(selection.overlaps(shape)) selectedShapes.add(shape)
    }
  }
}