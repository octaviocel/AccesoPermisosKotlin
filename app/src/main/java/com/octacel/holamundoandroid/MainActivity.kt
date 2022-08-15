package com.octacel.holamundoandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.octacel.holamundoandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //binding!!.TextView2.text = "On create changes"

        binding!!.btnMostrar.setOnClickListener(View.OnClickListener {
            binding!!.txtCambiar.setText(binding!!.inputTxt.text)
        })

        binding!!.btnClean.setOnClickListener(View.OnClickListener {
            binding!!.txtCambiar.setText("");
            binding!!.inputTxt.setText("");
        })

        binding!!.btnOtraActividad.setOnClickListener(View.OnClickListener {
         var i = Intent(this, SecondActivity::class.java)
         startActivity(i)
        }
        )
    }

    override fun onResume() {
        super.onResume()


    }
}