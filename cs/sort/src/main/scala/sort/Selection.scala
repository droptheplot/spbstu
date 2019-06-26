package sort

class Selection[T]()(implicit o: T => Ordered[T])
  extends Impl[T] {

  def apply(a: Array[T]): (Int, Int) = {
    var i, swaps, comparisons: Int = 0

    while(i < (a.length - 1)) {
      var min: Int = i
      var j: Int = i + 1

      while (j < a.length) {
        comparisons += 1

        if(a(j) < a(min)) {
          min = j
        }

        j += 1
      }

      swap(a, i, min)
      swaps += 1

      i += 1
    }

    (swaps, comparisons)
  }
}

