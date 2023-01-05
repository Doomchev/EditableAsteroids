import mod.actions.list.IsListEmptyFactory
import mod.dragging.enterDouble
import mod.dragging.enterInt

object repeatSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return RepeatFactory(enterDouble("Введите количество повторений:"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return RepeatFactory(node.getFormula("quantity"), getActions(node))
  }

  override fun actionFromNode(node: Node): Action {
    return Repeat(node.getFormula("quantity").get().toInt(), getActions(node))
  }

  override fun toString(): String = "Повторить"
}

class RepeatFactory(private var quantity: Formula, var actions: MutableList<SpriteActionFactory>): SpriteActionFactory(nullSpriteEntry) {
  constructor(quantity: Formula, vararg actions: SpriteActionFactory) : this(quantity, mutableListOf<SpriteActionFactory>()) {
    this.actions.addAll(actions)
  }

  override fun create(): SpriteAction {
    return Repeat(quantity.get().toInt(), actions)
  }

  override fun addChildBlocks() {
    addChildBlocks(actions)
  }

  override fun toString(): String = "Повторить $quantity раз"

  override fun toNode(node: Node) {
    node.setField("actions", actions)
  }
}

class Repeat(private var quantity: Int, private var actions: MutableList<SpriteActionFactory>): SpriteAction(nullSprite) {
  override fun execute() {
    for(i in 0 until quantity)
    for(factory in actions) {
      factory.create().execute()
    }
  }

  override fun toString(): String = "Повторить $quantity раз"
}