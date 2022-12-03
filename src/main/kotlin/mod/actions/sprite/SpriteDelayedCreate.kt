package mod.actions.sprite

import Formula
import Node
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

class SpriteDelayedCreateFactory(private var spriteClass: SpriteClass = emptyClass, private var delay: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteDelayedCreateFactory(selectClass(), enterDouble("Введите интервал (сек):"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteDelayedCreate(sprite, spriteClass, delay.get())
  }

  override fun toString(): String = "Создать позже"
  override fun fullText(): String = "Создать $spriteClass через $delay"

  override fun toNode(node: Node) {
    node.setField("spriteClass", spriteClass)
    node.setFormula("delay", delay)
  }

  override fun fromNode(node: Node) {
    spriteClass = node.getField("spriteClass") as SpriteClass
    delay = node.getFormula("delay")
  }
}

class SpriteDelayedCreate(sprite: Sprite, private var spriteClass: SpriteClass, private var delay: Double): SpriteAction(sprite) {
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

  override fun toNode(node: Node) {
    node.setField("spriteClass", spriteClass)
    node.setDouble("delay", delay)
    node.setDouble("time", time)
  }

  override fun fromNode(node: Node) {
    spriteClass = node.getField("spriteClass") as SpriteClass
    delay = node.getDouble("delay")
    time = node.getDouble("time")
  }
}