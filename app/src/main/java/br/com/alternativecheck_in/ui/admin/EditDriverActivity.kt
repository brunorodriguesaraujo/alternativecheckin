package br.com.alternativecheck_in.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.alternativecheck_in.R
import br.com.alternativecheck_in.databinding.ActivityEditDriverBinding
import br.com.alternativecheck_in.model.Driver

class EditDriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDriverBinding
    private val driver: Driver by lazy {
        intent.extras?.getSerializable(getString(R.string.all_driver)) as Driver
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFields()
    }

    private fun setFields() {
        binding.apply {
            edittextCod.setText(driver.driverCod.toString())
            edittextUsername.setText(driver.driverName)
        }
    }
}