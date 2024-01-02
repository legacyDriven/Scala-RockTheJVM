package com.eugeniusz.scala
package scala_advanced_fp.part5implicits

// TYPE CLASS
trait MyTypeClassTemplate[T] {
  def action(value: T): String
}

object MyTypeClassTemplate {
  def apply[T](implicit instance: MyTypeClassTemplate[T]): MyTypeClassTemplate[T] = instance
}