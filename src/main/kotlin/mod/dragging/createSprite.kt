package mod.dragging

import Shape
import mod.actions.currentImageArray
import snapX
import snapY

object createSprite: createRectangle() {
  override fun pressed(x: Double, y: Double) {
    startingX = snapX(x)
    startingY = snapY(y)
    shape = Shape(x, y, 0.0, 0.0)
    shape!!.image = if(selectedShapes.size > 0) selectedShapes.first.image else currentImageArray!!.images[0]
    shapes.add(shape!!)
    selectedShapes.clear()
    selectedShapes.add(shape!!)
  }

  override fun dragged(x: Double, y: Double) {
    dragged(x, y, true)
  }

  override fun released(x: Double, y: Double) {
    if(shape!!.width == 0.0 && shape!!.height == 0.0) {
      shapes.remove(shape)
      selectedShapes.clear()
    }
  }
}