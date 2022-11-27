package mod.actions.sprite

import Formula
import Image
import ImageArray
import Node
import Sprite
import SpriteAction
import SpriteFactory
import blankImage
import mod.dragging.enterDouble
import mod.dragging.selectImageArray
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SpriteSetImageFactory(val image: Image = blankImage): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetImageFactory(selectImageArray().images[0])
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetImage(sprite, image)
  }

  override fun fullText(): String = "Установить изображение"

  override fun getClassName(): String = "SpriteSetImageFactory"

  override fun store(node: Node) {
    node.setObject("image", image)
  }
}

class SpriteSetImage(sprite: Sprite, val image: Image): SpriteAction(sprite) {
  override fun execute() {
    sprite.image = image
  }

  override fun toString(): String = "Установить изображение"

  override fun getClassName(): String = "SpriteSetImage"

  override fun store(node: Node) {
    node.setObject("sprite", sprite)
    node.setObject("image", image)
  }
}