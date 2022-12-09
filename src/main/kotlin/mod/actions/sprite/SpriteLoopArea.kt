package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import mod.Serializer
import mod.selectedSprites
import nullSprite

object spriteLoopAreaSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteLoopAreaFactory(selectedSprites.first)
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteLoopAreaFactory(node.getField("bounds") as Sprite)
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteLoopArea(node.getField("sprite") as Sprite, node.getField("bounds") as Sprite)
  }

}

class SpriteLoopAreaFactory(private var bounds: Sprite = nullSprite): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpriteLoopArea(sprite, bounds)
  }

  override fun toString(): String = "Зациклить пространство"
  override fun fullText(): String = "Зациклить пространство в $bounds"

  override fun toNode(node: Node) {
    node.setField("bounds", bounds)
  }
}

class SpriteLoopArea(sprite: Sprite, var bounds: Sprite = nullSprite): SpriteAction(sprite) {
  override fun execute() {
    if(sprite.centerX < bounds.leftX) sprite.centerX += bounds.width
    if(sprite.centerX >= bounds.rightX) sprite.centerX -= bounds.width
    if(sprite.centerY < bounds.topY) sprite.centerY += bounds.height
    if(sprite.centerY >= bounds.bottomY) sprite.centerY -= bounds.height
  }

  override fun toString(): String = "Зациклить пространство для $sprite в $bounds"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setField("bounds", bounds)
  }
}