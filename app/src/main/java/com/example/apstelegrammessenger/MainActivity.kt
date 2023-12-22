package com.example.apstelegrammessenger

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.apstelegrammessenger.databinding.ActivityMainBinding
import com.example.apstelegrammessenger.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val myIntent = Intent(this, SettingsActivity::class.java)
                //myIntent.putExtra("key", value) //Optional parameters

                this@MainActivity.startActivity(myIntent)

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}