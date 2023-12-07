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
  def map[B](transformer: A => B): MyList[B]  // receives a transformer and returns a list of type B

  def flatMap[B](transformer: A => MyList[B]): MyList[B]  // receives a transformer and returns a list of type B

  def filter(predicate: A => Boolean): MyList[A]  // receives a predicate and returns a list of type A

  //concatenation
  def ++[B >: A](list: MyList[B]): MyList[B]  // receives a list of type B and returns a list of type B, B is super type of A

  // hofs
  def foreach(f: A => Unit): Unit  // receives a function that returns Unit and returns Unit

  def sort(compare: (A, A) => Int): MyList[A]  // receives a function that takes two As and returns an Int and returns a list of type A

  def zipWith[B, C](list: MyList[B], zip: (A, B) => C): MyList[C]  // receives a list of type B and a function that takes two As and returns a C and returns a list of type C

  def fold[B](start: B)(operator: (B, A) => B): B  // receives a start value of type B and a function that takes a B and an A and returns a B and returns a B
}

// serializable - can be sent over the network, toString, hashcode, equals, etc. are implemented
case object Empty extends MyList[Nothing] {  // Nothing is a subtype of any other type

  def head: Nothing = throw new NoSuchElementException

  def tail: Nothing = throw new NoSuchElementException
  def isEmpty: Boolean = true
  def add[B >: Nothing](element: B): MyList[B] = new Cons(element, Empty)

  override def printElements: String = ""

  def map[B](transformer: Nothing => B): MyList[B] = Empty

  def flatMap[B](transformer: Nothing => MyList[B]): MyList[B] = Empty

  def filter(predicate: Nothing => Boolean): MyList[Nothing] = Empty

  def ++[B >: Nothing](list: MyList[B]): MyList[B] = list  // returns the list that is passed in

  // hofs
  def foreach(f: Nothing => Unit): Unit = ()  // returns Unit

  def sort(compare: (Nothing, Nothing) => Int): MyList[Nothing] = Empty

  def zipWith[B, C](list: MyList[B], zip: (Nothing, B) => C): MyList[C] = {  // returns Empty
    if (!list.isEmpty) throw new RuntimeException("Lists do not have the same length")  // if list is not empty, throw an exception
    else Empty  // else return Empty
  }

  def fold[B](start: B)(operator: (B, Nothing) => B): B = start  // returns start
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

  def filter(predicate: A => Boolean): MyList[A] =  // predicate is a function that takes an A and returns a Boolean
    if (predicate(h)) new Cons(h, t.filter(predicate))  // if predicate.test(h) is true, return a new Cons with h and t.filter(predicate)
    else t.filter(predicate)  // else return t.filter(predicate)

  /*
  [1,2,3].map(n*2)
    = new Cons(2, [2,3].map(n*2))  // n*2 = transformer.transform(h)
    = new Cons(2, new Cons(4, [3].map(n*2)))  // [2,3].map(n*2) = t.map(transformer)
    = new Cons(2, new Cons(4, new Cons(6, Empty.map(n*2))))  // [3].map(n*2) = t.map(transformer)
    = new Cons(2, new Cons(4, new Cons(6, Empty)))  // Empty.map(n*2) = Empty
   */

  def map[B](transformer: A => B ): MyList[B] =  // transformer is a function that takes an A and returns a B
    new Cons(transformer(h), t.map(transformer))  // return a new Cons with transformer.transform(h) and t.map(transformer)

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
  def flatMap[B](transformer: A => MyList[B]): MyList[B] =  // transformer is a function that takes an A and returns a MyList[B]
    transformer(h) ++ t.flatMap(transformer)  // return transformer.transform(h) concatenated with t.flatMap(transformer)

  // hofs
  def foreach(f: A => Unit): Unit = {  // f is a function that takes an A and returns Unit
    f(h)  // apply f to h
    t.foreach(f)  // apply f to t
  }

  def sort(compare: (A, A) => Int): MyList[A] = {  // compare is a function that takes two As and returns an Int
    def insert(x: A, sortedList: MyList[A]): MyList[A] = {  // insert is a function that takes an A and a sorted list of As and returns a sorted list of As
      if (sortedList.isEmpty) new Cons(x, Empty)  // if sortedList is empty, return a new Cons with x and Empty
      else if (compare(x, sortedList.head) <= 0) new Cons(x, sortedList)  // if compare(x, sortedList.head) <= 0, return a new Cons with x and sortedList
      else new Cons(sortedList.head, insert(x, sortedList.tail))  // else return a new Cons with sortedList.head and insert(x, sortedList.tail)
    }
    val sortedTail = t.sort(compare)  // sortedTail is t.sort(compare)
    insert(h, sortedTail)  // return insert(h, sortedTail)
  }

  def zipWith[B, C](list: MyList[B], zip: (A, B) => C): MyList[C] = {  // list is a list of type B, zip is a function that takes two As and returns a C
    if (list.isEmpty) throw new RuntimeException("Lists do not have the same length")  // if list is empty, throw an exception
    else new Cons(zip(h, list.head), t.zipWith(list.tail, zip))  // else return a new Cons with zip(h, list.head) and t.zipWith(list.tail, zip)
  }

  /*
  [1,2,3].fold(0)(+) =
    = [2,3].fold(1)(+)  // 1 + 1 = operator(start, h)
    = [3].fold(3)(+)  // 1 + 2 = operator(start, h)
    = [].fold(6)(+)  // 3 + 3 = operator(start, h)
    = 6  // 6 = start
   */
  def fold[B](start: B)(operator: (B, A) => B): B = t.fold(operator(start, h))(operator)  // returns t.fold(operator(start, h))(operator)

}

