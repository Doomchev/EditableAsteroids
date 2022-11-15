package mod.actions.sprite

import Sprite
import SpriteAction
import mod.dragging.selectedSprites

class SpriteMoveTo: SpriteAction() {
  var speed: Double = 0.0
  var sprite2: Sprite? = null

  override fun create(sprite: Sprite?): SpriteAction {
    val action = SpriteDirectAs()
    action.sprite = sprite
    action.sprite2 = sprite2
    return action
  }

  override fun settings() {
    sprite2 = selectedSprites.first
  }

  override fun execute() {
    sprite!!.centerX = sprite2!!.centerX
    sprite!!.centerY = sprite2!!.centerY
  }

  override fun toString(): String = "Переместить на выделенный"
}