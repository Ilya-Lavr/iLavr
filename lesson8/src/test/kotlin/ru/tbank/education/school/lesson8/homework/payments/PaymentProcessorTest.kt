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
            resultStatus != "REJECTED"
        }


        assertTrue { validCard("506282173456789205") }
        assertFalse { validCard("506282173456789201") }
        assertFalse { validCard("11115235134134587") }
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



        System.setOut(originalOut)
    }

    @Test
    fun currencyCheck() {
        val correctCurrency = listOf(
            "USD",
            "EUR",
            "GBP",
            "JPY",
            "RUB"
        )

        for (cur in correctCurrency) {
            assertTrue {
                processor.processPayment(
                    100,
                    "506282173456789205",
                    1,
                    2027,
                    cur,
                    "42"
                ).status.contains("SUCCESS")
            }
        }
    }

    @Test
    fun gatewayResultCheck() {
        assertTrue {
            processor.processPayment(
                100001,
                "506282173456789205",
                11,
                2025,
                "USD",
                "42"
            ).message.contains("Transaction limit exceeded")
        }
        assertTrue {
            processor.processPayment(
                100,
                "5500123412341160",
                11,
                2025,
                "USD",
                "42"
            ).message.contains("Insufficient funds")
        }
        assertTrue {
            processor.processPayment(
                34,
                "506282173456789205",
                11,
                2025,
                "USD",
                "42"
            ).message.contains("Gateway timeout")
        }
    }

    @Test
    fun bulkProcessCheck() {
        assertTrue { processor.bulkProcess(emptyList()).isEmpty() }
        val paymentInvalid = PaymentData(
            0,
            "",
            0,
            0,
            "",
            ""
        )
        assertTrue { processor.bulkProcess(listOf(paymentInvalid))[0].status == "REJECTED" }
        val paymentValid = PaymentData(
            100,
            "506282173456789205",
            1,
            2027,
            "EUR",
            "42"
        )
        assertTrue { processor.bulkProcess(listOf(paymentValid))[0].status == "SUCCESS" }
    }

    @Test
    fun calculateLoyaltyDiscountCheck() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.calculateLoyaltyDiscount(0, 0)
        }
        assertTrue {
            processor.calculateLoyaltyDiscount(10000, 1000000) == 5000 &&
            processor.calculateLoyaltyDiscount(10000, 5000) == 1000 &&
            processor.calculateLoyaltyDiscount(5000, 100000000) == 3000 &&
            processor.calculateLoyaltyDiscount(5000, 20000) == 3000 &&
            processor.calculateLoyaltyDiscount(2000, 1000000000) == 1500 &&
            processor.calculateLoyaltyDiscount(2000, 10000) == 1000 &&
            processor.calculateLoyaltyDiscount(500, 1000000000) == 500 &&
            processor.calculateLoyaltyDiscount(500, 2000) == 100 &&
            processor.calculateLoyaltyDiscount(10, 1000000) == 0

        }
    }

    @Test
    fun test84to90() {
        val payment = PaymentData(
            100001,
            "506282173456789205",
            11,
            2025,
            "USD",
            "42"
        )

        assertTrue { processor.bulkProcess(listOf(payment))[0].status == "FAILED" }
    }
}