package mod.actions.variables

import Action
import Node
import Serializer
import ActionFactory
import mod.dragging.enterString
import mod.dragging.enterInt

object VariableSetSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return VariableAddFactory(enterString("Введите имя перемнной:"), enterInt("Введите инкремент:"))
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    return VariableSetFactory(node.getString("variable"), node.getInt("increment"))
  }

  override fun actionFromNode(node: Node): Action {
    return VariableSet(node.getString("variable"), node.getInt("increment"))
  }

  override fun toString(): String = "Установить"
}

class VariableSetFactory(private var varName: String, private var value: Int): ActionFactory() {
  override fun create(): Action {
    return VariableSet(varName, value)
  }

  override fun toString(): String = "Установить"
  override fun fullText(): String = "$varName = $value"

  override fun toNode(node: Node) {
    node.setString("variable", varName)
    node.setInt("value", value)
  }
}

class VariableSet(private var varName: String, private var value: Int): Action {
  override fun execute() {
    findVariable(varName).set(value)
  }

  override fun toString(): String = "$varName = $value"

  override fun toNode(node: Node) {
    node.setString("variable", varName)
    node.setInt("increment", value)
  }
}