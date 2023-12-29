package com.eugeniusz.scala
package scala_advanced_fp.part5implicits

import scala.language.implicitConversions

object ImplicitsIntro extends App {

  val pair = "Daniel" -> "555"  // -> is an implicit method, operators actually are methods
  val intPair = 1 -> 2

  case class Person(name: String) {
    def greet = s"Hi, my name is $name"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)  // implicit conversion, compiler will look for this method when it needs Person

  println("Peter".greet)  // compiler will look for fromStringToPerson method and will convert String to Person

//  class A {
//    def greet: Int = 2
//  }
//  // compiler will be confused which method to use, so we need to use implicit keyword
//  implicit def fromStringToA(str: String): A = new A  // implicit conversion, compiler will look for this method when it needs Person

  // implicit parameters
  def increment(x: Int)(implicit amount: Int) = x + amount

  implicit val defaultAmount: Int = 10  // must add type, scala 2 gives warning, scala 3 will not compile without type

  val value = increment(2)  // compiler will look for implicit value of type Int and will pass it to the method
  println(value)

  // NOT default args
}
