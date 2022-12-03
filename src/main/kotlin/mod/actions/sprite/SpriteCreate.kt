package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteClass
import SpriteFactory
import emptyClass
import mod.dragging.parentSprite
import mod.dragging.selectClass
import newActions

class SpriteCreateFactory(private var spriteClass: SpriteClass = emptyClass): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteCreateFactory(selectClass())
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteCreate(sprite, spriteClass)
  }

  override fun toString(): String = "Создать спрайт"
  override fun fullText(): String = "Создать $spriteClass"

  override fun toNode(node: Node) {
    node.setField("spriteClass", spriteClass)
  }

  override fun fromNode(node: Node) {
    spriteClass = node.getField("spriteClass") as SpriteClass
  }
}

class SpriteCreate(sprite: Sprite, private var spriteClass: SpriteClass): SpriteAction(sprite) {
  override fun execute() {
    val newSprite = Sprite()
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
    node.setField("spriteClass", spriteClass)
  }

  override fun fromNode(node: Node) {
    spriteClass = node.getField("spriteClass") as SpriteClass
  }
}