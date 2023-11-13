package com.eugeniusz.scala
package scala_essentials.part1basics

import scala.annotation.tailrec

object DefaultArgs extends App {
  
  @tailrec
  def trFact(n: Int, acc: Int = 1): Int = // tail recursive function
    if (n <= 1) acc  // this is the last expression in the function
    else trFact(n - 1, n * acc)  // this is the last expression in the function
  
  val fact10 = trFact(10)  // acc is optional
  val fact10_2 = trFact(10, 2)  // acc is optional
  
  def savePicture(format: String = "jpg", width: Int = 1920, height: Int = 1080): Unit = println("saving picture")  // default arguments  
  def savePictureTwo(format: String = "jpg", width: Int = 1920, height: Int = 1080, author: String): Unit = println("saving picture")  // default arguments  
  
  savePicture(width = 800)  // named arguments
  savePictureTwo(author = "me")  // named arguments
  
  /*
   * 1. pass in every leading argument
   * 2. name the arguments
   */
  
  savePicture(height = 600, width = 800, format = "bmp")  // named arguments

}
