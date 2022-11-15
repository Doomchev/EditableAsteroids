package mod.actions.sprite

import ImageArray
import Sprite
import SpriteAction
import fpsk
import mod.dragging.enterDouble

class SpriteAnimation: SpriteAction() {
  var array: ImageArray? = null
  var frame = 0.0
  var speed: Double = 0.0

  override fun create(sprite: Sprite?): SpriteAction {
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
    val images = currentImageArray!!.images
    frame += fpsk * speed
    while(frame < 0.0) {
      frame += images.size
    }
    sprite!!.image = images[frame.toInt() % images.size]
  }

  override fun toString(): String = "Анимировать"
}