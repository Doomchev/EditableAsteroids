import java.awt.Color
import java.awt.Graphics2D
import java.util.*

class Block(var message: String) {

}

val blocks = LinkedList<Block>()
fun showActions() {
  for(button in buttons) {
    if(button.project != user) continue
    showActions("При клике на ", button, button.onClickActions)
    showActions("При нажатой ", button, button.onPressActions)
  }
  for(spriteClass in classes) {
    showActions("При создании ", spriteClass, spriteClass.onCreate)
    showActions("Всегда для ", spriteClass, spriteClass.always)
  }

  addBlock("Всегда")
  for(action in actions) {
    addBlock("  $action")
  }
}

fun showActions(message: String, spriteClass: SpriteClass, factories: LinkedList<SpriteFactory>) {
  if(factories.isEmpty()) return
  addBlock("$message$spriteClass:")
  for(factory in factories) {
    addBlock("  $factory")
  }
}

fun showActions(message: String, button: Pushable, actions: LinkedList<ActionEntry>) {
  if(actions.isEmpty()) return
  addBlock("$message$button:")
  for(entry in actions) {
    addBlock("  $entry")
  }
}

fun addBlock(string: String) {
  blocks.add(Block(string))
}