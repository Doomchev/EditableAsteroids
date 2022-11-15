package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteClass
import mod.dragging.enterDouble
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SpriteCreate: SpriteAction() {
  var spriteClass: SpriteClass? = null

  override fun create(sprite: Sprite?): SpriteAction {
    val action = SpriteCreate()
    action.sprite = sprite
    return action
  }

  override fun settings() {
  }

  override fun execute() {
  }

  override fun toString(): String {
    return "Создать"
  }
}