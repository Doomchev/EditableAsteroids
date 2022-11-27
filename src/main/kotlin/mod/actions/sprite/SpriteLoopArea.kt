package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import currentCanvas
import mod.dragging.enterDouble
import mod.dragging.selectedSprites
import nullSprite

class SpriteLoopAreaFactory(private val bounds: Sprite = nullSprite): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteLoopAreaFactory(selectedSprites.first)
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteLoopArea(sprite, bounds)
  }

  override fun toString(): String = "Зациклить пространство"
  override fun fullText(): String = "Зациклить пространство в $bounds"

  override fun getClassName(): String = "SpriteLoopAreaFactory"

  override fun store(node: Node) {
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

  override fun getClassName(): String = "SpriteLoopArea"

  override fun store(node: Node) {
    node.setObject("sprite", sprite)
    node.setField("bounds", bounds)
  }
}