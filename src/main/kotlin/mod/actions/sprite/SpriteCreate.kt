package mod.actions.sprite

import Formula
import Sprite
import SpriteAction
import SpriteClass
import SpriteFactory
import actions
import classes
import emptyClass
import fpsk
import frame
import mod.dragging.enterDouble
import mod.dragging.selectClass
import zero
import javax.swing.JOptionPane
import kotlin.math.cos
import kotlin.math.sin

class SpriteCreateFactory(val spriteClass: SpriteClass = emptyClass, private val delay: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteCreateFactory(selectClass(), enterDouble("Введите интервал (сек):"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteCreate(sprite, spriteClass, delay.get())
  }

  override fun toString(): String = "Создать"
}

class SpriteCreate(sprite: Sprite, private val spriteClass: SpriteClass, private val delay: Double): SpriteAction(sprite) {
  var time: Double = 0.0
  override fun execute() {
    time = maxOf(0.0, time - fpsk)
    if(time > 0.0) return
    time = delay
    val newSprite = Sprite()
    spriteClass.add(newSprite)
    for(factory in spriteClass.onCreate) {
      factory.create(newSprite).execute()
    }
    for(action in spriteClass.always) {
      actions.add(action.create(newSprite))
    }
  }
}