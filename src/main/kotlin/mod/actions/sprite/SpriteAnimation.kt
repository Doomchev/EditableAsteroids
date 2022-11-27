package mod.actions.sprite

import Formula
import ImageArray
import Node
import Sprite
import SpriteAction
import SpriteFactory
import fpsk
import mod.dragging.RandomDoubleValue
import mod.dragging.enterDouble
import mod.dragging.selectImageArray
import zero

class SpriteAnimationFactory(private val array: ImageArray? = null, private val speed: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteAnimationFactory(selectImageArray(), enterDouble("Введите скорость (кадров/сек):"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteAnimation(sprite, array!!, speed.get())
  }

  override fun toString(): String = "Анимировать"
  override fun fullText(): String = "Анимировать $array со скоростью $speed"

  override fun getClassName(): String = "SpriteAnimationFactory"

  override fun store(node: Node) {
    node.setObject("array", array!!)
    node.setFormula("speed", speed)
  }
}

class SpriteAnimation(sprite: Sprite, private val array: ImageArray, private val speed: Double): SpriteAction(sprite) {
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

  override fun getClassName(): String = "SpriteAnimation"

  override fun store(node: Node) {
    node.setObject("sprite", sprite)
    node.setObject("array", array)
    node.setDouble("speed", speed)
    node.setDouble("frame", frame)
  }
}