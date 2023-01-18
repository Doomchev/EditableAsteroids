package mod.actions.list

import Action
import Node
import Serializer
import SpriteAction
import ActionFactory
import SpriteClass
import nullSprite
import selectClass

object isListEmptySerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return IsListEmptyFactory(selectClass())
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    return IsListEmptyFactory(node.getField("class") as SpriteClass, getActions(node))
  }

  override fun actionFromNode(node: Node): Action {
    return IsListEmpty(node.getField("class") as SpriteClass, getActions(node))
  }

  override fun toString(): String = "При состоянии"
}

class IsListEmptyFactory(private var spriteClass: SpriteClass, var actions: MutableList<ActionFactory>): ActionFactory() {
  constructor(spriteClass: SpriteClass, vararg actions: ActionFactory) : this(spriteClass, mutableListOf<ActionFactory>()) {
    this.actions.addAll(actions)
  }

  override fun create(): SpriteAction {
    return IsListEmpty(spriteClass, actions)
  }

  override fun addChildBlocks() {
    addChildFactoryBlocks(actions)
  }

  override fun toString(): String = "Если $caption пуст"

  override fun toNode(node: Node) {
    node.setField("actions", actions)
  }
}

class IsListEmpty(private var spriteClass: SpriteClass, private var actions: MutableList<ActionFactory>): SpriteAction(nullSprite) {
  override fun execute() {
    if(spriteClass.sprites.isNotEmpty()) return
    for(factory in actions) {
      factory.create().execute()
    }
  }

  override fun toString(): String = "Если $spriteClass пуст"
}