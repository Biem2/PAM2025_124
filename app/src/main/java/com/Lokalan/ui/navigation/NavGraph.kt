package com.Lokalan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Lokalan.ui.features.auth.LoginScreen
import com.Lokalan.ui.features.auth.RegisterScreen
import com.Lokalan.ui.features.auth.WelcomeScreen
import com.Lokalan.ui.features.detail.view.DetailScreen
import com.Lokalan.ui.features.home.HomeScreen
import com.Lokalan.ui.features.kategori.view.KategoriScreen
import com.Lokalan.ui.features.profile.view.ProfileScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("register") },
                onGuestClick = { navController.navigate("home") }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("home") },
                onRegisterClick = { navController.navigate("register") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("home") },
                onLoginClick = { navController.navigate("login") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(
                onBudayaClick = { budayaId -> navController.navigate("detail/$budayaId") },
                onKategoriClick = { navController.navigate("kategori") },
                onProfileClick = { navController.navigate("profile") },
                onLogout = { navController.navigate("welcome") { popUpTo(0) } }
            )
        }

        composable("detail/{budayaId}") { backStackEntry ->
            val budayaId = backStackEntry.arguments?.getString("budayaId")?.toIntOrNull()
            if (budayaId != null) {
                DetailScreen(
                    budayaId = budayaId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable("kategori") {
            KategoriScreen(
                onBackClick = { navController.popBackStack() },
                onBudayaClick = { budayaId -> navController.navigate("detail/$budayaId") }
            )
        }

        composable("profile") {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = { navController.navigate("welcome") { popUpTo(0) } }
            )
        }
    }
}