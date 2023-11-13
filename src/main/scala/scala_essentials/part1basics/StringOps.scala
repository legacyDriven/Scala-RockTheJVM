package com.eugeniusz.scala
package scala_essentials.part1basics

object StringOps extends App {

  val str: String = "Hello, I am learning Scala" // String is Java class

  println(str.charAt(2))  // charAt is Java method
  println(str.substring(7, 11)) // substring is Java method
  println(str.split(" ").toList)  // split is Java method
  println(str.startsWith("Hello"))  // startsWith is Java method
  println(str.replace(" ", "-"))  // replace is Java method
  println(str.toLowerCase())  // toLowerCase is Java method
  println(str.length)  // length is Java method

  val aNumberString = "45"  // String
  val aNumber = aNumberString.toInt  // Int
  println('a' +: aNumberString :+ 'z')  // +: and :+ are Scala-specific and can be used only with sequences
  println(str.reverse)  // reverse is Scala-specific
  println(str.take(2))  // take is Scala-specific and does not throw exception if index is out of bounds
  
  // prepending operator +: and appending operator :+ are Scala-specific and can be used only with sequences

  // Scala-specific: String interpolators

  // S-interpolators
  val name = "David"
  val age = 12
  val greeting = s"Hello, my name is $name and I am $age years old" // s-interpolator allows to use variables inside string
  val anotherGreeting = s"Hello, my name is $name and I will be turning ${age + 1} years old"  // curly braces allow to use expressions

  println(greeting)
  println(anotherGreeting)

  // F-interpolators
  val speed = 1.2f
  val myth = f"$name can eat $speed%2.2f burgers per minute"  // %2.2f means 2 characters total and 2 decimals
  // breakdown of f-interpolator:
  // 1. f - format interpolator
  // 2. $name - variable
  // 3. $speed - variable
  // 4. %2.2f - format specifier
  // 5. burgers per minute - string
  // format specifiers:
  // 1. %s - string
  // 2. %f - float
  // 3. %d - integer
  // 4. %2.2f - float with 2 decimals
  // 5. %2.4f - float with 4 decimals
  // 6. %6.2f - float with 6 characters total and 2 decimals
  // 7. %06.2f - float with 6 characters total and 2 decimals, fill with 0s
  // 8. %2d - integer with 2 characters total
  // 9. %2d - integer with 2 characters total, fill with 0s
  // 10. %s%s - string followed by string
  // 11. %2d-%2d - integer followed by integer

  println(myth)

  // raw-interpolator
  println(raw"This is a \n newline")  // raw-interpolator prints string as it is
  val escaped = "This is a \n newline"  // \n is a newline character
  println(raw"$escaped")  // raw-interpolator does not print string as it is

}
