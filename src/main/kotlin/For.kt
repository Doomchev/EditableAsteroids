import mod.dragging.enterDouble

object repeatSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return RepeatFactory(enterDouble("Введите количество повторений:"))
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    return RepeatFactory(node.getFormula("quantity"), getActions(node))
  }

  override fun actionFromNode(node: Node): Action {
    return Repeat(node.getFormula("quantity").getDouble().toInt(), getActions(node))
  }

  override fun toString(): String = "Повторить"
}

class RepeatFactory(private var quantity: Formula, var actions: MutableList<ActionFactory>): ActionFactory() {
  constructor(quantity: Formula, vararg actions: ActionFactory) : this(quantity, mutableListOf<ActionFactory>()) {
    this.actions.addAll(actions)
  }

  override fun create(): SpriteAction {
    return Repeat(quantity.getDouble().toInt(), actions)
  }

  override fun addChildBlocks() {
    addChildBlocks(actions)
  }

  override fun toString(): String = "Повторить $quantity раз"

  override fun toNode(node: Node) {
    node.setField("actions", actions)
  }
}

class Repeat(private var quantity: Int, private var actions: MutableList<ActionFactory>): SpriteAction(nullSprite) {
  override fun execute() {
    for(i in 0 until quantity)
    for(factory in actions) {
      factory.create().execute()
    }
  }

  override fun toString(): String = "Повторить $quantity раз"
}