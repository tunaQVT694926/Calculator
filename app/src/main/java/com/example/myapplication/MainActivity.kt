package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var tvResult: TextView
    private var currentInput = ""
    private var lastNumeric = false
    private var operatorAdded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)
        tvResult = findViewById(R.id.tvResult)

        // Set click listeners cho các nút số
        setNumberClickListeners()
        setOperationClickListeners()
    }

    private fun setNumberClickListeners() {
        val numberButtons = listOf(
            findViewById<Button>(R.id.btn0),
            findViewById<Button>(R.id.btn1),
            findViewById<Button>(R.id.btn2),
            findViewById<Button>(R.id.btn3),
            findViewById<Button>(R.id.btn4),
            findViewById<Button>(R.id.btn5),
            findViewById<Button>(R.id.btn6),
            findViewById<Button>(R.id.btn7),
            findViewById<Button>(R.id.btn8),
            findViewById<Button>(R.id.btn9),
            findViewById<Button>(R.id.btnDot)
        )

        for (button in numberButtons) {
            button.setOnClickListener {
                onDigit(button.text.toString())
            }
        }
    }

    private fun setOperationClickListeners() {
        findViewById<Button>(R.id.btnAdd).setOnClickListener { onOperator("+") }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { onOperator("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { onOperator("*") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { onOperator("/") }

        // Nút dấu bằng
        findViewById<Button>(R.id.btnEqual).setOnClickListener { onEqual() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { onClear() }
    }

    private fun onDigit(digit: String) {
        if (currentInput == "0") {
            currentInput = digit
        } else {
            currentInput += digit
        }
        lastNumeric = true
        updateResult()
    }

    private fun onOperator(operator: String) {
        if (lastNumeric && !operatorAdded) {
            currentInput += operator
            lastNumeric = false
            operatorAdded = true
        }
        updateResult()
    }

    private fun onEqual() {
        if (lastNumeric) {
            try {
                val result = evaluateExpression(currentInput)
                currentInput = result.toString()
                operatorAdded = false
            } catch (e: ArithmeticException) {
                currentInput = "Error"
            }
        }
        updateResult()
    }

    private fun onClear() {
        currentInput = ""
        updateResult()
    }

    private fun onDelete() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            updateResult()
        }
    }

    private fun evaluateExpression(expression: String): Double {
        val tokens = expression.split("(?<=[-+*/])|(?=[-+*/])".toRegex())
        var result = tokens[0].toDouble()
        var i = 1
        while (i < tokens.size) {
            val operator = tokens[i]
            val nextValue = tokens[i + 1].toDouble()
            when (operator) {
                "+" -> result += nextValue
                "-" -> result -= nextValue
                "*" -> result *= nextValue
                "/" -> result /= nextValue
            }
            i += 2
        }
        return result
    }

    private fun updateResult() {
        tvResult.text = currentInput
    }
}