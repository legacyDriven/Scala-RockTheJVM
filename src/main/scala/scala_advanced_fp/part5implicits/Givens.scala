package com.eugeniusz.scala
package scala_advanced_fp.part5implicits

import scala.language.implicitConversions

object Givens extends App {

  val aList = List(2,4,3,1)
  val anOrderedList = aList.sorted  // implicit Ordering[Int] is passed to sorted method

  println(aList.sorted) // List(4, 3, 2, 1) - default ordering

  // scala 2 style
  object implicits {
    implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  }
//  import implicits._
//  println(aList.sorted) // List(1, 2, 3, 4) - reverse ordering

  // scala 3 style
  object Givens {
    given reverseOrdering: Ordering[Int] = Ordering.fromLessThan (_ > _)
  }

  // given <=> implicit vals
  // because given belongs to the same scope as sorted method, it is passed automatically
  // regardless of the import and order of the code

  // instantiating an anonymous class
  object GivensAnonymousClassNaive {
    given reverseOrdering_v2: Ordering[Int] = new Ordering[Int]{
      override def compare(x: Int, y: Int): Int = y - x
    }
  }

  object GivenWith {
    given reverseOrdering_v3: Ordering[Int] with {
      override def compare(x: Int, y: Int): Int = y - x
    }
  }

  import GivenWith._ // in scala 3 this import does NOT import givens as well
  import GivenWith.given // this imports all givens

  // implicit arguments
  def extremes[A](list: List[A])(implicit ordering: Ordering[A]): (A, A) = {
    val sortedList = list.sorted
    (sortedList.head, sortedList.last)
  }

  // implicit arguments <=> using clauses
  def extremes_v2[A](list: List[A])(using ordering: Ordering[A]): (A, A) = {
    val sortedList = list.sorted  // using is an alias for implicit
    (sortedList.head, sortedList.last)
  }

  // implicit def (synthesize new implicit values)

  trait Combinator[A] {  // semigroup
    def combine(x: A, y: A): A
  }

  // List(1,2,3) < List(4,5,6), comparing Lists based on their sums

  /*
  implicit def is the ability of the compiler to synthesize an implicit value of type ordering of List[A]
  if we have a ordering A and a combinator of A in scope,
  so if these two are present compiler can automatically create a list ordering if needed
   */
  implicit def listOrdering[A](implicit simpleOrdering: Ordering[A], combinator: Combinator[A]): Ordering[List[A]] =
    new Ordering[List[A]] {
      override def compare(x: List[A], y: List[A]): Int = {
        val sumX = x.reduce(combinator.combine)
        val sumY = y.reduce(combinator.combine)
        simpleOrdering.compare(sumX, sumY)
      }
    }

  // equivalent in Scala 3 with givens
  given listOrdering_v2[A](using simpleOrdering: Ordering[A], combinator: Combinator[A]): Ordering[List[A]] with {
    override def compare(x: List[A], y: List[A]): Int = {
      val sumX = x.reduce(combinator.combine)
      val sumY = y.reduce(combinator.combine)
      simpleOrdering.compare(sumX, sumY)
    }
  }

  // implicit conversions (abused in Scala 2)
  case class Person(name: String) {
    def greet(): String = s"Hi, my name is $name."
  }

  implicit def string2Person(string: String): Person = Person(string)
  val danielsGreet = "Daniel".greet()
  
  // in Scala 3
  import scala.language.implicitConversions  // required in Scala 3
  given string2PersonConversion: Conversion[String, Person] with {
    override def apply(x: String): Person = Person(x)
  }

}
