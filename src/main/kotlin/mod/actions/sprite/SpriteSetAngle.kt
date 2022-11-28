package mod.actions.sprite

import Formula
import Node
import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.enterDouble
import zero
import kotlin.math.PI

class SpriteSetAngleFactory(private var angle: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetAngleFactory(enterDouble("Введите скорость поворота (град/сек):"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetAngle(sprite, angle.get() * PI / 180.0)
  }

  override fun toString(): String = "Задать угол"
  override fun fullText(): String = "Задать угол $angle"

  override fun getClassName(): String = "SpriteSetAngleFactory"

  override fun store(node: Node) {
    node.setFormula("angle", angle)
  }

  override fun load(node: Node) {
    angle = node.getFormula("angle")
  }
}

class SpriteSetAngle(sprite: Sprite, private var angle: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle = angle
  }

  override fun toString(): String = "Задать угол $angle"

  override fun getClassName(): String = "SpriteSetAngle"

  override fun store(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("angle", angle)
  }

  override fun load(node: Node) {
    sprite = node.getField("sprite") as Sprite
    angle = node.getDouble("angle")
  }
}