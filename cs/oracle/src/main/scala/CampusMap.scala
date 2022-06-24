import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.{Group, Scene}
import javafx.scene.paint.Color
import javafx.scene.shape.{Circle, Rectangle}
import javafx.scene.text.Text
import javafx.stage.Stage

import java.lang.Math.sqrt
import scala.concurrent.Future
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

object CampusMap {
  def main(args: Array[String]): Unit = launch(classOf[CampusMapApp])
}

class CampusMapApp extends Application {
  override def start(stage: Stage): Unit = {
    val root = new Group()
    val scene = new Scene(root, 1000, 800, Color.rgb(236, 240, 241))

    val dorm1 = new Dorm(1, 52, 152, 219)
    val dorm2 = new Dorm(2, 26, 188, 156)
    val dorm3 = new Dorm(3, 155, 89, 182)
    val dorm4 = new Dorm(4, 231, 76, 60)

    val dorms = List(dorm1, dorm2, dorm3, dorm4)

    // Инициализируем точку центра всех общаг
    val dormitoryCenterPoint = new DormitoryCenterPoint(dorms: _*)
    // Инициализируем точку центра студенческой группы
    val studyCenterPoint = new StudyCenterPoint(dorm1, dorm2, dorm4)

    // Добавляем всё на экран
    root.getChildren.addAll(
      List(
        dormitoryCenterPoint.distance,
        dormitoryCenterPoint.center,
        dormitoryCenterPoint.label,
        studyCenterPoint.distance,
        studyCenterPoint.center,
        studyCenterPoint.label
      ) ++
        dorms.map(_.dormLabel)
        ++ dorms.map(_.populationLabel)
        ++ dorms.map(_.rectangle): _*
    )

    stage.setTitle("Карта универа")
    stage.setScene(scene)
    stage.setResizable(false)
    stage.show()

    // Форкаем тред для обновления координат и населения общаг в бекграунде
    Future {
      while (true) {
        Thread.sleep(2000)

        dorm1.refresh()
        dorm2.refresh()
        dorm3.refresh()
        dorm4.refresh()

        dormitoryCenterPoint.setCenter(dorm1, dorm2, dorm3, dorm4)
        studyCenterPoint.setCenter(dorm1, dorm2, dorm4)
      }
    }
  }
}

class Dorm(id: Int, r: Int, g: Int, b: Int) {
  val rectangle = new Rectangle(0, 0, 10, 10)
  var width = 0
  var population = 0

  var x: Int = id * 200
  var y: Int = 0

  val dormLabel = new Text(0, 40, s"Общага $id")
  val populationLabel = new Text(0, 40, "")

  refresh()

  def refresh(): Unit = {
    y = Random.between(100, 600)
    population = Random.between(100, 500)

    width = sqrt(population * 10).toInt

    rectangle.setHeight(width)
    rectangle.setWidth(width)
    rectangle.setFill(Color.rgb(r, g, b))
    rectangle.setLayoutX(x - (width / 2))
    rectangle.setLayoutY(y - (width / 2))

    dormLabel.setLayoutY(y + (width / 2) - 25)
    dormLabel.setLayoutX(x - (width / 2))

    populationLabel.setLayoutY(y + (width / 2) - 10)
    populationLabel.setLayoutX(x - (width / 2))
    populationLabel.setText("Проживает: " + population)
  }

  def getX: Double = x + (width / 2)

  def getY: Double = y + (width / 2)
}


trait CenterPoint {
  var x: Double = 500.0
  var y: Double = 400.0

  val center: Circle = new Circle(3)
  center.setVisible(true)
  center.setCenterX(x)
  center.setCenterY(y)
  center.setStrokeWidth(2)
  center.setStroke(Color.rgb(255, 255, 255))

  val distance = new Text(0, 40, "Расстояние: " + 0)
  distance.setLayoutX(x)
  distance.setLayoutY(y + 10)
  distance.setFill(Color.rgb(0, 0, 0))

  val label = new Text(0, 40, "")
  label.setLayoutX(x)
  label.setLayoutY(y + 5)
  label.setFill(Color.rgb(0, 0, 0))

  def setCenter(dorms: Dorm*): Unit = {
    var totalX: Double = 0
    var totalY: Double = 0
    var totalPopulation: Double = 0
    var xy: Int = 0

    dorms.foreach { dorm =>
      totalX += (dorm.getX - (dorm.width / 2)) * dorm.population
      totalY += (dorm.getY - (dorm.width / 2)) * dorm.population

      totalPopulation += dorm.population
    }

    x = totalX / totalPopulation
    y = totalY / totalPopulation

    dorms.foreach { dorm =>
      var tempX: Int = x.toInt
      var tempY: Int = y.toInt
      tempX -= dorm.getX.toInt
      tempY -= dorm.getY.toInt
      xy = (tempX + tempY) + 22
    }

    distance.setText("Расстояние: " + Math.abs(xy))
    distance.setLayoutX(x - 10)
    distance.setLayoutY(y - 5)

    center.setCenterX(x)
    center.setCenterY(y)

    label.setLayoutX(x - 10)
    label.setLayoutY(y - 20)
  }
}

class StudyCenterPoint(dorms: Dorm*) extends CenterPoint {
  label.setText("Учебная группа")
  setCenter(dorms: _*)
}
class DormitoryCenterPoint(dorms: Dorm*) extends CenterPoint {
  label.setText("Общаги")
  setCenter(dorms: _*)
}
