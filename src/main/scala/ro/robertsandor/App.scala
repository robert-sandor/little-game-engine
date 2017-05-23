package ro.robertsandor

import ro.robertsandor.engine.Game

/**
  * @author ${user.name}
  */
object App {

  def main(args: Array[String]) {
    Game.loadMapping(getClass.getResource("/mapping.yaml").getPath)
    Game.newGameFromYaml(getClass.getResource("/game.yaml").getPath)
  }

}
