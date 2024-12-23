package com.isu.permission.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.events.Clickables
import com.isu.common.events.CommonScreenEvents
import com.isu.common.navigation.AuthenticationScreens
import com.isu.common.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor():ViewModel() {
    // UI State for Forgot Password Screen




    fun onEvent(event: CommonScreenEvents){
        when(event){

            is CommonScreenEvents.OnClick <*>-> {handleButtonClicked(event.type)}
            else->{}
        }
    }

    private fun handleButtonClicked(type: Clickables) {
        when(type){
            PermissionButtonType.PermissionGranted -> {
                viewModelScope.launch {

                    NavigationEvent.helper.emit(
                        NavigationEvent.NavigateToNextScreen(
                            AuthenticationScreens.EnterMobileNumberDeviceBindingScreen
                        )
                    )
                }
            }
        }
    }
}