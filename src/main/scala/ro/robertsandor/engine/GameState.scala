package ro.robertsandor.engine

import ro.robertsandor.engine.objects.GameObject
import ro.robertsandor.engine.services.Service

/**
  * Created by robert on 5/22/17.
  */
class GameState (rootGameObject: GameObject) {
  var services: List[Service] = List()

  def getRootObject: GameObject = rootGameObject
}
