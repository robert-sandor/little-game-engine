package ro.robertsandor.engine.base.services

import ro.robertsandor.engine.base.events.BaseEvent

/**
  * Created by robert on 5/22/17.
  */
trait EventService extends Service {
  final override val serviceType: String = "event"

  def addEventListener(objectName: String, eventName: String, listener: BaseEvent => Any): Unit

  def removeEventListener(objectName: String, eventName: String, listener: BaseEvent => Any): Unit

  def queueEvent(event: BaseEvent): Unit

  def dispatchEvent(event: BaseEvent, objectName: String): Option[Any]
}
