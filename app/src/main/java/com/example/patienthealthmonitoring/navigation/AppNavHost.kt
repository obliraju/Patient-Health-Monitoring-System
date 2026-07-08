package com.example.patienthealthmonitoring.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import com.example.patienthealthmonitoring.data.AppContainer
import com.example.patienthealthmonitoring.ui.screens.auth.LoginScreen
import com.example.patienthealthmonitoring.ui.screens.auth.RegisterScreen
import com.example.patienthealthmonitoring.ui.screens.home.HomeScreen
import com.example.patienthealthmonitoring.ui.screens.patient.PatientDetailsScreen
import com.example.patienthealthmonitoring.ui.screens.patient.PatientFormScreen
import com.example.patienthealthmonitoring.viewmodel.AuthViewModel
import com.example.patienthealthmonitoring.viewmodel.HomeViewModel
import com.example.patienthealthmonitoring.viewmodel.PatientDetailsViewModel
import com.example.patienthealthmonitoring.viewmodel.PatientFormViewModel
import com.example.patienthealthmonitoring.viewmodel.PatientViewModelFactory

@Composable
fun AppNavHost(container: AppContainer) {
    val navController = rememberNavController()
    val factory = remember(container) { PatientViewModelFactory(container) }
    val startDestination = remember {
        if (container.sessionManager.isLoggedIn) Screen.Home.route else Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = viewModel(factory = factory)
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            val authViewModel: AuthViewModel = viewModel(factory = factory)
            RegisterScreen(
                viewModel = authViewModel,
                onBackClick = { navController.popBackStack() },
                onRegistrationComplete = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = factory)
            HomeScreen(
                viewModel = homeViewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onAddPatient = { navController.navigate(Screen.AddPatient.route) },
                onPatientClick = { patientId ->
                    navController.navigate(Screen.PatientDetails.createRoute(patientId))
                }
            )
        }

        composable(Screen.AddPatient.route) {
            val patientFormViewModel: PatientFormViewModel = viewModel(factory = factory)
            PatientFormScreen(
                patientId = null,
                viewModel = patientFormViewModel,
                onCancel = { navController.popBackStack() },
                onSaved = { navController.popBackStack(Screen.Home.route, inclusive = false) }
            )
        }

        composable(
            route = Screen.PatientDetails.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: return@composable
            val detailsViewModel: PatientDetailsViewModel = viewModel(factory = factory)
            PatientDetailsScreen(
                patientId = patientId,
                viewModel = detailsViewModel,
                onBack = { navController.popBackStack() },
                onEdit = { id -> navController.navigate(Screen.EditPatient.createRoute(id)) },
                onDeleted = { navController.popBackStack(Screen.Home.route, inclusive = false) }
            )
        }

        composable(
            route = Screen.EditPatient.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: return@composable
            val patientFormViewModel: PatientFormViewModel = viewModel(factory = factory)
            PatientFormScreen(
                patientId = patientId,
                viewModel = patientFormViewModel,
                onCancel = { navController.popBackStack() },
                onSaved = { navController.popBackStack(Screen.Home.route, inclusive = false) }
            )
        }
    }
}

private sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object AddPatient : Screen("add_patient")
    object PatientDetails : Screen("patient_details/{patientId}") {
        fun createRoute(patientId: String) = "patient_details/$patientId"
    }
    object EditPatient : Screen("edit_patient/{patientId}") {
        fun createRoute(patientId: String) = "edit_patient/$patientId"
    }
}
