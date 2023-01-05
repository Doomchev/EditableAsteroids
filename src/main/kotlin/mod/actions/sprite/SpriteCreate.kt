package mod.actions.sprite

import Action
import FactoryBlock
import NewSprite
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteClass
import SpriteEntry
import blankImage
import blocks
import indent
import mod.parentEntry
import newActions
import newSprites
import selectClass
import selectSprite
import java.util.*

object spriteCreateSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteCreateFactory(selectSprite("Выберите родительский спрайт:"),  selectClass())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteCreateFactory(node.getField("spriteentry") as SpriteEntry, node.getField("spriteClass") as SpriteClass, getActions(node))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteCreate(node.getField("sprite") as Sprite, node.getField("spriteClass") as SpriteClass, getActions(node))
  }

  override fun toString(): String = "Создать"
}

class SpriteCreateFactory(spriteEntry: SpriteEntry, private var spriteClass: SpriteClass, var actions: MutableList<SpriteActionFactory>): SpriteActionFactory(spriteEntry) {
  constructor(spriteEntry: SpriteEntry, spriteClass: SpriteClass, vararg actions: SpriteActionFactory) : this(spriteEntry, spriteClass, mutableListOf<SpriteActionFactory>()) {
    this.actions.addAll(actions)
  }

  override fun addChildBlocks() {
    addChildBlocks(actions)
  }

  override fun toString(): String = "Создать"
  override fun fullText(): String = "Создать $spriteClass на основе $spriteEntry"

  override fun create(): SpriteAction {
    return SpriteCreate(spriteEntry.resolve(), spriteClass, this.actions)
  }

  override fun toNode(node: Node) {
    node.setField("spriteClass", spriteClass)
  }
}

class SpriteCreate(sprite: Sprite, private var spriteClass: SpriteClass, private var actions: MutableList<SpriteActionFactory>): SpriteAction(sprite) {
  override fun execute() {
    val newSprite = Sprite(blankImage)
    newSprites.add(NewSprite(spriteClass, newSprite))
    parentEntry.sprite = sprite
    for(factory in spriteClass.onCreate + actions) {
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