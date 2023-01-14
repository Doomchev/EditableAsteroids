package mod.actions

import Action
import FactoryBlock
import Formula
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import blocks
import indent
import mod.dragging.enterDouble
import mod.dragging.enterString
import nullSprite
import nullSpriteEntry

object variableIfEqualSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return VariableIfEqualFactory(enterString(""), enterDouble(""), mutableListOf())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    val actions = mutableListOf<SpriteActionFactory>()
    node.getField("actions", actions)
    return VariableIfEqualFactory(node.getString("variable"), node.getFormula("value"), actions)
  }

  override fun actionFromNode(node: Node): Action {
    val actions = mutableListOf<SpriteAction>()
    node.getField("actions", actions)
    return VariableIfEqual(node.getString("variable"), node.getFormula("value").getInt(), actions)
  }

  override fun toString(): String = "При состоянии"
}

class VariableIfEqualFactory(private var varName: String, private var value: Formula, private var factories: MutableList<SpriteActionFactory>): SpriteActionFactory(nullSpriteEntry) {
  override fun create(): SpriteAction {
    val actions = mutableListOf<SpriteAction>()
    for(factory in factories) {
      actions.add(factory.create())
    }
    return VariableIfEqual(varName, value.getInt(), actions)
  }

  override fun toString(): String = "Если $varName == $value"

  override fun toNode(node: Node) {
    node.setField("actions", factories)
  }
}

class VariableIfEqual(private var varName: String, private var value: Int, private var actions: MutableList<SpriteAction>): SpriteAction(nullSprite) {
  override fun execute() {
    if(findVariable(varName).getInt() == value) {
      for(action in actions) {
        action.execute()
      }
    }
  }

  override fun toString(): String = "Если $varName == $value"
}