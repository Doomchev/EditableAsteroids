package mod.actions.sprite

import Action
import Formula
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import mod.dragging.enterDouble
import selectSprite

object spriteMoveSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteSetMovingVectorFactory(
      selectSprite()
      , enterDouble("Введите приращение по Х:")
      , enterDouble("Введите приращение по Y:"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteMoveFactory(
      node.getField("spriteentry") as SpriteEntry
      , node.getFormula("length"))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteMove(node.getField("sprite") as Sprite
      , node.getDouble("length"))
  }

  override fun toString(): String = "Задать движение"
}

class SpriteMoveFactory(spriteEntry: SpriteEntry, private var speed: Formula): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteMove(spriteEntry.resolve(), speed.get())
  }

  override fun toString(): String = "Переместить"
  override fun fullText(): String = "Переместить$caption вперёд на $speed"

  override fun toNode(node: Node) {
    node.setFormula("length", speed)
  }
}

class SpriteMove(sprite: Sprite, private var speed: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX += sprite.dx * speed
    sprite.centerY += sprite.dy * speed
  }

  override fun toString(): String = "Переместить $sprite вперёд на $speed"

  override fun toNode(node: Node) {
    node.setDouble("length", speed)
  }
}
