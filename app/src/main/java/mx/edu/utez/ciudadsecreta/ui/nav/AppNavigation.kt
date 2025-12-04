package mx.edu.utez.ciudadsecreta.ui.nav

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import mx.edu.utez.ciudadsecreta.data.retrofit.ApiService
import mx.edu.utez.ciudadsecreta.data.retrofit.RetrofitClient
import mx.edu.utez.ciudadsecreta.repository.UserRepository
import mx.edu.utez.ciudadsecreta.ui.screen.LoginScreen
import mx.edu.utez.ciudadsecreta.ui.screen.RegisterScreen
import mx.edu.utez.ciudadsecreta.viewmodel.LoginViewModel
import mx.edu.utez.ciudadsecreta.viewmodel.factories.LoginViewModelFactory
import mx.edu.utez.ciudadsecreta.viewmodel.RegisterViewModel
import mx.edu.utez.ciudadsecreta.viewmodel.factories.RegisterViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val apiService = RetrofitClient.api
    val userRepository = UserRepository(apiService as ApiService)

    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(userRepository))
    val registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(
        userRepository
    )
    )


    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val showBottomBar = navBarItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavBar(navController = navController)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(padding)
        ) {

            // PANTALLA LOGIN
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = loginViewModel,
                    onNavigateToRegister = {
                        navController.navigate(Screen.Login.route)
                    },
                    onLoginSuccess = {

                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    viewModel = registerViewModel,
                    onRegistrationSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = {
                        // El usuario presionó "Volver"
                        navController.popBackStack()
                    }
                )
            }
        }
    }

}