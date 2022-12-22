import mod.Element
import mod.currentEntry
import mod.dragging.objectsList
import java.lang.Error

object spriteEntrySerializer: ElementSerializer {
  override fun fromNode(node: Node): Element {
    val sprite = node.getField("sprite") as Sprite
    return SpriteEntry(node.getString("caption"), if(node.hasField("sprite")) node.getField("sprite") as Sprite else null)
  }
}

val nullSpriteEntry = SpriteEntry("")

open class SpriteEntry(var caption: String, var sprite: Sprite? = null):
  Element {
  fun resolve(): Sprite = sprite!!
  override fun toString() = caption
  override fun toNode(node: Node) {
    node.setString("caption", caption)
    if(sprite != null) node.setField("sprite", sprite!!)
  }
}

fun spriteName(sprite: Sprite): String {
  for(entry in objectsList) {
    if(entry.sprite == sprite) return entry.caption
  }
  throw Error()
}