package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.scene
import mod.dragging.selectedSprites
import nullSprite

class SpriteSetBoundsFactory(private val bounds: Sprite = nullSprite): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetBoundsFactory(bounds)
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetBounds(sprite, bounds)
  }

  override fun toString(): String = "Установить границы в $bounds"
}

class SpriteSetBounds(sprite: Sprite, var bounds: Sprite = nullSprite): SpriteAction(sprite) {
  override fun execute() {
    if(sprite.rightX < bounds.leftX || sprite.leftX > bounds.rightX || sprite.bottomY < bounds.topY || sprite.topY > bounds.bottomY) {
      scene.remove(sprite)
    }
  }

  override fun toString(): String = "Установить границы $sprite в $bounds"
}