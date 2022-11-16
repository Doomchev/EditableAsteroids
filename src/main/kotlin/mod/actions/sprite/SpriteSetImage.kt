package mod.actions.sprite

import Image
import ImageArray
import Sprite
import SpriteAction
import mod.dragging.enterDouble
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SpriteSetImage: SpriteAction() {
  var image: Image? = null

  override fun create(sprite: Sprite?): SpriteAction {
    val action = SpriteSetImage()
    action.sprite = sprite
    action.image = image
    return action
  }

  override fun settings() {
    image = currentImageArray!!.images[0]
  }

  override fun execute() {
    sprite!!.image = image
  }

  override fun toString(): String {
    return "Установить изображение"
  }
}