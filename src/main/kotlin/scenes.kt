import mod.*
import mod.actions.SoundPlayFactory
import mod.actions.splitImage
import mod.actions.sprite.*
import mod.dragging.*
import java.util.*
import kotlin.math.PI

fun tilemap() {
  splitImage(imageArrays[4], 5, 7)
  project.add(TileMap(10, 16, 1.0, 1.0, imageArrays[4]))
}

fun snow() {
  val flake = addClass("Снежинка")

  val area = SpriteEntry("snow gen", Sprite(blankImage, 0.0, -10.0, 10.0, 2.0))
  flake.onCreate.apply {
    add(SpritePositionInAreaFactory(currentEntry, area))
    add(SpriteSetSizeFactory(currentEntry, RandomDoubleValue(0.25, 1.0)))
    add(SpriteSetImageFactory(currentEntry, imageArrays[4].images[0]))
    add(SpriteSetMovingVectorFactory(currentEntry, zero, RandomDoubleValue(1.0, 5.0)))
  }
  flake.always.add(SpriteMoveFactory(currentEntry))

  project.add(area.sprite!!)
  actions.add(SpriteDelayedCreate(nullSprite, flake, 0.1))
}

fun asteroids() {

  /// IMAGES

  val asteroidImage = imageArrays[1]
  splitImage(asteroidImage, 8, 4)

  val bulletImage = imageArrays[2]
  splitImage(bulletImage, 1, 16)
  bulletImage.setCenter(43.0 / 48.0, 5.5 / 12.0)
  bulletImage.setVisibleArea(10.5, 3.0)

  val shipImage = imageArrays[3]
  shipImage.setCenter(0.35, 0.5)
  shipImage.setVisibleArea(1.5, 1.5)

  val explosionImage = imageArrays[5]
  splitImage(explosionImage, 4, 4)
  explosionImage.setVisibleArea(1.5, 1.5)

  /// SPRITES

  val player = Sprite(imageArrays[3].images[0], -3.0, -5.0, 1.0, 1.0)
  player.setName("игрок")

  Key(97, user).onPressActions.add(ActionEntry(world, SpriteRotation(player, -1.5 * PI)))
  Key(100, user).onPressActions.add(ActionEntry(world, SpriteRotation(player, 1.5 * PI)))
  Key(119, user).onPressActions.add(ActionEntry(world, SpriteAcceleration(player, 50.0, 10.0)))

  val bounds = SpriteEntry("границы поля", Sprite(blankImage, world.centerX, world.centerY, world.width + 2.0,world.height + 2.0))

  actions.apply {
    add(SpriteAcceleration(player, -15.0, 100.0))
    add(SpriteLoopArea(player, bounds.sprite!!))
    add(SpriteMove(player))
  }

  val bullet = addClass("Пуля")

  bullet.onCreate.apply {
    add(SoundPlayFactory(sounds[0]))
    add(SpritePositionAsFactory(currentEntry, parentEntry))
    add(SpriteDirectAsFactory(currentEntry, parentEntry))
    add(SpriteSetSizeFactory(currentEntry, DoubleValue(0.15)))
    add(SpriteSetSpeedFactory(currentEntry, DoubleValue(15.0)))
  }
  bullet.always.apply {
    add(SpriteMoveFactory(currentEntry))
    add(SpriteAnimationFactory(currentEntry, imageArrays[2], DoubleValue(16.0)))
    add(SpriteSetBoundsFactory(currentEntry, bounds))
  }

  Key(32, user).onPressActions.add(ActionEntry(world, SpriteDelayedCreate(player, bullet, 0.1)))

  val asteroid = addClass("Астероид")
  asteroid.onCreate.apply {
    add(SpritePositionInAreaFactory(currentEntry, bounds))
    add(SpriteSetSizeFactory(currentEntry, DoubleValue(2.0)))
    add(SpriteSetSpeedFactory(currentEntry, DoubleValue(15.0)))
  }

  asteroid.always.apply {
    add(SpriteAnimationFactory(currentEntry, imageArrays[1], RandomDirection(RandomDoubleValue(12.0, 20.0))))
    add(SpriteRotationFactory(currentEntry, RandomDoubleValue(-180.0, 180.0)))
    add(SpriteLoopAreaFactory(currentEntry, bounds))
  }

  Key(98, user).onClickActions.add(ActionEntry(world, SpriteCreate(Sprite(blankImage), asteroid, LinkedList())))

  val explosion = addClass("Взрыв")

  explosion.onCreate.apply {
    //add(SpriteSetImageFactory(currentEntry, imageArrays[5].images[0]))
    add(SoundPlayFactory(sounds[1]))
    add(SpritePositionAsFactory(currentEntry, parentEntry))
    add(SpriteSetSizeFactory(currentEntry, DoubleValue(2.0)))
  }
  
  explosion.always.apply {
    add(SpriteAnimationFactory(currentEntry, explosionImage, DoubleValue(16.0)))
    add(SpriteDelayedRemoveFactory(currentEntry, DoubleValue(1.0)))
  }

  bullet.apply {
    val list = LinkedList<SpriteActionFactory>()
    list.add(SpriteSetSizeFactory(currentEntry, DoubleValue(1.0)))
    addOnCollision(asteroid, SpriteCreateFactory(sprite1Entry, explosion, list))
    addOnCollision(asteroid, SpriteRemoveFactory(sprite1Entry))

    val list2 = LinkedList<SpriteActionFactory>()
    list2.add(SpriteSetSizeFactory(currentEntry, DoubleValue(2.0)))
    addOnCollision(asteroid, SpriteCreateFactory(sprite2Entry, explosion, list2))
    addOnCollision(asteroid, SpriteRemoveFactory(sprite2Entry))
  }

  project.apply {
    add(bounds.sprite!!)
    add(bullet)
    add(asteroid)
    add(player)
    add(explosion)
  }
}