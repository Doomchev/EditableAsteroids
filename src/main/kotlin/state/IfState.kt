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
import selectSprite
import java.util.*

object ifStateSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return IfStateFactory(selectSprite(), selectState())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    val actions = LinkedList<SpriteActionFactory>()
    node.getField("actions", actions)
    return IfStateFactory(node.getField("spriteentry") as SpriteEntry, findStates(node.getString("state")), actions)
  }

  override fun actionFromNode(node: Node): Action {
    val actions = LinkedList<SpriteActionFactory>()
    node.getField("actions", actions)
    return IfState(node.getField("sprite") as Sprite, findStates(node.getString("state")), actions)
  }

  override fun toString(): String = "При состоянии"
}

class IfStateFactory(spriteEntry: SpriteEntry, private var values: MutableList<State>, var actions: MutableList<SpriteActionFactory>): SpriteActionFactory(spriteEntry) {

  constructor(spriteEntry: SpriteEntry, value: State, vararg actions: SpriteActionFactory) : this(spriteEntry, mutableListOf(value), mutableListOf<SpriteActionFactory>()) {
    this.actions.addAll(actions)
  }

  constructor(spriteEntry: SpriteEntry, values: MutableList<State>, vararg actions: SpriteActionFactory) : this(spriteEntry, values, mutableListOf<SpriteActionFactory>()) {
    this.actions.addAll(actions)
  }

  override fun create(): SpriteAction {
    return IfState(spriteEntry.resolve(), values, actions)
  }

  override fun addChildBlocks() {
    for(factory in actions) {
      blocks.add(FactoryBlock(factory, actions,"    ${factory.fullText()}", true))
      factory.addChildBlocks()
    }
  }

  override fun toString(): String = "Если $caption == $values"

  override fun toNode(node: Node) {
  }
}

class IfState(sprite: Sprite, private var states: MutableList<State>, private var actions: MutableList<SpriteActionFactory>): SpriteAction(sprite) {
  override fun execute() {
    for(state in states) {
      if(sprite.state == state) {
        for(factory in actions) {
          factory.create().execute()
        }
        return
      }
    }
  }

  override fun toString(): String = "Если $sprite == $states"
}