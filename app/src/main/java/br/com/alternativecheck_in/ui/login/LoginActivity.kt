package br.com.alternativecheck_in.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import br.com.alternativecheck_in.databinding.ActivityLoginBinding
import br.com.alternativecheck_in.extension.*
import br.com.alternativecheck_in.helper.FirebaseHelper
import br.com.alternativecheck_in.helper.PreferencesHelper

class LoginActivity : AppCompatActivity() {

    companion object {
        const val UID_ADMIN = "WH4WtXIiQNQzE05bmkOv8mf8VKr1"
    }

    private lateinit var binding: ActivityLoginBinding
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listener()
        verifyIfAdminIsAuth()
        hideProgress()
    }

    private fun listener() {
        binding.apply {
            edittextEmail.addTextChangedListener { email = it.toString() }
            edittextPassword.addTextChangedListener { password = it.toString() }
            buttonLogin.setOnClickListener { validateData() }
            textviewRecovery.setOnClickListener { startRecovery() }
        }
    }


    private fun validateData() {
        showProgress()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                baseContext, "Preencha os campos",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            FirebaseHelper
                .getAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        directionUserType()
                    } else {
                        Toast.makeText(
                            baseContext, FirebaseHelper.validError(
                                task.exception?.message ?: ""
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                        hideProgress()
                    }
                }
        }
    }

    private fun hideProgress() {
        binding.apply {
            buttonLogin.visible()
            progressBar.gone()
            textviewRecovery.visible()
        }
    }

    private fun showProgress() {
        binding.apply {
            progressBar.visible()
            textviewRecovery.gone()
            buttonLogin.gone()
        }
    }

    private fun directionUserType() {
        if (FirebaseHelper.getIdUser() == UID_ADMIN) {
            PreferencesHelper.getInstance(this).setPreferencesBoolean("LoginAdmin", true)
            startAdmin()
        } else {
            startMaps()
        }
    }

    private fun verifyIfAdminIsAuth() {
        val isLoginAdmin: Boolean =
            PreferencesHelper.getInstance(this).getPreferencesBoolean("LoginAdmin", false)
        if (FirebaseHelper.isAutenticated() && isLoginAdmin) {
            startAdmin()
        } else if (FirebaseHelper.isAutenticated()) {
            startMaps()
        }
    }
}