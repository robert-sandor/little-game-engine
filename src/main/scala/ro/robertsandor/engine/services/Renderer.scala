package ro.robertsandor.engine.services

import ro.robertsandor.engine.GameState

/**
  * Created by robert on 5/22/17.
  */
trait Renderer extends Service {
  def renderGameState(gameState: GameState): Unit
}
