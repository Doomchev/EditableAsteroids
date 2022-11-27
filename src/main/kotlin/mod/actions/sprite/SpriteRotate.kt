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

class SpriteRotationFactory(private val speed: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteRotationFactory(enterDouble("Введите скорость поворота (град/сек):"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteRotation(sprite, speed.get() * PI / 180.0)
  }

  override fun toString(): String = "Повернуть"
  override fun fullText(): String = "Повернуть со скоростью $speed"

  override fun getClassName(): String = "SpriteRotationFactory"

  override fun store(node: Node) {
    node.setFormula("speed", speed)
  }
}

class SpriteRotation(sprite: Sprite, private val speed: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle += fpsk * speed
  }

  override fun toString(): String = "Повернуть $sprite со скоростью ${format(speed)}"

  override fun getClassName(): String = "SpriteRotation"

  override fun store(node: Node) {
    node.setObject("sprite", sprite)
    node.setDouble("speed", speed)
  }
}