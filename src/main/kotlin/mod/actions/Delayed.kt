package mod.actions

import Action
import FactoryBlock
import Formula
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import blocks
import delayedActions
import fpsk
import indent
import mod.dragging.enterDouble
import newActions
import nullSprite
import selectSprite

object delaySerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return DelayFactory(selectSprite(), enterDouble("Введите время в секундах:"), mutableListOf())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    val actions = mutableListOf<SpriteActionFactory>()
    node.getField("actions", actions)
    return DelayFactory(node.getField("sprite") as SpriteEntry, node.getFormula("time"), actions)
  }

  override fun actionFromNode(node: Node): Action {
    val actions = mutableListOf<SpriteAction>()
    node.getField("actions", actions)
    return Delay(node.getField("sprite") as Sprite, node.getDouble("time"), actions)
  }

  override fun toString(): String = "Запустить через"
}

class DelayFactory(sprite: SpriteEntry, private var time: Formula, private var factories: MutableList<SpriteActionFactory>): SpriteActionFactory(sprite) {
  constructor(sprite: SpriteEntry, time: Formula, vararg factories: SpriteActionFactory): this(sprite, time, mutableListOf<SpriteActionFactory>()) {
    this.factories.addAll(factories)
  }

  override fun create(): SpriteAction {
    val actions = mutableListOf<SpriteAction>()
    for(factory in factories) {
      actions.add(factory.create())
    }
    val delay = Delay(spriteEntry.resolve(), time.getDouble(), actions)
    delayedActions.add(delay)
    return delay
  }

  override fun addChildBlocks() {
    indent += "  "
    for(factory in factories) {
      blocks.add(FactoryBlock(factory, factories,"$indent${factory.fullText()}", true))
      factory.addChildBlocks()
    }
    indent = indent.substring(2)
  }

  override fun toString(): String = "Запустить через $time сек"

  override fun toNode(node: Node) {
    node.setField("actions", factories)
  }
}

interface DelayedAction: Action {
  fun check(): Boolean
}

class Delay(sprite: Sprite, private var time: Double, private var actions: MutableList<SpriteAction>): SpriteAction(nullSprite), DelayedAction  {
  override fun check(): Boolean {
    time = maxOf(0.0, time - fpsk)
    if(time > 0.0) return false
    for(action in actions) {
      action.execute()
    }
    return true
  }

  override fun toString(): String = "Запустить через $time сек"
}