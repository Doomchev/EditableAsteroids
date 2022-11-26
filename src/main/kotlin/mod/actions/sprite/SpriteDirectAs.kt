package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.parentSprite
import mod.dragging.selectedSprites
import nullSprite

class SpriteDirectAsFactory(): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteDirectAsFactory()
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteDirectAs(sprite)
  }

  override fun toString(): String = "Направить как родителя"
}
class SpriteDirectAs(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle = parentSprite.angle
  }

  override fun toString(): String = "Направить как родителя"
}