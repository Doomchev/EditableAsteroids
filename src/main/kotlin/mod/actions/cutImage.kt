package mod.actions

import Action
import mod.dragging.enterDouble
import mod.dragging.enterInt
import kotlin.math.PI

object cutImage: Action {
  override fun execute(x: Double, y: Double) {
    val xquantity = enterInt("Введите кол-во избображений по горизонтали:") * PI / 180
    val yquantity = enterInt("Введите кол-во избображений по вертикали:") * PI / 180

  }
}