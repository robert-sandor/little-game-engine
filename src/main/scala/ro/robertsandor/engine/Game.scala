package ro.robertsandor.engine

import org.yaml.snakeyaml.Yaml
import ro.robertsandor.engine.components.Component
import ro.robertsandor.engine.mapping.Mapping
import ro.robertsandor.engine.objects.GameObject
import ro.robertsandor.engine.services.Service

import scala.collection.JavaConverters._
import scala.io.Source
import scala.util.Properties

/**
  * Created by robert on 5/22/17.
  */
object Game {

  def newGameFromYaml(yamlPath: String): Unit = {
    val yaml = new Yaml()
    val gameContent = Source.fromFile(yamlPath, "utf-8")
      .getLines
      .mkString(Properties.lineSeparator)
    val yamlResult = yaml.load(gameContent)

    val scalaMap: Map[String, Any] = parseResult(yamlResult)

    yamlResult match {
      case data: java.util.Map[String, Any] => {
        val rootObject = new GameObject(None)
        rootObject.loadData(data.asScala.toMap)
        val gameState = new GameState(rootObject)
        println(gameState.getRootObject.getChildren("anotherChildObject").getFullName)
      }
      case _ => throw new RuntimeException("Invalid game file format!")
    }
  }

  def parseResult(yamlResult: AnyRef): Map[String, Any] = {
    yamlResult match {
      case javaMap: java.util.Map[String, Any] =>
    }
  }

  def newGame(gameState: GameState): Unit = {

  }

  def loadMapping(yamlPath: String): Unit = {
    val yaml = new Yaml()
    val mappingContent = Source.fromFile(yamlPath, "utf-8")
      .getLines
      .mkString(Properties.lineSeparator)
    val yamlResult = yaml.load(mappingContent)

    yamlResult match {
      case map: java.util.Map[String, Any] =>
        loadServiceMapping(map.asScala.get("services"))
        loadComponentMapping(map.asScala.get("components"))
      case _ => throw new RuntimeException("Invalid mapping format!")
    }
  }

  private def loadServiceMapping(serviceMapping: Option[Any]) = {
    serviceMapping.get match {
      case serviceMap: java.util.Map[String, String] =>
        serviceMap.asScala.foreach(_ match {
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
      case componentMap: java.util.Map[String, String] =>
        componentMap.asScala.foreach(_ match {
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
