package com.Lokalan


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.Lokalan.ui.features.auth.LoginScreen
import com.Lokalan.ui.features.auth.RegisterScreen
import com.Lokalan.ui.features.auth.WelcomeScreen
import com.Lokalan.ui.features.profile.view.ProfileScreen
import com.Lokalan.ui.features.detail.view.DetailScreen
import com.Lokalan.ui.features.home.HomeScreen
import com.Lokalan.ui.features.kategori.view.KategoriScreen
import com.Lokalan.ui.theme.LokalanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LokalanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "welcome" // Ubah start ke welcome
                    ) {
                        composable("welcome") {
                            WelcomeScreen(
                                onLoginClick = { navController.navigate("login") },
                                onRegisterClick = { navController.navigate("register") },
                                onGuestClick = {
                                    // Masuk sebagai tamu, bersihkan welcome dari backstack
                                    navController.navigate("home") {
                                        popUpTo("welcome") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("welcome") { inclusive = true }
                                    }
                                },
                                onRegisterClick = { navController.navigate("register") },
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        // Register Screen - TAMBAHKAN onBackClick
                        composable("register") {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    navController.popBackStack()
                                    navController.navigate("login")
                                },
                                onLoginClick = { navController.popBackStack() },
                                onBackClick = { navController.popBackStack() } // ✅ INI YANG DITAMBAH
                            )
                        }

                        // Home Screen
                        composable("home") {
                            HomeScreen(
                                onProfileClick = { navController.navigate("profile") },
                                onBudayaClick = { budayaId ->
                                    navController.navigate("detail/$budayaId")
                                },
                                onKategoriClick = { navController.navigate("kategori") },
                                onLogout = {
                                    navController.popBackStack()
                                    navController.navigate("login")
                                }
                            )
                        }

                        // Profile Screen
                        composable("profile") {
                            ProfileScreen(
                                onBackClick = { navController.popBackStack() },
                                onLogout = {
                                    navController.popBackStack()
                                    navController.navigate("login")
                                }
                            )
                        }

                        // Detail Screen
                        composable(
                            route = "detail/{budayaId}",
                            arguments = listOf(
                                navArgument("budayaId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val budayaId = backStackEntry.arguments?.getInt("budayaId") ?: 0
                            DetailScreen(
                                budayaId = budayaId,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        // Kategori Screen
                        composable("kategori") {
                            KategoriScreen(
                                onBackClick = { navController.popBackStack() },
                                onBudayaClick = { budayaId ->
                                    navController.navigate("detail/$budayaId")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}