package mod.dragging

import Shape
import snapX
import snapY
import xFromScreen
import yFromScreen
import java.awt.event.MouseEvent.BUTTON3

object createSprite: createRectangle() {
  override fun pressed(x: Int, y: Int) {
    startingX = snapX(xFromScreen(x))
    startingY = snapY(yFromScreen(y))
    shape = Shape(0.0, 0.0, 0.0, 0.0)
    shapes.add(shape!!)
    selectedShapes.clear()
    selectedShapes.add(shape!!)
  }

  override fun dragged(x: Int, y: Int) {
    dragged(x, y, true)
  }

  override fun released(x: Int, y: Int) {
  }
}