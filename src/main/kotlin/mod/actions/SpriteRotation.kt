package mod.actions

import Shape
import mod.dragging.ShapeAction
import mod.dragging.enterDouble
import kotlin.math.PI

class SpriteRotation: ShapeAction() {
  var speed: Double = 0.0
  override fun create(shape: Shape): ShapeAction {
    val action = SpriteRotation()
    action.shape = shape
    action.speed = speed
    return action
  }

  override fun execute(x: Double, y: Double) {
  }

  override fun onButtonDown(x: Double, y: Double) {
    shape!!.angle += speed
  }

  override fun settings() {
    speed = 0.01 * enterDouble("Введите скорость (град/сек):") * PI / 180.0
  }
}