package com.eugeniusz.scala
package test


def testSamePackage(): Unit = {
  println(x)
}

object Wildcard {
  def x = "Import x poprzez symbol wieloznaczny"
}

def testWildcardImport(): Unit = {
  import Wildcard._
  println(x)
}

object Explicit {
  def x = "Jawny import x"
}
def testExplicitImport(): Unit = {
  import Explicit.x
  import Wildcard._
  println(x)
}

def testInlineDefinition(): Unit = {
  val x = "Lokalna definicja x"
  import Explicit.x
  import Wildcard._
  println(x)
}

object Test {
  def main(args: Array[String]): Unit = {
    testSamePackage()
    testWildcardImport()
    testExplicitImport()
    testInlineDefinition()
  }
}
