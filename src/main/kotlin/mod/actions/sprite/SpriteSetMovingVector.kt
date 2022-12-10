package mod.actions.sprite

import Formula
import Node
import Sprite
import SpriteAction
import SpriteFactory
import Serializer
import mod.dragging.enterDouble

object spriteSetMovingVectorSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteSetMovingVectorFactory(enterDouble("Введите приращение по Х:"
    ), enterDouble("Введите приращение по Y:"))
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteSetMovingVectorFactory(node.getFormula("dx"), node.getFormula("dy"))
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteSetMovingVector(node.getField("sprite") as Sprite, node.getDouble("dx"), node.getDouble("dy"))
  }
}

class SpriteSetMovingVectorFactory(private var dx: Formula, private var dy: Formula): SpriteFactory() {

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetMovingVector(sprite, dx.get(), dy.get())
  }

  override fun toString(): String = "Задать движение"
  override fun fullText(): String = "Задать движение ($dx, $dy)"

  override fun toNode(node: Node) {
    node.setFormula("dx", dx)
    node.setFormula("dy", dy)
  }
}

class SpriteSetMovingVector(sprite: Sprite, private var dx: Double, private var dy: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.dx += dx
    sprite.dy += dy
  }

  override fun toString(): String = "Задать движение ($dx, $dy)"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("dx", dx)
    node.setDouble("dy", dy)
  }
}