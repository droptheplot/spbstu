import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.{Group, Scene}
import javafx.scene.paint.Color
import javafx.scene.shape.{Circle, Rectangle}
import javafx.scene.text.Text
import javafx.stage.Stage

import java.lang.Math.sqrt
import scala.util.Random

object CampusMap {
  def main(args: Array[String]): Unit = launch(classOf[CampusMapApp])
}

class CampusMapApp extends Application {
  override def start(stage: Stage): Unit = {
    val root = new Group()
    val scene = new Scene(root, 1000, 800, Color.rgb(236, 240, 241))

    val dorm1 = new Dorm(1, 420, 52, 152, 219)
    val dorm2 = new Dorm(2, 40, 26, 188, 156)
    val dorm3 = new Dorm(3, 430, 155, 89, 182)
    val dorm4 = new Dorm(4, 350, 231, 76, 60)

    val dormitoryCenterPoint = new DormitoryCenterPoint(dorm1, dorm2, dorm3, dorm4)
    val studyCenterPoint = new StudyCenterPoint(dorm1, dorm2, dorm4)

    root.getChildren.addAll(dorm1.dormLabel, dorm2.dormLabel, dorm3.dormLabel, dorm4.dormLabel)
    root.getChildren.addAll(dorm1.populationLabel, dorm2.populationLabel, dorm3.populationLabel, dorm4.populationLabel)
    root.getChildren.addAll(dorm1.rectangle, dorm2.rectangle, dorm3.rectangle, dorm4.rectangle)

    root.getChildren.addAll(dormitoryCenterPoint.distance, dormitoryCenterPoint.center, dormitoryCenterPoint.label)
    root.getChildren.addAll(studyCenterPoint.distance, studyCenterPoint.center, studyCenterPoint.label)

    dormitoryCenterPoint.setCenter(dorm1, dorm2, dorm3, dorm4)
    studyCenterPoint.setCenter(dorm1, dorm2, dorm4)

    stage.setTitle("Карта универа")
    stage.setScene(scene)
    stage.setResizable(false)
    stage.show()
  }
}

class Dorm(id: Int, val population: Double = 1, r: Int, g: Int, b: Int) {
  val rectangle = new Rectangle(0, 0, 10, 10)
  var width = 0
  val dormLabel = new Text(0, 40, s"Общага $id")
  val populationLabel = new Text(0, 40, "Проживает: " + population.toInt)

  val x: Int = id * 200
  val y: Int = Random.between(100, 600)

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
