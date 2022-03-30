package com.thk.movieranking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.thk.movieranking.network.MovieApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setBottomNav()
        checkSessionId()
    }

    private fun setBottomNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setupWithNavController(navController)
    }

    private fun checkSessionId() {
        getSessionPreference().getSessionId() ?: CoroutineScope(Dispatchers.IO).launch {
            val result = MovieApiService.api.getGuestSessionId()
            logd(result.toString())

            if (result.success) {
                getSessionPreference().putSessionId(result.guestSessionId)
                logd(">> get sessionId success")
            } else {
                Toast.makeText(this@MainActivity, "get sessionId failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}