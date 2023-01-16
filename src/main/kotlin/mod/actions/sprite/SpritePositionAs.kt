package mod.actions.sprite

import Action
import Node
import Serializer
import Sprite
import SpriteAction
import ActionFactory
import SpriteActionFactory
import SpriteEntry
import selectSprite

object spritePositionAsSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return SpritePositionAsFactory(selectSprite("Выберите спрайт, который перемещается:"), selectSprite("Выберите спрайт, к которому перемещается:"))
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    return SpritePositionAsFactory(node.getField("spriteentry") as SpriteEntry, node.getField("destination") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpritePositionAs(node.getField("source") as Sprite, node.getField("destination") as Sprite)
  }

  override fun toString(): String = "Переместить"
}

class SpritePositionAsFactory(spriteEntry: SpriteEntry, private var destination: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpritePositionAs(spriteEntry.resolve(), destination.resolve())
  }

  override fun toString(): String = "Переместить$caption к $destination"

  override fun toNode(node: Node) {
    node.setField("destination", destination)
  }
}

class SpritePositionAs(sprite: Sprite, private var destination: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX = destination.centerX
    sprite.centerY = destination.centerY
  }

  override fun toString(): String = "Переместить $sprite к $destination"

  override fun toNode(node: Node) {
    node.setField("destination", destination)
  }
}

