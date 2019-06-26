package sort

class Bubble[T]()(implicit o: T => Ordered[T])
  extends Impl[T] {

  def apply(a: Array[T]): (Int, Int) = {
    val indices: IndexedSeq[(Int, Int)] =
      a.indices zip a.indices.drop(1)

    var changed: Boolean = true
    var swaps, comparisons: Int = 0

    do {
      changed = false

      for ((i, j) <- indices) yield {
        comparisons += 1

        if (a(i) > a(j)) {
          changed = true

          swap(a, i, j)
          swaps += 1
        }
      }
    } while (changed)

    (swaps, comparisons)
  }
}
