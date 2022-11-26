package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteFactory
import actionsToRemove
import mod.dragging.scene
import mod.dragging.selectedSprites
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
}

class SpriteSetBounds(sprite: Sprite, var bounds: Sprite = nullSprite): SpriteAction(sprite) {
  override fun execute() {
    if(sprite.rightX < bounds.leftX || sprite.leftX > bounds.rightX || sprite.bottomY < bounds.topY || sprite.topY > bounds.bottomY) {
      spritesToRemove.add(sprite)
      actionsToRemove.add(this)
    }
  }

  override fun toString(): String = "Установить границы $sprite в $bounds"
}