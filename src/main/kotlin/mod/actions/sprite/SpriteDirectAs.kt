package mod.actions.sprite

import Node
import Sprite
import Action
import SpriteActionFactory
import Serializer
import SpriteAction
import SpriteEntry
import mod.dragging.selectSprite

object spriteDirectAsSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteDirectAsFactory(selectSprite(), selectSprite("Направить как:"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteDirectAsFactory(node.getField("spriteentry") as SpriteEntry, node.getField("template") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteDirectAs(node.getField("sprite") as Sprite, node.getField("template") as Sprite)
  }

  override fun toString(): String = "Направить как"
}

class SpriteDirectAsFactory(spriteEntry: SpriteEntry, private var template: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteDirectAs(spriteEntry.resolve(), template.resolve())
  }

  override fun toString(): String = "Направить $spriteEntry как $template"

  override fun toNode(node: Node) {
    node.setField("template", template)
  }
}

class SpriteDirectAs(sprite: Sprite, private var template: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle = template.angle
  }

  override fun toString(): String = "Направить $sprite как $template"

  override fun toNode(node: Node) {
    node.setField("template", template)
  }
}

