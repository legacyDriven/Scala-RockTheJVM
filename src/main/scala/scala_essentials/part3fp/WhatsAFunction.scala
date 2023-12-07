package lectures.part3fp


object WhatsAFunction extends App {

  //DREAM: use functions as first class elements
  //problem: oop

  val doubler = new MyFunction[Int, Int]{
    override def apply(element: Int): Int = element * 2
  }

  println(doubler(2))


  //function types = Function1[A, B]
  val stringToIntConverter = new Function1[String, Int] {
    override def apply(v1: String): Int = v1.toInt
  }

  println(stringToIntConverter("3") + 4)

  val adder: ((Int, Int) => Int) = new Function2[Int, Int, Int] {
    override def apply(v1: Int, v2: Int): Int = v1 + v2
  }

  //Function types Function2[A, B, R] === (A, B) => R

  //ALL SCALA FUNCTIONS ARE OBJECTS


  /*
  1. a function which takes 2 strings and concatenates them
  2. transform the MyPredicate and MyTransformer into function types
  3. define a function which takes an int and returns another function which takes an int and returns an int
      - what's the type of this function
      - how to do it
   */

  def stringContatenator: (String, String) => String = new Function2[String, String, String] {
    override def apply(v1: String, v2: String): String = v1 + v2
  }

  println(stringContatenator("Hello ", "Scala"))

  //Function1[Int, Function1[Int, Int]]

  val superAdder: Function1[Int, Function1[Int, Int]] = new Function1[Int, Function1[Int, Int]] {
    override def apply(v1: Int): Function1[Int, Int] = new Function1[Int, Int] {
      override def apply(v2: Int): Int = v1 + v2
    }
  }

  //curried function
  val adder3 = superAdder(3)  //Int => Int = y => 3 + y
  println(adder3(4))  //7
  println(superAdder(3)(4))  //curried function
  println(superAdder(3)(5)) //curried function
}

class MyFunction[A, B] {
  def apply(element: A): B = ???

}