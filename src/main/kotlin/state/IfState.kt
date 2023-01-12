package state

import Action
import FactoryBlock
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import blocks
import indent
import selectSprite

object ifStateSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return IfStateFactory(selectSprite(), selectState())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    val actions = mutableListOf<SpriteActionFactory>()
    node.getField("actions", actions)
    return IfStateFactory(node.getField("spriteentry") as SpriteEntry, findStates(node.getString("state")), actions)
  }

  override fun actionFromNode(node: Node): Action {
    val actions = mutableListOf<SpriteAction>()
    node.getField("actions", actions)
    return IfState(node.getField("sprite") as Sprite, findStates(node.getString("state")), actions)
  }

  override fun toString(): String = "При состоянии"
}

class IfStateFactory(spriteEntry: SpriteEntry, private var values: MutableList<State>, private var factories: MutableList<SpriteActionFactory>): SpriteActionFactory(spriteEntry) {

  constructor(spriteEntry: SpriteEntry, value: State, vararg factories: SpriteActionFactory) : this(spriteEntry, mutableListOf(value), mutableListOf<SpriteActionFactory>()) {
    this.factories.addAll(factories)
  }

  constructor(spriteEntry: SpriteEntry, values: MutableList<State>, vararg factories: SpriteActionFactory) : this(spriteEntry, values, mutableListOf<SpriteActionFactory>()) {
    this.factories.addAll(factories)
  }

  override fun create(): SpriteAction {
    val actions = mutableListOf<SpriteAction>()
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

class IfState(sprite: Sprite, private var states: MutableList<State>, private var actions: MutableList<SpriteAction>): SpriteAction(sprite) {
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