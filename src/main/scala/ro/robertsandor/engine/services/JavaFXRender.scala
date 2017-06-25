package ro.robertsandor.engine.services

import java.util.{Timer, TimerTask}
import javafx.application.Platform
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.text.Font

import ro.robertsandor.engine.base.GameState
import ro.robertsandor.engine.base.`object`.GameObject
import ro.robertsandor.engine.base.services.RenderService
import ro.robertsandor.engine.base.transform.Transform
import ro.robertsandor.engine.components.JavaFXRenderComponent


object JavaFXRender extends RenderService {
  val width: Int = 800
  val height: Int = 600

  var gc: GraphicsContext = _
  var frames: Int = 0
  var fps: Int = 0

  val fpsTimer = new Timer()
  val displayFrameRate = new TimerTask {
    override def run(): Unit = {
      fps = frames
      frames = 0
      println("fps : " + fps)
    }
  }
  fpsTimer.schedule(displayFrameRate, 0, 1000)

  override def render(gameState: GameState): Unit = {
    Platform.runLater(() => {
      clear()
      gameState.rootGameObjects.foreach(child => drawGameObject(child, Transform()))
      gc.setFill(new Color(0, 0, 0, 1))
      gc.setFont(new Font(24))
      gc.fillText("fps : " + fps, 20, 20)
      frames += 1
    })
  }

  def drawGameObject(gameObject: GameObject, parentTransform: Transform): Unit = {
    val renderDataOpt: Option[JavaFXRenderComponent] = GameObject.getComponentData(gameObject)

    val transform: Transform = if (gameObject.transform.absolute) {
      gameObject.transform
    } else {
      Transform.getAbsoluteTransform(parentTransform, gameObject.transform)
    }

    if (renderDataOpt.isDefined) {
      val renderData = renderDataOpt.get

      renderData.renderType match {
        case "image" => renderImage(renderData, transform)
        case "rect" => renderRect(renderData, transform)
        case "circle" => renderCircle(renderData, transform)
      }
    }

    gameObject.children.foreach(child => drawGameObject(child, transform))
  }

  def renderImage(renderData: JavaFXRenderComponent, transform: Transform): Unit = {
    val image = new Image(renderData.source.get)
    gc.drawImage(image,
      transform.position.x,
      transform.position.y,
      image.getWidth * transform.scale.x,
      image.getHeight * transform.scale.y
    )
  }

  def renderRect(renderData: JavaFXRenderComponent, transform: Transform): Unit = {
    gc.setFill(newColor(renderData))
    gc.fillRect(transform.position.x,
      transform.position.y,
      renderData.width.get * transform.scale.x,
      renderData.height.get * transform.scale.y
    )
  }

  def newColor(renderData: JavaFXRenderComponent): Color = new Color(renderData.color.red, renderData.color.green, renderData.color.blue, renderData.color.alpha)

  def renderCircle(renderData: JavaFXRenderComponent, transform: Transform): Unit = {
    gc.setFill(newColor(renderData))
    gc.fillOval(transform.position.x,
      transform.position.y,
      renderData.diameter.get * transform.scale.x,
      renderData.diameter.get * transform.scale.y
    )
  }

  def clear(): Unit = {
    gc.clearRect(0, 0, width, height)
//    System.gc()
  }
}
