package mod.actions.sprite

import Formula
import Node
import Serializer
import Sprite
import Action
import SpriteAction
import SpriteClass
import SpriteActionFactory
import blankImage
import emptyClass
import fpsk
import mod.dragging.*
import mod.parentEntry
import mod.project
import newActions
import nullSprite
import zero

object spriteCreateSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteCreateFactory(selectSprite("Выберите родительский спрайт:"),  selectClass())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteCreateFactory(node.getField("spriteentry") as SpriteEntry, node.getField("spriteClass") as SpriteClass)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteCreate(node.getField("sprite") as Sprite, node.getField("spriteClass") as SpriteClass)
  }

  override fun toString(): String = "Создать позже"
}

class SpriteCreateFactory(spriteEntry: SpriteEntry, private var spriteClass: SpriteClass): SpriteActionFactory(spriteEntry) {
  override fun toString(): String = "Создать"
  override fun fullText(): String = "Создать $spriteClass на основе $spriteEntry"

  override fun create(): SpriteAction {
    return SpriteCreate(spriteEntry.resolve(), spriteClass)
  }

  override fun toNode(node: Node) {
    node.setField("spriteClass", spriteClass)
  }
}

class SpriteCreate(sprite: Sprite, private var spriteClass: SpriteClass): SpriteAction(sprite) {
  override fun execute() {
    val newSprite = Sprite(blankImage)
    spriteClass.add(newSprite)
    parentEntry.sprite = sprite
    for(factory in spriteClass.onCreate) {
      factory.create(newSprite).execute()
    }
    for(action in spriteClass.always) {
      newActions.add(action.create(newSprite))
    }
  }

  override fun toString(): String = "Создать $spriteClass "

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setField("spriteClass", spriteClass)
  }
}