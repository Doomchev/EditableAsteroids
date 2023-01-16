package mod.actions.list

import Action
import Node
import Serializer
import SpriteAction
import ActionFactory
import SpriteClass
import nullSprite
import nullSpriteEntry
import selectClass

object clearListSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return ClearListFactory(selectClass())
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    return ClearListFactory(node.getField("class") as SpriteClass)
  }

  override fun actionFromNode(node: Node): Action {
    return ClearList(node.getField("class") as SpriteClass)
  }

  override fun toString(): String = "Очистить список"
}

class ClearListFactory(private var spriteClass: SpriteClass, var actions: MutableList<ActionFactory>): ActionFactory() {
  constructor(spriteClass: SpriteClass, vararg actions: ActionFactory) : this(spriteClass, mutableListOf<ActionFactory>()) {
    this.actions.addAll(actions)
  }

  override fun create(): Action {
    return ClearList(spriteClass)
  }

  override fun toString(): String = "Удалить все $caption "

  override fun toNode(node: Node) {
    node.setField("actions", actions)
  }
}

class ClearList(private var spriteClass: SpriteClass): Action {
  override fun execute() {
    spriteClass.sprites.clear()
  }

  override fun toString(): String = "Удалить все $spriteClass"
}