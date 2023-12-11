package com.eugeniusz.scala
package scala_essentials.part3fp

import java.util
import scala.util.Random

object Sequences extends App{

  // seq
  val aSequence = Seq(1,2,3,4)  //
  println(aSequence)
  println(aSequence.reverse)
  println(aSequence(2))
  println(aSequence ++ Seq(7,5,6))
  println(aSequence.sorted)

  // ranges
  val aRange: Seq[Int] = 1 until 10
  val aRange2: Seq[Int] = 1 to 10
  aRange.foreach(println)
  aRange2.foreach(println)

  (1 to 10).foreach(_ => println("Hello"))

  // lists

  val aList = List(1,2,3)
  println(aList)
  val prepended = 42 :: aList
  println(prepended)
  val prepended2 = 48 +: aList
  println(prepended2)
  val prependedAppended = 48 +: aList :+ 89
  println(prependedAppended)

  val apples5 = List.fill(5)("apple")
  println(apples5)

  println(aList.mkString("-|-"))

  // arrays

  val numbers = Array(1,2,3,4)
  val threeElements = Array.ofDim[Int](3)
  println(threeElements)
  threeElements.foreach(println)

  val threeElements2 = Array.ofDim[String](3)
  threeElements2.foreach(println)

  // mutation
  numbers(2) = 0  // syntax sugar for numbers.update(2, 0)
  println(numbers.mkString(" "))

  // arrays and seq
  val numbersSeq: Seq[Int] = numbers  // implicit conversion
  println(numbersSeq)

  // vectors
  val vector: Vector[Int] = Vector(1,2,3)
  println(vector)

  // vectors vs lists

  val maxRuns = 1000
  val maxCapacity = 1_000_000
  def getWriteTime(collection: Seq[Int]): Double = {
    var r = new Random
    val times = for {
      it <- 1 to maxRuns
    } yield{
      val currentTime = System.nanoTime()
      collection.updated(r.nextInt(maxCapacity), r.nextInt())
      System.nanoTime() - currentTime
    }

    times.sum * 1.0 / maxRuns
  }

  val numbersList = (1 to maxCapacity).toList
  val numbersVector = (1 to maxCapacity).toVector

  //  keeps reference to tail
  //  updating an element in the middle takes time
  println(getWriteTime(numbersList))
  
  //  depth of the tree is small
  //  needs to replace an entire 32-element chunk
  println(getWriteTime(numbersVector))
  
}
