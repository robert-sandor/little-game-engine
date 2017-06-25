package ro.robertsandor

import java.util.{Timer, TimerTask}
import java.util.concurrent.Executors
import javafx.application.Application
import javafx.scene.canvas.Canvas
import javafx.scene.{Group, Scene}
import javafx.stage.Stage

import ro.robertsandor.engine.Game
import ro.robertsandor.engine.GameBootstrap.loadMapping
import ro.robertsandor.engine.base.GameState
import ro.robertsandor.engine.base.`object`.GameObject
import ro.robertsandor.engine.base.transform.{Transform, Vec}
import ro.robertsandor.engine.components.{BallComponent, Color, JavaFXRenderComponent}
import ro.robertsandor.engine.services.JavaFXRender
import ro.robertsandor.engine.services.JavaFXRender.{frames, gc}

import scala.compat.Platform
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.io.Source
import scala.util.Random

/**
  * @author ${user.name}
  */
class App extends Application {

  var engineThread: Future[Unit] = _
  var timer: Timer = _

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Game")
    val root = new Group()
    val canvas = new Canvas(800, 600)

    JavaFXRender.gc = canvas.getGraphicsContext2D
    //    JavaFXInput.scene = scene

    //    scene.setOnKeyPressed((_: KeyEvent) => {
    //      println("key pressed")
    //    })

    //    val scene = JavaFXInput.initializeInputEvents(new Scene(root))

    root.getChildren.add(canvas)
    primaryStage.setScene(new Scene(root))
    //    primaryStage.setScene(scene)
    primaryStage.show()

    App.launchEngine()
  }

  override def stop(): Unit = {
    super.stop()
    App.stopEngine()
    JavaFXRender.fpsTimer.cancel()
  }
}

object App {
  var timerOpt: Option[Timer] = None

  implicit val context: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  def stopEngine(): Unit = {
    timerOpt match {
      case Some(timer) => timer.cancel()
      case _ => println("WTF")
    }
  }

  def main(args: Array[String]) {
    Application.launch(classOf[App])
    println("YYYY")
  }

  def launchEngine(): Unit = {
    loadMapping(Source.fromResource("mapping.json").mkString)

    //    val gameState = loadGameState(getClass.getResource("/game.json").getPath)

    val random = new Random()

    val gameState = GameState(
      properties = Map(),
      rootGameObjects = Range(0, 10000).map(number => {
        GameObject(
          "",
          "object" + number,
          "",
          List(
            JavaFXRenderComponent(
              renderType = "circle",
              diameter = Some(10),
              color = Color(1)
            ),
            BallComponent()
          ),
          children = List(),
          transform = Transform(
            speed = Vec(random.nextDouble() * 10 - 5, random.nextDouble() * 10 - 5),
            position = Vec(random.nextDouble() * 800, random.nextDouble() * 600)
          )
        )
      }).toList
    )

    timerOpt = Some(Game.loop(gameState, 100))

    println("Started engine.")
  }
}
