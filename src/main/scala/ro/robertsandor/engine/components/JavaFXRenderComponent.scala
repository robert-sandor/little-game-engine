package ro.robertsandor.engine.components

import ro.robertsandor.engine.base.`object`.GameObject
import ro.robertsandor.engine.base.component.{Component, ComponentData}

case class Color(red: Double = 0, green: Double = 0, blue: Double = 0, alpha: Double = 1)

case class JavaFXRenderComponent(
                                  componentType: String = JavaFXRenderComponent.COMPONENT_TYPE,
                                  renderType: String,
                                  source: Option[String] = None,
                                  width: Option[Int] = None,
                                  height: Option[Int] = None,
                                  diameter: Option[Int] = None,
                                  color: Color = Color()
                                ) extends ComponentData

object JavaFXRenderComponent extends Component[JavaFXRenderComponent] {
  val RENDER_TYPE_IMAGE = "image"
  val RENDER_TYPE_RECT = "rect"
  val RENDER_TYPE_CIRCLE = "circle"
  val COMPONENT_TYPE = "render"

  override def newComponentData(componentMap: Map[String, _]): JavaFXRenderComponent = {
    componentMap("renderType") match {
      case RENDER_TYPE_IMAGE => newImageRenderData(componentMap)
      case RENDER_TYPE_RECT => newRectRenderData(componentMap)
      case RENDER_TYPE_CIRCLE => newCircleRenderData(componentMap)
      case _ => throw new IllegalArgumentException(s"Incompatible renderType $componentMap")
    }
  }

  def newImageRenderData(componentMap: Map[String, _]): JavaFXRenderComponent = {
    JavaFXRenderComponent(
      renderType = RENDER_TYPE_IMAGE,
      source = getSource(componentMap),
      width = getWidth(componentMap),
      height = getHeight(componentMap)
    )
  }

  def getSource(componentMap: Map[String, _]): Option[String] = componentMap.get("source") match {
    case Some(value: String) => Some(value)
    case _ => throw new IllegalArgumentException("RenderComponent of type image requires a source path.")
  }

  def newRectRenderData(componentMap: Map[String, _]): JavaFXRenderComponent = {
    JavaFXRenderComponent(
      renderType = RENDER_TYPE_RECT,
      width = getWidth(componentMap) match {
        case Some(value: Int) => Some(value)
        case _ => throw new IllegalArgumentException("RenderComponent of type rect requires a width value.")
      },
      height = getHeight(componentMap) match {
        case Some(value: Int) => Some(value)
        case _ => throw new IllegalArgumentException("RenderComponent of type rect requires a height value.")
      },
      color = getColor(componentMap)
    )
  }

  def getWidth(componentMap: Map[String, _]): Option[Int] = componentMap.get("width") match {
    case Some(value: Int) => Some(value)
    case Some(value: BigInt) => Some(value.intValue())
    case _ => None
  }

  def getHeight(componentMap: Map[String, _]): Option[Int] = componentMap.get("height") match {
    case Some(value: Int) => Some(value)
    case Some(value: BigInt) => Some(value.intValue())
    case _ => None
  }

  def newCircleRenderData(componentMap: Map[String, _]): JavaFXRenderComponent = {
    JavaFXRenderComponent(
      renderType = RENDER_TYPE_CIRCLE,
      diameter = getDiameter(componentMap),
      color = getColor(componentMap)
    )
  }

  def getColorOnly(colorVector: Vector[Any]): Color = {
    val colorData = (colorVector(0), colorVector(1), colorVector(2))
    colorData match {
      case (red: BigInt, green: BigInt, blue: BigInt) =>
        Color(red.doubleValue(), green.doubleValue(), blue.doubleValue())
      case (red: BigDecimal, green: BigDecimal, blue: BigDecimal) =>
        Color(red.doubleValue(), green.doubleValue(), blue.doubleValue())
      case (red: Int, green: Int, blue: Int) =>
        Color(red, green, blue)
      case (red: Double, green: Double, blue: Double) =>
        Color(red, green, blue)
      case _ =>
        Color()
    }
  }

  def getColorWithAlpha(colorVector: Vector[Any]): Color = {
    val colorData = (colorVector(0), colorVector(1), colorVector(2), colorVector(3))
    colorData match {
      case (red: BigInt, green: BigInt, blue: BigInt, alpha: BigInt) =>
        Color(red.doubleValue(), green.doubleValue(), blue.doubleValue(), alpha.doubleValue())
      case (red: BigDecimal, green: BigDecimal, blue: BigDecimal, alpha: BigDecimal) =>
        Color(red.doubleValue(), green.doubleValue(), blue.doubleValue(), alpha.doubleValue())
      case (red: Int, green: Int, blue: Int, alpha: Int) =>
        Color(red, green, blue, alpha)
      case (red: Double, green: Double, blue: Double, alpha: Double) =>
        Color(red, green, blue, alpha)
      case _ =>
        Color()
    }
  }

  def getColorData(colorVector: Vector[Any]): Color = {
    colorVector.size match {
      case 3 =>
        getColorOnly(colorVector)
      case 4 =>
        getColorWithAlpha(colorVector)
      case _ => Color()
    }
  }

  def getColor(componentMap: Map[String, _]): Color = {
    componentMap.get("color") match {
      case Some(colorVector: Vector[_]) =>
        getColorData(colorVector)
      case Some(colorList: List[_]) =>
        getColorData(colorList.toVector)
      case _ => Color()
    }
  }

  def getDiameter(componentMap: Map[String, _]): Option[Int] = componentMap.get("diameter") match {
    case Some(value: Int) => Some(value)
    case Some(value: BigInt) => Some(value.intValue())
    case _ => throw new IllegalArgumentException("RenderComponent of type circle requires a source path.")
  }

  override def update(gameObject: GameObject): GameObject = gameObject
}