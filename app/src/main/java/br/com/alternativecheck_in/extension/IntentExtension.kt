package br.com.alternativecheck_in.extension

import android.app.Activity
import android.content.Intent
import br.com.alternativecheck_in.ui.admin.AdminActivity
import br.com.alternativecheck_in.ui.login.LoginActivity
import br.com.alternativecheck_in.ui.maps.MapsActivity
import br.com.alternativecheck_in.ui.position.PositionListActivity
import br.com.alternativecheck_in.ui.splash.ScreenActivity

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