package mod.actions.sprite

import Image
import Node
import Sprite
import Action
import SpriteActionFactory
import blankImage
import Serializer
import SpriteAction
import SpriteEntry
import mod.dragging.selectImageArray
import mod.dragging.selectSprite

object spriteSetImageSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteSetImageFactory(selectSprite(), selectImageArray().images[0])
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteSetImageFactory(node.getField("spriteentry") as SpriteEntry, node.getField("image") as Image)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteSetImage(node.getField("sprite") as Sprite, node.getField("image") as Image)
  }

  override fun toString(): String = "Установить изображение "
}

class SpriteSetImageFactory(spriteEntry: SpriteEntry, var image: Image = blankImage): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteSetImage(spriteEntry.resolve(), image)
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