package mod.actions.sprite

import Sprite
import SpriteAction
import mod.dragging.selectedSprites

class SpriteDirectAs: SpriteAction() {
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
    sprite!!.angle = sprite2!!.angle
  }

  override fun toString(): String = "Повернуть как"
}