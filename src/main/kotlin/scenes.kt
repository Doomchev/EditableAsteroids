import mod.actions.splitImage
import mod.actions.sprite.*
import mod.dragging.*
import kotlin.math.PI

fun tilemap() {
  splitImage(imageArrays[4], 5, 7)
  scene.add(TileMap(10, 16, 1.0, 1.0, imageArrays[4]))
}

fun snow() {
  val flake = addClass("Снежинка")

  val area = Sprite(0.0, -10.0, 10.0, 2.0, "snow gen")
  flake.onCreate.add(SpritePositionInAreaFactory(area))
  flake.onCreate.add(SpriteSetSizeFactory(RandomDoubleValue(0.25, 1.0)))
  flake.onCreate.add(SpriteSetImageFactory(imageArrays[4].images[0]))
  flake.onCreate.add(SpriteSetMovingVectorFactory(zero, RandomDoubleValue(1.0, 5.0)))
  flake.always.add(SpriteMoveFactory())

  scene.add(area)
  actions.add(SpriteCreate(Sprite(), flake, 0.1))
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

  /// SPRITES

  val player = Sprite(-3.0, -5.0, 1.0, 1.0, "игрок")
  player.image = imageArrays[4].images[0]

  Key(97, user).onPressActions.add(ActionEntry(world,
    SpriteRotation(player, -1.5 * PI)
  ))
  Key(100, user).onPressActions.add(ActionEntry(world,
    SpriteRotation(player, 1.5 * PI)
  ))
  Key(119, user).onPressActions.add(ActionEntry(world,
    SpriteAcceleration(player, 50.0, 10.0)
  ))

  val bounds = Sprite(world.centerX, world.centerY, world.width + 2.0, world.height + 2.0, "границы поля")
  actions.add(SpriteAcceleration(player, -15.0, 100.0))
  actions.add(SpriteLoopArea(player, bounds))
  actions.add(SpriteMove(player))

  val bullet = addClass("Пуля")

  bullet.onCreate.add(SpritePositionAsFactory(player))
  bullet.onCreate.add(SpriteDirectAsFactory(player))
  bullet.onCreate.add(SpriteSetSizeFactory(DoubleValue(0.15)))
  bullet.onCreate.add(SpriteSetSpeedFactory(DoubleValue(15.0)))
  bullet.always.add(SpriteMoveFactory())
  bullet.always.add(SpriteAnimationFactory(imageArrays[2], DoubleValue(16.0)))
  bullet.always.add(SpriteSetBoundsFactory(bounds))

  Key(32, user).onPressActions.add(ActionEntry(world, SpriteCreate(player, bullet, 0.1)))

  val asteroid = addClass("Астероид")
  asteroid.onCreate.add(SpritePositionInAreaFactory(bounds))
  asteroid.onCreate.add(SpriteSetSizeFactory(RandomDoubleValue(0.5, 2.0)))
  asteroid.onCreate.add(SpriteSetSpeedFactory(DoubleValue(15.0)))
  asteroid.always.add(
    SpriteAnimationFactory(imageArrays[1], RandomDirection(
      RandomDoubleValue(12.0, 20.0)
    )
    )
  )
  asteroid.always.add(SpriteRotationFactory(RandomDoubleValue(-180.0, 180.0)))
  asteroid.always.add(SpriteLoopAreaFactory(bounds))

  Key(98, user).onClickActions.add(ActionEntry(world, SpriteCreate(Sprite(), asteroid, 0.0)))

  scene.add(bounds)
  scene.add(bullet)
  scene.add(asteroid)
  scene.add(player)
}