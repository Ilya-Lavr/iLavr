package ru.tbank.education.school.lesson8.homework.payments

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayOutputStream
import java.io.PrintStream
//import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertTrue

class PaymentProcessorTest {
    private lateinit var processor: PaymentProcessor

    @BeforeEach
    fun setUp() {
        processor = PaymentProcessor()
    }

    @Test
    fun isValidArgument() {
        val validArgument = listOf(
            1,
            "12345678901234",
            1,
            2027,
            "EUR",
            "42",
        )
        val allIllegalArguments = listOf(
            listOf(0, -1, -100),
            listOf("123456789012", "12345678901234567890", "", "a1234567890123"),
            listOf(0, 13),
            listOf(2025, 2024, 1984),
            listOf(""),
            listOf("")
        )


        for (i in 0 until 6) {
            for (value in allIllegalArguments[i]) {
                assertThrows(IllegalArgumentException::class.java) {
                    processor.processPayment(
                        (if (i == 0) value else validArgument[0]) as Int,
                        (if (i == 1) value else validArgument[1]) as String,
                        (if (i == 2) value else validArgument[2]) as Int,
                        (if (i == 3) value else validArgument[3]) as Int,
                        (if (i == 4) value else validArgument[4]) as String,
                        (if (i == 5) value else validArgument[5]) as String,
                    )
                }
            }
        }
    }

    @Test
    fun isSafety() {
        val validCard: (cardNumber: String) -> Boolean = { cardNumber ->
            val resultStatus = processor.processPayment(
                1,
                cardNumber,
                1,
                2027,
                "EUR",
                "42"
            ).status
            resultStatus == "REJECTED"
        }


        assertFalse { validCard("506282173456789205") }
        assertTrue { validCard("506282173456789201") }
        assertTrue { validCard("11115235134134587") }
    }

    @Test
    fun logCheck() {
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)
        val originalOut = System.out
        System.setOut(printStream)



        processor.processPayment( // undignified
            1,
            "506282173456789205",
            1,
            2027,
            "KRW",
            "42"
        )
        assertTrue { outputStream.toString().contains("WARNING") }

        processor.processPayment( // insufficient funds
            123,
            "5500123412341160",
            1,
            2027,
            "EUR",
            "42"
        )
        assertTrue { outputStream.toString().contains("insufficient funds") }
        assertTrue {  }

        System.setOut(originalOut)
    }
}