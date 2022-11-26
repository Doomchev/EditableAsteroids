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

class ActionBlock(var entry: ActionEntry, var entries: LinkedList<ActionEntry>, message: String, private val discrete: Boolean) : Block(message) {
  override fun addElement() {
    entries.add(entries.indexOf(entry) + 1, ActionEntry(world, selectFactory(discrete).copy().create(selectSprite())))
    updateActions()
  }

  override fun removeElement() {
    entries.remove(entry)
    updateActions()
  }
}

class CollisionBlock(private val entry: CollisionEntry, message: String) : Block(message){
  override fun addElement() {
    entry.factories.addFirst(selectFactory(false).copy())
    updateActions()
  }

  override fun removeElement() {
    entry.factories.clear()
    updateActions()
  }
}

val blocks = LinkedList<Block>()
fun updateActions() {
  blocks.clear()

  for(button in buttons) {
    if(button.project != user) continue
    showButtonActions(button, button.onClickActions, "При клике на $button", true)
    showButtonActions(button, button.onPressActions, "При нажатии на $button", false)
  }

  for(spriteClass in classes) {
    showClassActions(spriteClass, spriteClass.onCreate, "При создании $spriteClass", true)
    showClassActions(spriteClass, spriteClass.always, "Всегда для $spriteClass", false)
    for(entry in spriteClass.onCollision) {
      if(entry.factories.isEmpty()) continue
      showCollisionActions(entry, "При столкновении $spriteClass с ${entry.spriteClass} ", true)
    }
  }
}

fun showClassActions(spriteClass: SpriteClass, factories: LinkedList<SpriteFactory>, message: String, discrete: Boolean) {
  if(factories.isEmpty()) return
  blocks.add(ClassBlock(factories, message))
  for(factory in factories) {
    blocks.add(FactoryBlock(factory, factories, "  $factory", discrete))
  }
}

fun showButtonActions(button: Pushable, actions: LinkedList<ActionEntry>, message: String, discrete: Boolean) {
  if(actions.isEmpty()) return
  blocks.add(ButtonBlock(actions, message, discrete))
  for(entry in actions) {
    blocks.add(ActionBlock(entry, actions,"  $entry", discrete))
  }
}

fun showCollisionActions(entry:CollisionEntry, message: String, discrete: Boolean) {
  blocks.add(CollisionBlock(entry, message))
  for(factory in entry.factories) {
    blocks.add(FactoryBlock(factory, entry.factories,"  $factory", discrete))
  }
}