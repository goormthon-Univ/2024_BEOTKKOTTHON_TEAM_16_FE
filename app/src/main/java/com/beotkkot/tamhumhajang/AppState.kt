package com.beotkkot.tamhumhajang

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberAppState(
    isMapDetail: MutableState<Boolean> = mutableStateOf(false),
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    kakaoCameraPositionState: com.beotkkot.kakaomap_compose.state.CameraPositionState = com.beotkkot.kakaomap_compose.state.rememberCameraPositionState()
): AppState {
    return remember(Unit) {
        AppState(
            isMapDetail,
            navController,
            scaffoldState,
            scope,
            cameraPositionState,
            kakaoCameraPositionState
        )
    }
}

@Stable
class AppState(
    val isMapDetail: MutableState<Boolean>,
    val navController: NavHostController,
    val scaffoldState: ScaffoldState,
    val scope: CoroutineScope,
    val cameraPositionState: CameraPositionState,
    val kakaoCameraPositionState: com.beotkkot.kakaomap_compose.state.CameraPositionState
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val topLevelDestination = TopLevelDestination.values().toList()

    val shouldShowBottomBar: Boolean
        @Composable get() = !isMapDetail.value && currentDestination?.route ==
                topLevelDestination.find { it.route == currentDestination?.route }?.route


    fun updateIsMapDetail(isMapDetail: Boolean) {
        this.isMapDetail.value = isMapDetail
    }

    fun showSnackbar(message: String) = scope.launch {
        scaffoldState.snackbarHostState.showSnackbar(message)
    }

    fun navigate(destination: String, builder: NavOptionsBuilder.() -> Unit = {}) {
        navController.navigate(destination, builder)
    }

    fun navigate(
        destination: String,
        navOptions: NavOptions? = null,
        navigatorExtras: Navigator.Extras? = null
    ) {
        navController.navigate(destination, navOptions, navigatorExtras)
    }

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        navController.navigate(topLevelDestination.route, navOptions)
    }
}