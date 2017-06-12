package ro.robertsandor.engine.base.transform

case class Transform(absolute: Boolean = false,
                     position: Vec = Vec.zeros,
                     size: Vec = Vec.zeros,
                     speed: Vec = Vec.zeros,
                     acceleration: Vec = Vec.zeros,
                     scale: Vec = Vec.ones,
                     rotation: Vec = Vec.zeros)

object Transform {
  def newTransform(transformMap: Map[String, _]): Transform = Transform(
    position = newVec(transformMap, "position"),
    size = newVec(transformMap, "size"),
    speed = newVec(transformMap, "speed"),
    acceleration = newVec(transformMap, "acceleration"),
    scale = newVec(transformMap, "scale"),
    rotation = newVec(transformMap, "rotation")
  )

  def newVec(transformMap: Map[String, _], key: String): Vec = {
    transformMap.get(key) match {
      case Some(positionArray: Array[Double]) => Vec.fromArray(positionArray)
      case Some(positionVector: Vector[Double]) => Vec.fromVector(positionVector)
      case Some(positionList: List[Double]) => Vec.fromList(positionList)
      case _ =>
        key match {
          case "scale" => Vec.ones
          case _ => Vec.zeros
        }
    }
  }

  def getAbsoluteTransform(parentTransform: Transform, transform: Transform): Transform = {
    val absoluteScale = parentTransform.scale :* transform.scale
    val absolutePosition = (transform.position :* parentTransform.scale) + parentTransform.position
    val absoluteSpeed = transform.speed :* parentTransform.scale
    val absoluteAcceleration = transform.acceleration :* parentTransform.scale
    val absoluteRotation = transform.rotation + parentTransform.rotation
    val absoluteSize = transform.size :* parentTransform.scale

    Transform(
      position = absolutePosition,
      size = absoluteSize,
      speed = absoluteSpeed,
      acceleration = absoluteAcceleration,
      scale = absoluteScale,
      rotation = absoluteRotation
    )
  }
}