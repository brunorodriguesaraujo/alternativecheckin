package br.com.alternativecheck_in.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import br.com.alternativecheck_in.R
import br.com.alternativecheck_in.databinding.ActivityEditDriverBinding
import br.com.alternativecheck_in.databinding.DialogCustomBinding
import br.com.alternativecheck_in.extension.createToast
import br.com.alternativecheck_in.extension.gone
import br.com.alternativecheck_in.extension.visible
import br.com.alternativecheck_in.helper.FirebaseHelper
import br.com.alternativecheck_in.helper.ObservableHelper
import br.com.alternativecheck_in.model.Driver
import com.google.firebase.auth.EmailAuthProvider

class EditDriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDriverBinding
    private lateinit var dialog: AlertDialog
    private val driver: Driver by lazy {
        intent.extras?.getSerializable(getString(R.string.all_driver)) as Driver
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFields()
        setListener()
    }

    private fun setFields() {
        binding.apply {
            edittextCod.setText(driver.driverCod)
            edittextUsername.setText(driver.driverName)
        }
    }

    private fun setListener() {
        binding.apply {
            edittextCod.addTextChangedListener { driver.driverCod = it.toString() }
            edittextUsername.addTextChangedListener { driver.driverName = it.toString() }
            buttonEditDriver.setOnClickListener { updateDriver() }
            imageDeleteDriver.setOnClickListener { showDialog() }
            imageBack.setOnClickListener { finish() }
        }
    }

    private fun showDialog() {
        val build = AlertDialog.Builder(this, R.style.DialogCustomer)
        val bindingDialogCustom = DialogCustomBinding.inflate(layoutInflater)
        build.setView(bindingDialogCustom.root)
        bindingDialogCustom.apply {
            buttonDialogCancel.visible()
            textviewDialog.text = getString(R.string.dialog_text_delete)
            buttonDialogCancel.setOnClickListener { dialog.dismiss() }
            buttonDialogContinue.setOnClickListener {
                dialog.dismiss()
                deleteDriver()
            }
        }
        dialog = build.create()
        dialog.show()
    }

    private fun updateDriver() {
        binding.progressLottie.animationView.visible()
        FirebaseHelper
            .getDatabase()
            .child(getString(R.string.all_driver))
            .child(driver.id)
            .setValue(driver)
            .addOnCompleteListener { driver ->
                if (driver.isSuccessful) {
                    binding.progressLottie.animationView.gone()
                    createToast(this, "Motorista atualizado com sucesso!")
                    finish()
                } else {
                    createToast(this, "Erro")
                }
            }.addOnFailureListener {
                createToast(this, "Erro")
            }
    }

    private fun deleteDriver() {
        binding.progressLottie.animationView.visible()
        FirebaseHelper
            .getDatabase()
            .child(getString(R.string.all_driver))
            .child(driver.id)
            .removeValue()
            .addOnCompleteListener { driver ->
                if (driver.isSuccessful) {
                    binding.progressLottie.animationView.gone()
                    val user = FirebaseHelper.getAuth().currentUser
                    val credential = EmailAuthProvider.getCredential(
                        this.driver.driverEmail,
                        EmailAuthProvider.PROVIDER_ID
                    )
                    user?.reauthenticate(credential)
                    user?.delete()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            createToast(this, "Motorista deletado com sucesso!")
                            ObservableHelper.notifyDeleteDriver(this.driver)
                            finish()
                        } else {
                            createToast(this, "Erro")
                        }
                    }
                } else {
                    createToast(this, "Erro")
                }
            }.addOnFailureListener {
                createToast(this, "Erro")
            }
    }
}