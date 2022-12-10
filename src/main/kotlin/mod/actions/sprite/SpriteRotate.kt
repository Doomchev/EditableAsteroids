package mod.actions.sprite

import Formula
import Node
import Sprite
import SpriteAction
import SpriteFactory
import format
import fpsk
import Serializer
import mod.dragging.enterDouble
import kotlin.math.PI

object spriteRotationSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteRotationFactory(enterDouble("Введите скорость поворота (град/сек):"))
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteRotationFactory(node.getFormula("speed"))
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteRotation(node.getField("sprite") as Sprite, node.getDouble("speed"))
  }
}

class SpriteRotationFactory(private var speed: Formula): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpriteRotation(sprite, speed.get() * PI / 180.0)
  }

  override fun toString(): String = "Повернуть"
  override fun fullText(): String = "Повернуть со скоростью $speed"

  override fun toNode(node: Node) {
    node.setFormula("speed", speed)
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
}