import mod.actions.SoundPlayFactory
import mod.actions.sprite.*
import java.util.*
import javax.swing.JOptionPane
import kotlin.random.Random

fun doubleToFormula(string: String): Formula {
  var dir = false
  var s = string
  if(string.startsWith("+-")) {
    s = s.substring(2)
    dir = true
  }
  if(string.contains("..")) {
    val parts = s.split("..")
    val formula = RandomDoubleValue(parts[0].toDouble(), parts[1].toDouble())
    return if(dir) RandomDirection(formula) else formula
  }
  return DoubleValue(string.toDouble())
}

class DoubleValue(private val value: Double): Formula() {
  override fun get(): Double {
    return value
  }

  override fun toString(): String {
    return "$value"
  }
}

class RandomDoubleValue(private val from: Double, private val size:Double): Formula() {
  override fun get(): Double {
    return from + size * Random.nextDouble()
  }

  override fun toString(): String {
    return "$from..$size"
  }
}

class RandomDirection(private val formula: Formula) : Formula() {
  override fun get(): Double {
    return if(Random.nextBoolean()) -formula.get() else formula.get()
  }

  override fun toString(): String {
    return "+-$formula"
  }
}