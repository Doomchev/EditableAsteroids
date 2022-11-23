import java.util.LinkedList

class Property(val name: String, val value: String)

class Structure(val name: String) {
  val children = LinkedList<Structure>()
  val attributes = LinkedList<Property>()
  val properties = LinkedList<Property>()
}

fun main() {

}