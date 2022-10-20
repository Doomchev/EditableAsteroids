package mod.dragging

import Shape
import xFromScreen
import yFromScreen
import java.awt.event.MouseEvent.BUTTON3

object createSprite: createRectangle() {
  override fun conditions(x: Int, y: Int, button: Int): Boolean {
    return button == BUTTON3
  }

  override fun mousePressed(x: Int, y: Int, button: Int) {
    startingX = xFromScreen(x)
    startingY = yFromScreen(y)
    shape = Shape(0.0, 0.0, 0.0, 0.0)
    shapes.add(shape!!)
    selectedShapes.clear()
    selectedShapes.add(shape!!)
    super.mousePressed(x, y, button)
  }

  override fun mouseReleased(x: Int, y: Int) {
  }
}