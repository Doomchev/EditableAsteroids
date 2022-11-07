package mod.actions.sprite

import Sprite
import fpsk
import mod.dragging.SpriteAction
import mod.dragging.enterDouble

class SpriteDeceleration: SpriteAction() {
  var speed = 0.0

  override fun create(sprite: Sprite): SpriteAction {
    val action = SpriteDeceleration()
    action.sprite = sprite
    action.speed = speed
    return action
  }

  override fun settings() {
    speed = enterDouble("Введите значение уменьшения длины:")
  }

  override fun execute() {
    val length = sprite!!.movingVector.length
    val newLength = length - speed * fpsk
    if(newLength <= 0) {
      sprite!!.movingVector.x = 0.0
      sprite!!.movingVector.y = 0.0
    } else {
      sprite!!.movingVector.x *= newLength / length
      sprite!!.movingVector.y *= newLength / length
    }
  }
}