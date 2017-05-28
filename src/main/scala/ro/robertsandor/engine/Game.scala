package ro.robertsandor.engine

import org.json4s._
import org.json4s.native.JsonMethods._
import ro.robertsandor.engine.components.Component
import ro.robertsandor.engine.mapping.Mapping
import ro.robertsandor.engine.objects.GameObject
import ro.robertsandor.engine.services.Service

import scala.io.Source
import scala.util.Properties


/**
  * Created by robert on 5/22/17.
  */
object Game {
  implicit val formats = DefaultFormats

  def newGameFromJson(jsonPath: String): Unit = {
    val gameContent = Source.fromFile(jsonPath, "utf-8")
      .getLines
      .mkString(Properties.lineSeparator)
    val jsonResult = parse(gameContent)

    jsonResult match {
      case data: JObject => {
        val rootObject = new GameObject(None)
        rootObject.loadData(data.extract[Map[String, Any]])
        val gameState = new GameState(rootObject)
        println(gameState.getRootObject.getChildren("childGameObject").getChildren("childOfChild").getFullName)
      }
      case _ => throw new RuntimeException("Invalid game file format!")
    }
  }

  def newGame(gameState: GameState): Unit = {

  }

  def loadMapping(yamlPath: String): Unit = {
    val mappingContent = Source.fromFile(yamlPath, "utf-8")
      .getLines
      .mkString(Properties.lineSeparator)
    val jsonResult = parse(mappingContent)

    jsonResult match {
      case data: JObject =>
        loadServiceMapping(data.extract[Map[String, Any]].get("services"))
        loadComponentMapping(data.extract[Map[String, Any]].get("components"))
      case _ => throw new RuntimeException("Invalid mapping format!")
    }
  }

  private def loadServiceMapping(serviceMapping: Option[Any]) = {
    serviceMapping.get match {
      case serviceMap: Map[String, String] =>
        serviceMap.foreach(_ match {
          case (name: String, className: String) =>
            try
              Mapping.serviceMap += (name -> Class.forName(className).asSubclass(classOf[Service]))
            catch {
              case _: ClassNotFoundException => throw new RuntimeException(s"Class $className was not found in classpath!")
              case _: Throwable => throw new RuntimeException(s"Class $className might not be extending Service!")
            }
          case _ => throw new RuntimeException("Invalid service mapping format!")
        })
      case _ => throw new RuntimeException("Invalid service mapping format!")
    }
  }

  private def loadComponentMapping(componentMapping: Option[Any]): Unit = {
    componentMapping.get match {
      case componentMap: Map[String, String] =>
        componentMap.foreach(_ match {
          case (name: String, className: String) =>
            try {
              Mapping.componentMap += name -> Class.forName(className).asSubclass(classOf[Component])
            } catch {
              case _: ClassNotFoundException => throw new RuntimeException(s"Class $className was not found in classpath!")
              case _: Throwable => throw new RuntimeException(s"Class $className might not be extending Component!")
            }
          case _ => throw new RuntimeException("Invalid component mapping format!")
        })
      case _ => throw new RuntimeException("Invalid component mapping format!")
    }
  }
}
