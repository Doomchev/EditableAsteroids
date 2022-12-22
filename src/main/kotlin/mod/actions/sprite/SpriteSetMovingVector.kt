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
import mod.dragging.selectSprite

object spriteSetMovingVectorSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteSetMovingVectorFactory(
      selectSprite(), enterDouble("Введите приращение по Х:"
    ), enterDouble("Введите приращение по Y:"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteSetMovingVectorFactory(node.getField("spriteentry") as SpriteEntry, node.getFormula("dx"), node.getFormula("dy"))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteSetMovingVector(node.getField("sprite") as Sprite, node.getDouble("dx"), node.getDouble("dy"))
  }

  override fun toString(): String = "Задать движение"
}

class SpriteSetMovingVectorFactory(spriteEntry: SpriteEntry, private var dx: Formula, private var dy: Formula): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteSetMovingVector(spriteEntry.resolve(), dx.get(), dy.get())
  }

  override fun toString(): String = "Задать движение"
  override fun fullText(): String = "Задать движение$caption на вектор ($dx, $dy)"

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

  override fun toString(): String = "Задать движение $sprite на вектор ($dx, $dy)"

  override fun toNode(node: Node) {
    node.setDouble("dx", dx)
    node.setDouble("dy", dy)
  }
}