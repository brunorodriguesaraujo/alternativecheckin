package br.com.alternativecheck_in.ui.maps

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.alternativecheck_in.R
import br.com.alternativecheck_in.databinding.ActivityMapsBinding
import br.com.alternativecheck_in.databinding.DialogCustomBinding
import br.com.alternativecheck_in.extension.startPositionList
import br.com.alternativecheck_in.ui.position.PositionListActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private val fuseLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mListener: LocationListener
    private var locationManager: LocationManager? = null
    private lateinit var mLastLocation: LatLng
    private lateinit var circleOption: CircleOptions
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createMapFragment()
        binding.buttonMapsList.setOnClickListener {
            startPositionList()
        }
    }

    private fun createMapFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        addCircle()
        enableMyLocation()
    }

    private fun addCircle() {
        val rodJp = LatLng(-7.118388, -34.890722)
        circleOption = CircleOptions()
            .center(rodJp)
            .radius(100.0)
            .strokeColor(R.color.colorPrimary)
        mMap.addCircle(circleOption)
    }

    @SuppressLint("MissingPermission")
    private fun successPermission() {
        mMap.isMyLocationEnabled = true
        moveCamera()
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        mMap.isMyLocationEnabled = true
        moveCamera()
        locationUpdates()

    }

    @SuppressLint("MissingPermission")
    private fun moveCamera() {
        fuseLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                calculateDistance(it)
                val currentLocation = LatLng(it.latitude, it.longitude)
                mLastLocation = currentLocation
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLastLocation, 15F))
            }
        }
    }

    private fun calculateDistance(location: Location) {
        val distance = FloatArray(2)
        Location.distanceBetween(
            location.latitude, location.longitude,
            circleOption.center!!.latitude,
            circleOption.center!!.longitude,
            distance
        )
        setButtonCheckin(distance)
    }

    private fun setButtonCheckin(distance: FloatArray) {
        binding.buttonMapsCheckin.setOnClickListener {
            if (distance[0] >= circleOption.radius) {
                showDialog()
            } else {
                val intent = Intent(this, PositionListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showDialog() {
        val build = AlertDialog.Builder(this, R.style.DialogCustomer)
        val bindingDialogCustom = DialogCustomBinding.inflate(layoutInflater)
        build.setView(bindingDialogCustom.root)
        bindingDialogCustom.apply {
            textviewDialog.text = getString(R.string.dialog_text_location)
            buttonDialog.setOnClickListener { dialog.dismiss() }
        }
        dialog = build.create()
        dialog.show()
    }

    @SuppressLint("MissingPermission")
    fun locationUpdates() {
        mListener = LocationListener { location -> onLocationChangedUser(location) }
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0L,
            0F,
            mListener
        )
    }

    private fun onLocationChangedUser(location: Location) {
        val newlocal = LatLng(location.latitude, location.longitude)
        mLastLocation = newlocal
        calculateDistance(location)
    }
}