package mod.actions.sprite

import Formula
import Sprite
import SpriteAction
import SpriteClass
import SpriteFactory
import emptyClass
import fpsk
import mod.dragging.enterDouble
import mod.dragging.parentSprite
import mod.dragging.selectClass
import newActions
import zero

class SpriteDelayedCreateFactory(private val spriteClass: SpriteClass = emptyClass, private val delay: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteDelayedCreateFactory(selectClass(), enterDouble("Введите интервал (сек):"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteDelayedCreate(sprite, spriteClass, delay.get())
  }

  override fun toString(): String = "Создать $spriteClass через $delay"
}

class SpriteDelayedCreate(sprite: Sprite, private val spriteClass: SpriteClass, private val delay: Double): SpriteAction(sprite) {
  var time: Double = 0.0
  override fun execute() {
    time = maxOf(0.0, time - fpsk)
    if(time > 0.0) return
    time = delay
    val newSprite = Sprite()
    spriteClass.add(newSprite)
    parentSprite = sprite
    for(factory in spriteClass.onCreate) {
      factory.create(newSprite).execute()
    }
    for(action in spriteClass.always) {
      newActions.add(action.create(newSprite))
    }
  }

  override fun toString(): String = "Создать $spriteClass через $delay"
}