package com.eugeniusz.scala
package scala_essentials.part2oop

import playground.{Cinderella, PrinceCharming}

object PackagingAndImports extends App {

  // package members are accessible by their simple name
  val writer = new Writer("Eugeniusz", "Kowalski", 1990)

  // import the package
  val princess = new Cinderella  // playground.Cinderella = fully qualified name

  // packages are in hierarchy
  // matching folder structure

  // package object
  sayHello
  println(SPEED_OF_LIGHT)

  // imports
  val prince = new PrinceCharming

  // 1. use FQ names
  val cinderella = new com.eugeniusz.scala.scala_essentials.part2oop.Cinderella

  // 2. use aliasing
  val cinderella2 = new Cinderella

  // default imports
  // java.lang - String, Object, Exception
  // scala - Int, Nothing, Function
  // scala.Predef - println, ???
}
