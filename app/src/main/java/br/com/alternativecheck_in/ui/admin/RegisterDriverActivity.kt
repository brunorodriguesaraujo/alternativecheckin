package br.com.alternativecheck_in.ui.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import br.com.alternativecheck_in.R
import br.com.alternativecheck_in.databinding.ActivityRegisterDriverBinding
import br.com.alternativecheck_in.helper.FirebaseHelper
import br.com.alternativecheck_in.model.Driver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterDriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterDriverBinding

    private val driver: Driver by lazy {
        Driver()
    }
    private var password = ""
    private var email = ""
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        setListener()
    }

    private fun setListener() {
        binding.apply {
            edittextCod.addTextChangedListener { driver.driverCod = it.toString().toInt() }
            edittextUsername.addTextChangedListener { driver.driverName = it.toString() }
            edittextEmail.addTextChangedListener { email = it.toString() }
            edittextPassword.addTextChangedListener { password = it.toString() }
            buttonAddDriver.setOnClickListener { validateData() }
        }
    }

    private fun validateData() {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                baseContext, "Preencha os campos",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { driver ->
                    if (driver.isSuccessful) {
                        saveDriver()
                    } else {
                        Toast.makeText(
                            baseContext, FirebaseHelper.validError(
                                driver.exception?.message ?: ""
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun saveDriver() {
        FirebaseHelper
            .getDatabase()
            .child(getString(R.string.all_driver))
            .child(FirebaseHelper.getIdAdmin())
            .child(driver.id)
            .setValue(driver)
            .addOnCompleteListener { driver ->
                if (driver.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Motorista salvo com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(this, "erro", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "erro", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}