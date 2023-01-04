import mod.*
import mod.actions.SoundPlayFactory
import mod.actions.splitImage
import mod.actions.sprite.*
import state.IfStateFactory
import state.SpriteSetStateFactory
import state.newState
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
  flake.always.add(SpriteMoveForwardFactory(currentEntry))

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
    add(SpriteMoveForward(player))
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
    add(SpriteMoveForwardFactory(currentEntry))
    add(SpriteAnimationFactory(currentEntry, imageArrays[2], DoubleValue(16.0)))
    add(SpriteSetBoundsFactory(currentEntry, bounds))
  }

  Key(32, user).onPressActions.add(ActionEntry(world, SpriteDelayedCreate(player, bullet, 0.1)))

  val asteroid = addClass("Астероид")
  val big = newState("большой")
  val medium = newState("средний")
  val small = newState("маленький")

  asteroid.always.apply {
    add(SpriteAnimationFactory(currentEntry, imageArrays[1], RandomDirection(RandomDoubleValue(12.0, 20.0))))
    add(SpriteRotationFactory(currentEntry, RandomDoubleValue(-180.0, 180.0)))
    add(SpriteLoopAreaFactory(currentEntry, bounds))
    add(SpriteMoveForwardFactory(currentEntry))
  }

  Key(98, user).onClickActions.add(ActionEntry(world, SpriteCreate(Sprite(blankImage), asteroid, mutableListOf<SpriteActionFactory>(
    SpriteSetStateFactory(currentEntry, big),
    SpriteSetSizeFactory(currentEntry, DoubleValue(3.0)),
    SpriteSetAngleFactory(currentEntry, RandomDoubleValue(0.0, 360.0)),
    SpriteSetSpeedFactory(currentEntry, RandomDoubleValue(2.0, 3.0)))
  )))


  val explosion = addClass("Взрыв")

  explosion.onCreate.apply {
    add(SoundPlayFactory(sounds[1]))
    add(SpritePositionAsFactory(currentEntry, parentEntry))
  }
  
  explosion.always.apply {
    add(SpriteAnimationFactory(currentEntry, explosionImage, DoubleValue(16.0)))
    add(SpriteDelayedRemoveFactory(currentEntry, DoubleValue(1.0)))
  }

  bullet.apply {
    addOnCollision(asteroid
      /*, SpriteCreateFactory(sprite1Entry, explosion
        , SpriteSetSizeFactory(currentEntry, DoubleValue(1.0)))*/
      , IfStateFactory(sprite2Entry, big
        , SpriteCreateFactory(sprite2Entry, explosion
          , SpriteSetSizeFactory(currentEntry, DoubleValue(3.0)))
        , SpriteCreateFactory(sprite2Entry, asteroid
          , SpriteSetStateFactory(currentEntry, medium)
          , SpritePositionAsFactory(currentEntry, sprite2Entry)
          , SpriteSetSizeFactory(currentEntry, DoubleValue(2.0))
          , SpriteDirectToFactory(currentEntry, sprite1Entry)
          , SpriteTurnFactory(currentEntry, RandomDoubleValue(150.0, 210.0))
          , SpriteSetSpeedFactory(currentEntry, RandomDoubleValue(3.0, 5.0))))

      , IfStateFactory(sprite2Entry, mutableListOf(big, medium)
        , SpriteCreateFactory(sprite2Entry, explosion
          , SpriteSetSizeFactory(currentEntry, DoubleValue(2.0)))
        , SpriteCreateFactory(sprite2Entry, asteroid
          , SpriteSetStateFactory(currentEntry, small)
          , SpritePositionAsFactory(currentEntry, sprite2Entry)
          , SpriteSetSizeFactory(currentEntry, DoubleValue(1.0))
          , SpriteDirectToFactory(currentEntry, sprite1Entry)
          , SpriteTurnFactory(currentEntry, RandomDoubleValue(210.0, 270.0))
          , SpriteSetSpeedFactory(currentEntry, RandomDoubleValue(5.0, 7.0)))

        , SpriteCreateFactory(sprite2Entry, asteroid
          , SpriteSetStateFactory(currentEntry, small)
          , SpritePositionAsFactory(currentEntry, sprite2Entry)
          , SpriteSetSizeFactory(currentEntry, DoubleValue(1.0))
          , SpriteDirectToFactory(currentEntry, sprite1Entry)
          , SpriteTurnFactory(currentEntry, RandomDoubleValue(90.0, 150.0))
          , SpriteSetSpeedFactory(currentEntry, RandomDoubleValue(5.0, 7.0))))

      , IfStateFactory(sprite2Entry, small
        , SpriteCreateFactory(sprite2Entry, explosion
          , SpriteSetSizeFactory(currentEntry, DoubleValue(1.0))))

      , SpriteRemoveFactory(sprite1Entry)

      , SpriteRemoveFactory(sprite2Entry)
    )
  }

  project.apply {
    add(bounds.sprite!!)
    add(bullet)
    add(asteroid)
    add(player)
    add(explosion)
  }
}