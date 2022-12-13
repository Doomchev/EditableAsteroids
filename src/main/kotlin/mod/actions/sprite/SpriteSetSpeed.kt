package mod.actions.sprite

import Formula
import Node
import Sprite
import SpriteAction
import SpriteFactory
import Serializer
import mod.dragging.enterDouble
import kotlin.math.cos
import kotlin.math.sin

object spriteSetSpeedSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteSetSpeedFactory(enterDouble("Введите скорость:"))
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteSetSpeedFactory(node.getFormula("speed"))
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteSetSpeed(node.getField("sprite") as Sprite, node.getDouble("speed"))
  }

  override fun toString(): String = "Задать скорость"
}

class SpriteSetSpeedFactory(private var speed: Formula): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetSpeed(sprite, speed.get())
  }

  override fun toString(): String = "Задать скорость"
  override fun fullText(): String = "Задать скорость $speed"

  override fun toNode(node: Node) {
    node.setFormula("speed", speed)
  }
}

class SpriteSetSpeed(sprite: Sprite, private var speed: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.dx = speed * cos(sprite.angle)
    sprite.dy = speed * sin(sprite.angle)
  }

  override fun toString(): String = "Задать $sprite скорость $speed"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("speed", speed)
  }
}