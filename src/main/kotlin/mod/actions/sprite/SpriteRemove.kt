package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteFactory
import actionsToRemove
import spritesToRemove

class SpriteRemoveFactory(): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteRemoveFactory()
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteRemove(sprite)
  }

  override fun fullText(): String = "Удалить"
}

class SpriteRemove(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    spritesToRemove.add(sprite)
    actionsToRemove.add(this)
  }

  override fun toString(): String = "Удалить"
}