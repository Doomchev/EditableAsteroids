package mod.actions.sprite

import Action
import Formula
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import mod.dragging.enterDouble
import selectSprite
import zero
import kotlin.math.PI

object spriteSetAngleSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteSetAngleFactory(selectSprite(), enterDouble("Введите скорость поворота (град/сек):"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteSetAngleFactory(node.getField("spriteentry") as SpriteEntry, node.getFormula("angle"))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteSetAngle(node.getField("sprite") as Sprite, node.getDouble("angle"))
  }

  override fun toString(): String = "Задать угол"
}

class SpriteSetAngleFactory(spriteEntry: SpriteEntry, private var angle: Formula = zero): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteSetAngle(spriteEntry.resolve(), angle.getDouble() * PI / 180.0)
  }

  override fun toString(): String = "Задать угол"
  override fun fullText(): String = "Задать угол $angle$forCaption"

  override fun toNode(node: Node) {
    node.setFormula("angle", angle)
  }
}

class SpriteSetAngle(sprite: Sprite, private var angle: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle = angle
  }

  override fun toString(): String = "Задать $sprite угол $angle"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("angle", angle)
  }
}