package mod.actions

import Action
import IntValue
import Node
import Serializer
import SpriteAction
import SpriteActionFactory
import mod.dragging.enterString
import nullSprite
import nullSpriteEntry
import mod.dragging.enterInt

object VariableSetSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return VariableAddFactory(enterString("Введите имя перемнной:"), enterInt("Введите инкремент:"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return VariableSetFactory(node.getString("variable"), node.getInt("increment"))
  }

  override fun actionFromNode(node: Node): Action {
    return VariableSet(node.getString("variable"), node.getInt("increment"))
  }

  override fun toString(): String = "Прибавить"
}

class VariableSetFactory(private var varName: String, private var value: Int): SpriteActionFactory(nullSpriteEntry) {
  override fun create(): SpriteAction {
    return VariableSet(varName, value)
  }

  override fun toString(): String = "Прибавить"
  override fun fullText(): String = "$varName = $value"

  override fun toNode(node: Node) {
    node.setString("variable", varName)
    node.setInt("value", value)
  }
}

class VariableSet(private var varName: String, private var value: Int): SpriteAction(nullSprite) {
  override fun execute() {
    findVariable(varName).set(value)
  }

  override fun toString(): String = "$varName = $value"

  override fun toNode(node: Node) {
    node.setString("variable", varName)
    node.setInt("increment", value)
  }
}