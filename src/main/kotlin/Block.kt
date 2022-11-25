import mod.dragging.selectFactory
import mod.dragging.selectSprite
import java.util.*

abstract class Block(var message:String) {
  abstract fun addElement()
  abstract fun removeElement()
}

class ClassBlock(var factories: LinkedList<SpriteFactory>, message: String) : Block(message) {
  override fun addElement() {
    factories.addFirst(selectFactory(true))
    updateActions()
  }

  override fun removeElement() {
    factories.clear()
    updateActions()
  }
}

class FactoryBlock(var factory: SpriteFactory, var factories: LinkedList<SpriteFactory>, message: String, var discrete: Boolean) : Block(message) {
  override fun addElement() {
    factories.add(factories.indexOf(factory) + 1, selectFactory(discrete))
    updateActions()
  }

  override fun removeElement() {
    factories.remove(factory)
    updateActions()
  }
}

class ButtonBlock(var entries: LinkedList<ActionEntry>, message: String, private val discrete: Boolean) : Block(message) {
  override fun addElement() {
    entries.addFirst(ActionEntry(world, selectFactory(discrete).copy().create(selectSprite())))
    updateActions()
  }

  override fun removeElement() {
    entries.clear()
    updateActions()
  }
}

class ActionEntryBlock(var entry: ActionEntry, var entries: LinkedList<ActionEntry>, message: String, private val discrete: Boolean) : Block(message) {
  override fun addElement() {
    entries.add(entries.indexOf(entry) + 1, ActionEntry(world, selectFactory(discrete).copy().create(selectSprite())))
    updateActions()
  }

  override fun removeElement() {
    entries.remove(entry)
    updateActions()
  }
}

class AlwaysBlock(message: String) : Block(message) {
  override fun addElement() {
    actions.addFirst(selectFactory(false).copy().create(selectSprite()))
    updateActions()
  }

  override fun removeElement() {
    actions.clear()
    updateActions()
  }
}

class ActionBlock(val action: Action, message: String) : Block(message) {
  override fun addElement() {
    actions.add(actions.indexOf(action) + 1, selectFactory(false).copy().create(selectSprite()))
    updateActions()
  }

  override fun removeElement() {
    actions.remove(action)
    updateActions()
  }
}

val blocks = LinkedList<Block>()
fun updateActions() {
  blocks.clear()
  for(button in buttons) {
    if(button.project != user) continue
    showButtonActions(button, button.onClickActions, true, "При клике на ")
    showButtonActions(button, button.onPressActions, false, "При нажатии на ")
  }
  for(spriteClass in classes) {
    showClassActions(spriteClass, spriteClass.onCreate, true, "При создании ")
    showClassActions(spriteClass, spriteClass.always, false, "Всегда для ")
  }

  blocks.add(AlwaysBlock("Всегда"))
  for(action in actions) {
    blocks.add(ActionBlock(action, "  $action"))
  }
}

fun showClassActions(spriteClass: SpriteClass, factories: LinkedList<SpriteFactory>, discrete: Boolean, message: String) {
  if(factories.isEmpty()) return
  blocks.add(ClassBlock(factories, "$message$spriteClass:"))
  for(factory in factories) {
    blocks.add(FactoryBlock(factory, factories, "  $factory", discrete))
  }
}

fun showButtonActions(button: Pushable, actions: LinkedList<ActionEntry>, discrete: Boolean, message: String) {
  if(actions.isEmpty()) return
  blocks.add(ButtonBlock(actions, "$message$button:", discrete))
  for(entry in actions) {
    blocks.add(ActionEntryBlock(entry, actions,"  $entry", discrete))
  }
}