package mod.actions

import Action
import SpriteClass
import classes
import mod.dragging.enterString

object createClass: Action {
  override fun execute() {
    val sClass = SpriteClass(enterString("Введите название класса:"))
    classes.add(sClass)
    //addMenuItem(objectMenu, sClass.name, ClassMenuListener(sClass))
  }
}