package br.com.alternativecheck_in.ui.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.alternativecheck_in.databinding.ActivityAdminBinding
import br.com.alternativecheck_in.extension.startRegisterDriver
import br.com.alternativecheck_in.helper.FirebaseHelper
import br.com.alternativecheck_in.model.Driver
import br.com.alternativecheck_in.ui.admin.adapter.AdminAdapter
import br.com.alternativecheck_in.ui.admin.adapter.SendDriver
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private val listener: SendDriver? = null
    private val driverList = mutableListOf<Driver>()
    private val adapterAdmin: AdminAdapter by lazy {
        AdminAdapter(driverList, listener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
        getDriver()
    }

    private fun initAdapter() {
        binding.recyclerDrivers.adapter = adapterAdmin
    }

    private fun getDriver() {
        FirebaseHelper
            .getDatabase()
            .child("driver")
            .child(FirebaseHelper.getIdAdmin())
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
                    tasksEmpty()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AdminActivity, "Erro", Toast.LENGTH_SHORT).show()
                }

            })
    }
    private fun tasksEmpty() {
//        binding.textInfo.text = if(taskList.isEmpty()){
//            getText(R.string.text_task_list_empty_done_fragment)
//        }else {
//            ""
//        }
    }


    private fun setListener() {
        binding.apply {
            fabAddDriver.setOnClickListener { startRegisterDriver() }
        }
    }
}