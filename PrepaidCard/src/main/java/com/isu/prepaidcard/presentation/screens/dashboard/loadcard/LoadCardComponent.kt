package com.isu.prepaidcard.presentation.screens.dashboard.loadcard

import com.isu.common.events.Clickables
import com.isu.common.events.CommonTextField

sealed interface LoadCardButtons : Clickables {
    data object LoadCard : LoadCardButtons
    data object PayApp : LoadCardButtons
    data object GoToHome : LoadCardButtons
    data object AddAgain : LoadCardButtons
    data object TryAgain : LoadCardButtons
}

sealed interface LoadCardTextFields : CommonTextField {
    data object LoadCardAmount : LoadCardTextFields
}