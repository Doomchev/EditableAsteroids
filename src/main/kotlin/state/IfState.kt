package state

import Action
import FactoryBlock
import Node
import Serializer
import Sprite
import SpriteAction
import ActionFactory
import SpriteActionFactory
import SpriteEntry
import blocks
import indent
import selectSprite

object ifStateSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return IfStateFactory(selectSprite(), selectState())
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    val actions = mutableListOf<ActionFactory>()
    node.getField("actions", actions)
    return IfStateFactory(node.getField("spriteentry") as SpriteEntry, findStates(node.getString("state")), actions)
  }

  override fun actionFromNode(node: Node): Action {
    val actions = mutableListOf<Action>()
    node.getField("actions", actions)
    return IfState(node.getField("sprite") as Sprite, findStates(node.getString("state")), actions)
  }

  override fun toString(): String = "При состоянии"
}

class IfStateFactory(spriteEntry: SpriteEntry, private var values: MutableList<State>, private var factories: MutableList<ActionFactory>): SpriteActionFactory(spriteEntry) {

  constructor(spriteEntry: SpriteEntry, value: State, vararg factories: ActionFactory) : this(spriteEntry, mutableListOf(value), mutableListOf<ActionFactory>()) {
    this.factories.addAll(factories)
  }

  constructor(spriteEntry: SpriteEntry, values: MutableList<State>, vararg factories: ActionFactory) : this(spriteEntry, values, mutableListOf<ActionFactory>()) {
    this.factories.addAll(factories)
  }

  override fun create(): Action {
    val actions = mutableListOf<Action>()
    for(factory in factories) {
      actions.add(factory.create())
    }
    return IfState(spriteEntry.resolve(), values, actions)
  }

  override fun addChildBlocks() {
    indent += "  "
    for(factory in factories) {
      blocks.add(FactoryBlock(factory, factories,"$indent${factory.fullText()}", true))
      factory.addChildBlocks()
    }
    indent = indent.substring(2)
  }

  override fun toString(): String = "Если $caption == $values"

  override fun toNode(node: Node) {
    node.setField("actions", factories)
  }
}

class IfState(sprite: Sprite, private var states: MutableList<State>, private var actions: MutableList<Action>): SpriteAction(sprite) {
  override fun execute() {
    for(state in states) {
      if(sprite.state == state) {
        for(action in actions) {
          action.execute()
        }
        return
      }
    }
  }

  override fun toString(): String = "Если $sprite == $states"
}