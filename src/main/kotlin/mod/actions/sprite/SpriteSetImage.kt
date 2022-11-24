package mod.actions.sprite

import Formula
import Image
import ImageArray
import Sprite
import SpriteAction
import SpriteFactory
import blankImage
import mod.dragging.enterDouble
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SpriteSetImageFactory(val image: Image = blankImage): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetImageFactory(currentImageArray!!.images[0])
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetImage(sprite, image)
  }

  override fun toString(): String = "Установить изображение"
}

class SpriteSetImage(sprite: Sprite, val image: Image): SpriteAction(sprite) {
  override fun execute() {
    sprite.image = image
  }

  override fun toString(): String = "Установить изображение"
}