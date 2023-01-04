import mod.Element

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