import mod.project

var indent: String = ""

abstract class Block(var message:String) {
  open fun editElement() {}
  abstract fun addElement()
  abstract fun removeElement()
}

class ClassBlock(private var factories: MutableList<ActionFactory>, message: String) : Block(message) {
  override fun addElement() {
    factories.add(0, selectSerializer(true))
    updateActions()
  }

  override fun removeElement() {
    factories.clear()
    updateActions()
  }
}

class FactoryBlock(private var factory: ActionFactory, private var factories: MutableList<ActionFactory>, message: String, private var discrete: Boolean) : Block(message) {
  override fun addElement() {
    factories.add(factories.indexOf(factory) + 1, selectSerializer(discrete))
    updateActions()
  }

  override fun removeElement() {
    factories.remove(factory)
    updateActions()
  }
}

class ButtonBlock(private var actions: MutableList<Action>, message: String, private val discrete: Boolean) : Block(message) {
  override fun addElement() {
    val action = selectSerializer(discrete).create()
    //action.sprite = selectSprite().resolve()
    actions.add(0, action)
    updateActions()
  }

  override fun removeElement() {
    actions.clear()
    updateActions()
  }
}

class ActionBlock(private var action: Action, private var actions: MutableList<Action>, message: String, private val discrete: Boolean) : Block(message) {
  override fun addElement() {
    actions.add(actions.indexOf(action) + 1, selectSerializer(discrete).create(selectSprite().resolve()))
    updateActions()
  }

  override fun removeElement() {
    actions.remove(action)
    updateActions()
  }
}

class CollisionBlock(private val entry: CollisionEntry, message: String) : Block(message){
  override fun addElement() {
    entry.factories.add(0, selectSerializer(true))
    updateActions()
  }

  override fun removeElement() {
    entry.factories.clear()
    updateActions()
  }
}

class VariableBlock(private var variable: Variable, message: String) : Block(message) {
  override fun addElement() {
    TODO("Not yet implemented")
  }

  override fun removeElement() {
    TODO("Not yet implemented")
  }

  override fun editElement() {
    variable.change()
    updateActions()
  }
}

val blocks = mutableListOf<Block>()
fun updateActions() {
  blocks.clear()
  for(variable in variables) {
    blocks.add(VariableBlock(variable, "  ${variable.name} = ${variable}"))
  }

  for(button in buttons) {
    if(button.project != user) continue
    showButtonActions(button.onClickActions, "При клике на $button", true)
    showButtonActions(button.onPressActions, "При нажатии на $button", false)
  }

  for(spriteClass in project.classes) {
    showClassActions(spriteClass, spriteClass.onCreate, "При создании $spriteClass", true)
    showClassActions(spriteClass, spriteClass.always, "Всегда для $spriteClass", false)
    for(entry in spriteClass.onCollision) {
      if(entry.factories.isEmpty()) continue
      showCollisionActions(entry, "При столкновении $spriteClass с ${entry.spriteClass} ", true)
    }
  }
}

fun showClassActions(spriteClass: SpriteClass, factories: MutableList<ActionFactory>, message: String, discrete: Boolean) {
  if(factories.isEmpty()) return
  blocks.add(ClassBlock(factories, message))
  indent += "  "
  for(factory in factories) {
    blocks.add(FactoryBlock(factory, factories, "$indent${factory.fullText()}", discrete))
    factory.addChildBlocks()
  }
  indent = indent.substring(2)
}

fun showButtonActions(actions: MutableList<Action>, message: String, discrete: Boolean) {
  if(actions.isEmpty()) return
  blocks.add(ButtonBlock(actions, message, discrete))
  indent += "  "
  for(action in actions) {
    blocks.add(ActionBlock(action, actions,"$indent$action", discrete))
    action.addChildBlocks()
  }
  indent = indent.substring(2)
}

fun showCollisionActions(entry:CollisionEntry, message: String, discrete: Boolean) {
  blocks.add(CollisionBlock(entry, message))
  indent += "  "
  for(factory in entry.factories) {
    blocks.add(FactoryBlock(factory, entry.factories,"$indent${factory.fullText()}", discrete))
    factory.addChildBlocks()
  }
  indent = indent.substring(2)
}