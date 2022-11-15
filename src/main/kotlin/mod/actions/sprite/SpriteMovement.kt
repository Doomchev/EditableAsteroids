package mod.actions.sprite

import Sprite
import SpriteAction
import fpsk
import mod.dragging.enterDouble
import mod.dragging.selectedSprites

class SpriteMovement: SpriteAction() {
  override fun create(sprite: Sprite?): SpriteAction {
    val action = SpriteMovement()
    action.sprite = sprite
    return action
  }

  override fun settings() {
    val dx = enterDouble("Введите приращение по Х:")
    val dy = enterDouble("Введите приращение по Y:")
    for(spr in selectedSprites) {
      spr.movingVector.x = dx
      spr.movingVector.y = dy
    }
  }

  override fun execute() {
    sprite!!.centerX += fpsk * sprite!!.movingVector.x
    sprite!!.centerY += fpsk * sprite!!.movingVector.y
  }

  override fun toString(): String = "Перемещать"
}