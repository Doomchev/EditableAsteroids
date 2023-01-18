import mod.dragging.enterDouble
import mod.dragging.enterInt

interface Variable {
  var name:String
  fun change()
}

val variables = mutableListOf<Variable>()

class IntVariable(override var name: String, value: Int, format: String = ""): IntValue(value, format), Variable {
  init {variables.add(this)}

  override fun change() {
    value = enterInt("Введите целое число:")
  }
}

class DoubleVariable(override var name: String, value: Double): DoubleValue(value), Variable {
  init {variables.add(this)}

  override fun change() {
    value = enterDouble("Введите дробное число:").getDouble()
  }
}