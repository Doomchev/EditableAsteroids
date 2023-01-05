package mod.actions.list

import Action
import Node
import Serializer
import SpriteAction
import SpriteActionFactory
import SpriteClass
import nullSprite
import nullSpriteEntry
import selectClass

object isListEmptySerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return IsListEmptyFactory(selectClass())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return IsListEmptyFactory(node.getField("class") as SpriteClass, getActions(node))
  }

  override fun actionFromNode(node: Node): Action {
    return IsListEmpty(node.getField("class") as SpriteClass, getActions(node))
  }

  override fun toString(): String = "При состоянии"
}

class IsListEmptyFactory(private var spriteClass: SpriteClass, var actions: MutableList<SpriteActionFactory>): SpriteActionFactory(nullSpriteEntry) {
  constructor(spriteClass: SpriteClass, vararg actions: SpriteActionFactory) : this(spriteClass, mutableListOf<SpriteActionFactory>()) {
    this.actions.addAll(actions)
  }

  override fun create(): SpriteAction {
    return IsListEmpty(spriteClass, actions)
  }

  override fun addChildBlocks() {
    addChildBlocks(actions)
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