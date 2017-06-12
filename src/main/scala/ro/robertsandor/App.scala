package ro.robertsandor

import java.util.Timer
import java.util.concurrent.Executors
import javafx.application.Application
import javafx.scene.canvas.Canvas
import javafx.scene.{Group, Scene}
import javafx.stage.Stage

import ro.robertsandor.engine.Game
import ro.robertsandor.engine.GameBootstrap.{loadMapping, newGameStateFromJson}
import ro.robertsandor.engine.services.JavaFXRender

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

/**
  * @author ${user.name}
  */
class App extends Application {

  var engineThread: Future[Unit] = _

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Game")
    val root = new Group()
    val canvas = new Canvas(800, 600)
    JavaFXRender.gc = canvas.getGraphicsContext2D
    root.getChildren.add(canvas)
    primaryStage.setScene(new Scene(root))
    primaryStage.show()

    App.launchEngine()
  }

  override def stop(): Unit = {
    super.stop()
    App.stopEngine()
  }
}

object App {
  def stopEngine(): Unit = {
    timerOpt match {
      case Some(timer) => timer.cancel()
      case _ => println("WTF")
    }
  }

  implicit val context: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  var timerOpt: Option[Timer] = None

  def main(args: Array[String]) {
    Application.launch(classOf[App])
    println("YYYY")
  }

  def launchEngine(): Unit = {
    loadMapping(getClass.getResource("/mapping.json").getPath)

    val gameState = newGameStateFromJson(getClass.getResource("/game.json").getPath)

    timerOpt = Some(Game.loop(gameState, 60))

    println("Started engine.")
  }
}
