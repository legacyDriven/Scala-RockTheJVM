package com.eugeniusz.scala
package scala_essentials.part1basics

object CallByNameVSbyValue extends App {

  // call by value
  def calledByValue (x: Long): Unit = { // x is evaluated before the function is called
    println("by value: " + x)
    println("by value: " + x)
  }

  def calledByName (x: => Long): Unit = { // x is evaluated every time is used
    println("by name: " + x)
    println("by name: " + x)
  }

  calledByValue(System.nanoTime()) // the same value is used
  calledByName(System.nanoTime()) // different values are used

  def infinite(): Int = 1 + infinite()  // this function will crash with stack overflow
  def printFirst(x: Int, y: => Int): Unit = println(x)  // y is never evaluated

//  printFirst(infinite(), 34) // stack overflow
  printFirst(34, infinite()) // no stack overflow because infinite() is never evaluated

}
