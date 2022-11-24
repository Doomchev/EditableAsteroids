package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteFactory
import fpsk
import mod.dragging.enterDouble
import mod.dragging.scene

class SpriteRemoveFactory(private val delay: Double = 0.0): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteRemoveFactory(enterDouble("Введите задержку:").get())
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteRemove(sprite, delay)
  }

  override fun toString(): String = "Удалить через $delay сек."
}

class SpriteRemove(sprite: Sprite, var delay: Double = 0.0): SpriteAction(sprite) {
  override fun execute() {
    delay -= fpsk
    if(delay <= 0) scene.remove(sprite)
  }

  override fun toString(): String = "Удалить через $delay сек."
}