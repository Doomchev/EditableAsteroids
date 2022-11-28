package mod.actions.sprite

import Formula
import Node
import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.enterDouble
import zero
import kotlin.math.cos
import kotlin.math.sin

class SpriteSetSpeedFactory(private var speed: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetSpeedFactory(enterDouble("Введите скорость:"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetSpeed(sprite, speed.get())
  }

  override fun toString(): String = "Задать скорость"
  override fun fullText(): String = "Задать скорость $speed"

  override fun getClassName(): String = "SpriteSetSpeedFactory"

  override fun store(node: Node) {
    node.setFormula("speed", speed)
  }

  override fun load(node: Node) {
    speed = node.getFormula("speed")
  }
}

class SpriteSetSpeed(sprite: Sprite, private var speed: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.dx = speed * cos(sprite.angle)
    sprite.dy = speed * sin(sprite.angle)
  }

  override fun toString(): String = "Задать $sprite скорость $speed"

  override fun getClassName(): String = "SpriteSetAngle"

  override fun store(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("speed", speed)
  }

  override fun load(node: Node) {
    sprite = node.getField("sprite") as Sprite
    speed = node.getDouble("speed")
  }
}