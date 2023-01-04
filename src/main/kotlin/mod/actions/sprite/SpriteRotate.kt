package mod.actions.sprite

import Action
import Formula
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import format
import fpsk
import mod.dragging.enterDouble
import selectSprite
import kotlin.math.PI

object spriteRotationSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteRotationFactory(selectSprite(), enterDouble("Введите скорость поворота (град/сек):"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteRotationFactory(node.getField("spriteentry") as SpriteEntry, node.getFormula("speed"))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteRotation(node.getField("sprite") as Sprite, node.getDouble("speed"))
  }

  override fun toString(): String = "Вращать"
}

class SpriteRotationFactory(spriteEntry: SpriteEntry, private var speed: Formula): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteRotation(spriteEntry.resolve(), speed.get() * PI / 180.0)
  }

  override fun toString(): String = "Вращать"
  override fun fullText(): String = "Вращать$caption со скоростью $speed"

  override fun toNode(node: Node) {
    node.setFormula("speed", speed)
  }
}

class SpriteRotation(sprite: Sprite, private var speed: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle += fpsk * speed
  }

  override fun toString(): String = "Вращать $sprite со скоростью ${format(speed)}"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("speed", speed)
  }
}