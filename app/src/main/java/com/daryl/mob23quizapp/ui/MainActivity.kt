package com.daryl.mob23quizapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.LOGOUT
import com.daryl.mob23quizapp.core.services.AuthService
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.core.utils.Utils.capitalize
import com.daryl.mob23quizapp.core.utils.Utils.popUpOptions
import com.daryl.mob23quizapp.data.models.Roles
import com.daryl.mob23quizapp.data.models.Roles.TEACHER
import com.daryl.mob23quizapp.data.repositories.UserRepo
import com.daryl.mob23quizapp.databinding.FragmentTeacherDashboardBinding
import com.daryl.mob23quizapp.ui.teacherDashboard.TeacherDashboardFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authService: AuthService
    @Inject
    lateinit var userRepo: UserRepo
    @Inject
    lateinit var resourceProvider: ResourceProvider
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        setSplashScreenDuration()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavController()
        setupDrawerNavigation()
        checkLoginStatus()
    }
    private fun setSplashScreenDuration() {
        var keepOnScreen = true
        installSplashScreen().setKeepOnScreenCondition { keepOnScreen }
        lifecycleScope.launch(Dispatchers.IO) {
            delay(2500)
            keepOnScreen = false
        }
    }
    private fun checkLoginStatus() {
        authService.getUid() ?: return
        lifecycleScope.launch {
            val user = userRepo.getUserById() ?: return@launch
            setupNavViewMenu(user.role)
            val destination = when(user.role) {
                TEACHER -> R.id.teacherDashboardFragment
                else -> R.id.studentHomeFragment
            }
            navController.navigate(
                destination, null, popUpOptions(R.id.loginRegisterFragment, true)
            )
        }
    }
    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
    }
    private fun setupDrawerNavigation() {
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        navView = findViewById(R.id.navView)
        val navList = setOf(R.id.teacherDashboardFragment, R.id.studentHomeFragment)
        appBarConfig = AppBarConfiguration(navList, drawerLayout)

        setupToolbarConfig()
        setupActionBarWithNavController(navController, appBarConfig)
    }
    private fun setupToolbarConfig() {
        toolbar.apply {
            setSupportActionBar(this)
            setupWithNavController(navController, appBarConfig)
            navController.addOnDestinationChangedListener { _, dest, _ ->
                visibility = if(dest.id == R.id.loginRegisterFragment) View.GONE else View.VISIBLE
            }
        }
    }
    fun setupNavViewMenu(role: Roles) {
        navView.apply {
            menu.clear()
            inflateMenu(
                when(role) {
                    TEACHER -> R.menu.drawer_teacher_menu
                    else -> R.menu.drawer_student_menu
                }
            )
        }
        setupNavViewConfig()
    }
    private fun setupNavViewConfig() {
        navView.apply {
            setupWithNavController(navController)
            menu.findItem(R.id.logout).setOnMenuItemClickListener {
                drawerLayout.close()
                authService.logout()
                showLogoutSnackBar()
                navController.navigate(
                    R.id.loginRegisterFragment,
                    null,
                    navController.currentDestination?.id?.let { id -> popUpOptions(id, true) }
                )
                menu.clear()
                true
            }
        }
    }
    private fun showLogoutSnackBar() =
        Snackbar
            .make(
                findViewById(R.id.navHostFragment),
                resourceProvider.getString(R.string.success_message, LOGOUT.capitalize()),
                Snackbar.LENGTH_LONG
            ).setBackgroundTint(ContextCompat.getColor(this, R.color.blue)).show()
    override fun onNavigateUp(): Boolean =
        navController.navigateUp(appBarConfig) || super.onNavigateUp()
}