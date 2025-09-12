package ru.tbank.education.school.lesson1

fun MutableList<Int>.sumEvenNumbers(): Int {
    var sum : Int = 0;
    for (num in this) {
        if (num % 2 == 0) {sum += num;}
    }
    return sum;
}

fun main() {
    val res = readln().calculate();
    res?.let {println(res)} ?: {println("Ошибка")}
}
