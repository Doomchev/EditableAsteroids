package mod.actions.sprite

import Formula
import Node
import Sprite
import SpriteAction
import SpriteClass
import SpriteFactory
import blankImage
import emptyClass
import fpsk
import Serializer
import mod.dragging.enterDouble
import mod.dragging.parentSprite
import mod.dragging.selectClass
import newActions
import zero

object spriteDelayedCreateSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteDelayedCreateFactory(selectClass(), enterDouble("Введите интервал (сек):"))
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteDelayedCreateFactory(node.getField("spriteClass") as SpriteClass, node.getFormula("delay"))
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteDelayedCreate(node.getField("sprite") as Sprite,
    node.getField("spriteClass") as SpriteClass, node.getDouble("delay"), node.getDouble("time"))
  }

  override fun toString(): String = "Создать позже"
}

class SpriteDelayedCreateFactory(private var spriteClass: SpriteClass = emptyClass, private var delay: Formula = zero): SpriteFactory() {
  override fun toString(): String = "Создать позже"
  override fun fullText(): String = "Создать $spriteClass через $delay"

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteDelayedCreate(sprite, spriteClass, delay.get())
  }

  override fun toNode(node: Node) {
    node.setField("spriteClass", spriteClass)
    node.setFormula("delay", delay)
  }
}

class SpriteDelayedCreate(sprite: Sprite, private var spriteClass: SpriteClass, private var delay: Double, var time: Double = 0.0): SpriteAction(sprite) {
  override fun execute() {
    time = maxOf(0.0, time - fpsk)
    if(time > 0.0) return
    time = delay
    val newSprite = Sprite(blankImage)
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

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setField("spriteClass", spriteClass)
    node.setDouble("delay", delay)
    node.setDouble("time", time)
  }
}