//trait MyPredicate[-T] {  // T => Boolean -T means contravariant, means that MyPredicate is a super class of T
//  def test(element: T): Boolean
//}
//
//trait MyTransformer[-A, B] {  // A => B -A means contravariant, means that MyTransformer is a super class of A
//  def transform(element: A): B
//}



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

  println(listOfIntegers.map(new Function1[Int, Int] {
    override def apply(element: Int): Int = element * 2
  }).toString)  // [2 4 6]

  println(listOfIntegers.map(elem => elem * 2).toString)  // [2 4 6]
  println(listOfIntegers.map(_ * 2).toString)  // [2 4 6]


  println(listOfIntegers.filter(new Function1[Int, Boolean] {
    override def apply(element: Int): Boolean = element % 2 == 0
  }).toString)  // [2]

  println(listOfIntegers.filter(elem => elem % 2 == 0).toString)  // [2]
  println(listOfIntegers.filter(_ % 2 == 0).toString)  // [2]


  println((listOfIntegers ++ anotherListOfIntegers).toString)  // [1 2 3 4 5]

  println(listOfIntegers.flatMap(new Function1[Int, MyList[Int]] {
    override def apply(element: Int): MyList[Int] = new Cons(element, new Cons(element + 1, Empty))
  }).toString)  // [1 2 2 3 3 4]

  println(listOfIntegers.flatMap(elem => new Cons(elem, new Cons(elem + 1, Empty))).toString)  // [1 2 2 3 3 4]

  println(listOfIntegers == clonelistOfIntegers)  // true because of case class

  listOfIntegers.foreach(println)  // 1 2 3

  println(listOfIntegers.sort((x, y) => y - x))  // [3 2 1]

  println(anotherListOfIntegers.zipWith[String, String](listOfStrings, _ + "-" + _))  // [4-Hello 5-Scala]

  println(listOfIntegers.fold(0)(_ + _))  // 6
  
  // for comprehensions
  val combinations = for {
    n <- listOfIntegers
    string <- listOfStrings
  } yield n + "-" + string
  
  println(combinations)  // [1-Hello 1-Scala 2-Hello 2-Scala 3-Hello 3-Scala]
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