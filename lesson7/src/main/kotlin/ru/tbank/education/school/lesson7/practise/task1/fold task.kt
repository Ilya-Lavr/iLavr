package ru.tbank.education.school.lesson7.practise.task1

/**
 *
 * Дано: список сотрудников.
 *
 * Нужно:
 *  - Посчитать общую сумму зарплат (`totalSala7ry`),
 *  - Среднюю зарплату (`avgSalary`),
 *  - Количество отделов (`departmentsCount`).
 *
 * Верни результат в виде `SalaryReport`.
 *
 * Подсказки:
 *  - `fold(initial, operation)` — позволяет задать аккумулятор с другим типом.
 *  - Используй `Set` для накопления уникальных департаментов.
 *
 * Пример:
 * ```
 * employees = [
 *   ("Anna", 100000, "IT"),
 *   ("Ivan", 80000, "IT"),
 *   ("Olga", 70000, "HR")
 * ]
 * → SalaryReport(totalSalary=250000, avgSalary≈83333.33, departmentsCount=2)
 * ```
 */
data class Employee(val name: String, val salary: Double, val department: String)
data class SalaryReport(val totalSalary: Double, val avgSalary: Double, val departmentsCount: Int)

fun buildSalaryReport(employees: List<Employee>): SalaryReport {
    if (employees.isEmpty()) { return SalaryReport(0.0, 0.0, 0) }
    val totalSalary : Double = employees.sumOf { it.salary }
    val departmentsCount : Int = employees.map { it.department }.toSet().size
    return SalaryReport(totalSalary, totalSalary / employees.size.toDouble(), departmentsCount);
}
