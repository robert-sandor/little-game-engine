package ro.robertsandor.engine

import org.json4s._
import org.json4s.native.JsonMethods._
import ro.robertsandor.engine.base.GameState
import ro.robertsandor.engine.base.`object`.GameObject
import ro.robertsandor.engine.base.component.{Component, ComponentData}
import ro.robertsandor.engine.base.services.Service
import ro.robertsandor.engine.mapping.Mapping

import scala.io.Source
import scala.reflect.runtime.universe

/**
  * Created by robert on 5/22/17.
  */
object GameBootstrap {
  implicit val formats = DefaultFormats
  private val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)

  def newGameStateFromJson(jsonPath: String): GameState = {
    try {
      val parseResult = parse(Source.fromFile(jsonPath).mkString)

      val jsonResult = parseResult.extract[Map[String, Any]]

      GameState(
        rootGameObjects = getGameObjects(jsonResult),
        properties = getProperties(jsonResult)
      )
    } catch {
      case e: Throwable => throw new RuntimeException(s"Failed to parse file $jsonPath", e)
    }
  }

  def getProperties(gameMap: Map[String, Any]): Map[String, Any] = {
    gameMap.get("properties") match {
      case Some(value: Map[String, Any]) => value
      case _ => throw new RuntimeException("No properties in game file")
    }
  }

  def getGameObjects(gameMap: Map[String, Any]): List[GameObject] = {
    gameMap.get("objects") match {
      case Some(list: List[Map[String, Any]]) => list.map(data => GameObject.newGameObject(data, None))
      case _ => throw new RuntimeException("No objects in game file")
    }
  }

  def loadMapping(jsonPath: String): Unit = {
    try {
      val jsonResult = parse(Source.fromFile(jsonPath).mkString)
        .extract[Map[String, Map[String, String]]]
      loadServiceMapping(jsonResult.getOrElse("services", Map()))
      loadComponentMapping(jsonResult.getOrElse("components", Map()))
    } catch {
      case e: Throwable => throw new RuntimeException(s"Failed to parse file $jsonPath", e)
    }
  }

  private def loadServiceMapping(serviceMapping: Map[String, String]): Unit = {
    serviceMapping.foreach(pair => {
      try {
        val serviceObject = runtimeMirror.reflectModule(runtimeMirror.staticModule(pair._2))
        val service = serviceObject.instance.asInstanceOf[Service]
        Mapping.serviceMap(pair._1) = service
      } catch {
        case e: Throwable => throw new RuntimeException(s"Error while loading Service object ${pair._2}", e)
      }
    })
    initServices()
  }

  def initServices(): Unit = {
    if (Mapping.eventService.isDefined &&
      Mapping.renderService.isDefined) {
      Mapping.renderService.get.init()
      Mapping.eventService.get.init()
    } else {
      throw new RuntimeException("Must have Services are not defined in the mapping file!")
    }
  }

  private def loadComponentMapping(componentMapping: Map[String, String]): Unit = {
    componentMapping.foreach(pair => {
      try {
        val componentObject = runtimeMirror.reflectModule(runtimeMirror.staticModule(pair._2))
        val component = componentObject.instance.asInstanceOf[Component[ComponentData]]
        Mapping.componentMap(pair._1) = component
      } catch {
        case e: Throwable => throw new RuntimeException(s"Error while loading Component object ${pair._2}", e)
      }
    })
  }
}
