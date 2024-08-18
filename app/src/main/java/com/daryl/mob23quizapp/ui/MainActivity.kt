package com.daryl.mob23quizapp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.daryl.mob23quizapp.R
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavController()
        setupDrawerNavigation()
    }
    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
    }
    private fun setupDrawerNavigation() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val navList = setOf(R.id.homeFragment)
        appBarConfig = AppBarConfiguration(navList, drawerLayout)

        setupNavViewConfig(drawerLayout, navView)
        setupToolbarConfig(toolbar)
        setupActionBarWithNavController(navController, appBarConfig)
    }
    private fun setupNavViewConfig(drawerLayout: DrawerLayout, navView: NavigationView) {
        navView.apply {
            setupWithNavController(navController)
//            menu.findItem().setOnMenuItemClickListener {  }
        }
    }
    private fun setupToolbarConfig(toolbar: Toolbar) {
        toolbar.apply {
            setSupportActionBar(this)
            setupWithNavController(navController, appBarConfig)
            navController.addOnDestinationChangedListener { _, dest, _ ->
                visibility = if(dest.id == R.id.loginRegisterFragment) View.GONE else View.VISIBLE
            }
        }
    }
    override fun onNavigateUp(): Boolean =
        navController.navigateUp(appBarConfig) || super.onNavigateUp()
}