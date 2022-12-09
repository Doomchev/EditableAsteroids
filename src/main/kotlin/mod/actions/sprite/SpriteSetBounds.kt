package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import mod.Serializer
import mod.selectedSprites
import nullSprite
import spritesToRemove

object spriteSetBoundsSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteSetBoundsFactory(selectedSprites.first)
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteSetBoundsFactory(node.getField("bounds") as Sprite)
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteSetBounds(node.getField("sprite") as Sprite, node.getField("bounds") as Sprite)
  }
}

class SpriteSetBoundsFactory(private var bounds: Sprite): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetBounds(sprite, bounds)
  }

  override fun toString(): String = "Установить границы"
  override fun fullText(): String = "Установить границы в $bounds"

  override fun toNode(node: Node) {
    node.setField("bounds", bounds)
  }
}

class SpriteSetBounds(sprite: Sprite, var bounds: Sprite = nullSprite): SpriteAction(sprite) {
  override fun execute() {
    if(sprite.rightX < bounds.leftX || sprite.leftX > bounds.rightX || sprite.bottomY < bounds.topY || sprite.topY > bounds.bottomY) {
      spritesToRemove.add(sprite)
    }
  }

  override fun toString(): String = "Установить границы $sprite в $bounds"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setField("bounds", bounds)
  }
}