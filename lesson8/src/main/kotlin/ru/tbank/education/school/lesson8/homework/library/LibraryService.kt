package ru.tbank.education.school.lesson8.homework.library

class LibraryService {
    private val books = mutableMapOf<String, Book>()
    private val borrowedBooks = mutableSetOf<String>()
    private val borrowerFines = mutableSetOf<String>()

    fun addBook(book: Book) {
        books[book.isbn] = book
    }

    fun borrowBook(isbn: String, borrower: String) {
        if (!isAvailable(isbn)) {
            throw IllegalArgumentException("not available")
        }
        if (hasOutstandingFines(borrower)) {
            throw IllegalArgumentException("borrower has fine")
        }
        borrowedBooks.add(isbn)
        borrowerFines.add(borrower);
    }

    fun returnBook(isbn: String) {
        if (!borrowedBooks.contains(isbn)) {
            throw IllegalArgumentException()
        }
        borrowedBooks.remove(isbn)
    }

    fun isAvailable(isbn: String): Boolean {
        return !borrowedBooks.contains(isbn) && books.containsKey(isbn)
    }

    fun calculateOverdueFine(isbn: String, daysOverdue: Int): Int {
        if (!borrowedBooks.contains(isbn)) {
            return 0
        }
        return maxOf(daysOverdue - 10, 0) * 60
    }

    private fun hasOutstandingFines(borrower: String): Boolean {
        return borrowerFines.contains(borrower)
    }
}