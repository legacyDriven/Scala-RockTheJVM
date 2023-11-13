package com.eugeniusz.scala

enum Tree:
  case Sum(l: Tree, r: Tree)  // sum
  case Var(n: String) // variable
  case Const(v: Int)  // constant