//package com.eugeniusz.scala.scala_essentials;
//
//
///*
//[16:50] Marcin Pijanowski
//Write a function solution that, given a String S consisting of N letters 'a' and/or 'b'
//returns
//    true when all occurrences of letter 'a' are before all occurrences of letter 'b'
//    false otherwise.
//Examples:
//1. Given S = "aabbb", the function should return true.
//2. Given S = "ba", the function should return false.
//3. Given S = "aaa", the function should return true. Note that 'b' does not need to occur in S.
//4. Given S = "b", the function should return true. Note that 'a' does not need to occur in S.
//5. Given S = "abba", the function should return false.
//Write an efficient algorithm for the following assumptions:
//N is an integer within the range [1..300,000]; string S is made only of the characters "a" and/or "b".
//[16:51] Marcin Pijanowski
//public boolean check(String s) {
//boolean result = false;
//return result;
//}
// */
//
//public boolean checkString(String s){
//    Integer lastIndexOfA = null;
//    Integer firstIndexOfB = null;
//    char[] values = s.split("");
//    for(int i = 0; i < s.length(); i++ ){
//        if(values[i]=='a') lastIndexOfA = i;
//        else if (values[i]=='b') firstIndexOfB = i;
//
//        if (firstIndexOfB<lastIndexOfA) return false;
//    }
//    return true;
//}
//
//}
