package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteFactory
import fpsk
import mod.dragging.enterDouble
import mod.dragging.selectedSprites

class SpriteMoveFactory: SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteMoveFactory()
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteMove(sprite)
  }

  override fun toString(): String = "Перемещать"
}
class SpriteMove(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX += fpsk * sprite.dx
    sprite.centerY += fpsk * sprite.dy
  }
}