package com.eugeniusz.scala

def oncePerSecond(callback: () => Unit): Unit =    // callback is a function that takes no parameters and returns Unit
  while true do {
    callback(); Thread.sleep(1000)  // sleep for 1 second
  }

def timeFlies(): Unit = // this is the callback function
  println("time flies like an arrow...")  // this is the callback function

//  @main def Timer(): Unit = // this is the main function
//    oncePerSecond(timeFlies)  // this is the main function

@main def TimerAnonymous(): Unit =  // this is the main function
  oncePerSecond(() => // this is the callback function
    println("time flies like an arrow...")) // this is the callback function

