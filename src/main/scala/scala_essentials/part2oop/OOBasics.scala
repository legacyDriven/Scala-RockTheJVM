package com.eugeniusz.scala
package scala_essentials.part2oop

object OOBasics extends App {

  val person = new Person("John", 26)
  println(person.x)
  person.greet("Daniel")
  person.greet()

  val author = new Writer("Charles", "Dickens", 1812)
  val imposter = new Writer("Charles", "Dickens", 1812)
  val novel = new Novel("Great Expectations", 1861, author)

  println(novel.authorAge())
  println(novel.isWrittenBy(author))  // true
  println(novel.isWrittenBy(imposter))  // false

  val counter = new Counter(0)
  counter.inc.print
  counter.inc.inc.inc.print
  counter.inc(10).print
  counter.dec.print
  counter.dec.dec.dec.print
  counter.dec(10).print
  

}

// class parameters are NOT FIELDS
class Person(name: String, val age: Int) { // constructor

  //body
  val x = 2 // field

  println(1 + 3)  // this will be executed when the class is instantiated

  // method
  def greet(name: String): Unit = println(s"${this.name} says: Hi, $name") // this.name is the class parameter

  // overloading
  def greet(): Unit = println(s"Hi, I am $name")  // name is implied as this.name as there is no parameter

  // multiple constructors

  def this(name: String) = this(name, 0)  // this is a constructor that calls the primary constructor

  def this() = this("John Doe") // this is a constructor that calls the primary constructor
}

/*
  Novel and a Writer

  Writer: first name, surname, year
    - method fullname

  Novel: name, year of release, author
    - authorAge
    - isWrittenBy(author)
    - copy (new year of release) = new instance of Novel

 */

class Writer(firstName: String, surname: String, val year: Int) {

  def fullName(): String = s"$firstName $surname"

}

class Novel(name: String, yearOfRelease: Int, author: Writer) {

  def authorAge(): Int = yearOfRelease - author.year

  def isWrittenBy(author: Writer): Boolean = author == this.author

  def copy(newYearOfRelease: Int): Novel = new Novel(name, newYearOfRelease, author)

}

/*
  Counter class
    - receives an int value
    - method current count
    - method to increment/decrement => new Counter
    - overload inc/dec to receive an amount
 */

class Counter(val count: Int) {

  def inc = {
    println("incrementing")
    new Counter(count + 1) // immutability
  }

  def dec = {
    println("decrementing")
    new Counter(count - 1)
  }

  def inc(n: Int): Counter = {
    if (n <= 0) this
    else inc.inc(n - 1)
  }

  def dec(n: Int): Counter = {
    if (n <= 0) this
    else dec.dec(n - 1)
  }

  def print = println(count)

}
