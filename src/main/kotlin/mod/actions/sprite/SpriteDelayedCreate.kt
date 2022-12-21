package mod.actions.sprite

import Formula
import Node
import Serializer
import Sprite
import Action
import SpriteAction
import SpriteClass
import SpriteActionFactory
import blankImage
import emptyClass
import fpsk
import mod.dragging.*
import mod.parentEntry
import mod.project
import newActions
import nullSprite
import zero

object spriteDelayedCreateSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteDelayedCreateFactory(selectSprite("Выберите родительский спрайт:"),  selectClass(), enterDouble("Введите интервал (сек):"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteDelayedCreateFactory(node.getField("spriteentry") as SpriteEntry, node.getField("spriteClass") as SpriteClass, node.getFormula("delay"))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteDelayedCreate(node.getField("sprite") as Sprite, node.getField("spriteClass") as SpriteClass, node.getDouble("delay"), node.getDouble("time"))
  }

  override fun toString(): String = "Создать позже"
}

class SpriteDelayedCreateFactory(spriteEntry: SpriteEntry, private var spriteClass: SpriteClass = emptyClass, private var delay: Formula = zero): SpriteActionFactory(spriteEntry) {
  override fun toString(): String = "Создать позже"
  override fun fullText(): String = "Создать $spriteClass через $delay на основе $spriteEntry"

  override fun create(): SpriteAction {
    return SpriteDelayedCreate(spriteEntry.resolve(), spriteClass, delay.get())
  }

  override fun toNode(node: Node) {
    node.setField("spriteClass", spriteClass)
    node.setFormula("delay", delay)
  }
}

class SpriteDelayedCreate(sprite: Sprite, private var spriteClass: SpriteClass, private var delay: Double, private var time: Double = 0.0): SpriteAction(sprite) {
  override fun execute() {
    time = maxOf(0.0, time - fpsk)
    if(time > 0.0) return
    time = delay
    val newSprite = Sprite(blankImage)
    spriteClass.add(newSprite)
    parentEntry.sprite = sprite
    for(factory in spriteClass.onCreate) {
      factory.create(newSprite).execute()
    }
    for(action in spriteClass.always) {
      newActions.add(action.create(newSprite))
    }
  }

  override fun toString(): String = "Создать $spriteClass через $delay на основе $sprite"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setField("spriteClass", spriteClass)
    node.setDouble("delay", delay)
    node.setDouble("time", time)
  }
}