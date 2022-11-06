package mod.actions

import ImageArray
import Shape
import mod.dragging.ShapeAction
import mod.dragging.enterDouble
import kotlin.math.PI

class SpriteAnimation: ShapeAction() {
  var array: ImageArray? = null
  var frame = 0.0
  var speed: Double = 0.0

  override fun create(shape: Shape): ShapeAction {
    val action = SpriteAnimation()
    action.array = currentImageArray
    action.shape = shape
    action.speed = speed
    return action
  }

  override fun execute() {
    frame += speed
    val images = currentImageArray!!.images
    shape!!.image = images[frame.toInt() % images.size]
  }

  override fun settings() {
    speed = 0.01 * enterDouble("Введите скорость (кадров/сек):")
  }
}