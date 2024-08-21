package com.daryl.mob23quizapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.services.AuthService
import com.daryl.mob23quizapp.data.models.Roles.TEACHER
import com.daryl.mob23quizapp.data.repositories.UserRepo
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authService: AuthService
    @Inject
    lateinit var userRepo: UserRepo
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavController()
        setupDrawerNavigation()
        checkLoginStatus()
    }
    private fun checkLoginStatus() {
        authService.getUid() ?: return
        lifecycleScope.launch {
            val user = userRepo.getUserById() ?: return@launch
            val destination = when(user.role) {
                TEACHER -> R.id.teacherDashboardFragment
                else -> -1
            }
            navController.navigate(
                destination,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.loginRegisterFragment, true).build()
            )
        }
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
        val navList = setOf(R.id.teacherDashboardFragment)
        appBarConfig = AppBarConfiguration(navList, drawerLayout)

        setupNavViewConfig(drawerLayout, navView)
        setupToolbarConfig(toolbar)
        setupActionBarWithNavController(navController, appBarConfig)
    }
    private fun setupNavViewConfig(drawerLayout: DrawerLayout, navView: NavigationView) {
        navView.apply {
            setupWithNavController(navController)
            menu.findItem(R.id.logout).setOnMenuItemClickListener {
                drawerLayout.close()
                authService.logout()
                navController.navigate(
                    R.id.loginRegisterFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(navController.graph.startDestinationId, true).build()
                )
                true
            }
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