package com.eugeniusz.scala
package scala_for_impatient_v2

class Ch1_Basics {

}
  object Basics extends App {

    /*
    In the Scala REPL window, enter 3., then press the TAB key. Which methods can be called?
    */
    val number = 3
    val other = number.toByte // 3
    val other2 = number.toChar // 3
    val other3 = number.toDouble // 3.0
    val other4 = number.toFloat // 3.0
    val other6 = number.toLong  // 3
    val other7 = number.toShort // 3
    val other8 = number.toString  // 3
    val other9 = number.getClass // int
    println(other9) // int

    /*
  In the Scala REPL window, calculate the square root of 3, then square the result.
  How much does the final result differ from 3? (Hint: val variables are your friends.)
    */

    val value = 3
    val squareRoot = math.sqrt(value)
    val square = squareRoot * squareRoot
    val diff = value - square

    println(s"Square root of value: $value is: $squareRoot")  // 1.7320508075688772
    println(s"Square of square root: $squareRoot is: $square") // 2.9999999999999996
    println(s"The difference between $value and $square is: ${value - square}") // 4.440892098500626E-16
    println(diff) // 4.440892098500626E-16

    /*
  Are val variables values or actual var variables?
    val declares a read-only (immutable) variable. Once a val is assigned a value, it cannot
    be reassigned to a different value. Essentially, it's a constant.
    var declares a mutable variable. A var can be reassigned to different values after its initial assignment.
    val variables are values (constants) and are not mutable var variables.
    They are two distinct ways to declare variables in Scala, with val being immutable and var being mutable.
    */

    /*
  The Scala language allows multiplying strings by numbers -
  try executing the expression "crazy" * 3 in REPL. What's the result?
  Where in ScalaDocs can you find its description?
    */
    val crazy = "crazy" * 3
    println(crazy) // crazycrazycrazy

    /*
  What does 10 max 2 mean? In which class is the max method defined?
    */

    10 max 2 // means that 10 is greater than 2
    println(10 max 2) // 10
    println(10 min 2) // 2

    val methodClass = 10.max(2).getClass
    println(s"The 'max' method returns an instance of: $methodClass")

    /*
  Using a number of type BigInt, compute 2^1024.
  What's the numeric type of the result?
    */
    val bigInt = BigInt(2)
    val result = bigInt.pow(1024)
    println(result) // 2^1024
    println(result.getClass) // class scala.math.BigInt

    /*
  What do you need to import to find a random prime number by calling the probablePrime(100, Random)
  method without using any prefixes before the names probablePrime and Random?
    */
    import scala.math.BigInt.probablePrime
    import scala.util.Random
    println(probablePrime(100, Random)) // 100 digit prime number

    /*
  One way to create a file or directory with a random name is to generate a random BigInt number
  and convert it to base 36, resulting in a string like "qsnvbevtomcj38006kul".
  Find the methods in ScalaDocs that could be used for this.
    */
    import scala.math.BigInt

    val randomBigInt = BigInt(130, Random)
    val randomName = randomBigInt.toString(36)
    println(randomName)

    /*
  How do you get the first character of a string in Scala? What about the last character?
    */
    val string = "Hello World"
    val firstChar = string.head
    val lastChar = string.last
    println(s"The first character of '$string' is: $firstChar")
    println(s"The last character of '$string' is: $lastChar")

    /*
  What do the string functions take, drop, takeRight, and dropRight do?

    take(n) returns the first n characters of the string
    drop(n) returns the string without its first n characters
    takeRight(n) returns the last n characters of the string
    dropRight(n) returns the string without its last n characters

  What are their advantages and disadvantages compared to substring?

    substring is a Java method that returns a substring of the string.
    It takes two parameters: the starting index and the ending index.
    The substring method is more flexible than the take, drop, takeRight, and dropRight methods.
    The substring method can be used to return a substring of any length, whereas the take, drop, takeRight,
    and dropRight methods can only return a substring of the same length as the original string.
    */

}
