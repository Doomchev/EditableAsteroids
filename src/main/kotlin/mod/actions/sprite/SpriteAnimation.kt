package mod.actions.sprite

import Action
import Formula
import ImageArray
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import fpsk
import mod.dragging.enterDouble
import selectImageArray
import selectSprite
import kotlin.math.floor

object spriteAnimationSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteAnimationFactory(
      selectSprite(),
      selectImageArray(), enterDouble("Введите скорость (кадров/сек):"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteAnimationFactory(node.getField("spriteentry") as SpriteEntry, node.getField("array") as ImageArray, node.getFormula("speed"))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteAnimation(node.getField("sprite") as Sprite, node.getField("array") as ImageArray, node.getDouble("speed"), node.getDouble("frame"))
  }

  override fun toString(): String = "Анимировать"
}

class SpriteAnimationFactory(spriteEntry: SpriteEntry, private var array: ImageArray, private var speed: Formula): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteAnimation(spriteEntry.resolve(), array, speed.get(), 0.0)
  }

  override fun toString(): String = "Анимировать"
  override fun fullText(): String = "Анимировать$caption используя $array со скоростью $speed"

  override fun toNode(node: Node) {
    node.setField("array", array)
    node.setFormula("speed", speed)
  }
}

class SpriteAnimation(sprite: Sprite, private var array: ImageArray, private var speed: Double, private var frame: Double): SpriteAction(sprite) {
  override fun execute() {
    val images = array.images
    frame += fpsk * speed
    while(frame < 0.0) {
      frame += images.size
    }
    while(frame > images.size) {
      frame -= images.size
    }
    sprite.image = images[floor(frame).toInt()]
  }

  override fun toString(): String = "Анимировать $sprite со скоростью $speed используя $array"

  override fun toNode(node: Node) {
    node.setField("array", array)
    node.setDouble("speed", speed)
    node.setDouble("frame", frame)
  }
}