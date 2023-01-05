package mod.actions.list

import Action
import FactoryBlock
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteClass
import SpriteEntry
import blocks
import indent
import nullSprite
import nullSpriteEntry
import selectClass
import state.State

object isListEmptySerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return IsListEmptyFactory(selectClass())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    val actions = mutableListOf<SpriteActionFactory>()
    node.getField("actions", actions)
    return IsListEmptyFactory(node.getField("class") as SpriteClass, actions)
  }

  override fun actionFromNode(node: Node): Action {
    val actions = mutableListOf<SpriteActionFactory>()
    node.getField("actions", actions)
    return IsListEmpty(node.getField("class") as SpriteClass, actions)
  }

  override fun toString(): String = "При состоянии"
}

class IsListEmptyFactory(private var spriteClass: SpriteClass, var actions: MutableList<SpriteActionFactory>): SpriteActionFactory(nullSpriteEntry) {
  constructor(spriteClass: SpriteClass, vararg actions: SpriteActionFactory) : this(spriteClass, mutableListOf<SpriteActionFactory>()) {
    this.actions.addAll(actions)
  }

  constructor(spriteClass: SpriteClass, values: MutableList<State>, vararg actions: SpriteActionFactory) : this(spriteClass, mutableListOf<SpriteActionFactory>()) {
    this.actions.addAll(actions)
  }

  override fun create(): SpriteAction {
    return IsListEmpty(spriteClass, actions)
  }

  override fun addChildBlocks() {
    indent += "  "
    for(factory in actions) {
      blocks.add(FactoryBlock(factory, actions,"$indent${factory.fullText()}", true))
      factory.addChildBlocks()
    }
    indent = indent.substring(2)
  }

  override fun toString(): String = "Если $caption "

  override fun toNode(node: Node) {
    node.setField("actions", actions)
  }
}

class IsListEmpty(private var spriteClass: SpriteClass, private var actions: MutableList<SpriteActionFactory>): SpriteAction(nullSprite) {
  override fun execute() {
    if(spriteClass.sprites.isNotEmpty()) return
    for(factory in actions) {
      factory.create().execute()
    }
  }

  override fun toString(): String = "Если нет $spriteClass"
}