import java.util.*

open class EnumerationValue {
  var index: Int = 0
}

class Enumeration<Value: EnumerationValue> {
  private val values = mutableListOf<Value>()

  fun add(value: Value) {
    value.index = values.size
    values.add(value)
  }
}