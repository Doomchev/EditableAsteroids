package state

import Action
import Node
import Serializer
import Sprite
import SpriteAction
import ActionFactory
import SpriteActionFactory
import SpriteEntry
import selectSprite

object spriteSetStateSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return SpriteSetStateFactory(selectSprite(), selectState())
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    return SpriteSetStateFactory(node.getField("spriteentry") as SpriteEntry, findState(node.getString("state")))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteSetState(node.getField("sprite") as Sprite, findState(node.getString("state")))
  }

  override fun toString(): String = "Изменить состояние"
}

class SpriteSetStateFactory(spriteEntry: SpriteEntry, private var state: State): SpriteActionFactory(spriteEntry) {
  override fun create(): Action {
    return SpriteSetState(spriteEntry.resolve(), state)
  }

  override fun toString(): String = "Изменить состояние $spriteEntry на $state"

  override fun toNode(node: Node) {
    node.setString("state", state.name)
  }
}

class SpriteSetState(sprite: Sprite, private var state: State): SpriteAction(sprite) {
  override fun execute() {
    sprite.state = state
  }

  override fun toString(): String = "Изменить состояние $sprite на $state"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setString("state", state.name)
  }
}