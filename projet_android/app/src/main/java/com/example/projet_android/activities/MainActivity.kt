package com.example.projet_android.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.projet_android.R

class MainActivity : AppCompatActivity() {

    fun AjouterFlux(button:View){
            val aj = Intent(this, AjouterFlux::class.java)
            startActivity(aj)
    }

    fun ListFlux(button:View){
        val aj = Intent(this, ListFlux::class.java)
        startActivity(aj)
    }

    fun ListInfo(button:View){
        val aj = Intent(this, ListInfo::class.java)
        startActivity(aj)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when( item.itemId ){
            android.R.id.home     -> {
                AlertDialog.Builder(this)
                    .setMessage("Voulez vous terminer ?")
                    .setCancelable(false)
                    .setPositiveButton("Oui") { dialog: DialogInterface, t: Int ->
                        finish()
                    }
                    .setNeutralButton("non") {
                            dialogue, _ ->dialogue.cancel()
                        Log.d("Message", "cancel")
                    }.show()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}