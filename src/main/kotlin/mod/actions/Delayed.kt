package mod.actions

import Action
import FactoryBlock
import Formula
import Node
import Serializer
import Sprite
import SpriteAction
import ActionFactory
import SpriteEntry
import blocks
import delayedActions
import fpsk
import indent
import mod.dragging.enterDouble
import nullSprite
import selectSprite

object delaySerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return DelayFactory(enterDouble("Введите время в секундах:"), mutableListOf())
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    val actions = mutableListOf<ActionFactory>()
    node.getField("actions", actions)
    return DelayFactory(node.getFormula("time"), actions)
  }

  override fun actionFromNode(node: Node): Action {
    val actions = mutableListOf<Action>()
    node.getField("actions", actions)
    return Delay(node.getDouble("time"), actions)
  }

  override fun toString(): String = "Запустить через"
}

class DelayFactory(private var time: Formula, private var factories: MutableList<ActionFactory>): ActionFactory() {
  constructor(time: Formula, vararg factories: ActionFactory): this(time, mutableListOf<ActionFactory>()) {
    this.factories.addAll(factories)
  }

  override fun create(): Action {
    val actions = mutableListOf<Action>()
    for(factory in factories) {
      actions.add(factory.create())
    }
    val delay = Delay(time.getDouble(), actions)
    delayedActions.add(delay)
    return delay
  }

  override fun addChildBlocks() {
    addChildBlocks(factories)
  }

  override fun toString(): String = "Запустить через $time сек"

  override fun toNode(node: Node) {
    node.setField("actions", factories)
  }
}

interface DelayedAction: Action {
  fun check(): Boolean
}

class Delay(private var time: Double, private var actions: MutableList<Action>): Action, DelayedAction  {
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