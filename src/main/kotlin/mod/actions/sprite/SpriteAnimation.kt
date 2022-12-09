package mod.actions.sprite

import Formula
import ImageArray
import Node
import Sprite
import SpriteAction
import SpriteFactory
import fpsk
import mod.Serializer
import mod.dragging.enterDouble
import mod.dragging.selectImageArray
import zero

object spriteAnimationSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteAnimationFactory(selectImageArray(), enterDouble("Введите скорость (кадров/сек):"))
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteAnimationFactory(node.getField("array") as ImageArray, node.getFormula("speed"))
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteAnimation(node.getField("sprite") as Sprite, node.getField("array") as ImageArray, node.getDouble("speed"), node.getDouble("frame"))
  }
}

class SpriteAnimationFactory(private var array: ImageArray, private var speed: Formula): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpriteAnimation(sprite, array, speed.get(), 0.0)
  }

  override fun toString(): String = "Анимировать"
  override fun fullText(): String = "Анимировать $array со скоростью $speed"

  override fun toNode(node: Node) {
    node.setField("array", array)
    node.setFormula("speed", speed)
  }
}

class SpriteAnimation(sprite: Sprite, private var array: ImageArray, private var speed: Double, var frame: Double): SpriteAction(sprite) {
  override fun execute() {
    val images = array.images
    frame += fpsk * speed
    while(frame < 0.0) {
      frame += images.size
    }
    sprite.image = images[frame.toInt() % images.size]
  }

  override fun toString(): String = "Анимировать со скоростью $speed"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setField("array", array)
    node.setDouble("speed", speed)
    node.setDouble("frame", frame)
  }
}