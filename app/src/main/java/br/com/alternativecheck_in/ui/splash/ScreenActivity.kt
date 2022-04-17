package br.com.alternativecheck_in.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.alternativecheck_in.R
import br.com.alternativecheck_in.databinding.DialogCustomBinding
import br.com.alternativecheck_in.ui.maps.MapsActivity

class ScreenActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen)
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showDialog()
                }

            }
        }
    }

    private fun showDialog() {
        val build = AlertDialog.Builder(this, R.style.DialogCustomer)
        val bindingDialogCustom = DialogCustomBinding.inflate(layoutInflater)
        build.setView(bindingDialogCustom.root)
        bindingDialogCustom.apply {
            textviewDialog.text = getString(R.string.dialog_text_permission)
            buttonDialog.setOnClickListener {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
                dialog.dismiss()
            }
        }
        dialog = build.create()
        dialog.show()
    }
}