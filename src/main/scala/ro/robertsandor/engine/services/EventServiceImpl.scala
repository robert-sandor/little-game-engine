package ro.robertsandor.engine.services

import ro.robertsandor.engine.base.GameState
import ro.robertsandor.engine.base.events.BaseEvent
import ro.robertsandor.engine.base.services.EventService

import scala.collection.mutable

/**
  * Created by sando on 6/4/2017.
  */
object EventServiceImpl extends EventService {
  private val listenerMap = new mutable.HashMap[String, mutable.HashSet[(String, (BaseEvent) => Any)]]()
  private val eventQueue = new mutable.Queue[BaseEvent]()

  override def addEventListener(objectName: String, eventName: String, listener: (BaseEvent) => Any): Unit = {
    if (listenerMap.get(eventName).isEmpty) {
      listenerMap += (eventName -> new mutable.HashSet[(String, (BaseEvent) => Any)]())
    }
    listenerMap(eventName) += ((objectName, listener))
  }

  override def removeEventListener(objectName: String, eventName: String, listener: (BaseEvent) => Any): Unit = {
    if (listenerMap.get(eventName).isDefined) {
      listenerMap(eventName) -= ((objectName, listener))
    }
  }

  override def queueEvent(event: BaseEvent): Unit = {
    eventQueue += event
  }

  override def dispatchEvent(event: BaseEvent, objectNameOpt: Option[String] = None): Unit = {
    val listenersOpt = listenerMap.get(event.name)
    listenersOpt match {
      case Some(listeners) =>
        objectNameOpt match {
          case Some(objectName) =>
            val toCall = listeners.find(tuple => tuple._1 == objectName)
            toCall match {
              case Some((_, listener)) => listener.apply(event)
              case _ => None
            }
          case None => listeners.foreach(_._2.apply(event))
        }
      case _ => None
    }
  }

  override def update(gameState: GameState): GameState = {
    eventQueue.foreach(event => {
      val listenersOpt = listenerMap.get(event.name)
      listenersOpt match {
        case Some(listeners) =>
          listeners.map(_._2)
            .foreach(listener => {
              listener.apply(event)
            })
      }
    })
    gameState
  }
}
