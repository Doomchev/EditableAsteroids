import mod.Serializer
import mod.actions.soundPlaySerializer
import mod.actions.sprite.*

val discreteActions = arrayOf(spriteCreateSerializer, spritePositionAsSerializer, spritePositionInAreaSerializer, spriteSetSizeSerializer, spriteSetAngleSerializer, spriteDirectAsSerializer, spriteSetMovingVectorSerializer, spriteSetSpeedSerializer, soundPlaySerializer, spriteSetImageSerializer, spriteRemoveSerializer)

val continuousActions = arrayOf(spriteDelayedCreateSerializer, spriteRotateSerializer, spriteMoveSerializer, spriteAccelerationSerializer, spriteAnimationSerializer, spriteSetBoundsSerializer, spriteLoopAreaSerializer, spriteDelayedRemoveSerializer)

val elements = arrayOf(imageSerializer, imageArraySerializer, textureSerializer, spriteSerializer, spriteClassSerializer)

val actionSerializers = HashMap<String, Serializer>()
val factorySerializers = HashMap<String, Serializer>()

fun registerSerializers() {
  fun add(array: Array<Serializer>) {
    for(serializer in array) {
      var name = serializer.javaClass.kotlin.simpleName!!
      name = name.substring(0, name.indexOf("Serializer"))
      actionSerializers[name] = serializer
      factorySerializers[name + "Factory"] = serializer
    }
  }
  add(discreteActions)
  add(continuousActions)
}