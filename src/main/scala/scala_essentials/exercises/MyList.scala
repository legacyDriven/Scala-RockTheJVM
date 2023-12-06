package com.eugeniusz.scala
package scala_essentials.exercises

abstract class MyList[+A] {

  /*
    head = first element of the list
    tail = remainder of the list
    isEmpty = is this list empty
    add(int) => new list with this element added
    toString => a string representation of the list
   */

  def head: A
  def tail: MyList[A]
  def isEmpty: Boolean
  def add[B >: A] (element: B): MyList[B]
  def printElements: String
  // polymorphic call
  override def toString: String = "[" + printElements + "]"

  // higher-order functions
  def map[B](transformer: MyTransformer[A, B]): MyList[B]  // receives a transformer and returns a list of type B

  def flatMap[B](transformer: MyTransformer[A, MyList[B]]): MyList[B]  // receives a transformer and returns a list of type B

  def filter(predicate: MyPredicate[A]): MyList[A]  // receives a predicate and returns a list of type A

  //concatenation
  def ++[B >: A](list: MyList[B]): MyList[B]  // receives a list of type B and returns a list of type B, B is super type of A

}

// serializable - can be sent over the network, toString, hashcode, equals, etc. are implemented
case object Empty extends MyList[Nothing] {  // Nothing is a subtype of any other type

  def head: Nothing = throw new NoSuchElementException

  def tail: Nothing = throw new NoSuchElementException
  def isEmpty: Boolean = true
  def add[B >: Nothing](element: B): MyList[B] = new Cons(element, Empty)

  override def printElements: String = ""

  def map[B](transformer: MyTransformer[Nothing, B]): MyList[B] = Empty

  def flatMap[B](transformer: MyTransformer[Nothing, MyList[B]]): MyList[B] = Empty

  def filter(predicate: MyPredicate[Nothing]): MyList[Nothing] = Empty

  def ++[B >: Nothing](list: MyList[B]): MyList[B] = list  // returns the list that is passed in

}

case class Cons[+A](h: A, t: MyList[A]) extends MyList[A] {
  def head: A = h
  def tail: MyList[A] = t
  def isEmpty: Boolean = false
  def add[B >: A](element: B): MyList[B] = new Cons(element, this)
  def printElements: String =
    if (t.isEmpty) "" + h.toString  // if t is empty, print h, else print h + " " + t.printElements
    else h.toString + " " + t.printElements // h + " " + recursive call on t, HAD TO ADD toString to compile - CHECK WHY

  /*
  [1,2,3].filter(n % 2 == 0) =
    = [2,3].filter(n % 2 == 0)  // n % 2 == 0 = predicate.test(h)
    = new Cons(2, [3].filter(n % 2 == 0))  // [2,3].filter(n % 2 == 0) = t.filter(predicate)
    = new Cons(2, Empty.filter(n % 2 == 0))  // [3].filter(n % 2 == 0) = t.filter(predicate)
    = new Cons(2, Empty)  // Empty.filter(n % 2 == 0) = Empty
   */

  def filter(predicate: MyPredicate[A]): MyList[A] =  // predicate is a function that takes an A and returns a Boolean
    if (predicate.test(h)) new Cons(h, t.filter(predicate))  // if predicate.test(h) is true, return a new Cons with h and t.filter(predicate)
    else t.filter(predicate)  // else return t.filter(predicate)

  /*
  [1,2,3].map(n*2)
    = new Cons(2, [2,3].map(n*2))  // n*2 = transformer.transform(h)
    = new Cons(2, new Cons(4, [3].map(n*2)))  // [2,3].map(n*2) = t.map(transformer)
    = new Cons(2, new Cons(4, new Cons(6, Empty.map(n*2))))  // [3].map(n*2) = t.map(transformer)
    = new Cons(2, new Cons(4, new Cons(6, Empty)))  // Empty.map(n*2) = Empty
   */

  def map[B](transformer: MyTransformer[A, B]): MyList[B] =  // transformer is a function that takes an A and returns a B
    new Cons(transformer.transform(h), t.map(transformer))  // return a new Cons with transformer.transform(h) and t.map(transformer)

  /*
  [1,2] ++ [3,4,5]
    = new Cons(1, [2] ++ [3,4,5])  // [2] ++ [3,4,5] = t ++ list
    = new Cons(1, new Cons(2, Empty ++ [3,4,5]))  // [2] ++ [3,4,5] = t ++ list
    = new Cons(1, new Cons(2, new Cons(3, new Cons(4, new Cons(5)))))  // Empty ++ [3,4,5] = list
   */
  def ++[B >: A](list: MyList[B]): MyList[B] = new Cons(h, t ++ list)  // returns a new Cons with h and t concatenated with list

  /*
  [1,2].flatMap(n => [n, n+1])
    = [1,2] ++ [2].flatMap(n => [n, n+1])  // [2].flatMap(n => [n, n+1]) = transformer.transform(h)
    = [1,2] ++ [2,3] ++ Empty.flatMap(n => [n, n+1])  // Empty.flatMap(n => [n, n+1]) = t.flatMap(transformer)
    = [1,2] ++ [2,3] ++ Empty  // Empty.flatMap(n => [n, n+1]) = t.flatMap(transformer)
    = [1,2,2,3]   // Empty = list
   */
  def flatMap[B](transformer: MyTransformer[A, MyList[B]]): MyList[B] =  // transformer is a function that takes an A and returns a MyList[B]
    transformer.transform(h) ++ t.flatMap(transformer)  // return transformer.transform(h) concatenated with t.flatMap(transformer)
}

