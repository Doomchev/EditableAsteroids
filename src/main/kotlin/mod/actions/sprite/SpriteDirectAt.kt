package mod.actions.sprite

import Action
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import mod.dragging.selectSprite
import kotlin.math.atan2

object spriteDirectAtSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteDirectAtFactory(selectSprite(), selectSprite("Направить как:"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteDirectAtFactory(node.getField("spriteentry") as SpriteEntry, node.getField("template") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteDirectAt(node.getField("sprite") as Sprite, node.getField("template") as Sprite)
  }

  override fun toString(): String = "Направить на"
}

class SpriteDirectAtFactory(spriteEntry: SpriteEntry, private var template: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteDirectAt(spriteEntry.resolve(), template.resolve())
  }

  override fun toString(): String = "Направить$caption на $template"

  override fun toNode(node: Node) {
    node.setField("template", template)
  }
}

class SpriteDirectAt(sprite: Sprite, private var template: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle = atan2(sprite.centerY - template.centerY, sprite.centerX - template.centerX)
  }

  override fun toString(): String = "Направить $sprite на $template"

  override fun toNode(node: Node) {
    node.setField("template", template)
  }
}

