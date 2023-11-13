package com.eugeniusz.scala
package scala_essentials.part2oop

object OOBasics extends App {

  val person = new Person("John", 26)
  println(person.age)
  person.greet("Daniel")
  person.greet()

}

class Person(str: String, i: Int) {
  val name: String = str
  val age: Int = i

  def greet(name: String): Unit = println(s"${this.name} says: Hi, $name")
  def greet(): Unit = println(s"Hi, I am $name")

  def this(name: String) = this(name, 0)  // auxiliary constructor
  def this() = this("John Doe")  // auxiliary constructor
}
