package mod.dragging

import Shape
import xFromScreen
import yFromScreen
import java.awt.event.MouseEvent.BUTTON3

object createSprite: createRectangle() {
  override fun pressed(x: Int, y: Int) {
    startingX = xFromScreen(x)
    startingY = yFromScreen(y)
    shape = Shape(0.0, 0.0, 0.0, 0.0)
    shapes.add(shape!!)
    selectedShapes.clear()
    selectedShapes.add(shape!!)
    super.pressed(x, y)
  }

  override fun released(x: Int, y: Int) {
  }
}