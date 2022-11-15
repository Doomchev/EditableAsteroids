package mod.actions.sprite

import Sprite
import SpriteAction
import mod.dragging.enterDouble
import mod.dragging.selectedSprites
import kotlin.math.PI

class SpriteDirectAs: SpriteAction() {
  var speed: Double = 0.0
  var asSprite: Sprite? = null

  override fun create(sprite: Sprite?): SpriteAction {
    val action = SpriteDirectAs()
    action.sprite = sprite
    action.asSprite = asSprite
    return action
  }

  override fun settings() {
    asSprite = selectedSprites.first
  }

  override fun execute() {
    sprite!!.angle = asSprite!!.angle
  }

  override fun toString(): String {
    return "Повернуть как выделенный"
  }
}