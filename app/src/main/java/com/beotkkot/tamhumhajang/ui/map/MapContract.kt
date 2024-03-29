package com.beotkkot.tamhumhajang.ui.map

import androidx.navigation.NavOptions
import com.beotkkot.tamhumhajang.common.UiEffect
import com.beotkkot.tamhumhajang.common.UiEvent
import com.beotkkot.tamhumhajang.common.UiState
import com.beotkkot.tamhumhajang.data.model.Quest
import com.beotkkot.tamhumhajang.data.model.RecommendMarket
import com.beotkkot.tamhumhajang.data.model.response.BadgePopup
import com.beotkkot.tamhumhajang.data.model.response.BadgePosition
import com.beotkkot.tamhumhajang.data.model.response.LevelUpResponse
import com.beotkkot.tamhumhajang.data.model.response.QuizQuestionPopup
import com.beotkkot.tamhumhajang.data.model.response.QuizWarningPopup
import com.beotkkot.tamhumhajang.data.model.response.Shop
import com.beotkkot.tamhumhajang.ui.toast.ToastType
import com.kakao.vectormap.LatLng

class MapContract {

    data class State(
        val isLoading: Boolean = false,

        val userGrade: Int = 1,

        val userPosition: LatLng = DEFAULT_LATLNG,
        val movingCameraPosition: MovingCameraWrapper = MovingCameraWrapper.DEFAULT,

        val showingToast: ToastType? = null,
        val toastName: String = "",
        val toastOnClick: () -> Unit = { },

        val sequence: Int = 0,

        val isFixedPerspective: Boolean = false,

        val shops: List<Shop> = emptyList(),
        val badgePosition: BadgePosition? = null,
        val merchantAssociationPosition: BadgePosition? = null,

        val showFirstBadgePopup: Boolean = false,

        val showBadgePopup: Boolean = false,
        val badgePopup: BadgePopup? = null,

        val showQuizWarningPopup: Boolean = false,
        val quizWarningPopup: QuizWarningPopup? = null,

        val showQuizQuestionPopup: Boolean = false,
        val quizQuestionPopup: QuizQuestionPopup? = null,

        val showConnectionPopup: Boolean = false,

        val showLevelUpPopup: Boolean = false,
        val levelUpPopup: LevelUpResponse? = null,

        val showRewardPopup: Boolean = false,

        val showRecommendMarketPopup: Boolean = false,
        val recommendMarkets: List<RecommendMarket> = emptyList(),

        val showQuestPopup: Boolean = false,
        val questSequence: Int = 1,
        val quests: List<Quest> = emptyList()
    ) : UiState

    sealed class Event : UiEvent {
    }

    sealed class Effect : UiEffect {
        data class NavigateTo(
            val destination: String,
            val navOptions: NavOptions? = null
        ) : Effect()

        data class ShowSnackBar(val message: String) : Effect()

        data class ShowToast(
            val type: ToastType,
            val name: String,
            val onClick: () -> Unit
        ) : Effect()
    }

    companion object {
        val DEFAULT_LATLNG = LatLng.from(37.5437, 127.0659)
    }
}