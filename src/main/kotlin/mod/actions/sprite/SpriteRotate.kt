package mod.actions.sprite

import Formula
import Node
import Sprite
import SpriteAction
import SpriteFactory
import format
import fpsk
import mod.dragging.enterDouble
import zero
import kotlin.math.PI

class SpriteRotationFactory(private var speed: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteRotationFactory(enterDouble("Введите скорость поворота (град/сек):"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteRotation(sprite, speed.get() * PI / 180.0)
  }

  override fun toString(): String = "Повернуть"
  override fun fullText(): String = "Повернуть со скоростью $speed"

  override fun toNode(node: Node) {
    node.setFormula("speed", speed)
  }

  override fun fromNode(node: Node) {
    speed = node.getFormula("speed")
  }
}

class SpriteRotation(sprite: Sprite, private var speed: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle += fpsk * speed
  }

  override fun toString(): String = "Повернуть $sprite со скоростью ${format(speed)}"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("speed", speed)
  }

  override fun fromNode(node: Node) {
    sprite = node.getField("sprite") as Sprite
    speed = node.getDouble("speed")
  }
}