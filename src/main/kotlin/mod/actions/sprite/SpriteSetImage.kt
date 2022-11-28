package mod.actions.sprite

import Image
import ImageArray
import Node
import Sprite
import SpriteAction
import SpriteFactory
import blankImage
import mod.dragging.selectImageArray

class SpriteSetImageFactory(var image: Image = blankImage): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetImageFactory(selectImageArray().images[0])
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetImage(sprite, image)
  }

  override fun fullText(): String = "Установить изображение"

  override fun getClassName(): String = "SpriteSetImageFactory"

  override fun store(node: Node) {
    node.setField("image", image)
  }

  override fun load(node: Node) {
    image = node.getField("image") as Image
  }
}

class SpriteSetImage(sprite: Sprite, var image: Image): SpriteAction(sprite) {
  override fun execute() {
    sprite.image = image
  }

  override fun toString(): String = "Установить изображение"

  override fun getClassName(): String = "SpriteSetImage"

  override fun store(node: Node) {
    node.setField("sprite", sprite)
    node.setField("image", image)
  }

  override fun load(node: Node) {
    sprite = node.getField("sprite") as Sprite
    image = node.getField("image") as Image
  }
}