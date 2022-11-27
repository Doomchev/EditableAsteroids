package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import nullSprite
import spritesToRemove

class SpriteSetBoundsFactory(private val bounds: Sprite = nullSprite): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetBoundsFactory(bounds)
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetBounds(sprite, bounds)
  }

  override fun toString(): String = "Установить границы"
  override fun fullText(): String = "Установить границы в $bounds"

  override fun getClassName(): String = "SpriteSetBoundsFactory"

  override fun store(node: Node) {
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

  override fun getClassName(): String = "SpriteSetBounds"

  override fun store(node: Node) {
    node.setObject("sprite", sprite)
    node.setField("bounds", bounds)
  }
}