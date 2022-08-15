package com.octacel.holamundoandroid

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.octacel.holamundoandroid.databinding.ActivitySecondBinding
import com.octacel.holamundoandroid.viewModel.MainViewModel


class SecondActivity : AppCompatActivity(), LocationListener, SensorEventListener {

    private var binding: ActivitySecondBinding? = null

    private val vm : MainViewModel by viewModels()

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private var valorTemp= "C°: "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/John_baptist_de_la_salle_1.jpg/220px-John_baptist_de_la_salle_1.jpg").into(binding!!.imageView);


        /*binding!!.btnReturn.setOnClickListener(View.OnClickListener {
            var i = Intent(this, MainActivity::class.java)
            startActivity(i)
        })*/


        binding!!.btnLocation.setOnClickListener(View.OnClickListener {
            getLocation()
        })

        binding!!.btnGaleria.setOnClickListener(View.OnClickListener {
            requestPermission()
        })

        binding!!.btnLlama.setOnClickListener(View.OnClickListener {
            val phoneNo: String = binding!!.txtPhone.text.toString()
            if (!TextUtils.isEmpty(phoneNo)) {
                val dial = "tel:$phoneNo"
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(dial)))
            } else {
                Toast.makeText(this@SecondActivity, "Ingresa un numero de Telefono", Toast.LENGTH_SHORT).show()
            }
        })

        binding!!.btnMessage.setOnClickListener(View.OnClickListener {
            val message: String = binding!!.txtMessage.text.toString()
            val phoneNo: String = binding!!.txtPhone.text.toString()
            if (!TextUtils.isEmpty(message) && !TextUtils.isEmpty(phoneNo)) {
                val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phoneNo"))
                smsIntent.putExtra("sms_body", message)
                startActivity(smsIntent)
            }else {
                Toast.makeText(this@SecondActivity, "Ingresa un numero de Telefono o Mensaje", Toast.LENGTH_SHORT).show()
            }
        })

        binding!!.btnBatery.setOnClickListener(View.OnClickListener {
            binding!!.txtBateria.text = "Porcentaje de Bateria: " + getBatteryPercentage(SecondActivity@this).toString() + " %"
        })

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        var listaSensores = sensorManager.getSensorList(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if (!listaSensores.isEmpty()) {
            val temperatureSensor = listaSensores[0]
            sensorManager.registerListener(
                this, temperatureSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        binding!!.btnTemp.setOnClickListener(View.OnClickListener {
            binding!!.txtTemp.text = valorTemp
        })
    }

    override fun onAccuracyChanged(sensor: Sensor?, precision: Int) {}


    override fun onSensorChanged(evento: SensorEvent) {

        //Cada sensor puede provocar que un thread principal pase por aquí

        //así que sincronizamos el acceso (se verá más adelante)
        synchronized(this) {
            when (evento.sensor.type) {
                Sensor.TYPE_ORIENTATION -> {
                    var i = 0
                    while (i < 3) {
                        i++
                    }
                }
                Sensor.TYPE_ACCELEROMETER -> {
                    var i = 0
                    while (i < 3) {
                        i++
                    }
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    var i = 0
                    while (i < 3) {
                        i++
                    }
                }
                else -> {
                    var i = 0
                    while (i < evento.values.size) {
                        valorTemp = "C°: " + evento.values[i]
                        i++
                    }
                }
            }
        }
    }


    private fun getBatteryPercentage(context: Context): Int {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

        val batteryPct = level / scale.toFloat()

        return (batteryPct * 100).toInt()
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }
    override fun onLocationChanged(location: Location) {
        binding!!.txtLat.text = "Lat: "+location.latitude.toString()
        binding!!.txtLong.text = "Long: " +location.longitude.toString()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestPermission() {
        // Verificaremos el nivel de API para solicitar los permisos
        // en tiempo de ejecución
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {

                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    pickPhotoFromGallery()
                }

                else -> requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else {
            // Se llamará a la función para APIs 22 o inferior
            // Esto debido a que se aceptaron los permisos
            // al momento de instalar la aplicación
            pickPhotoFromGallery()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->

        if (isGranted){
            pickPhotoFromGallery()
        }else{
            Toast.makeText(
                this,
                "Permission denied",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityResult.launch(intent)
    }

    private val startForActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data
            binding!!.imageView.setImageURI(data)
        }
    }

}


