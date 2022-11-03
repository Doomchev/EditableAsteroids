package mod.dragging

import Shape
import imags
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
    shape!!.image = if(selectedShapes.size > 0) selectedShapes.first.image else imags.first
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