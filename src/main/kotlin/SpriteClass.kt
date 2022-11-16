import java.util.LinkedList

val classes = LinkedList<SpriteClass>()

class SpriteClass(var name: String) {
  val onCreate = LinkedList<SpriteAction>()
  val always = LinkedList<SpriteAction>()
}