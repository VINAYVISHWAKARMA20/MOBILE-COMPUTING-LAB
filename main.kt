package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Views
        val num1 = findViewById<EditText>(R.id.num1)
        val num2 = findViewById<EditText>(R.id.num2)
        val resultText = findViewById<TextView>(R.id.resultText)
        
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnSub = findViewById<Button>(R.id.btnSub)
        val btnMul = findViewById<Button>(R.id.btnMul)
        val btnDiv = findViewById<Button>(R.id.btnDiv)

        // Click Listener Function
        fun calculate(operation: (Double, Double) -> Double) {
            val n1 = num1.text.toString().toDoubleOrNull()
            val n2 = num2.text.toString().toDoubleOrNull()

            if (n1 != null && n2 != null) {
                val result = operation(n1, n2)
                resultText.text = "Result: $result"
            } else {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            }
        }

        // Set Click Listeners
        btnAdd.setOnClickListener { calculate { a, b -> a + b } }
        btnSub.setOnClickListener { calculate { a, b -> a - b } }
        btnMul.setOnClickListener { calculate { a, b -> a * b } }
        btnDiv.setOnClickListener {
            if (num2.text.toString() == "0") {
                Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show()
            } else {
                calculate { a, b -> a / b }
            }
        }
    }
}