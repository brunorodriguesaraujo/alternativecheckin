package br.com.alternativecheck_in.ui.recovery

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import br.com.alternativecheck_in.databinding.ActivityRecoveryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecoveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecoveryBinding
    private lateinit var auth: FirebaseAuth
    private var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        listener()
    }

    private fun listener() {
        binding.edittextEmail.addTextChangedListener { email = it.toString() }
        binding.buttonSend.setOnClickListener { validateData() }
        binding.imageBack.setOnClickListener { finish() }
    }

    private fun validateData() {
        if (email.isEmpty()) {
            Toast.makeText(this, "Preencha o campo", Toast.LENGTH_SHORT).show()
        } else {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Acabamos de enviar o link de recuperação para o seu e-mail",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}