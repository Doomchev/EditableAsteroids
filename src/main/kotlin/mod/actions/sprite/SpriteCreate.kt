package mod.actions.sprite

import Sprite
import SpriteAction
import Function

class SpriteCreate: SpriteAction() {
  var function: Function? = null

  override fun create(sprite: Sprite?): SpriteAction {
    val action = SpriteCreate()
    action.sprite = sprite
    return action
  }

  override fun settings() {
  }

  override fun execute() {
  }

  override fun toString(): String = "Создать"
}