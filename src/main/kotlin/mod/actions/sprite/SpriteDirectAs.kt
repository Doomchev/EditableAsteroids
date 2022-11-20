package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.selectedSprites
import nullSprite

class SpriteDirectAsFactory(private val asSprite: Sprite = nullSprite): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteDirectAsFactory(selectedSprites.first)
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteDirectAs(sprite, asSprite)
  }

  override fun toString(): String = "Повернуть как"
}
class SpriteDirectAs(sprite: Sprite, private val asSprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle = asSprite.angle
  }
}