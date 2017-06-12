package ro.robertsandor.engine.base

import ro.robertsandor.engine.base.`object`.GameObject

case class GameState(
                      rootGameObjects: List[GameObject],
                      properties: Map[String, Any]
                    )

object GameState {
  var currentGameState: GameState = new GameState(List(), Map())

  def getObjectByName(parent: GameObject, name: String): Option[GameObject] = parent.children.find(_.name == name)

  def getObjectByFullName(fullName: String): Option[GameObject] = {
    val fullNameParts = fullName.split("/").tail
    val rootObject = currentGameState.rootGameObjects.find(_.name == fullNameParts.head)

    getObjectRecursive(fullNameParts.tail, rootObject)
  }

  private def getObjectRecursive(nameParts: Array[String], parent: Option[GameObject]): Option[GameObject] = {
    parent match {
      case Some(gameObject) =>
        if (nameParts.isEmpty) {
          Some(gameObject)
        } else {
          getObjectRecursive(nameParts.tail, gameObject.children.find(_.name == nameParts.head))
        }
      case None => None
    }
  }
}