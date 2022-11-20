package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.selectedSprites
import nullSprite

class SpritePositionAsFactory(private val asSprite: Sprite = nullSprite): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteDirectAsFactory(selectedSprites.first)
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpritePositionAs(sprite, asSprite)
  }

  override fun toString(): String = "Переместить к"
}
class SpritePositionAs(sprite: Sprite, private val asSprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX = asSprite.centerX
    sprite.centerY = asSprite.centerY
  }
}

