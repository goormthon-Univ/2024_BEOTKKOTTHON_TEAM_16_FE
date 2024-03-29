package com.beotkkot.tamhumhajang.ui.map

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beotkkot.kakaomap_compose.KakaoMap
import com.beotkkot.kakaomap_compose.overlay.Label
import com.beotkkot.kakaomap_compose.settings.DefaultMapViewSettings
import com.beotkkot.kakaomap_compose.settings.MapDirection
import com.beotkkot.kakaomap_compose.settings.MapWidgetPosition
import com.beotkkot.kakaomap_compose.state.rememberCameraPositionState
import com.beotkkot.tamhumhajang.AppState
import com.beotkkot.tamhumhajang.BottomSheetState
import com.beotkkot.tamhumhajang.R
import com.beotkkot.tamhumhajang.data.model.response.BadgePosition
import com.beotkkot.tamhumhajang.design.theme.TamhumhajangTheme
import com.beotkkot.tamhumhajang.ui.BOOKMARK
import com.beotkkot.tamhumhajang.ui.PROFILE
import com.beotkkot.tamhumhajang.ui.bookmark.ShopBottomSheet
import com.beotkkot.tamhumhajang.ui.popup.BadgePopup
import com.beotkkot.tamhumhajang.ui.popup.ConnectionPopup
import com.beotkkot.tamhumhajang.ui.popup.FirstBadgePopup
import com.beotkkot.tamhumhajang.ui.popup.LevelUpPopup
import com.beotkkot.tamhumhajang.ui.popup.QuestListPopup
import com.beotkkot.tamhumhajang.ui.popup.QuestionPopup
import com.beotkkot.tamhumhajang.ui.popup.RecommendMarketPopup
import com.beotkkot.tamhumhajang.ui.popup.TrophyPopup
import com.beotkkot.tamhumhajang.ui.popup.WarningPopup
import com.beotkkot.tamhumhajang.ui.toast.BookmarkToast
import com.beotkkot.tamhumhajang.ui.toast.CheckProfileToast
import com.beotkkot.tamhumhajang.ui.toast.NavigateToast
import com.beotkkot.tamhumhajang.ui.toast.ToastType
import com.beotkkot.tamhumhajang.ui.toast.UseRewardToast
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    appState: AppState,
    bottomSheetState: BottomSheetState,
    viewModel: MapViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val effectFlow = viewModel.effect

    val cameraPositionState = rememberCameraPositionState()

    val context = LocalContext.current

    ManageLocationPermission(
        addLocationListener = viewModel::addLocationListener,
        showSnackbar = appState::showSnackbar,
        removeLocationListener = viewModel::removeLocationListener
    )

    LaunchedEffect(true) {
        effectFlow.collect {
            when (it) {
                is MapContract.Effect.NavigateTo -> {
                    appState.navigate(it.destination)
                }
                is MapContract.Effect.ShowSnackBar -> {
                    appState.showSnackbar(it.message)
                }
                is MapContract.Effect.ShowToast -> {
                    appState.scope.launch {
                        viewModel.updateShowingToast(it.type)
                        viewModel.updateToastName(it.name)
                        viewModel.updateToastOnClick(it.onClick)

                        delay(5000)

                        viewModel.updateShowingToast(null)
                    }
                }
            }
        }
    }

    LaunchedEffect(uiState.movingCameraPosition) {
        when (val movingCameraPosition = uiState.movingCameraPosition) {
            MovingCameraWrapper.DEFAULT -> {
                // Do Nothing
            }

            is MovingCameraWrapper.MOVING -> {
                Log.d("debugging", "위치 이동 : ${movingCameraPosition.location.latitude} ${movingCameraPosition.location.longitude}")

                cameraPositionState.move(
                    CameraUpdateFactory.newCenterPosition(LatLng.from(movingCameraPosition.location.latitude, movingCameraPosition.location.longitude)),
                    CameraAnimation.from(100, true, true)
                )
                viewModel.updateIsFixedPerspective(false)
                viewModel.updateMovingCameraPositionToDefault()
            }
        }
    }

    LaunchedEffect(uiState.badgePosition) {
        while (true) {
            delay(2500)

            viewModel.checkIsNearMarker()
        }
    }

    LaunchedEffect(uiState.isFixedPerspective) {
        while (true) {
            delay(100)

            if (uiState.isFixedPerspective) {
                cameraPositionState.move(
                    CameraUpdateFactory.newCenterPosition(uiState.userPosition),
                    CameraAnimation.from(100, true, true)
                )
            } else {
                break
            }
        }
    }

    // 처음 시작시 뱃지 하나 획득, 초기 상태에서 추천시장 팝업 열을 시 뱃지마커 띄워줌
    if (uiState.showRecommendMarketPopup) {
        RecommendMarketPopup(
            markets = uiState.recommendMarkets,
            onClose = {
                if (uiState.sequence == 1) {
                    viewModel.updateBadgePosition(
                        badgePosition = BadgePosition(
                            uiState.recommendMarkets[1].latitude,
                            uiState.recommendMarkets[1].longitude
                        )
                    )
                    viewModel.showQuests()
                }
                viewModel.updateShowRecommendShopPopup(false)
            },
            onClick = { latitude, longitude ->
                Log.d("debugging", "시장 : ${uiState.recommendMarkets[1]}")
                Log.d("debugging", "뱃지 획득 갯수 : ${uiState.sequence}")

                if (uiState.sequence == 1) {
                    viewModel.updateBadgePosition(
                        badgePosition = BadgePosition(
                            uiState.recommendMarkets[1].latitude,
                            uiState.recommendMarkets[1].longitude
                        )
                    )
                    viewModel.showQuests()
                }
                viewModel.updateShowRecommendShopPopup(false)
                appState.scope.launch {
                    cameraPositionState.move(
                        CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude)),
                        CameraAnimation.from(100, true, true)
                    )

                    if (uiState.sequence == 0) {
                        delay(2000)
                        viewModel.showQuests()
                    }
                }
            }
        )
    }

    // 첫 시작 후 퀘스트 목록 화면 띄운다면 마천 시장 이동 토스트 메시지 출력
    if (uiState.showQuestPopup) {
        QuestListPopup(
            sequence = uiState.questSequence,
            quests = uiState.quests
        ) {
            viewModel.updateShowQuestPopup(false)

            if (uiState.sequence == 1) {
                val market = uiState.recommendMarkets[1]
                val userPosition = uiState.userPosition

                viewModel.showNavigatePopup(
                    "네오네 정육가게"
                ) {
                    navigateToKakaoMap(
                        userPosition.latitude,
                        userPosition.longitude,
                        market.latitude,
                        market.longitude,
                        context
                    )
                }
            }
        }
    }

    if (uiState.showFirstBadgePopup) {
        FirstBadgePopup(
            onClick = {
                viewModel.updateShowFirstBadgePopup(false)
                viewModel.showRecommendMarkets()
            },
            onClose = {
                viewModel.updateShowFirstBadgePopup(false)
                viewModel.showRecommendMarkets()
            }
        )
    }
    
    if (uiState.showBadgePopup) {
        Log.d("debugging", "뱃지 획득 갯수 : ${uiState.sequence}")

        uiState.badgePopup?.let { popUp ->
            BadgePopup(
                popup = popUp,
                onConfirm = {
                    if (uiState.sequence == 2) viewModel.updateShowConnectionPopup(true)

                    // 3의 배수 배지 획득시마다 레벨업 호출
                    if (uiState.sequence % 3 == 0) viewModel.levelUp()

                    viewModel.updateShowBadgePopup(false)
                },
                navigateToProfile = {
                    appState.navigate(PROFILE)
                },
                onClose = {
                    if (uiState.sequence == 2) viewModel.updateShowConnectionPopup(true)

                    // 3의 배수 배지 획득시마다 레벨업 호출
                    if (uiState.sequence % 3 == 0) viewModel.levelUp()

                    viewModel.updateShowBadgePopup(false)
                }
            )
        }
    }

    if (uiState.showLevelUpPopup) {
        uiState.levelUpPopup?.let { popup ->
            LevelUpPopup(
                popup = popup,
                onClick = {
                    viewModel.updateShowLevelUpPopup(false)
                    viewModel.updateShowRewardPopup(true)
                }
            ) {
                viewModel.updateShowLevelUpPopup(false)
                viewModel.updateShowRewardPopup(true)
            }
        }
    }

    if (uiState.showRewardPopup) {
        TrophyPopup {
            viewModel.updateShowRewardPopup(false)
            bottomSheetState.hideBottomSheet()

            uiState.merchantAssociationPosition?.let {
                val userPosition = uiState.userPosition

                viewModel.showNavigatePopup(
                    "시장상인회"
                ) {
                    navigateToKakaoMap(
                        userPosition.latitude,
                        userPosition.longitude,
                        it.latitude,
                        it.longitude,
                        context
                    )
                }
            }
        }
    }

    if (uiState.showQuizWarningPopup) {
        uiState.quizWarningPopup?.let {
            WarningPopup(
                popup = it
            ) {
                viewModel.updateShowQuizWarningPopup(false)
                viewModel.updateShowQuizQuestionPopup(true)
            }
        }
    }


    if (uiState.showQuizQuestionPopup) {
        uiState.quizQuestionPopup?.let {
            QuestionPopup(
                popup = it,
                onChoose = viewModel::verifyAnswer
            )
        }
    }


    if (uiState.showConnectionPopup) {
        ConnectionPopup {
            viewModel.updateShowConnectionPopup(false)

            viewModel.updateShowingToast(ToastType.BOOKMARK)
            viewModel.updateToastOnClick { viewModel.updateShowingToast(null) }

            appState.scope.launch {
                delay(5000)
                viewModel.updateShowingToast(null)
            }
        }
    }

    LaunchedEffect(uiState.badgePosition) {
        while (true) {
            delay(5000)

            viewModel.checkIsNearMarker()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        KakaoMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            mapViewSettings = DefaultMapViewSettings.copy(
                compassPosition = MapWidgetPosition(
                    MapDirection.BottomLeft,
                    50f,
                    50f
                )
            ),
            onMapReady = {
                if (MapViewModel.initialMarkerLoadFlag && uiState.userPosition != MapContract.DEFAULT_LATLNG) {
                    MapViewModel.initialMarkerLoadFlag = false
                    viewModel.updateMovingCameraPosition(
                        MovingCameraWrapper.MOVING(
                            Location("UserPosition").apply {
                                latitude = uiState.userPosition.latitude
                                longitude = uiState.userPosition.longitude
                            }
                        )
                    )
                }
            }
        ) {
            uiState.shops.forEach { shop ->
                Label(
                    position = LatLng.from(shop.latitude, shop.longitude),
                    iconResId = R.drawable.img_shop_location,
                    tag = "가게",
                    onClick = {
                        bottomSheetState.showBottomSheet {
                            ShopBottomSheet(
                                id = shop.id,
                                title = shop.name,
                                isBookmarked = false,
                                address = shop.address,
                                category = shop.category,
                                imgUrl = shop.imgUrl,
                                tags = shop.tags
                            ) {
                                viewModel.postBookmark(shop.id)
                            }
                        }
                    }
                )
            }

            uiState.badgePosition?.let {
                Label(
                    position = LatLng.from(it.latitude, it.longitude),
                    iconResId = R.drawable.img_badge_location,
                    tag = "뱃지"
                )
            }

            uiState.merchantAssociationPosition?.let {
                Label(
                    position = LatLng.from(it.latitude, it.longitude),
                    iconResId = R.drawable.img_merchange_association,
                    tag = "시장 상인회"
                )
            }

            Label(
                position = uiState.userPosition,
                iconResId = if (uiState.userGrade == 1) {
                    R.drawable.img_explorer_level_1
                } else {
                    R.drawable.img_explorer_level_2
                },
                tag = "나"
            ) {

            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BookButton {
                appState.navigate(PROFILE)
            }

            BookmarkButton {
                appState.navigate(BOOKMARK)
            }
        }

        Column(
            modifier = Modifier
                .padding(end = 14.dp, bottom = 28.dp)
                .align(Alignment.BottomEnd),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuestButton {
                viewModel.showQuests()
            }

            ShopButton {
                viewModel.showRecommendMarkets()
            }

            TrackingButton(
                isTracking = uiState.isFixedPerspective
            ) {
                viewModel.updateIsFixedPerspective(!uiState.isFixedPerspective)
            }
        }

        Box(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 10.dp
                )
                .align(Alignment.BottomCenter)
        ) {
            uiState.showingToast?.let {
                when (it) {
                    ToastType.PROFILE -> {
                        CheckProfileToast {
                            appState.navigate(PROFILE)
                        }
                    }
                    ToastType.NAVIGATE -> {
                        NavigateToast(name = uiState.toastName) {
                            uiState.toastOnClick()
                        }
                    }
                    ToastType.REWARD -> {
                        UseRewardToast {
                            uiState.toastOnClick()
                        }
                    }
                    ToastType.BOOKMARK -> {
                        BookmarkToast {
                            uiState.toastOnClick()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BookButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(10.dp),
        color = TamhumhajangTheme.colors.color_ffffff,
        elevation = 10.dp,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_book),
                contentDescription = "IC_BOOK",
                tint = Color.Unspecified
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BookmarkButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(10.dp),
        color = TamhumhajangTheme.colors.color_ffffff,
        elevation = 10.dp,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = "IC_STAR",
                tint = Color.Unspecified
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun QuestButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(10.dp),
        color = TamhumhajangTheme.colors.color_ffffff,
        elevation = 10.dp,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_quest),
                contentDescription = "IC_QUEST",
                tint = Color.Unspecified
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ShopButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(10.dp),
        color = TamhumhajangTheme.colors.color_ffffff,
        elevation = 10.dp,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_shop),
                contentDescription = "IC_SHOP",
                tint = Color.Unspecified
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TrackingButton(
    modifier: Modifier = Modifier,
    isTracking: Boolean = true,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(10.dp),
        color = if (isTracking) TamhumhajangTheme.colors.color_0fa958 else TamhumhajangTheme.colors.color_ffffff,
        elevation = 10.dp,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = if (isTracking) R.drawable.ic_current_location_enabled else  R.drawable.ic_current_location_disabled),
                contentDescription = "IC_CURRENT_LOCATION",
                tint = if (isTracking) Color.White else Color.Unspecified
            )
        }
    }
}

private fun navigateToKakaoMap(
    startlat: Double,
    startlng: Double,
    endlat: Double,
    endlng: Double,
    context: Context
) {
    val url = "kakaomap://route?sp=${startlat}, ${startlng}&ep=${endlat}, ${endlng}&by=PUBLICTRANSIT"

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addCategory(Intent.CATEGORY_BROWSABLE)

    val installCheck = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER),
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
        )
    } else {
        context.packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER),
            PackageManager.GET_META_DATA
        )
    }

    // 네이버맵이 설치되어 있다면 앱으로 연결, 설치되어 있지 않다면 스토어로 이동
    if (installCheck.isEmpty()) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=com.nhn.android.nmap")
            )
        )
    } else {
        context.startActivity(intent)
    }
}