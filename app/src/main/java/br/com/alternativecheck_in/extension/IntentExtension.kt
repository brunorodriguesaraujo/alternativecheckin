package br.com.alternativecheck_in.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import br.com.alternativecheck_in.R
import br.com.alternativecheck_in.model.Driver
import br.com.alternativecheck_in.ui.admin.AdminActivity
import br.com.alternativecheck_in.ui.admin.EditDriverActivity
import br.com.alternativecheck_in.ui.admin.RegisterDriverActivity
import br.com.alternativecheck_in.ui.login.LoginActivity
import br.com.alternativecheck_in.ui.maps.MapsActivity
import br.com.alternativecheck_in.ui.position.PositionListActivity
import br.com.alternativecheck_in.ui.recovery.RecoveryActivity

fun Activity.startMaps() {
    val intent = Intent(this, MapsActivity::class.java)
    startActivity(intent)
    finish()
}

fun Activity.startLogin() {
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    finish()
}

fun Activity.startPositionList() {
    val intent = Intent(this, PositionListActivity::class.java)
    startActivity(intent)
}

fun Activity.startAdmin() {
    val intent = Intent(this, AdminActivity::class.java)
    startActivity(intent)
    finish()
}

fun Activity.startRecovery() {
    val intent = Intent(this, RecoveryActivity::class.java)
    startActivity(intent)
}

fun Activity.startRegisterDriver() {
    val intent = Intent(this, RegisterDriverActivity::class.java)
    startActivity(intent)
}

fun Activity.startEditDriver(driver: Driver) {
    val intent = Intent(this, EditDriverActivity::class.java)
    val bundle = Bundle()
    bundle.putSerializable(getString(R.string.all_driver), driver)
    intent.putExtras(bundle)
    startActivity(intent)
}