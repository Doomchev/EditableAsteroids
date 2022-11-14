import java.util.LinkedList

val classes = LinkedList<SpriteClass>()

class SpriteClass(var name: String) {
  val objects = LinkedList<Sprite>()
  val onCreate = LinkedList<SpriteAction>()
}