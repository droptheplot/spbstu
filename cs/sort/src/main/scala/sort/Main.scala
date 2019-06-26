package sort

import scala.util.Random

object Main {
  val sizes: List[Int] =
    List[Int](25, 43, 79, 160, 362, 909, 2457)

  def main(args: Array[String]): Unit = {
    val elements: Array[Int] =
      Random.shuffle((1 to sizes.max).toList).toArray


    val bubble: Bubble[Int] = new Bubble[Int]
    val selection: Selection[Int] = new Selection[Int]

    sizes.foreach { size =>
      val ary: Array[Int] = elements.take(size)
      println(bubble(ary))
    }


    sizes.foreach { size =>
      val ary: Array[Int] = elements.take(size)
      println(selection(ary))
    }
  }
}
