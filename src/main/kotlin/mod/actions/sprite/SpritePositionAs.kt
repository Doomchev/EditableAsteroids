package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.parentSprite
import mod.dragging.selectedSprites
import nullSprite

class SpritePositionAsFactory(): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpritePositionAsFactory()
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpritePositionAs(sprite,)
  }

  override fun toString(): String = "Переместить к родителю"
}

class SpritePositionAs(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX = parentSprite.centerX
    sprite.centerY = parentSprite.centerY
  }

  override fun toString(): String = "Переместить к родителю"
}

