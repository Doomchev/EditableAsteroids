package mod.actions.sprite

import Formula
import Node
import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.enterDouble
import zero

class SpriteSetMovingVectorFactory(private var dx: Formula = zero, private var dy: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetMovingVectorFactory(enterDouble("Введите приращение по Х:"
    ), enterDouble("Введите приращение по Y:"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetMovingVector(sprite, dx.get(), dy.get())
  }

  override fun toString(): String = "Задать движение"
  override fun fullText(): String = "Задать движение ($dx, $dy)"

  override fun getClassName(): String = "SpriteSetMovingVectorFactory"

  override fun store(node: Node) {
    node.setFormula("dx", dx)
    node.setFormula("dy", dy)
  }

  override fun load(node: Node) {
    dx = node.getFormula("dx")
    dy = node.getFormula("dy")
  }
}

class SpriteSetMovingVector(sprite: Sprite, private var dx: Double, private var dy: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.dx += dx
    sprite.dy += dy
  }

  override fun toString(): String = "Задать движение ($dx, $dy)"

  override fun getClassName(): String = "SpriteSetMovingVector"

  override fun store(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("dx", dx)
    node.setDouble("dy", dy)
  }

  override fun load(node: Node) {
    sprite = node.getField("sprite") as Sprite
    dx = node.getDouble("dx")
    dy = node.getDouble("dy")
  }
}