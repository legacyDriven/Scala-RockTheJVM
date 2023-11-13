import scala.collection.mutable.ArrayBuffer
import scala.util.Sorting.quickSort
import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters._

//package com.eugeniusz.scala
//
//class Util {
//  def qsort[T <% Ordered[T]](list: List[T]): List[T] = {
//    list.match ({
//      case Nil => Nil
//      case x :: xs =>
//        val (before, after) = xs.partition({ i => i.<(x) }); qsort(before).++(qsort(after).::(x)));
//    })
//      ++ i ::
//  }
//
//  def qsort[T <% Ordered[T]](list: List[T]): List[T] = {
//    case Nil => Nil
//    case x :: xs =>
//      val (before, after) = xs partition (_ < x)
//      qsort(before) ++ (x :: qsort(after));
//  }
//
//}
object Some extends App{
  val x: String = ArrayBuffer("Hello", "transylvania", "world").max
  println(ArrayBuffer("Marry", "had", "a", "little", "lamb").max)
  println(ArrayBuffer(1,2,3).min)
  println(ArrayBuffer(1,2,3).reverse)
  println(ArrayBuffer(4,2,5).sorted(Ordering.Int))
  println(ArrayBuffer(4,2,5).sorted(Ordering.Int.reverse))

  val yy = ArrayBuffer(4,2,5)
  val yz = Array(5,8,7)
  quickSort(yz)
  println(yz.mkString(" "))
  yy.appendAll(yz)
  println("apendall" + yy)
  println("count" + yy.count(_ > 4))
  println("drop" + yy.drop(2))
  println("+=" + yy.+=(1,4))

  println(ArrayBuffer(4,2,5).sorted(Ordering.Int.reverse))
  println(x)

  val matrix = Array.ofDim[Int](3,4)
  matrix(1)(2) = 42

  val triangle = new Array[Array[Int]](10)
//  for (i <- 0 until triangle.length)
  for (i <- triangle.indices)
    triangle(i) = new Array[Int](i + 1)

    Array.apply(1,2,3)

}

