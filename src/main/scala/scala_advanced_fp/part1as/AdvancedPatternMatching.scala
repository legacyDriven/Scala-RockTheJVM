package com.eugeniusz.scala
package scala_advanced_fp.part1as

object AdvancedPatternMatching extends App {

  // Pattern matching
  val numbers = List(1, 2, 3)


  val description: Unit = numbers match {
    case head :: Nil => println(s"The only element is $head")
    case _ => println("More than one element")
  }

  /*
    - constants
    - wildcards
    - case classes
    - tuples
    - some special magic like above
   */

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)

  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a years old"  // Person.unapply(bob) is called
    case _ => "I don't know who I am"
  }

  println(greeting)  // I don't know who I am

  val legalStatus = bob.age match {  // Person.unapply(bob.age) is called
    case Person(status) => s"My legal status is $status"
  }

  println(legalStatus)  // My legal status is minor

  /*
    Exercise
   */
  val n: Int = 45
  val mathProperty = n match {
    case x if x < 10 => "single digit"
    case x if x % 2 == 0 => "an even number"
    case _ => "no property"
  }

  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
//      if (arg % 2 == 0) Some(true)
//      else None
  }

  object singleDigit {  // singleDigit.unapply(n) is called
    def unapply(arg: Int): Boolean = arg < 10 && arg > -10
//      if (arg < 10) Some(true)
//      else None
  }

  val nx: Int = 8
  val mathProperty2 = nx match {  // even.unapply(n) is called
    case singleDigit() => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }

  println(mathProperty2)  // single digit

  // infix patterns

  case class Or[A, B](a: A, b: B)  // Either

  val either = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s"$number is written as $string"  // Or(number, string)
    case Or(number, string) => s"$number is written as $string"  // same as above
  }

  println(humanDescription)  // 2 is written as two

  // decomposing sequences
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"  // List(1, 2, 3) => 1, 2, 3
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)  // list.head +: _ is the same as list.head +: Seq.empty
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))  // List(1, 2, 3)
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1 and 2"  // MyList.unapplySeq(myList) is called
    case _ => "something else"
  }

  println(decomposed)  // starting with 1 and 2

  // custom return types for unapply   !!! rather rarely used in practice
  // isEmpty: Boolean, get: something

  // used instead of Option[T] and must have isEmpty and get methods
  abstract class Wrapper[T] {  // T is the type of the value that we want to extract
    def isEmpty: Boolean  // person.name.isEmpty
    def get: T  // person.name
  }

  object PersonWrapper {  // PersonWrapper.unapply(person) is called
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {  // Wrapper[String] is the return type
      def isEmpty: Boolean = false  // person.name.isEmpty
      def get: String = person.name  // person.name
    }
  }

  println(bob match {
    case PersonWrapper(n) => s"This person's name is $n"  // PersonWrapper.unapply(bob) is called
    case _ => "An alien"
  })  // This person's name is Bob

  // unapply takes an argument we want to decompose, returns an option or a custom type with two methods: isEmpty and get(tuple) or unapplySeq(sequence)

}
