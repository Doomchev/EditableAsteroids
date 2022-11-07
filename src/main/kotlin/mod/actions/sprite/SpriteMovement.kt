package mod.actions.sprite

import Sprite
import fpsk
import mod.dragging.SpriteAction

class SpriteMovement: SpriteAction() {
  override fun create(sprite: Sprite): SpriteAction {
    val action = SpriteMovement()
    action.sprite = sprite
    return action
  }

  override fun execute() {
    sprite!!.centerX += fpsk * sprite!!.movingVector.x
    sprite!!.centerY += fpsk * sprite!!.movingVector.y
  }
}