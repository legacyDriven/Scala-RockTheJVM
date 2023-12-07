package com.eugeniusz.scala
package scala_essentials.exercises

abstract class Maybe[+T] {

}

case object MaybeNot extends Maybe[Nothing] {

}

case class Just[T](value: T) extends Maybe[T] {

}
