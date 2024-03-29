package com.beotkkot.tamhumhajang

import OnBoardingScreen
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.beotkkot.tamhumhajang.design.theme.TamhumhajangTheme
import com.beotkkot.tamhumhajang.ui.BOOKMARK
import com.beotkkot.tamhumhajang.ui.LOGIN
import com.beotkkot.tamhumhajang.ui.MAP
import com.beotkkot.tamhumhajang.ui.ONBOARDING
import com.beotkkot.tamhumhajang.ui.PROFILE
import com.beotkkot.tamhumhajang.ui.SPLASH
import com.beotkkot.tamhumhajang.ui.SplashScreen
import com.beotkkot.tamhumhajang.ui.bookmark.BookmarkScreen
import com.beotkkot.tamhumhajang.ui.bookmark.BookmarkViewModel
import com.beotkkot.tamhumhajang.ui.login.LoginScreen
import com.beotkkot.tamhumhajang.ui.login.LoginViewModel
import com.beotkkot.tamhumhajang.ui.map.MapScreen
import com.beotkkot.tamhumhajang.ui.map.MapViewModel
import com.beotkkot.tamhumhajang.ui.profile.ProfileScreen
import com.beotkkot.tamhumhajang.ui.profile.ProfileViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavHost() {
    val appState = rememberAppState()
    val bottomSheetState = rememberWineyBottomSheetState()
    val navController = appState.navController

    ModalBottomSheetLayout(
        sheetContent = {
            bottomSheetState.bottomSheetContent.value?.invoke(this)
        },
        sheetState = bottomSheetState.bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
        modifier = Modifier.navigationBarsPadding()
    ) {
        Scaffold(
            backgroundColor = TamhumhajangTheme.colors.color_ffffff,
            snackbarHost = {
                SnackBar(appState)
            }
        ) { padding ->
            NavHost(
                modifier = Modifier
                    .padding(padding)
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                navController = navController,
                startDestination = SPLASH
            ) {
                composable(SPLASH) {
                    SplashScreen(appState = appState)
                }
                
                composable(LOGIN) {
                    val viewModel: LoginViewModel = hiltViewModel()

                    LoginScreen(
                        appState = appState,
                        viewModel = viewModel
                    )
                }

                composable(
                    route = "$ONBOARDING?nickname={nickname}",
                    arguments = listOf(
                        navArgument("nickname") {
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    )
                ) {
                    OnBoardingScreen(
                        appState = appState,
                        nickname = it.arguments?.getString("nickname") ?: ""
                    )
                }


                composable(MAP) {
                    val viewModel: MapViewModel = hiltViewModel()

                    MapScreen(
                        appState = appState,
                        bottomSheetState = bottomSheetState,
                        viewModel = viewModel
                    )
                }

                composable(BOOKMARK) {
                    val viewModel: BookmarkViewModel = hiltViewModel()

                    BookmarkScreen(
                        appState = appState,
                        bottomSheetState = bottomSheetState,
                        viewModel = viewModel
                    )
                }

                composable(PROFILE) {
                    val viewModel: ProfileViewModel = hiltViewModel()

                    ProfileScreen(
                        appState = appState,
                        bottomSheetState = bottomSheetState,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun SnackBar(appState: AppState) {
    SnackbarHost(
        hostState = appState.scaffoldState.snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(
                    bottom = 30.dp,
                    start = 20.dp,
                    end = 20.dp
                )
            ) {
                Text(text = data.message)
            }
        }
    )
}