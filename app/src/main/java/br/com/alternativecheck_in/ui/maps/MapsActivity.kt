package br.com.alternativecheck_in.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.com.alternativecheck_in.R
import br.com.alternativecheck_in.databinding.ActivityMapsBinding
import br.com.alternativecheck_in.ui.extension.gone
import br.com.alternativecheck_in.ui.extension.visible
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

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }
    private val fuseLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mListener: LocationListener
    private var locationManager: LocationManager? = null
    private lateinit var mLastLocation: LatLng
    private lateinit var circleOption: CircleOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createMapFragment()
        binding.buttonList.setOnClickListener {
            val intent = Intent(this, PositionListActivity::class.java)
            startActivity(intent)
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

    private fun isPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
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
                    successPermission()
                } else {
                    failedPermission()
                }
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun successPermission() {
        binding.buttonMapsCheckin.visible()
        mMap.isMyLocationEnabled = true
        moveCamera()
    }

    private fun failedPermission() {
        binding.buttonMapsCheckin.gone()
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            mMap.isMyLocationEnabled = true
            moveCamera()
            locationUpdates()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }
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
        binding.buttonMapsCheckin.isEnabled = distance[0] <= circleOption.radius
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