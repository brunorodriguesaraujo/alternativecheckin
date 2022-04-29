package br.com.alternativecheck_in.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.alternativecheck_in.model.Driver

object ObservableHelper {

    val userDelete: LiveData<Driver>
        get() = mUserDelete
    private val mUserDelete = MutableLiveData<Driver>()

    fun notifyDeleteDriver(driver: Driver) {
        mUserDelete.value = driver
    }
}