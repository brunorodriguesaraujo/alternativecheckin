package br.com.alternativecheck_in.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.alternativecheck_in.R
import br.com.alternativecheck_in.databinding.ActivityAdminBinding
import br.com.alternativecheck_in.extension.*
import br.com.alternativecheck_in.helper.FirebaseHelper
import br.com.alternativecheck_in.helper.ObservableHelper
import br.com.alternativecheck_in.helper.PreferencesHelper
import br.com.alternativecheck_in.model.Driver
import br.com.alternativecheck_in.ui.admin.adapter.AdminAdapter
import br.com.alternativecheck_in.ui.admin.adapter.SendDriver
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private val driverList = mutableListOf<Driver>()
    private val adapterAdmin: AdminAdapter by lazy {
        AdminAdapter(driverList, initEditDriver())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
    }

    override fun onResume() {
        super.onResume()
        getDriver()
    }

    private fun initAdapter() {
        binding.recyclerDrivers.adapter = adapterAdmin
    }

    private fun initEditDriver() = object : SendDriver {
        override fun sendDriver(driver: Driver) {
            startEditDriver(driver)
        }
    }

    private fun getDriver() {
        binding.progressLottie.animationView.visible()
        FirebaseHelper
            .getDatabase()
            .child(getString(R.string.all_driver))
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        driverList.clear()
                        for (snap in snapshot.children) {
                            val driver = snap.getValue(Driver::class.java) as Driver
                            driverList.add(driver)
                        }
                        initAdapter()
                    }
                    ObservableHelper.userDelete.observe(this@AdminActivity) {
                        driverList.remove(it)
                    }
                    adapterAdmin.notifyDataSetChanged()
                    binding.progressLottie.animationView.gone()
                    driversEmpty()
                }

                override fun onCancelled(error: DatabaseError) {
                    createToast(this@AdminActivity, "Erro")
                }
            })
    }

    private fun driversEmpty() {
        binding.textviewEmpty.apply {
            if (driverList.isEmpty()) {
                visible()
            } else {
                gone()
            }
        }
    }

    private fun setListener() {
        binding.apply {
            fabAddDriver.setOnClickListener { startRegisterDriver() }
            imageLogout.setOnClickListener { logout() }
        }
    }

    private fun logout() {
        FirebaseHelper.getAuth().signOut()
        PreferencesHelper.getInstance(this).setPreferencesBoolean("LoginAdmin", false)
        startLogin()
    }
}