trait MyPredicate[-T] {  // T => Boolean -T means contravariant, means that MyPredicate is a super class of T
  def test(element: T): Boolean
}

trait MyTransformer[-A, B] {  // A => B -A means contravariant, means that MyTransformer is a super class of A
  def transform(element: A): B
}



object ListTest extends App {
  //  val list = new Cons(1, new Cons(2, new Cons(3, Empty)))
  //  println(list.head)  // 1
  //  println(list.tail.head) // 2
  //  println(list.add(4).head) // 4
  //  println(list.isEmpty) // false
  //
  //  println(list.toString) // [1 2 3]

  val listOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  val clonelistOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  val anotherListOfIntegers: MyList[Int] = new Cons(4, new Cons(5, Empty))
  val listOfStrings: MyList[String] = new Cons("Hello", new Cons("Scala", Empty))

  println(listOfIntegers.toString)
  println(listOfStrings.toString)

  println(listOfIntegers.map(new MyTransformer[Int, Int] {
    override def transform(element: Int): Int = element * 2
  }).toString)  // [2 4 6]

  println(listOfIntegers.filter(new MyPredicate[Int] {
    override def test(element: Int): Boolean = element % 2 == 0
  }).toString)  // [2]

  println((listOfIntegers ++ anotherListOfIntegers).toString)  // [1 2 3 4 5]

  println(listOfIntegers.flatMap(new MyTransformer[Int, MyList[Int]] {
    override def transform(element: Int): MyList[Int] = new Cons(element, new Cons(element + 1, Empty))
  }).toString)  // [1 2 2 3 3 4]

  println(listOfIntegers == clonelistOfIntegers)  // true because of case class
}

/*
  1. Generic trait MyPredicate[-T] with a little method test(T) => Boolean
  2. Generic trait MyTransformer[-A, B] with a method transform(A) => B
  3. MyList:
    - map(transformer) => MyList
    - filter(predicate) => MyList
    - flatMap(transformer from A to MyList[B]) => MyList[B]

    class EvenPredicate extends MyPredicate[Int]
    class StringToIntTransformer extends MyTransformer[String, Int]

    [1,2,3].map(n * 2) = [2,4,6]
    [1,2,3,4].filter(n % 2) = [2,4]
    [1,2,3].flatMap(n => [n, n+1]) => [1,2,2,3,3,4]
 */