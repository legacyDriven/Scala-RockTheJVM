package com.eugeniusz.scala
package scala_essentials.part2oop

import playground.{Cinderella => Princess, PrinceCharming}

import java.util.Date
import java.sql.{Date => SqlDate}  // aliasing

object PackagingAndImports extends App {

  // package members are accessible by their simple name
  val writer = new Writer("Eugeniusz", "Kowalski", 1990)

  // import the package
  val princess = new Princess  // playground.Cinderella = fully qualified name

  // packages are in hierarchy
  // matching folder structure

  // package object
  sayHello
  println(SPEED_OF_LIGHT)

  // imports
  val prince = new PrinceCharming

  // 1. use FQ names
  val date = new Date
  val sqlDate = new java.sql.Date(2018, 5, 4)
  val sqlDate2 = new SqlDate(2018, 5, 4)

//  val cinderella = new com.eugeniusz.scala.scala_essentials.part2oop.Cinderella

  // 2. use aliasing


  // default imports
  // java.lang - String, Object, Exception
  // scala - Int, Nothing, Function
  // scala.Predef - println, ???
}
