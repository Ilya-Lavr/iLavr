package ru.tbank.education.school.lesson1

/**
 * Метод для вычисления простых арифметических операций.
 */
fun calculate(a: Double, b: Double, operation: OperationType = OperationType.ADD): Double? {
    return when(operation) {
        OperationType.ADD -> a + b;
        OperationType.SUBTRACT -> a - b;
        OperationType.MULTIPLY -> a * b;
        OperationType.DIVIDE -> if (b != 0.0) {a / b} else null;
    }
}

/**
 * Функция вычисления выражения, представленного строкой
 * @return результат вычисления строки или null, если вычисление невозможно
 * @sample "5 * 2".calculate()
 */
@Suppress("ReturnCount")
fun String.calculate(): Double? {
    val query = this.trim().split(" ");
    val n = query.size;
    if (n <= 1 || n >= 4) {return null;}
    val a = query[0].toDoubleOrNull();
    var sgn = "+";
    if (n == 3) {sgn = query[1];}
    val b = query[n - 1].toDoubleOrNull();
    if (a == null || b == null) {return null}
    val operation = when(sgn) {
       "+" -> OperationType.ADD;
       "-" -> OperationType.SUBTRACT;
       "*" -> OperationType.MULTIPLY;
       "/" -> OperationType.DIVIDE;
        else -> return null;
    }
    return calculate(a=a, b=b, operation=operation);
}