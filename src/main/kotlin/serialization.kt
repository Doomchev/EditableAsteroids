import mod.Element
import mod.actions.delaySerializer
import mod.actions.soundPlaySerializer
import mod.actions.sprite.*
import state.ifStateSerializer
import state.spriteSetStateSerializer
import javax.swing.JOptionPane

interface Serializer {
  fun newFactory(): ActionFactory
  fun factoryFromNode(node: Node): ActionFactory
  fun actionFromNode(node: Node): Action
  fun getActions(node: Node): MutableList<ActionFactory> {
    val actions = mutableListOf<ActionFactory>()
    node.getField("actions", actions)
    return actions
  }
}

interface ElementSerializer {
  fun fromNode(node: Node): Element
}

val discreteActions = arrayOf(spriteCreateSerializer, spritePositionAsSerializer, spritePositionInAreaSerializer, spriteSetSizeSerializer, spriteSetAngleSerializer, spriteDirectAsSerializer, spriteSetMovingVectorSerializer, spriteSetSpeedSerializer, soundPlaySerializer, spriteSetImageSerializer, spriteRemoveSerializer, spriteDeactivateSerializer, spriteDirectAtSerializer, spriteTurnSerializer, ifStateSerializer, spriteSetStateSerializer, spriteCreateSerializer, repeatSerializer, spriteShowSerializer, spriteHideSerializer)

val continuousActions = arrayOf(spriteDelayedCreateSerializer, spriteRotationSerializer, spriteMoveForwardSerializer, spriteAccelerationSerializer, spriteAnimationSerializer, spriteSetBoundsSerializer, spriteLoopAreaSerializer, spriteDelayedRemoveSerializer, spriteDirectAtSerializer, delaySerializer)

val elements = listOf(imageSerializer, imageArraySerializer, textureSerializer, spriteSerializer, spriteClassSerializer, collisionEntrySerializer, keySerializer, mouseButtonSerializer, mouseWheelUpSerializer, mouseWheelDownSerializer, actionEntrySerializer, spriteEntrySerializer)

val actionSerializers = HashMap<String, Serializer>()
val factorySerializers = HashMap<String, Serializer>()
val elementSerializers = HashMap<String, ElementSerializer>()

fun capitalize(string: String): String {
  return string[0].uppercase() + string.substring(1, string.indexOf("Serializer"))
}

fun registerSerializers() {
  fun add(array: Array<Serializer>) {
    for(serializer in array) {
      val name = capitalize(serializer.javaClass.kotlin.simpleName!!)
      actionSerializers[name] = serializer
      factorySerializers[name + "Factory"] = serializer
    }
  }
  add(discreteActions)
  add(continuousActions)
  for(serializer in elements) {
    val name = serializer.javaClass.kotlin.simpleName!!
    elementSerializers[capitalize(name)] = serializer
  }
}

fun selectSerializer(discrete: Boolean): ActionFactory {
  val serArray = if(discrete) discreteActions else continuousActions
  return (JOptionPane.showInputDialog(frame,"Выберите действие:", "", JOptionPane.QUESTION_MESSAGE, null, serArray, serArray[0]) as Serializer).newFactory()
}