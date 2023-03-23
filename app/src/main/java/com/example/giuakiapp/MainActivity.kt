package com.example.giuakiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameEditText: EditText = findViewById(R.id.nameEditText)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val confirmPasswordEditText: EditText = findViewById(R.id.confirmPasswordEditText)
        val registerButton: Button = findViewById(R.id.registerButton)

        val sqlHelper = SqlHelper(this)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            if (!isEmailValid(email)) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val isSuccess = sqlHelper.registerUser(name, email, password, confirmPassword)
            if (isSuccess) {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                nameEditText.text.clear()
                emailEditText.text.clear()
                passwordEditText.text.clear()
                confirmPasswordEditText.text.clear()
                nameEditText.requestFocus()
            } else {
                Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
            }

        }

        sqlHelper.close()
    }
    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }
}