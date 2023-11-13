package com.eugeniusz.scala

class Complex(real: Double, imaginary: Double): // primary constructor
  def re: Double = real // getter
  def im: Double = imaginary  // getter
  override def toString: String = "" + re + (if im >= 0 then "+" else "") + im + "i" // toString method

object ComplexNumbers:  // companion object
  def main(args: Array[String]): Unit = // main method
    val c = Complex(1.2, 3.4) // call to apply method
    val d = new Complex(5.6, 7.8) // call to primary constructor
    println(s"Complex number: ${c.re} + ${c.im}i")  // Complex number: 1.2 + 3.4i
