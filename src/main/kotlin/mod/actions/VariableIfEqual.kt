package mod.actions

import Action
import Formula
import Node
import Serializer
import SpriteAction
import ActionFactory
import mod.dragging.enterDouble
import mod.dragging.enterString
import nullSprite
import nullSpriteEntry

object variableIfEqualSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return VariableIfEqualFactory(enterString(""), enterDouble(""), mutableListOf())
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    val actions = mutableListOf<ActionFactory>()
    node.getField("actions", actions)
    return VariableIfEqualFactory(node.getString("variable"), node.getFormula("value"), actions)
  }

  override fun actionFromNode(node: Node): Action {
    val actions = mutableListOf<Action>()
    node.getField("actions", actions)
    return VariableIfEqual(node.getString("variable"), node.getFormula("value").getInt(), actions)
  }

  override fun toString(): String = "При состоянии"
}

class VariableIfEqualFactory(private var varName: String, private var value: Formula, private var factories: MutableList<ActionFactory>): ActionFactory() {
  override fun create(): Action {
    val actions = mutableListOf<Action>()
    for(factory in factories) {
      actions.add(factory.create())
    }
    return VariableIfEqual(varName, value.getInt(), actions)
  }

  override fun addChildBlocks() {
    addChildBlocks(factories)
  }

  override fun toString(): String = "Если $varName == $value"

  override fun toNode(node: Node) {
    node.setField("actions", factories)
  }
}

class VariableIfEqual(private var varName: String, private var value: Int, private var actions: MutableList<Action>): Action {
  override fun execute() {
    if(findVariable(varName).getInt() == value) {
      for(action in actions) {
        action.execute()
      }
    }
  }

  override fun toString(): String = "Если $varName == $value"
}