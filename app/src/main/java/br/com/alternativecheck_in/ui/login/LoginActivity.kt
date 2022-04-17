package br.com.alternativecheck_in.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import br.com.alternativecheck_in.databinding.ActivityLoginBinding
import br.com.alternativecheck_in.extension.startAdmin
import br.com.alternativecheck_in.extension.startMaps
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val UID_ADMIN = "WH4WtXIiQNQzE05bmkOv8mf8VKr1"

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        setEmailAndPassword()
        listener()
    }

    private fun listener() {
        binding.buttonLogin.setOnClickListener {
            validateData()
        }
    }

    private fun setEmailAndPassword() {
        binding.apply {
            edittextEmail.addTextChangedListener {
                email = it.toString()
            }
            edittextPassword.addTextChangedListener {
                password = it.toString()
            }
        }
    }

    private fun validateData() {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                baseContext, "Preencha os campos",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        directionUserType()
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun directionUserType() {
        if (auth.uid == UID_ADMIN)
            startAdmin()
        else {
            startMaps()
        }
    }
}