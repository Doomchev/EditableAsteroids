package mod.actions.sprite

import Formula
import ImageArray
import Node
import Sprite
import SpriteAction
import SpriteFactory
import fpsk
import mod.dragging.enterDouble
import mod.dragging.selectImageArray
import zero

class SpriteAnimationFactory(private var array: ImageArray? = null, private var speed: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteAnimationFactory(selectImageArray(), enterDouble("Введите скорость (кадров/сек):"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteAnimation(sprite, array!!, speed.get())
  }

  override fun toString(): String = "Анимировать"
  override fun fullText(): String = "Анимировать $array со скоростью $speed"

  override fun toNode(node: Node) {
    node.setField("array", array!!)
    node.setFormula("speed", speed)
  }

  override fun fromNode(node: Node) {
    array = node.getField("array") as ImageArray
    speed = node.getFormula("speed")
  }
}

class SpriteAnimation(sprite: Sprite, private var array: ImageArray, private var speed: Double): SpriteAction(sprite) {
  var frame: Double = 0.0

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

  override fun fromNode(node: Node) {
    sprite = node.getField("sprite") as Sprite
    array = node.getField("array") as ImageArray
    speed = node.getDouble("speed")
    frame = node.getDouble("frame")
  }
}