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

object clearListSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return ClearListFactory(selectClass())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return ClearListFactory(node.getField("class") as SpriteClass)
  }

  override fun actionFromNode(node: Node): Action {
    return ClearList(node.getField("class") as SpriteClass)
  }

  override fun toString(): String = "Очистить список"
}

class ClearListFactory(private var spriteClass: SpriteClass, var actions: MutableList<SpriteActionFactory>): SpriteActionFactory(nullSpriteEntry) {
  constructor(spriteClass: SpriteClass, vararg actions: SpriteActionFactory) : this(spriteClass, mutableListOf<SpriteActionFactory>()) {
    this.actions.addAll(actions)
  }

  override fun create(): SpriteAction {
    return ClearList(spriteClass)
  }

  override fun toString(): String = "Удалить все $caption "

  override fun toNode(node: Node) {
    node.setField("actions", actions)
  }
}

class ClearList(private var spriteClass: SpriteClass): SpriteAction(nullSprite) {
  override fun execute() {
    spriteClass.sprites.clear()
  }

  override fun toString(): String = "Удалить все $spriteClass"
}