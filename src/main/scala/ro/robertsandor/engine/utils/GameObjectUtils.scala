package ro.robertsandor.engine.utils

import ro.robertsandor.engine.objects.GameObject

import scala.collection.mutable.ListBuffer

/**
  * Created by robert on 5/22/17.
  */
object GameObjectUtils {
  def generateFullName(gameObject: GameObject): String = {
    var parent = gameObject.getParent
    var listOfNames = new ListBuffer[String]
    listOfNames += gameObject.getName
    while (parent.isDefined) {
      listOfNames += parent.get.getName
      parent = parent.get.getParent
    }

    listOfNames.toList.reverse.mkString(".")
  }
}
