package mod.actions.sprite

import Sprite
import SpriteAction
import mod.dragging.enterDouble

class SpriteSetSize: SpriteAction() {
  var width: Double = 0.0
  var height: Double = 0.0

  override fun create(sprite: Sprite?): SpriteAction {
    val action = SpriteSetSize()
    action.width = width
    action.height = height
    return action
  }

  override fun settings() {
    width = enterDouble("Введите ширину:")
    height = enterDouble("Введите высоту:")
  }

  override fun execute() {
    sprite!!.width = width
    sprite!!.height = height
  }

  override fun toString(): String {
    return "Установить размер"
  }
}