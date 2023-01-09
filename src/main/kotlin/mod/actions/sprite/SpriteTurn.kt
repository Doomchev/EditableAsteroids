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

object spriteTurnSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteSetAngleFactory(selectSprite(), enterDouble("Введите скорость поворота (град/сек):"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteTurnFactory(node.getField("spriteentry") as SpriteEntry, node.getFormula("angle"))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteTurn(node.getField("sprite") as Sprite, node.getDouble("angle"))
  }

  override fun toString(): String = "Задать угол"
}

class SpriteTurnFactory(spriteEntry: SpriteEntry, private var angle: Formula = zero): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteTurn(spriteEntry.resolve(), angle.getDouble() * PI / 180.0)
  }

  override fun toString(): String = "Повернуть"
  override fun fullText(): String = "Повернуть$forCaption на $angle"

  override fun toNode(node: Node) {
    node.setFormula("angle", angle)
  }
}

class SpriteTurn(sprite: Sprite, private var angle: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle += angle
  }

  override fun toString(): String = "Повернуть $sprite на $angle"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("angle", angle)
  }
}