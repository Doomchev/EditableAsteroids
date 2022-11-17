package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteClass
import actions
import classes
import fpsk
import mod.dragging.enterDouble
import playSound

class SpriteCreate: SpriteAction() {
  var spriteClass: SpriteClass? = null
  var delay: Double = 0.0
  var time: Double = 0.0

  override fun create(sprite: Sprite?): SpriteAction {
    val action = SpriteCreate()
    action.sprite = sprite
    action.spriteClass = classes.first
    action.delay = delay
    return action
  }

  override fun settings() {
    delay = enterDouble("Введите интервал:")
  }

  override fun execute() {
    time = maxOf(0.0, time - fpsk)
    if(time > 0.0) return
    time = delay
    val newSprite = Sprite()
    spriteClass!!.add(newSprite)
    for(action in spriteClass!!.onCreate) {
      action.sprite = newSprite
      action.execute()
    }
    for(action in spriteClass!!.always) {
      actions.add(action.create(newSprite))
    }
  }

  override fun toString(): String = "Создать"
}