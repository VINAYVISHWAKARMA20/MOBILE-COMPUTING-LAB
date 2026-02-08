import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yourpackage.databinding.ActivityMainBinding // Replace with your actual package name

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up click listeners for all buttons
        binding.btnAdd.setOnClickListener { calculate("+") }
        binding.btnSub.setOnClickListener { calculate("-") }
        binding.btnMul.setOnClickListener { calculate("*") }
        binding.btnDiv.setOnClickListener { calculate("/") }
    }

    private fun calculate(operation: String) {
        val n1 = binding.num1.text.toString().toDoubleOrNull()
        val n2 = binding.num2.text.toString().toDoubleOrNull()

        if (n1 == null || n2 == null) {
            binding.txtResult.text = "Please enter numbers"
            return
        }

        val result = when (operation) {
            "+" -> n1 + n2
            "-" -> n1 - n2
            "*" -> n1 * n2
            "/" -> if (n2 != 0.0) n1 / n2 else "Error (Div by 0)"
            else -> 0.0
        }

        binding.txtResult.text = "Result: $result"
    }
}

