import mod.Element
import mod.actions.DelayedAction
import mod.currentEntry
import java.util.*

interface Action: Element {
  fun conditions(): Boolean = true
  fun execute() {}
  override fun toNode(node: Node) {}

  /*fun addChildBlocks(actions: MutableList<Action>) {
    indent += "  "
    for(action in actions) {
      blocks.add(ActionBlock(action, actions,"$indent$action", true))
      //action.addChildBlocks()
    }
  }*/

  open fun addChildBlocks() {}
}

abstract class ActionFactory: Element, Action {
  abstract fun create(): Action

  fun create(sprite: Sprite): SpriteAction {
    val action: SpriteAction = create() as SpriteAction
    action.sprite = sprite
    return action
  }

  open fun fullText(): String = toString()

  fun addChildBlocks(actions: MutableList<ActionFactory>) {
    indent += "  "
    for(factory in actions) {
      blocks.add(FactoryBlock(factory, actions,"$indent${factory.fullText()}", true))
      factory.addChildBlocks()
    }
    indent = indent.substring(2)
  }

  val caption get() = entryCaption(" ")
  val forCaption get() = entryCaption("для ")
  open fun entryCaption(prefix: String): String = ""

  open fun execute(sprite: Sprite) {
    create(sprite).execute()
  }
}

abstract class SpriteActionFactory(var spriteEntry: SpriteEntry): ActionFactory() {
  override fun entryCaption(prefix: String): String = if(spriteEntry == currentEntry) "текущий" else " $prefix$spriteEntry"
}

abstract class SpriteAction(var sprite: Sprite): Action {}

val actions = mutableListOf<SpriteAction>()
val newActions = mutableListOf<SpriteAction>()
val delayedActions = mutableListOf<DelayedAction>()