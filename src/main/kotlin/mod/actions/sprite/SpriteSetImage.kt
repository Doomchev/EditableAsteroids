package mod.actions.sprite

import Image
import Node
import Sprite
import SpriteAction
import SpriteFactory
import blankImage
import Serializer
import mod.dragging.selectImageArray

object spriteSetImageSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteSetImageFactory(selectImageArray().images[0])
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteSetImageFactory(node.getField("image") as Image)
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteSetImage(node.getField("sprite") as Sprite, node.getField("image") as Image)
  }

  override fun toString(): String = "Установить изображение"
}

class SpriteSetImageFactory(var image: Image = blankImage): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetImage(sprite, image)
  }

  override fun toString(): String = "Установить изображение"

  override fun toNode(node: Node) {
    node.setField("image", image)
  }
}

class SpriteSetImage(sprite: Sprite, var image: Image): SpriteAction(sprite) {
  override fun execute() {
    sprite.image = image
  }

  override fun toString(): String = "Установить изображение"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setField("image", image)
  }
}