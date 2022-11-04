package mod.dragging

import Shape
import imags
import mod.actions.currentImage
import snapX
import snapY
import xFromScreen
import yFromScreen
import java.awt.event.MouseEvent.BUTTON3

object createSprite: createRectangle() {
  override fun pressed(x: Double, y: Double) {
    startingX = snapX(x)
    startingY = snapY(y)
    shape = Shape(0.0, 0.0, 0.0, 0.0)
    shape!!.image = if(selectedShapes.size > 0) selectedShapes.first.image else currentImage
    shapes.add(shape!!)
    selectedShapes.clear()
    selectedShapes.add(shape!!)
  }

  override fun dragged(x: Double, y: Double) {
    dragged(x, y, true)
  }

  override fun released(x: Double, y: Double) {
  }
}