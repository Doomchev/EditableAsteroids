package mod.actions.sprite

import Formula
import Node
import Sprite
import Action
import SpriteActionFactory
import fpsk
import Serializer
import SpriteAction
import SpriteEntry
import mod.dragging.enterDouble
import mod.dragging.selectSprite
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object spriteAccelerationSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteAccelerationFactory(selectSprite(), enterDouble("Введите ускорение (ед/сек):"), enterDouble("Введите макс. скорость (ед/сек):"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteAccelerationFactory(node.getField("spriteentry") as SpriteEntry, node.getFormula("acceleration"), node.getFormula("limit"))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteAcceleration(node.getField("sprite") as Sprite,
    node.getDouble("acceleration"), node.getDouble("limit"))
  }

  override fun toString(): String = "Ускорять"
}

class SpriteAccelerationFactory(spriteEntry: SpriteEntry, private var acceleration: Formula, private var limit: Formula): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteAcceleration(spriteEntry.resolve(), acceleration.get(), limit.get())
  }

  override fun toString(): String = "Ускорять"
  override fun fullText(): String = "Ускорять $spriteEntry на $acceleration до $limit"

  override fun toNode(node: Node) {
    node.setFormula("acceleration", acceleration)
    node.setFormula("limit", limit)
  }
}

class SpriteAcceleration(sprite: Sprite, private var acceleration: Double, private var limit: Double): SpriteAction(sprite) {
  override fun execute() {
    var newLength = vectorLength(sprite.dx, sprite.dy) + acceleration * fpsk
    if(newLength < 0) newLength = 0.0
    if(limit > 0 && newLength > limit) newLength = limit
    sprite.dx = newLength * cos(sprite.angle)
    sprite.dy = newLength * sin(sprite.angle)
  }

  override fun toString(): String = "Ускорять $sprite на $acceleration до $limit"

  override fun toNode(node: Node) {
    node.setDouble("acceleration", acceleration)
    node.setDouble("limit", limit)
  }
}

private fun vectorLength(dx: Double, dy: Double): Double {
  return sqrt(dx * dx + dy * dy)
}