package ro.robertsandor

import ro.robertsandor.engine.Game

/**
  * @author ${user.name}
  */
object App {

  def main(args: Array[String]) {
    Game.loadMapping(getClass.getResource("/mapping.json").getPath)
    Game.newGameFromJson(getClass.getResource("/game.json").getPath)
  }

}
