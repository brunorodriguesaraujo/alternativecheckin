package br.com.alternativecheck_in.model

import android.os.Parcelable
import br.com.alternativecheck_in.helper.FirebaseHelper
import com.google.firebase.auth.FirebaseUser
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Driver(
    var id: String = "",
    var driverCod: String = "",
    var driverName: String = "",
    var driverEmail: String = ""
) : Parcelable, Serializable

