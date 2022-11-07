package mod.actions.sprite

import ImageArray
import Sprite
import fpsk
import mod.dragging.SpriteAction
import mod.dragging.enterDouble

class SpriteAnimation: SpriteAction() {
  var array: ImageArray? = null
  var frame = 0.0
  var speed: Double = 0.0

  override fun create(sprite: Sprite): SpriteAction {
    val action = SpriteAnimation()
    action.array = currentImageArray
    action.sprite = sprite
    action.speed = speed
    return action
  }

  override fun settings() {
    speed = enterDouble("Введите скорость (кадров/сек):")
  }

  override fun execute() {
    frame += fpsk * speed
    val images = currentImageArray!!.images
    sprite!!.image = images[frame.toInt() % images.size]
  }
}