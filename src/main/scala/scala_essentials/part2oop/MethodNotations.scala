package com.eugeniusz.scala
package scala_essentials.part2oop

import scala.language.postfixOps

object MethodNotations extends App {

  class Person(val name: String, favouriteMovie: String, val age: Int = 0){
    def likes(movie: String): Boolean = movie == favouriteMovie
    def +(person: Person): String = s"${this.name} is hanging out with ${person.name}"
    def unary_! : String = s"$name, what the heck?!"
    def isAlive: Boolean = true
    def learns(subject: String) = s"$name learns $subject"
    def learnsScala = this learns "Scala"
    def +(nickName: String): Person = new Person(s"$name ($nickName)", favouriteMovie)
    def apply(): String = s"Hi, my name is $name and I like $favouriteMovie"
    def apply(count: Int): String = s"$name watched $favouriteMovie $count times"

    def unary_+ : Person = new Person(name, favouriteMovie, age + 1)
  }

  val mary = new Person("Mary", "Inception")
  println(mary.likes("Inception"))  // normal notation
  println(mary likes "Inception") // infix notation = operator notation (syntactic sugar)

  // "operators" in Scala
  val tom = new Person("Tom", "Fight Club")
  println(mary + tom)

  // ALL OPERATORS ARE METHODS

  // prefix notation

  val x = -1 // equivalent with 1.unary_-
  val y = 1.unary_- // equivalent with -1
  // unary_ prefix only works with - + ~ !

  println(!mary)
  println(mary.unary_!)  // equivalent

  // postfix notation
  println(mary.isAlive)
  println(mary isAlive)  // postfix notation can induce potential ambiguity when reading code

  // apply
  println(mary.apply())
  println(mary())  // equivalent

  /*
    1. Overload the + operator
      mary + "the rockstar" => new person "Mary (the rockstar)"

    2. Add an age to the Person class
      Add a unary + operator => new person with the age + 1
      +mary => mary with the age incrementer

    3. Add a "learns" method in the Person class => "Mary learns Scala"
      Add a learnsScala method, calls learns method with "Scala"
      Use it in postfix notation

    4. Overload the apply method
      mary.apply(2) => "Mary watched Inception 2 times"
   */

  println((mary + "The Rockstar")())
  println((+mary).age)

  println(mary learnsScala)
  println(mary(10))
}
