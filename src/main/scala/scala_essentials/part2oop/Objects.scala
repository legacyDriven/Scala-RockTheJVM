package com.eugeniusz.scala
package scala_essentials.part2oop

object Objects extends App {

  // SCALA DOES NOT HAVE CLASS-LEVEL FUNCTIONALITY ("static")
  object Person { // type + its only instance!

    // "static"/"class" - level functionality
    val N_EYES = 2
    def canFly: Boolean = false

    // factory method
    def apply(mother: Person, father: Person): Person = new Person("Bobbie")
  }
  class Person(val name: String) {
    // instance-level functionality
  }
  // COMPANIONS they reside in the same scope and have the same name

  println(Person.N_EYES)
  println(Person.canFly)

  // Scala object = SINGLETON INSTANCE
  val mary = Person // the only instance
  val john = Person // the only instance
  println(mary == john) // true

  val me = new Person("Mary")
  val you = new Person("John")
  println(me == you) // false

  val bobbie = Person(me, you)  // factory method apply called

  // Scala Applications = Scala object with
  // def main(args: Array[String]): Unit
//  def main(args: Array[String]): Unit = {
//    println("Hello, Scala!")
//  }

}
