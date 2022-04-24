package br.com.alternativecheck_in.model

import android.os.Parcelable
import br.com.alternativecheck_in.helper.FirebaseHelper
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Driver(
    var id: String = "",
    var driverCod: Int? = null,
    var driverName: String = "",
): Parcelable, Serializable {
    init {
        this.id = FirebaseHelper.getDatabase().push().key ?: ""
    }
}
