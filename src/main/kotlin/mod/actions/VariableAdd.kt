package mod.actions

import Action
import Formula
import IntValue
import Node
import Serializer
import SpriteAction
import ActionFactory
import mod.dragging.enterString
import nullSprite
import nullSpriteEntry
import zero
import mod.dragging.enterInt

object VariableAddSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return VariableAddFactory(enterString("Введите имя перемнной:"), enterInt("Введите инкремент:"))
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    return VariableAddFactory(node.getString("variable"), node.getInt("increment"))
  }

  override fun actionFromNode(node: Node): Action {
    return VariableAdd(node.getString("variable"), node.getInt("increment"))
  }

  override fun toString(): String = "Прибавить"
}

class VariableAddFactory(private var varName: String, private var increment: Int): ActionFactory() {
  override fun create(): SpriteAction {
    return VariableAdd(varName, increment)
  }

  override fun toString(): String = "Прибавить"
  override fun fullText(): String = "Прибавить $increment к $varName"

  override fun toNode(node: Node) {
    node.setString("variable", varName)
    node.setInt("increment", increment)
  }
}

class VariableAdd(private var varName: String, private var increment: Int): SpriteAction(nullSprite) {
  override fun execute() {
    findVariable(varName).add(increment)
  }

  override fun toString(): String = "Прибавить $increment к $varName"

  override fun toNode(node: Node) {
    node.setString("variable", varName)
    node.setInt("increment", increment)
  }
}

fun selectVariable(): Formula {
  return zero
}

val varMap = mutableMapOf<String, Formula>()

fun findVariable(name: String): Formula {
  var variable = varMap[name]
  if(variable == null) {
    variable = IntValue(0)
    varMap[name] = variable
  }
  return variable
}