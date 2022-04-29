package br.com.alternativecheck_in.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.alternativecheck_in.R
import br.com.alternativecheck_in.databinding.ActivityRegisterDriverBinding
import br.com.alternativecheck_in.extension.createToast
import br.com.alternativecheck_in.extension.gone
import br.com.alternativecheck_in.extension.visible
import br.com.alternativecheck_in.helper.FirebaseHelper
import br.com.alternativecheck_in.model.Driver

class RegisterDriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterDriverBinding
    private val driver: Driver by lazy {
        Driver()
    }
    private var password = ""
    private var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pushKeyForFirebase()
        setListener()
    }

    private fun pushKeyForFirebase() {
        driver.id = FirebaseHelper.getDatabase().push().key ?: ""
    }

    private fun setListener() {
        binding.apply {
            buttonAddDriver.setOnClickListener { validateData() }
            imageBack.setOnClickListener { finish() }
        }
    }

    private fun validateData() {
        getText()
        if (verifyFieldsEmpty()) {
            createToast(this, "Preencha os campos")
        } else {
            FirebaseHelper.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { driver ->
                    if (driver.isSuccessful) {
                        this.driver.driverEmail = email
                        saveDriver()
                    } else {
                        createToast(
                            this, FirebaseHelper.validError(
                                driver.exception?.message ?: ""
                            )
                        )
                    }

                }.addOnFailureListener {
                    createToast(this, "Erro")
                }
        }
    }

    private fun verifyFieldsEmpty() =
        email.isEmpty() || password.isEmpty() || binding.edittextCod.text.toString()
            .isBlank() || binding.edittextUsername.text.toString().isBlank()

    private fun getText() {
        binding.apply {
            driver.driverCod = edittextCod.text.toString()
            driver.driverName = edittextUsername.text.toString()
            email = edittextEmail.text.toString()
            password = edittextPassword.text.toString()
        }
    }

    private fun saveDriver() {
        binding.progressLottie.animationView.visible()
        FirebaseHelper
            .getDatabase()
            .child(getString(R.string.all_driver))
            .child(driver.id)
            .setValue(driver)
            .addOnCompleteListener { driver ->
                if (driver.isSuccessful) {
                    binding.progressLottie.animationView.gone()
                    createToast(this, "Motorista salvo com sucesso!")
                    finish()
                } else {
                    createToast(this, "Erro")
                }
            }.addOnFailureListener {
                createToast(this, "Erro")
            }
    }
}