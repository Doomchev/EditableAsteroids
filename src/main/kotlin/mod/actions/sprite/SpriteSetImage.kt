package mod.actions.sprite

import Image
import Node
import Sprite
import Action
import ActionFactory
import blankImage
import Serializer
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import selectImageArray
import selectSprite

object spriteSetImageSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return SpriteSetImageFactory(selectSprite(), selectImageArray().images[0])
  }

  override fun factoryFromNode(node: Node): ActionFactory {
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

  override fun toString(): String = "Установить изображение $image"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setField("image", image)
  }
}