package sort

abstract class Impl[T]()(implicit o: T => Ordered[T]) {
  def swap(a: Array[T], i: Int, j: Int): Unit = {
    val tmp = a(i)
    a(i) = a(j)
    a(j) = tmp
  }

  def apply(a: Array[T]): (Int, Int)
}
