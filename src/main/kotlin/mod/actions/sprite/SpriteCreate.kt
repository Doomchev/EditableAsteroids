package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteClass
import SpriteFactory
import emptyClass
import mod.dragging.parentSprite
import mod.dragging.selectClass
import newActions

class SpriteCreateFactory(private val spriteClass: SpriteClass = emptyClass): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteCreateFactory(selectClass())
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteCreate(sprite, spriteClass)
  }

  override fun toString(): String = "Создать $spriteClass"
}

class SpriteCreate(sprite: Sprite, private val spriteClass: SpriteClass): SpriteAction(sprite) {
  var time: Double = 0.0
  override fun execute() {
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

  override fun toString(): String = "Создать $spriteClass"
}