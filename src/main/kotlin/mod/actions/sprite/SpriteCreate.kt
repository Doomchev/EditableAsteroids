package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteClass
import SpriteFactory
import Serializer
import mod.dragging.parentSprite
import mod.dragging.selectClass
import newActions

object spriteCreateSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteCreateFactory(selectClass())
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteCreateFactory(node.getField("spriteClass") as SpriteClass)
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteCreate(node.getField("sprite") as Sprite, node.getField("spriteClass") as SpriteClass)
  }

  override fun toString(): String = "Создать спрайт"
}

class SpriteCreateFactory(private var spriteClass: SpriteClass): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpriteCreate(sprite, spriteClass)
  }

  override fun toString(): String = "Создать спрайт"
  override fun fullText(): String = "Создать $spriteClass"

  override fun toNode(node: Node) {
    node.setField("spriteClass", spriteClass)
  }
}

class SpriteCreate(sprite: Sprite, private var spriteClass: SpriteClass): SpriteAction(sprite) {
  override fun execute() {
    val newSprite = Sprite(currentImageArray!!.images[0])
    spriteClass.add(newSprite)
    parentSprite = sprite
    for(factory in spriteClass.onCreate) {
      factory.create(newSprite).execute()
    }
    for(action in spriteClass.always) {
      newActions.add(action.create(newSprite))
    }
  }

  override fun toString(): String = "Создать $spriteClass"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setField("spriteClass", spriteClass)
  }
}