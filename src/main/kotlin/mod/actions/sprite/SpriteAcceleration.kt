package mod.actions.sprite

import Formula
import Node
import Sprite
import SpriteAction
import SpriteFactory
import fpsk
import mod.dragging.enterDouble
import nullSprite
import zero
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class SpriteAccelerationFactory(private var acceleration: Formula = zero, private var limit: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteAccelerationFactory(
      enterDouble("Введите ускорение (ед/сек):")
      , enterDouble("Введите макс. скорость (ед/сек):"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteAcceleration(sprite, acceleration.get(), limit.get())
  }

  override fun toString(): String = "Ускорять"
  override fun fullText(): String = "Ускорять на $acceleration до $limit"

  override fun fromNode(node: Node) {
    acceleration = node.getFormula("acceleration")
    limit = node.getFormula("limit")
  }

  override fun toNode(node: Node) {
    node.setFormula("acceleration", acceleration)
    node.setFormula("limit", limit)
  }
}

class SpriteAcceleration(sprite: Sprite = nullSprite, private var acceleration: Double = 0.0, private var limit: Double = 0.0): SpriteAction(sprite) {
  override fun execute() {
    var newLength = vectorLength(sprite.dx, sprite.dy) + acceleration * fpsk
    if(newLength < 0) newLength = 0.0
    if(limit > 0 && newLength > limit) newLength = limit
    sprite.dx = newLength * cos(sprite.angle)
    sprite.dy = newLength * sin(sprite.angle)
  }

  override fun toString(): String = "Ускорять $sprite на $acceleration до $limit"

  override fun fromNode(node: Node) {
    sprite = node.getField("sprite") as Sprite
    acceleration = node.getDouble("acceleration")
    limit = node.getDouble("limit")
  }

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("acceleration", acceleration)
    node.setDouble("limit", limit)
  }
}

private fun vectorLength(dx: Double, dy: Double): Double {
  return sqrt(dx * dx + dy * dy)
}