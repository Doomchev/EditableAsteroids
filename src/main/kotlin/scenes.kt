import mod.actions.SoundPlayFactory
import mod.actions.splitImage
import mod.actions.sprite.*
import mod.dragging.*
import mod.project
import kotlin.math.PI

fun tilemap() {
  splitImage(imageArrays[4], 5, 7)
  project.add(TileMap(10, 16, 1.0, 1.0, imageArrays[4]))
}

fun snow() {
  val flake = addClass("Снежинка")

  val area = Sprite(0.0, -10.0, 10.0, 2.0, "snow gen")
  flake.onCreate.apply {
    add(SpritePositionInAreaFactory(area))
    add(SpriteSetSizeFactory(RandomDoubleValue(0.25, 1.0)))
    add(SpriteSetImageFactory(imageArrays[4].images[0]))
    add(SpriteSetMovingVectorFactory(zero, RandomDoubleValue(1.0, 5.0)))
  }
  flake.always.add(SpriteMoveFactory())

  project.add(area)
  actions.add(SpriteDelayedCreate(Sprite(), flake, 0.1))
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

  val player = Sprite(-3.0, -5.0, 1.0, 1.0, "игрок")
  player.image = imageArrays[3].images[0]

  Key(97, user).onPressActions.add(ActionEntry(world, SpriteRotation(player, -1.5 * PI)))
  Key(100, user).onPressActions.add(ActionEntry(world, SpriteRotation(player, 1.5 * PI)))
  Key(119, user).onPressActions.add(ActionEntry(world, SpriteAcceleration(player, 50.0, 10.0)))

  val bounds = Sprite(world.centerX, world.centerY, world.width + 2.0,
    world.height + 2.0,"границы поля")
  actions.apply {
    add(SpriteAcceleration(player, -15.0, 100.0))
    add(SpriteLoopArea(player, bounds))
    add(SpriteMove(player))
  }

  val bullet = addClass("Пуля")

  bullet.onCreate.apply {
    add(SoundPlayFactory(sounds[0]))
    add(SpritePositionAsFactory())
    add(SpriteDirectAsFactory())
    add(SpriteSetSizeFactory(DoubleValue(0.15)))
    add(SpriteSetSpeedFactory(DoubleValue(15.0)))
  }
  bullet.always.apply {
    add(SpriteMoveFactory())
    add(SpriteAnimationFactory(imageArrays[2], DoubleValue(16.0)))
    add(SpriteSetBoundsFactory(bounds))
  }

  Key(32, user).onPressActions.add(ActionEntry(world, SpriteDelayedCreate(player, bullet, 0.1)))

  val asteroid = addClass("Астероид")
  asteroid.onCreate.apply {
    add(SpritePositionInAreaFactory(bounds))
    add(SpriteSetSizeFactory(RandomDoubleValue(0.5, 2.0)))
    add(SpriteSetSpeedFactory(DoubleValue(15.0)))
  }

  asteroid.always.apply {
    add(SpriteAnimationFactory(imageArrays[1], RandomDirection(RandomDoubleValue(12.0, 20.0))))
    add(SpriteRotationFactory(RandomDoubleValue(-180.0, 180.0)))
    add(SpriteLoopAreaFactory(bounds))
  }

  Key(98, user).onClickActions.add(ActionEntry(world, SpriteCreate(Sprite(), asteroid)))

  val explosion = addClass("Взрыв")

  explosion.onCreate.apply {
    add(SoundPlayFactory(sounds[1]))
    add(SpritePositionAsFactory())
    add(SpriteSetSizeFactory(DoubleValue(2.0)))
  }
  explosion.always.apply {
    add(SpriteAnimationFactory(explosionImage, DoubleValue(16.0)))
    add(SpriteDelayedRemoveFactory(1.0))
  }

  bullet.apply {
    addOnCollision(asteroid, SpriteCreateFactory(explosion))
    addOnCollision(asteroid, SpriteRemoveFactory())
  }

  project.apply {
    add(bounds)
    add(bullet)
    add(asteroid)
    add(player)
    add(explosion)
  }
}