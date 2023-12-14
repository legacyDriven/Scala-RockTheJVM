package com.eugeniusz.scala.scala_essentials.part4pm

import com.eugeniusz.scala.scala_essentials.exercises.{Cons, Empty, MyList};

object AllThePatterns extends App {

  // 1 - constants
  val x: Any = "Scala"
  val constants = x match {
    case 1 => "a number"
    case "Scala" => "THE Scala"
    case true => "The Truth"
    case AllThePatterns => "A singleton object"
  }

  // 2 - match anything
  // 2.1 wildcard
  val matchAnything = x match {
    case _ =>
  }

  // 2.2 variable
  val matchAVariable = x match {
    case something => s"I've found $something"
  }

  // 3 - tuples
  val aTuple = (1,2)
  val matchATuple = aTuple match {
    case (1, 1) =>
    case (something, 2) => s"I've found $something"
  }

  val nestedTuple = (1, (2, 3))
  val matchANestedTuple = nestedTuple match {
    case (_, (2, v)) =>  // nested pattern
  }

  // PMs can be NESTED!

  // 4 - case classes - constructor pattern
  // PMs can be nested with CCs as well
  val aList: MyList[Int] = Cons(1, Cons(2, Empty))
  val matchAList = aList match {
    case Empty =>
    case Cons(head, Cons(subhead, subtail)) =>
  }

  // 5 - list patterns
  val aStandardList = List(1,2,3,42)
  val standardListMatching = aStandardList match {
    case List(1, _, _, _) => // extractor - advanced, it matches the list with 4 elements, first is 1, and the rest are ignored
    case List(1, _*) => // list of arbitrary length - advanced, varargs - it matches the list with 1 as the first element and the rest is ignored
    case 1 :: List(_) => // infix pattern, it matches the list with 1 as the first element and the rest is ignored
    case List(1,2,3) :+ 42 => // infix pattern, it matches the list with 42 as the last element and the rest is ignored
  }

  // 6 - type specifiers
  val unknown: Any = 2
  val unknownMatch = unknown match {
    case list: List[Int] => // explicit type specifier, this will match only if the unknown is a list of integers
    case _ =>  // this will match anything
  }

  // 7 - name binding
  val nameBindingMatch = aList match {
    case nonEmptyList @ Cons(_, _) => // name binding => use the name later(here), name @ will match the whole pattern, and this will match the list with 2 elements
    case Cons(1, rest @ Cons(2, _)) => // name binding inside nested patterns, this will match the list with 1 as the first element and the rest is ignored
  }

  // 8 - multi-patterns
  val multipattern = aList match {
    case Empty | Cons(0, _) => // compound pattern (multi-pattern), | is the OR operator, pipe operator, will match the list with 0 as the first element and the rest is ignored or Empty
    case _ =>
  }

  // 9 - if guards
  val secondElementSpecial = aList match {
    case Cons(_, Cons(specialElement, _)) if specialElement % 2 == 0 => // if guard, this will match the list with 2 elements, the second element is special and it's even
  }

  /*
      Question.
  */

  val numbers = List(1,2,3)
  val numbersMatch = numbers match {
    case listOfStrings: List[String] => "a list of strings" // this will match, because of JVM trick question - type erasure
    case listOfNumbers: List[Int] => "a list of numbers" // this will match
    case _ => ""
  }
  println(numbersMatch) // a list of strings

  // JVM trick question
  // JVM trick question, because of type erasure, the JVM will remove the type parameters, so the JVM will see the same signature for both cases

}

