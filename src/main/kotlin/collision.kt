import mod.Element

object collisionEntrySerializer: ElementSerializer {
  override fun fromNode(node: Node): Element {
    val entry = CollisionEntry(node.getField("spriteClass") as SpriteClass)
    node.getChildren(entry.factories)
    return entry
  }
}

class CollisionEntry(var spriteClass: SpriteClass): Element {
  val factories = mutableListOf<ActionFactory>()

  constructor(spriteClass: SpriteClass, newFactories: Array<out ActionFactory>) : this(spriteClass) {
    factories.addAll(newFactories)
  }

  override fun toString(): String {
    return "При столкновении $spriteClass"
  }

  override fun toNode(node: Node) {
    node.setField("spriteClass", spriteClass)
    node.setChildren(factories)
  }
}