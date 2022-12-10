package mod.actions.sprite

import Formula
import Node
import Sprite
import SpriteAction
import SpriteFactory
import Serializer
import mod.dragging.enterDouble
import zero
import kotlin.math.PI

object spriteSetAngleSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteSetAngleFactory(enterDouble("Введите скорость поворота (град/сек):"))
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteSetAngleFactory(node.getFormula("angle"))
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteSetAngle(node.getField("sprite") as Sprite, node.getDouble("angle"))
  }
}

class SpriteSetAngleFactory(private var angle: Formula = zero): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetAngle(sprite, angle.get() * PI / 180.0)
  }

  override fun toString(): String = "Задать угол"
  override fun fullText(): String = "Задать угол $angle"

  override fun toNode(node: Node) {
    node.setFormula("angle", angle)
  }
}

class SpriteSetAngle(sprite: Sprite, private var angle: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle = angle
  }

  override fun toString(): String = "Задать угол $angle"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("angle", angle)
  }
}