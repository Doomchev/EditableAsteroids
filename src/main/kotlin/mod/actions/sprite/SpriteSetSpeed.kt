package mod.actions.sprite

import Formula
import Node
import Sprite
import Action
import SpriteActionFactory
import Serializer
import SpriteAction
import SpriteEntry
import mod.dragging.enterDouble
import mod.dragging.selectSprite
import kotlin.math.cos
import kotlin.math.sin

object spriteSetSpeedSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteSetSpeedFactory(selectSprite(), enterDouble("Введите скорость:"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteSetSpeedFactory(node.getField("spriteentry") as SpriteEntry, node.getFormula("speed"))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteSetSpeed(node.getField("sprite") as Sprite, node.getDouble("speed"))
  }

  override fun toString(): String = "Задать скорость"
}

class SpriteSetSpeedFactory(spriteEntry: SpriteEntry, private var speed: Formula): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteSetSpeed(spriteEntry.resolve(), speed.get())
  }

  override fun toString(): String = "Задать скорость"
  override fun fullText(): String = "Задать скорость $speed для $spriteEntry"

  override fun toNode(node: Node) {
    node.setFormula("speed", speed)
  }
}

class SpriteSetSpeed(sprite: Sprite, private var speed: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.dx = speed * cos(sprite.angle)
    sprite.dy = speed * sin(sprite.angle)
  }

  override fun toString(): String = "Задать для $sprite скорость $speed"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("speed", speed)
  }
}