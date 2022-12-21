import mod.Element
import mod.actions.soundPlaySerializer
import mod.actions.sprite.*

interface Serializer {
  fun newFactory(): SpriteActionFactory
  fun factoryFromNode(node: Node): SpriteActionFactory
  fun actionFromNode(node: Node): Action
}

interface ElementSerializer {
  fun fromNode(node: Node): Element
}

val discreteActions = arrayOf(spriteCreateSerializer, spritePositionAsSerializer, spritePositionInAreaSerializer, spriteSetSizeSerializer, spriteSetAngleSerializer, spriteDirectAsSerializer, spriteSetMovingVectorSerializer, spriteSetSpeedSerializer, soundPlaySerializer, spriteSetImageSerializer, spriteRemoveSerializer)

val continuousActions = arrayOf(spriteDelayedCreateSerializer, spriteRotationSerializer, spriteMoveSerializer, spriteAccelerationSerializer, spriteAnimationSerializer, spriteSetBoundsSerializer, spriteLoopAreaSerializer, spriteDelayedRemoveSerializer)

val elements = listOf(imageSerializer, imageArraySerializer, textureSerializer, spriteSerializer, spriteClassSerializer, collisionEntrySerializer, keySerializer, mouseButtonSerializer, mouseWheelUpSerializer, mouseWheelDownSerializer, actionEntrySerializer)

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