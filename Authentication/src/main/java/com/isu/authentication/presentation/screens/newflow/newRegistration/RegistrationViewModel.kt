package com.isu.authentication.presentation.screens.newflow.newRegistration
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.isu.authentication.data.remote.dto.request.AddonCardForChildRequest
import com.isu.authentication.data.remote.dto.request.AuthRequest
import com.isu.authentication.data.remote.dto.request.BulkCustomerGenerateOTP
import com.isu.authentication.data.remote.dto.request.BulkVerifyOTPRequest
import com.isu.authentication.data.remote.dto.request.CustomerInitiateRequest
import com.isu.authentication.data.remote.dto.request.FetchPinCodeDataRequest
import com.isu.authentication.data.remote.dto.request.ProfileDetailsFetchForBulkRequest
import com.isu.authentication.data.remote.dto.request.ResendSignUpOtp
import com.isu.authentication.data.remote.dto.request.SignInRequest
import com.isu.authentication.data.remote.dto.request.StatusCheckRequestModel
import com.isu.authentication.data.remote.dto.request.TwoFARequestModel
import com.isu.authentication.data.remote.dto.request.VerifySignInRequest
import com.isu.authentication.data.remote.dto.response.AuthResponse
import com.isu.authentication.data.remote.dto.response.BulkCustomerGenerateOTPResponseDecryptedData
import com.isu.authentication.data.remote.dto.response.CustomerInitiateResponse
import com.isu.authentication.data.remote.dto.response.StatusCheckResponse
import com.isu.authentication.domain.usecases.AddOnCardForChildApiUseCase
import com.isu.authentication.domain.usecases.AuthTokenUseCase
import com.isu.authentication.domain.usecases.BulkOtpGenerateUseCase
import com.isu.authentication.domain.usecases.BulkVerifyOTPUseCase
import com.isu.authentication.domain.usecases.CustomerInitiateApiUseCase
import com.isu.authentication.domain.usecases.FetchDeviceChangeOTPUsecase
import com.isu.authentication.domain.usecases.FetchPinCodeUseCase
import com.isu.authentication.domain.usecases.ProfileDetailsFetchForBulkUseCase
import com.isu.authentication.domain.usecases.ResendSelfSignUpUseCase
import com.isu.authentication.domain.usecases.SignInUseCase
import com.isu.authentication.domain.usecases.TwoFAUseCase
import com.isu.authentication.domain.usecases.VerifyDeviceChangeOTPUsecae
import com.isu.authentication.domain.usecases.VerifySelfSignInUseCase
import com.isu.authentication.domain.usecases.VerifyTwoFAOTPUseCase
import com.isu.authentication.domain.usecases.statuscheck_api_usecase.StatusCheckImpl
import com.isu.authentication.presentation.screens.newflow.kyc.OVDTypes
import com.isu.common.R
import com.isu.common.events.APIType
import com.isu.common.events.Clickables
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.CommonTextField
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.models.DataForCardSDK
import com.isu.common.navigation.AuthenticationScreens
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.utils.GlobalVariables
import com.isu.common.utils.Logger
import com.isu.common.utils.UiText
import com.isu.common.utils.Validation
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferenceData
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.common.utils.handleFlow
import com.isu.common.utils.mapFun
import com.isu.common.utils.mapFunCBC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import kotlin.math.log
import kotlin.random.Random


@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val fetchPinCodeUseCase: FetchPinCodeUseCase,
    private val signOnUseCase: SignInUseCase,
    private val verifySelfSignInUseCase: VerifySelfSignInUseCase,
    private val resendOtpUseCase: ResendSelfSignUpUseCase,
    private val statusCheckApi: StatusCheckImpl,
    private val dataStoreManager: DataStoreManager,
    private val verifyOtpUseCase: VerifyTwoFAOTPUseCase,
    private val generateTwoFAOtp: TwoFAUseCase,
    private val deviceChangeOTPUsecase: FetchDeviceChangeOTPUsecase,
    private val verifyDeviceChangeOTPUsecae: VerifyDeviceChangeOTPUsecae,
    private val bulkOtpGenerateUseCase: BulkOtpGenerateUseCase,
    private val bulkVerifyOTPUseCase: BulkVerifyOTPUseCase,
    private val addOnCardForChildApiUseCase: AddOnCardForChildApiUseCase,
    private val authTokenUseCase: AuthTokenUseCase,
    private val customerInitiateApiUseCase: CustomerInitiateApiUseCase,
    private val profileDetaisFetchForBulk:ProfileDetailsFetchForBulkUseCase,
) : ViewModel() {
    fun kycClearField() {
        viewModelScope.launch {
            _registrationState.update {
                it.copy(
                    onBoardingOTP = "",
                    onBoardingOTPError = false,
                )
            }
        }
    }
    val dataStoreInstance=dataStoreManager

    val customerIsBulkOnBoarded = mutableStateOf(false)
    private val _registrationState = MutableStateFlow(RegistrationState())
    val registrationState = _registrationState.asStateFlow()
    private val otpRefID = mutableStateOf("")
    val state = mutableStateOf("")
    val city = mutableStateOf("")
    private val twoFAParams = mutableStateOf("")
    private val cardReferedBySomeOne = mutableStateOf(false)
    private val sendToSDKFunction= mutableStateOf<(()->Unit)?>(null)

    fun profileDetailsFetchForBulk(onSuccess: () -> Unit){
        viewModelScope.launch {
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val mobilenumber=dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val request=ProfileDetailsFetchForBulkRequest(
                deviceId = deviceId,
                latLong = "10.0,940.0",
                mobileNo = mobilenumber
            )
            handleFlow(apiCall = {
                profileDetaisFetchForBulk.invoke(request)
            }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {resp->
                Log.d("BULK", "profileDetailsFetchForBulk: ${resp}")
                if(resp?.statusCode==0){
                    viewModelScope.launch {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.SuccessSnackBar,
                                UiText.DynamicString(resp?.statusDesc ?: "Success")
                            )
                        )
                        _registrationState.update {
                            it.copy(
                               lastName =  resp?.userDetails?.lastName?:"",
                                name = resp?.userDetails?.firstName?:"",
                                phoneNumber = resp?.userDetails?.mobileNumber?:"",
                                email = resp?.userDetails?.email?:"",
                            )
                        }
                        onSuccess()

                    }
                }

            }
            )
        }
    }

    fun acceptAddOnCardForChild(isApproved: Boolean) {

        viewModelScope.launch {
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val request = AddonCardForChildRequest(
                deviceId = deviceId, isApproved = isApproved, latLong = latLong

            )
            handleFlow(apiCall = {
                addOnCardForChildApiUseCase.invoke(request)
            }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {
                viewModelScope.launch {
                    ShowSnackBarEvent.helper.emit(
                        ShowSnackBarEvent.show(
                            SnackBarType.SuccessSnackBar,
                            UiText.DynamicString(it?.statusDesc ?: "Success")
                        )
                    )
                    NavigationEvent.helper.navigateTo(ProfileScreen.DashBoardScreen)
                }
            }

            )
        }

    }

    fun customerOnBoardingAPI(  onSuccess: (CustomerInitiateResponse?) -> Unit){
        viewModelScope.launch {
            val deviceId=dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val mobileNumber=dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val clientId=dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_ID)
            val clientSecret=dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_SECRET)
            val filledData=registrationState.value
            val request= CustomerInitiateRequest(

                city = city.value,
                country = "INDIA",
                createdBy = "PROG9999879898",
                dateOfBirth =filledData.dateOfBirth,
                deviceId = deviceId,
                email = filledData.email,
                firstName = filledData.name,
                gender = filledData.gender,
                kyctype = "MIN-KYC",
                lastName = filledData.lastName,
                latLong = "0,0",
                mobileNumber = mobileNumber,
                ovdId = filledData.ovdNumber,
                ovdType = filledData.ovdType.type,
                pinCode = filledData.pinCode,
                referalCode = filledData.referralCode.ifEmpty{null},
                state = state.value
            )
            handleFlow(
                apiCall = {
                    customerInitiateApiUseCase.invoke(request, clientSecret = clientSecret.toString(), clientId = clientId.toString())
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.Main,
                onSuccesss = {
                    Log.d("SDK", "customerOnBoardingAPI:$it ")
                  onSuccess(it)
                },
                onFailure = {_,msg,_->
                    try {
                        val errJsonObj = JSONObject(msg)
                        Log.d("ERR", "callStatusCheckApi: ${errJsonObj}")
                        val data=errJsonObj.getString("ResponseData")
                        Log.d("ERR", "callStatusCheckApi: ${data}")
                        viewModelScope.launch {
                            mapFunCBC<CustomerInitiateResponse>(data).collectLatest {
                                val dataToCheck=it?.statusDesc
                                Log.d("ERR", "callStatusCheckApi: ${dataToCheck}")

                                    LoadingErrorEvent.helper.emit(LoadingErrorEvent.errorEncountered(UiText.DynamicString(dataToCheck?:"Something went wrong")))

                            }
                        }





                    } catch (e: Exception) {
                        e.printStackTrace()
                        viewModelScope.launch {
                           LoadingErrorEvent.helper.emit(LoadingErrorEvent.errorEncountered(UiText.DynamicString(e.message?:"")))
                        }
                    }

                }

            )
        }

    }

    fun onEvent(event: CommonScreenEvents) {
        when (event) {
            is CommonScreenEvents.CallApi<*> -> {
                Log.d("API_CALL", "onEvent: ")
                handleApiCall(event.apiType,data=event.additionalInfo)
            }
            is CommonScreenEvents.ClearFields -> {
                viewModelScope.launch {
                    _registrationState.update {
                        it.copy(
                            onBoardingOTP = "",
                            onBoardingOTPError = false,
                            onBoardingOTPErrorMessage = UiText.StringResource(R.string.empty),
                            twoFAOTP = "",
                            twoFAOTPError = false,
                            twoFAOTPErrorMessage = UiText.StringResource(R.string.empty),
                            deviceChangeOTP = ""
                        )
                    }
                }
            }

            is CommonScreenEvents.ClearStack -> {}
            is CommonScreenEvents.NavigateTo -> {
                viewModelScope.launch {
                    NavigationEvent.helper.emit(NavigationEvent.NavigateToNextScreen(event.screen))
                }
            }

            is CommonScreenEvents.OnCheckChanged -> {}
            is CommonScreenEvents.OnClick<*> -> {
                handleRegistrationButtonClicks(type = event.type)
            }

            is CommonScreenEvents.OnTextChanged -> {
                handleRegistrationInputFieldChange(type = event.type, value = event.text)
            }

            is CommonScreenEvents.SaveToDataStore<*> -> {
                saveToDataStore(event.preferenceData)
            }

            is CommonScreenEvents.GetDataStoreData<*> -> {}
            else -> {}
        }
    }

    private fun handleApiCall(apiType: APIType, data: Any?) {
        val deviceId=data as String
        when(apiType){

            RegistrationAPIType.INITIATE_API->{


            }

            RegistrationAPIType.STATUS_CHECK->{

                Log.d("API_CALL", "onEvent: ")
                callStatusCheckApi(){
                    /**
                     * check if customer is onboard
                     * via bulk or normal onboarding
                     */
                    if(customerIsBulkOnBoarded(it)){
                        Log.d("BULK", "handleApiCall: ")
                        customerIsBulkOnBoarded.value=true
                        if(needOtpVerification(it)){
                            /**
                             * user data has been updated only
                             * verification  is required
                             * call resend otp and pass to otp verification screen
                             */
                            resendOtp(onSuccess = {
                                Log.d("BULK", "handleApiCall: ${it}")
                                viewModelScope.launch {
                                    NavigationEvent.helper.navigateTo(AuthenticationScreens.PhoneVerificationScreen)
                                }

                            })
                        }
                        else{
                            /**
                             * both user data fill and
                             * verification  is required
                             * fetch profile details
                             * navigate to personal detail screen
                             */
                            profileDetailsFetchForBulk {
                                Log.d("BULK", "handleApiCall: $it")
                                viewModelScope.launch {
                                    ShowSnackBarEvent.helper.emit(ShowSnackBarEvent.show(SnackBarType.SuccessSnackBar,UiText.DynamicString("Details fetched")))
                                }
                            }


                        }
                    }
                    else{
                        Log.d("RESEND", "handleApiCall: ")
                        if(needOtpVerification(it)){
                            Log.d("RESEND", "handleApiCall: ")
                            resendOtp(onSuccess = {
                                viewModelScope.launch {
                                    NavigationEvent.helper.navigateTo(AuthenticationScreens.PhoneVerificationScreen)
                                }

                            })

                        }else {
//                            viewModelScope.launch {
//                                ShowSnackBarEvent.helper.emit(ShowSnackBarEvent.show(SnackBarType.ErrorSnackBar,UiText.DynamicString("Invalid response")))
//                            }
                        }
                    }

                }
            }
        }
    }

    private fun customerIsBulkOnBoarded(it: StatusCheckResponse): Boolean {
        val dataToCheck=it.userDetails
        return  dataToCheck?.status=="SUCCESS" && dataToCheck.statusCode==3
    }

    private fun needOtpVerification(statResponse: StatusCheckResponse): Boolean {
        val dataToCheck=statResponse.userDetails
        return (dataToCheck?.status=="PENDING" &&(dataToCheck.statusCode==2||dataToCheck.statusCode==-2))||(dataToCheck?.status=="FAILED" &&(dataToCheck?.statusCode==1||dataToCheck?.statusCode==2))
    }

    private fun saveToDataStore(preferenceData: PreferenceData<out Any?>) {
        viewModelScope.launch {
            dataStoreManager.savePreferences(listOf(preferenceData))
        }
    }

    fun resendOtp(onSuccess: () -> Unit = {}) {

        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val number = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            Log.d(
                "OTP_REQUEST",
                "resendOtp: dataStore:$number ${registrationState.value.phoneNumber}"
            )
            otpRefID.value="487987"
            val otpRefIdValue = try {
                otpRefID.value
            } catch (e: Exception) {
                "172668"
            }
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val request = ResendSignUpOtp(
                deviceId = deviceId,
                latLong = latLong,
                mobileNumber = registrationState.value.phoneNumber.ifEmpty { number },
                otpRefId = otpRefIdValue

            )
            handleFlow(apiCall = {
                resendOtpUseCase.invoke(request)
            }, scope = viewModelScope, dispatcher = Dispatchers.IO,
                onSuccesss = {
                    Log.d("RESEND", "resendOtp: ${it}")
                otpRefID.value = it?.data?.otpRefID.toString()

                viewModelScope.launch {
                    _registrationState.update {
                        it.copy(startTimer = !it.startTimer)
                    }
                    ShowSnackBarEvent.helper.emit(
                        ShowSnackBarEvent.show(
                            SnackBarType.SuccessSnackBar,
                            UiText.DynamicString(it?.statusDesc ?: "Success")
                        )
                    )
                    onSuccess()
                }
            }

            )
        }

    }

    private fun callGenerateTwoFA(onSucess: () -> Unit = {}) {
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val phoneNumber =
                dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val params = "TWOFA${Random.nextInt()}"
            val request = TwoFARequestModel(
                deviceId = deviceId,
                expiryTime = "5",
                latLong = LatLongFlowProvider.latLongFlow.first(),
                mobileNumber = if (registrationState.value.phoneNumber.isEmpty()) phoneNumber else registrationState.value.phoneNumber,
                paramA = "",
                paramB = "",
                paramC = "",
                params = params
            )
            handleFlow(apiCall = {
                generateTwoFAOtp.invoke(request)
            }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {
                viewModelScope.launch {
                    ShowSnackBarEvent.helper.emit(
                        ShowSnackBarEvent.show(
                            SnackBarType.SuccessSnackBar,
                            UiText.DynamicString(it?.statusDesc ?: "Success")
                        )
                    )
                }
                twoFAParams.value = params
                onSucess()
            }

            )
        }

    }

    private fun handleRegistrationButtonClicks(type: Clickables) {
        when (type) {
            RegistrationButtonType.ResendOTP -> {
                resendOtp()
            }

            RegistrationButtonType.RegistrationButton -> {
                if (allMandatoryFieldsAreValid()) {
                    //handle navigation
                    viewModelScope.launch {

                        NavigationEvent.helper.emit(
                            NavigationEvent.NavigateToNextScreen(
                                AuthenticationScreens.KycScreen
                            )
                        )
                    }
                }
            }

            is RegistrationButtonType.KycOtpGenerationButton -> {

                if (allKycScreenValid()) {
                    Logger.d("allKycScreenValid")
                    customerOnBoardingAPI(onSuccess = {
                            otpRefID.value=it?.data?.otpRefId?:"048984"
                            Logger.d("customerOnBoardingAPISuccess")
                        /**
                             * here statusCheck is called
                             * to check if customer is directly onBoarded i.e. SUCCESS 0 Scenario
                             * or OTP verification is required i.e.
                             * else
                             */
                            callStatusCheckApi {
                                Logger.d("callStatusCheckApiSuccess")

                                /**
                                 * if customer is directly onBoarded
                                 * i.e SUCCESS 0
                                 * call fetch auth token and
                                 * send to card SDK
                                 */
                                if(customerIsAlreadyOnboarded(it)){
                                    Logger.d("customerIsAlreadyOnboarded = true")
                                    fetchAuthToken(onSuccess = {
                                        viewModelScope.launch {
                                            Logger.d("fetchAuthToken = Success")


                                            val clientId =
                                                dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_ID)
                                            val clientSecret =
                                                dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_SECRET)

                                            val userMobileNumber=dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
                                            sendToSDK(
                                                type.context,
                                                type.launcher,
                                                DataForCardSDK(
                                                    jwtToken = it?.accessToken,
                                                    clientID = clientId,
                                                    clientSecret = clientSecret.toString(),
                                                    deviceChanged = false,
                                                    userMobileNumber = userMobileNumber.toString()
                                                ),
                                            )
                                        }
                                    }, onFailure = {
                                        Logger.d("fetchAuthToken = failure")
                                        if(it.lowercase().contains("device id not".toRegex())){
                                            viewModelScope.launch {
                                                val clientId =
                                                    dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_ID)
                                                val clientSecret =
                                                    dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_SECRET)
                                                Logger.d("fetchAuthToken = device id not")

                                                val userMobileNumber=dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
                                                sendToSDK(type.context,type.launcher,DataForCardSDK(clientID = clientId, clientSecret = clientSecret.toString(), userMobileNumber = userMobileNumber.toString(), deviceChanged = true))
                                            }

                                        }
                                    })

                                }else {
                                    /**
                                     * otp will be generated
                                     * so send to otp verification
                                     * page
                                     */
                                    if(needOtpVerification(it)){
                                        Logger.d("needOtpVerification = true")
                                        viewModelScope.launch{
                                            Logger.d("PhoneVerificationScreen = navigate")

                                            NavigationEvent.helper.navigateTo(AuthenticationScreens.PhoneVerificationScreen)
                                        }
                                    }else{
                                        viewModelScope.launch{
                                            Logger.d("needOtpVerification = false")

                                            ShowSnackBarEvent.helper.emit(ShowSnackBarEvent.show(SnackBarType.ErrorSnackBar,UiText.DynamicString(
                                                it.userDetails?.statusDesc?:"Invalid Response"
                                            )))
                                        }
                                    }



                                }
                            }
                        })

                }
            }

           is RegistrationButtonType.OtpVerificationButton -> {
                if (otpFieldValid()) {
                    Log.d("OTP_VERIFY", "handleRegistrationButtonClicks: ")

                        callVerifySelfSignInAPi(onSuccess = {
                            callStatusCheckApi {
                                Log.d("LAT", "handleRegistrationButtonClicks:onoarded ")
                              /**
                               * if success go to
                               * Card SDK
                               */
                              if(customerIsAlreadyOnboarded(it)){
                                  Log.d("LAT", "handleRegistrationButtonClicks:onoarded ")

                                  fetchAuthToken(onSuccess = {
                                      Log.d("LAT", "handleRegistrationButtonClicks:onoarded ")
                                      viewModelScope.launch {
                                          val clientId =
                                              dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_ID)
                                          val clientSecret =
                                              dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_SECRET)

                                          val userMobileNumber = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
                                          Log.d("DATA_SDK", "handleRegistrationButtonClicks: $clientId ${clientSecret} ${userMobileNumber}")
                                          sendToSDK(
                                              type.context,
                                              type.launcher,
                                              DataForCardSDK(
                                                  jwtToken = it?.accessToken,
                                                  clientID = clientId,
                                                  clientSecret = clientSecret.toString(),
                                                  deviceChanged = false,
                                                  userMobileNumber = userMobileNumber.toString()
                                              ),
                                          )
                                      }
                                  }, onFailure = {
                                      Log.d("FAIL_AUTH", "handleRegistrationButtonClicks: ${it}")
                                                if(it.lowercase().contains("device id not".toRegex())){
                                                    viewModelScope.launch {
                                                        val clientId =
                                                            dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_ID)
                                                        val clientSecret =
                                                            dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_SECRET)

                                                        val userMobileNumber=dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
                                                        sendToSDK(type.context,type.launcher,DataForCardSDK(clientID = clientId, clientSecret = clientSecret.toString(), userMobileNumber = userMobileNumber.toString(), deviceChanged = true))
                                                    }

                                                }
                                  })
                              }
                          }

                        },
                            onFailure = {
                            callStatusCheckApi {
                                /**
                                 * if success go to
                                 * Card SDK
                                 */
                                if(customerIsAlreadyOnboarded(it)){

                                    fetchAuthToken(onSuccess = {
                                        viewModelScope.launch {
                                            val clientId =
                                                dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_ID)
                                            val clientSecret =
                                                dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_SECRET)

                                            val userMobileNumber=dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
                                            Log.d("DATA_SDK", "handleRegistrationButtonClicks: ${clientId} ${clientSecret} ${userMobileNumber}")
                                            sendToSDK(
                                                type.context,
                                                type.launcher,
                                                DataForCardSDK(
                                                    jwtToken = it?.accessToken,
                                                    clientID = clientId,
                                                    clientSecret = clientSecret.toString(),
                                                    deviceChanged = false,
                                                    userMobileNumber = userMobileNumber.toString()
                                                ),
                                            )
                                        }
                                    }, onFailure = {

                                    })
                                }
                            }
                        })



                }


            }

  /*          RegistrationButtonType.ResendTwoFAOTP -> {

                callGenerateTwoFA(onSucess = {
                    viewModelScope.launch {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.SuccessSnackBar,
                                UiText.DynamicString("OTP sent successfully")
                            )
                        )
                    }

                })
            }

            RegistrationButtonType.TwoFAOtpVerificationButton -> {
                if (validTwoFAOTP()) {
                    if (customerIsBulkOnBoarded.value) {
                        callBulkOnBoardVerifyOtp(onSuccess = {
                            callStatusCheckApi {
                                val code =
                                    it.data?.getOrNull(0)?.cardDetails?.getOrNull(0)?.card?.getOrNull(
                                        0
                                    )?.statusCode
                                val status =
                                    it.data?.getOrNull(0)?.cardDetails?.getOrNull(0)?.card?.getOrNull(
                                        0
                                    )?.status
                                if (code == 0 && status == "SUCCESS") {
                                    viewModelScope.launch {
                                        NavigationEvent.helper.navigateTo(ProfileScreen.DashBoardScreen)
                                    }
                                } else {
                                    viewModelScope.launch {
                                        ShowSnackBarEvent.helper.emit(
                                            ShowSnackBarEvent.show(
                                                SnackBarType.ErrorSnackBar,
                                                UiText.DynamicString("$status  $code")
                                            )
                                        )
                                    }
                                }
                            }
                        }, onFailure = {})
                    } else {
                        verifyTwoFA(onSuccess = {
                            fetctAuthToken(onSuccess = {
                                if (cardReferedBySomeOne.value) {
                                    viewModelScope.launch {
                                        NavigationEvent.helper.navigateTo(
                                            AuthenticationScreens.NewCardAddedBySomeOneScreen,
                                            SplashScreens.LoadingScreen
                                        )
                                    }
                                } else {
                                    viewModelScope.launch {

                                        dataStoreManager.savePreferences(
                                            preferencesList = listOf(
                                                PreferenceData(
                                                    PreferencesKeys.USER_MOBILE_NUMBER,
                                                    registrationState.value.phoneNumber
                                                )
                                            )
                                        )
                                        NavigationEvent.helper.emit(
                                            NavigationEvent.NavigateToNextScreen(
                                                ProfileScreen.DashBoardScreen
                                            )
                                        )

                                    }
                                    viewModelScope.launch {
                                        ShowBottomBarEvent.show()
                                    }
                                }

                            }, onFailure = {
                                viewModelScope.launch {
                                    fetchDevicdChangeOtp(onSuccess = {
                                        viewModelScope.launch {

                                            NavigationEvent.helper.navigateTo(AuthenticationScreens.DeviceChangeScreen)
                                        }
                                    }) {

                                    }

                                }
                            })


                        })
                    }
                }

            }*/

/*            RegistrationButtonType.ChangeDeviceBinding -> {
                verifyDevicdChangeOtp(onSuccess = {
                    fetctAuthToken(onSuccess = {
                        callStatusCheckApi {
                            viewModelScope.launch {
                                dataStoreManager.savePreferences(
                                    listOf(
                                        PreferenceData(
                                            PreferencesKeys.USER_MOBILE_NUMBER,
                                            registrationState.value.phoneNumber
                                        )
                                    )
                                )
                                NavigationEvent.helper.navigateTo(
                                    ProfileScreen.DashBoardScreen,
                                    SplashScreens.LoadingScreen
                                )
                            }
                            if (customerIsBulkOnBoarded.value) {
                                customerIsBulkOnBoarded.value = true
                                viewModelScope.launch {
                                    Log.d("CHECK", "handleRegistrationButtonClicks: 845")
                                    NavigationEvent.helper.navigateTo(AuthenticationScreens.PersonalDetailsScreen)
                                }
                            } else {
                                if (it.data?.get(0)?.cardDetails?.get(0)?.card?.get(0)?.requestChildCardStatus == true) {
                                    viewModelScope.launch {
                                        NavigationEvent.helper.navigateTo(
                                            AuthenticationScreens.NewCardAddedBySomeOneScreen,
                                            SplashScreens.LoadingScreen
                                        )
                                    }
                                } else {
                                    viewModelScope.launch {
                                        dataStoreManager.savePreferences(
                                            listOf(
                                                PreferenceData(
                                                    PreferencesKeys.USER_MOBILE_NUMBER,
                                                    registrationState.value.phoneNumber
                                                )
                                            )
                                        )
                                        NavigationEvent.helper.navigateTo(
                                            ProfileScreen.DashBoardScreen,
                                            SplashScreens.LoadingScreen
                                        )
                                    }
                                }
                            }

                        }
                    }, onFailure = {
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(
                                        "Something went wrong.\n$it"
                                    )
                                )
                            )
                        }

                    })

                }, onFailure = {

                })
            }

            RegistrationButtonType.ResendChangeDeviceBindingOTp -> {
                fetchDevicdChangeOtp(onSuccess = {
                    viewModelScope.launch {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.ErrorSnackBar,
                                UiText.DynamicString("OTP sent successfully")
                            )
                        )
                    }
                }, onFailure = {
                    viewModelScope.launch {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.ErrorSnackBar,
                                UiText.DynamicString("Something went wrong")
                            )
                        )
                    }
                })
            }*/
        }
    }

    private fun sendToSDK(context: Context,launcherForCardSDK: ManagedActivityResultLauncher<Intent, ActivityResult>, data: DataForCardSDK) {

            val packageManager: PackageManager = context.packageManager
            val jsonStringData= Gson().toJson(data)
            Logger.d("Inside sendToSdk")
            val intent = packageManager.getLaunchIntentForPackage("com.isu.cardissuance")?.apply {
                // Add custom data to the intent
                putExtra("data", jsonStringData)
            }
            intent?.addCategory(Intent.CATEGORY_LAUNCHER)
            if(intent!=null){
                Logger.d("Intent is not null")

                launcherForCardSDK.launch(intent)
            }else{
                Toast.makeText(context, "Intent was null", Toast.LENGTH_SHORT).show()
            }


    }

    private fun customerIsAlreadyOnboarded(it: StatusCheckResponse): Boolean {
        val dataToCheck=it.userDetails
        Log.d("OTP_VERIFY", "handleRegistrationButtonClicks: inside")
        return  dataToCheck?.status=="SUCCESS" && dataToCheck?.statusCode==0
    }

    private fun callBulkOnBoardVerifyOtp(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val latLong = LatLongFlowProvider.latLongFlow.first()

            val data = BulkCustomerGenerateOTP.Data(
                address = registrationState.value.pincodeData,
                address1 = registrationState.value.pincodeData,
                city = registrationState.value.pincodeData,
                country = "INDIA",
                dateOfBirth = registrationState.value.dateOfBirth,
                gender = registrationState.value.gender,
                kycNumber = registrationState.value.ovdNumber,
                kycType = registrationState.value.ovdType.type,
                pincode = registrationState.value.pinCode,
                state = registrationState.value.pincodeData
            )
            val request = BulkVerifyOTPRequest(


                latLong = latLong,
                mobileno = if (registrationState.value.phoneNumber.isEmpty()) userName?.toLong() else registrationState.value.phoneNumber.toLong(),
                otp = registrationState.value.twoFAOTP,
                otpRefId = "BULK",
                deviceId = deviceId
            )
            handleFlow(
                apiCall = {
                    bulkVerifyOTPUseCase.invoke(request = request)
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {
                    onSuccess()
                },

                )
        }
    }

    private fun callBulkOnBoardGenerateOTPCustomer(
        onSuccess: (BulkCustomerGenerateOTPResponseDecryptedData?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val latLong = LatLongFlowProvider.latLongFlow.first()

            val data = BulkCustomerGenerateOTP.Data(
                address = registrationState.value.pincodeData,
                address1 = registrationState.value.pincodeData,
                city = city.value,
                country = "INDIA",
                dateOfBirth = registrationState.value.dateOfBirth,
                gender = registrationState.value.gender,
                kycNumber = registrationState.value.ovdNumber,
                kycType = registrationState.value.ovdType.type,
                pincode = registrationState.value.pinCode,
                state = state.value
            )
            val request = BulkCustomerGenerateOTP(

                data = data, latLong = latLong, deviceId = deviceId
            )
            handleFlow(
                apiCall = {
                    bulkOtpGenerateUseCase.invoke(request = request)
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {
                    customerIsBulkOnBoarded.value = true
                    viewModelScope.launch {
                        val decryptedData = mapFun<BulkCustomerGenerateOTPResponseDecryptedData>(
                            it?.data ?: EncryptedData()
                        ).first()
                        onSuccess(decryptedData)
                    }

                },

                )
        }
    }



    fun fetchAuthToken(onSuccess: (AuthResponse?) -> Unit, onFailure: (String) -> Unit) {

        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)


            val request = AuthRequest(
                deviceId = deviceId,
                userName = if (userName.isNullOrEmpty()) "CUST${registrationState.value.phoneNumber}" else "CUST" + userName

            )
            handleFlow(apiCall = {
                authTokenUseCase.invoke(request = request)
            }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {
                if (it?.is2FAEnabled != null) {
                    onSuccess(it)
                    viewModelScope.launch {
                        GlobalVariables.AUTH_TOKEN = it.accessToken ?: ""
                        dataStoreManager.savePreferences(
                            listOf(
                                PreferenceData(
                                    PreferencesKeys.AUTH_TOKEN, it.accessToken ?: ""
                                )
                            )
                        )
                        dataStoreManager.savePreferences(
                            listOf(
                                PreferenceData(
                                    PreferencesKeys.IS_CHILD_CUSTOMER,
                                    it.parentUserName?.lowercase()?.contains("cust".toRegex())
                                        ?: false
                                )
                            )
                        )
                    }
                } else {
                    onFailure("Not authorized")
                }
            }, onFailure = { _, msg, _ ->
                Log.d("FAIL_AUTH", "fetchAuthToken: ${msg}")
                onFailure(msg)
            })
        }
    }

/*    fun fetchDevicdChangeOtp(onSuccess: () -> Unit, onFailure: () -> Unit) {

        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)


            val request = DeviceChangeOTPRequest(
                userName = "CUST${registrationState.value.phoneNumber.ifEmpty { userName }}"

            )
            handleFlow(apiCall = {
                deviceChangeOTPUsecase.invoke(request = request)
            }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {
                viewModelScope.launch {
                    dataStoreManager.savePreferences(
                        listOf(
                            PreferenceData(
                                PreferencesKeys.USER_MOBILE_NUMBER,
                                registrationState.value.phoneNumber
                            )
                        )
                    )
                }
                if (it?.statusCode == 0) {
                    onSuccess()
                } else {
                    onFailure()
                    viewModelScope.launch {
                        LoadingErrorEvent.helper.emit(
                            LoadingErrorEvent.errorEncountered(
                                UiText.DynamicString(
                                    it?.message ?: "Something went wrong"
                                )
                            )
                        )
                    }

                }
            })
        }
    }

    fun verifyDevicdChangeOtp(onSuccess: () -> Unit, onFailure: () -> Unit) {

        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)


            val request = VerifyDeviceChangeOTPRequest(

                userName = if (userName.isNullOrEmpty()) "CUST${registrationState.value.phoneNumber}" else "CUST" + userName,
                deviceId = deviceId,
                otp = registrationState.value.deviceChangeOTP

            )
            handleFlow(apiCall = {
                verifyDeviceChangeOTPUsecae.invoke(request = request)
            }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {
                if (it?.statusCode == 0) {
                    onSuccess()
                } else {
                    onFailure()
                }
            })
        }
    }

    private fun verifyTwoFA(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val request = VerifyTwoFAOTPRequest(
                deviceId = deviceId,
                latLong = LatLongFlowProvider.latLongFlow.first(),
                mobileNumber = registrationState.value.phoneNumber,
                otp = registrationState.value.twoFAOTP,
                params = twoFAParams.value,
            )
            handleFlow(apiCall = {
                verifyOtpUseCase.invoke(request)
            }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {
                if (it?.statusCode == 0) {

                    onSuccess()
                }
            }

            )
        }
    }*/

    fun callStatusCheckApi(onFailure: ((StatusCheckResponse?) -> Unit)?=null,onSuccess: (StatusCheckResponse) -> Unit) {
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val userNumber = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val statusCheckRequest = StatusCheckRequestModel(
                deviceId = deviceId,
                latLong = latLong,
                mobileNumber = registrationState.value.phoneNumber.ifEmpty { userNumber }
            )
            handleFlow(
                apiCall = {
                    statusCheckApi.invoke(request = statusCheckRequest)
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {


                    if (it?.statusCode == 0) {
                        onSuccess(it)



                    } else {
                        viewModelScope.launch {
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar, UiText.DynamicString(
                                        it?.statusDesc ?: "User not found" + ".Try registering"
                                    )
                                )
                            )

                        }
                    }

                },
                onFailure = { _, msg, _->

                    try {
                        val errJsonObj = JSONObject(msg)
                        Log.d("ERR", "callStatusCheckApi: ${errJsonObj}")
                        val data=errJsonObj.getString("ResponseData")
                        Log.d("ERR", "callStatusCheckApi: ${data}")
                        viewModelScope.launch {
                            mapFunCBC<StatusCheckResponse>(data).collectLatest {
                                val dataToCheck=it?.statusDesc
                                Log.d("ERR", "callStatusCheckApi: ${dataToCheck}")
                                if(onFailure!=null){
                                    onFailure(it)
                                }else{
//                                    LoadingErrorEvent.helper.emit(LoadingErrorEvent.errorEncountered(UiText.DynamicString(dataToCheck?:"Something went wrong")))
                                }




                            }
                        }





                    } catch (e: Exception) {
                        e.printStackTrace()
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(LoadingErrorEvent.errorEncountered(UiText.DynamicString(e.message?:"")))
                        }
                    }

                }
            )
        }


    }

    private fun otpFieldValid(): Boolean {
        if (registrationState.value.onBoardingOTP.isEmpty()) {
            _registrationState.update {
                it.copy(
                    onBoardingOTPError = true,
                    onBoardingOTPErrorMessage = UiText.StringResource(R.string.empty_field)
                )
            }
        }
        return !registrationState.value.onBoardingOTPError && registrationState.value.onBoardingOTP.isNotEmpty()
    }


    private fun callVerifySelfSignInAPi(onSuccess: () -> Unit,onFailure: (String) -> Unit={}) {
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val data = registrationState.value
            val phoneNumber =
                dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val request = VerifySignInRequest(
                deviceId = deviceId,
                latLong = LatLongFlowProvider.latLongFlow.first(),
                mobileNo = data.phoneNumber.ifEmpty { phoneNumber },
                otp = data.onBoardingOTP,
                otpRefId = otpRefID.value

            )
            handleFlow(apiCall = {
                verifySelfSignInUseCase.invoke(request)
            }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {

                viewModelScope.launch {
                    dataStoreManager.savePreferences(
                        listOf(
                            PreferenceData(
                                PreferencesKeys.USER_MOBILE_NUMBER,
                                data.phoneNumber.ifEmpty { phoneNumber }.toString()
                            )
                        )
                    )
                    ShowSnackBarEvent.helper.emit(
                        ShowSnackBarEvent.show(
                            SnackBarType.SuccessSnackBar,
                            UiText.DynamicString(it?.statusDesc ?: "Success")
                        )
                    )
                }
                onSuccess()
            }
                , onFailure = {_,msg,_->
                    try {
                        val errJsonObj = JSONObject(msg)
                        Log.d("ERR", "callStatusCheckApi: ${errJsonObj}")
                        val data=errJsonObj.getString("ResponseData")
                        Log.d("ERR", "callStatusCheckApi: ${data}")
                        viewModelScope.launch {
                            mapFunCBC<StatusCheckResponse>(data).collectLatest {
                                val dataToCheck=it?.statusDesc
                                Log.d("ERR", "callStatusCheckApi: ${dataToCheck}")

                                    LoadingErrorEvent.helper.emit(LoadingErrorEvent.errorEncountered(UiText.DynamicString(dataToCheck?:"Something went wrong")))





                            }
                        }
                        onFailure("")





                    } catch (e: Exception) {
                        e.printStackTrace()
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(LoadingErrorEvent.errorEncountered(UiText.DynamicString(e.message?:"")))
                        }
                    }
                }

            )
        }
    }

    private fun callSignOnApi(onSuccess: () -> Unit = {}) {
        val data = _registrationState.value
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val phoneNumber =
                dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val request = SignInRequest(
                city = city.value.trimEnd(),
                dateOfBirth = data.dateOfBirth.trimEnd(),
                deviceId = deviceId?.trimEnd(),
                email = data.email.trimEnd(),
                firstName = data.name.trimEnd(),
                gender = data.gender.trimEnd(),
                kyctype = "MIN-KYC",
                lastName = data.lastName.trimEnd(),
                latLong = LatLongFlowProvider.latLongFlow.first().trimEnd(),
                mobileNumber = data.phoneNumber.trimEnd(),
                ovdId = data.ovdNumber.trimEnd(),
                ovdType = when (data.ovdType) {
                    is OVDTypes.Aadhaar -> "AADHAR"
                    is OVDTypes.DrivingLicense -> "DL"
                    is OVDTypes.NONE -> ""
                    is OVDTypes.PAN -> "PAN"
                    is OVDTypes.Passport -> "PASSPORT"
                    is OVDTypes.VoterID -> "VOTER_ID"
                },
                pinCode = data.pinCode.trimEnd(),
                referalCode = null,
                state = state.value.trimEnd()

            )
            handleFlow(
                apiCall = { signOnUseCase.invoke(request) },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onFailure = { errObj, err, resp ->
                    viewModelScope.launch {
                        LoadingErrorEvent.helper.emit(
                            LoadingErrorEvent.errorEncountered(
                                UiText.DynamicString(
                                    err
                                )
                            )
                        )
                    }
                },
                onSuccesss = {
                    if (it?.statusCode == 0) {
                        viewModelScope.launch {
                            dataStoreManager.savePreferences(
                                listOf(
                                    PreferenceData(
                                        PreferencesKeys.USER_MOBILE_NUMBER,
                                        registrationState.value.phoneNumber
                                    )
                                )
                            )
                            dataStoreManager.savePreferences(
                                listOf(
                                    PreferenceData(
                                        PreferencesKeys.USER_MOBILE_NUMBER,
                                        registrationState.value.phoneNumber
                                    )
                                )
                            )
                        }

                        otpRefID.value = it?.data?.otpRefID ?: ""
                        Log.d("CARD_REF", "callSignOnApi:$it ")
                        onSuccess()
                    } else {
                        viewModelScope.launch {
                            dataStoreManager.savePreferences(
                                listOf(
                                    PreferenceData(
                                        PreferencesKeys.USER_MOBILE_NUMBER,
                                        registrationState.value.phoneNumber.ifEmpty { phoneNumber }
                                            .toString()
                                    )
                                )
                            )

                        }
                    }

                })

        }
    }

    private fun allKycScreenValid(): Boolean {
        val ovdValid = isOVDValid()
        val ovdNumberValid = isOvdNumberValid()
        return ovdValid && ovdNumberValid
    }

    private fun isOVDValid(): Boolean {
        val ovdError = registrationState.value.ovdTypeError
        val ovdType = registrationState.value.ovdType
        if (ovdType == OVDTypes.NONE()) {
            _registrationState.update {
                it.copy(
                    ovdTypeError = true,
                    ovdTypeErrorMessage = UiText.StringResource(R.string.please_select_your_ovd_type)
                )
            }
        }
        return !ovdError && ovdType != OVDTypes.NONE()
    }

    private fun isOvdNumberValid(): Boolean {
        if (registrationState.value.ovdNumber.isEmpty()) {
            _registrationState.update {
                it.copy(
                    isOVDNumberError = true,
                    ovdNumberErrorMessage = UiText.StringResource(R.string.please_enter_your_ovd_number)
                )
            }

        }
        return !registrationState.value.isOVDNumberError && registrationState.value.ovdNumber.isNotEmpty()
    }

    private fun allEnterPhoneNumeberFieldsAreValid(): Boolean {
        if (registrationState.value.phoneNumber.isEmpty()) {
            _registrationState.update {
                it.copy(
                    isPhoneNumberError = true,
                    phoneNumberErrorMessage = UiText.StringResource(R.string.please_enter_mobile_number)
                )
            }
        }
        return !registrationState.value.isPhoneNumberError && registrationState.value.phoneNumber.isNotEmpty()
    }

    private fun allMandatoryFieldsAreValid(): Boolean {
        val validName: Boolean = isNameValid()
        val validLastName: Boolean = isLastNameValid()
        val validEmail: Boolean = isEmailValid()
        val validDateOfBirth: Boolean = isDateOfBirthValid()
        val validPinCode: Boolean = isPinCodeValid()
        val validReferralCode: Boolean = isReferralCodeValid()
        val validGender: Boolean = isGenderValid()
        return validName && validEmail && validDateOfBirth && validPinCode && validReferralCode && validGender && validLastName
    }

    private fun isLastNameValid(): Boolean {
        if (_registrationState.value.lastName.isEmpty()) {
            _registrationState.update {
                it.copy(
                    isLastNameError = true,
                    lastNameErrorMessage = UiText.StringResource(R.string.please_enter_your_name)
                )
            }
        }
        return !registrationState.value.isLastNameError && _registrationState.value.lastName.isNotEmpty()
    }

    private fun isGenderValid(): Boolean {
        if (_registrationState.value.gender.isEmpty()) {
            _registrationState.update {
                it.copy(
                    isGenderError = true,
                    genderErrorMessage = UiText.StringResource(R.string.please_select_your_gender)
                )
            }
        }
        return !registrationState.value.isGenderError && registrationState.value.gender.isNotEmpty()
    }

    private fun isDateOfBirthValid(): Boolean {
        if (_registrationState.value.dateOfBirth.isEmpty()) {
            _registrationState.update {
                it.copy(
                    isDateOfBirthError = true, dateOfBirthErrorMessage = UiText.StringResource(

                        R.string.please_enter_your_date_of_birth
                    )
                )

            }
        }
        return _registrationState.value.dateOfBirth.isNotEmpty()
    }

    private fun isReferralCodeValid(): Boolean {
        return true
    }

    private fun isPinCodeValid(): Boolean {
        if (_registrationState.value.pinCode.isEmpty()) {
            _registrationState.update {
                it.copy(
                    isPinCodeError = true,
                    pinCodeErrorMessage = UiText.StringResource(R.string.please_enter_your_pin_code)
                )
            }
        }
        return !registrationState.value.isPinCodeError && _registrationState.value.pinCode.isNotEmpty()
    }

    private fun isEmailValid(): Boolean {
        if (_registrationState.value.email.isEmpty()) {
            _registrationState.update {
                it.copy(
                    isEmailError = true,
                    emailErrorMessage = UiText.StringResource(R.string.please_enter_your_email)
                )
            }

        }
        return !_registrationState.value.isEmailError && _registrationState.value.email.isNotEmpty()
    }

    private fun isNameValid(): Boolean {
        if (_registrationState.value.name.isEmpty()) {
            _registrationState.update {
                it.copy(
                    isNameError = true,
                    nameErrorMessage = UiText.StringResource(R.string.please_enter_your_name)
                )
            }
        }
        return !registrationState.value.isNameError && _registrationState.value.name.isNotEmpty()
    }

    private fun handleRegistrationInputFieldChange(type: CommonTextField, value: String) {
//        Log.d("TYPE", "handleRegistrationInputFieldChange:${value} ")
        when (type) {
            RegistrationInputField.DateOfBirth -> {
                _registrationState.update {
                    it.copy(
                        dateOfBirth = value,
                        isDateOfBirthError = false,
                        dateOfBirthErrorMessage = UiText.StringResource(R.string.empty)
                    )
                }
            }

            RegistrationInputField.Email -> {
                if (Validation.isValidEmail(value)) {
                    _registrationState.update {
                        it.copy(
                            email = value,
                            isEmailError = false,
                            emailErrorMessage = UiText.StringResource(R.string.empty)
                        )
                    }
                } else {
                    _registrationState.update {
                        it.copy(
                            email = value,
                            isEmailError = true,
                            emailErrorMessage = UiText.StringResource(
                                R.string.please_enter_a_valid_email
                            )
                        )
                    }
                }
            }

            RegistrationInputField.Name -> {
                if (Validation.containsOnlyAlphabetic(value)) {
                    _registrationState.update {
                        it.copy(
                            name = value,
                            isNameError = false,
                            nameErrorMessage = UiText.StringResource(R.string.empty)
                        )
                    }
                }
            }

            RegistrationInputField.PinCode -> {
                if (value.length <= 6) {
                    if (value.length == 6) {
                        _registrationState.update {
                            it.copy(
                                pinCode = value,
                                isPinCodeError = false,
                                pinCodeErrorMessage = UiText.StringResource(R.string.empty)
                            )
                        }
                        callfetchPinApi()
                    } else {
                        _registrationState.update {
                            it.copy(
                                pinCode = value,
                                isPinCodeError = true,
                                pinCodeErrorMessage = UiText.StringResource(R.string.pincode_should_be_6_digits)
                            )
                        }
                    }

                }
            }

            RegistrationInputField.ReferalCode -> {
                _registrationState.update {
                    it.copy(referralCode = value)
                }

            }

            RegistrationInputField.Gender -> {
                _registrationState.update {
                    it.copy(
                        gender = value,
                        isGenderError = false,
                        genderErrorMessage = UiText.StringResource(R.string.empty)
                    )
                }

            }

            RegistrationInputField.Mobile -> {
                Validation.isValidPhoneNumber(text = value, onValid = {
                    _registrationState.update {
                        it.copy(
                            phoneNumber = value,
                            isPhoneNumberError = false,
                            phoneNumberErrorMessage = UiText.StringResource(R.string.empty)
                        )
                    }
                }, onError = {
                    _registrationState.update {
                        it.copy(
                            phoneNumber = value,
                            isPhoneNumberError = true,
                            phoneNumberErrorMessage = UiText.StringResource(com.isu.authentication.R.string.please_enter_a_valid_phone_number)
                        )
                    }
                })
            }


            RegistrationInputField.OnBoardingOtpField -> {
                if (value.length <= 6) {
                    if (value.length == 6) {
                        _registrationState.update {
                            it.copy(
                                onBoardingOTP = value,
                                onBoardingOTPError = false,
                                onBoardingOTPErrorMessage = UiText.DynamicString("")
                            )
                        }
                    } else {
                        _registrationState.update {
                            it.copy(
                                onBoardingOTP = value,
                                onBoardingOTPError = true,
                                onBoardingOTPErrorMessage = UiText.StringResource(R.string.otp_must_be_6_digits)
                            )
                        }
                    }
                }
            }

            RegistrationInputField.DeviceChangeOTP -> {
                if (value.length <= 6) {
                    if (value.length == 6) {
                        _registrationState.update {
                            it.copy(
                                deviceChangeOTP = value,
                                deviceChangeOTPError = false,
                                deviceChangeOTPErrorMessage = UiText.DynamicString("")
                            )
                        }
                    } else {
                        _registrationState.update {
                            it.copy(
                                deviceChangeOTP = value,
                                deviceChangeOTPError = true,
                                deviceChangeOTPErrorMessage = UiText.StringResource(R.string.otp_must_be_6_digits)
                            )
                        }
                    }
                }
            }

            RegistrationInputField.OVDNumber -> {
                if (registrationState.value.ovdType != OVDTypes.NONE()) {
                    when (registrationState.value.ovdType) {
                        is OVDTypes.Aadhaar -> {
                            Validation.isAadhaarValid(value, isValid = {
                                _registrationState.update {
                                    it.copy(
                                        ovdNumber = value,
                                        isOVDNumberError = false,
                                        ovdNumberErrorMessage = UiText.StringResource(R.string.empty)
                                    )
                                }
                            }, isNotValid = { err ->
                                _registrationState.update {
                                    it.copy(
                                        ovdNumber = value,
                                        isOVDNumberError = true,
                                        ovdNumberErrorMessage = UiText.DynamicString(err)
                                    )
                                }
                            })
                        }

                        is OVDTypes.DrivingLicense -> {
                            Validation.isDriverLicenseValid(value, isValid = {
                                _registrationState.update {
                                    it.copy(
                                        ovdNumber = value,
                                        isOVDNumberError = false,
                                        ovdNumberErrorMessage = UiText.StringResource(R.string.empty)
                                    )
                                }

                            }, isNotValid = { err ->
                                _registrationState.update {
                                    it.copy(
                                        ovdNumber = value,
                                        isOVDNumberError = true,
                                        ovdNumberErrorMessage = UiText.DynamicString(err)
                                    )

                                }
                            })
                        }

                        is OVDTypes.NONE -> {
                            _registrationState.update {
                                it.copy(
                                    ovdNumber = value,
                                    isOVDNumberError = false,
                                    ovdNumberErrorMessage = UiText.StringResource(R.string.empty)
                                )
                            }

                        }

                        is OVDTypes.Passport -> {
                            Validation.isPassportValid(value, isValid = {
                                _registrationState.update {
                                    it.copy(
                                        ovdNumber = value,
                                        isOVDNumberError = false,
                                        ovdNumberErrorMessage = UiText.StringResource(R.string.empty)
                                    )
                                }
                            }, isNotValid = { err ->
                                _registrationState.update {
                                    it.copy(
                                        ovdNumber = value,
                                        isOVDNumberError = true,
                                        ovdNumberErrorMessage = UiText.DynamicString(err)
                                    )
                                }
                            })
                        }

                        is OVDTypes.VoterID -> {
                            Validation.isVoterIDValid(value, isValid = {
                                _registrationState.update {
                                    it.copy(
                                        ovdNumber = value,
                                        isOVDNumberError = false,
                                        ovdNumberErrorMessage = UiText.StringResource(R.string.empty)
                                    )

                                }
                            }, isNotValid = { err ->
                                _registrationState.update {
                                    it.copy(
                                        ovdNumber = value,
                                        isOVDNumberError = true,
                                        ovdNumberErrorMessage = UiText.DynamicString(err)
                                    )
                                }
                            })
                        }

                        is OVDTypes.PAN -> {
                            if (Validation.isPANValid(value)) {
                                _registrationState.update {
                                    it.copy(
                                        ovdNumber = value,
                                        isOVDNumberError = false,
                                        ovdNumberErrorMessage = UiText.StringResource(R.string.empty)
                                    )
                                }
                            } else {
                                _registrationState.update {
                                    it.copy(
                                        ovdNumber = value,
                                        isOVDNumberError = true,
                                        ovdNumberErrorMessage = UiText.DynamicString("Invalid Pan Number")
                                    )
                                }
                            }

                        }


                    }
                } else {
                    _registrationState.update {
                        it.copy(
                            ovdNumber = value,
                            isOVDNumberError = true,
                            ovdNumberErrorMessage = UiText.StringResource(R.string.please_select_your_ovd_type)
                        )
                    }
                }


            }

            RegistrationInputField.OVDType -> {
                Log.d("TYPE", "handleRegistrationInputFieldChange:${value} ")
                _registrationState.update {
                    it.copy(
                        ovdType = when (value) {
                            OVDTypes.Aadhaar().aadharType -> {
                                OVDTypes.Aadhaar()
                            }

                            OVDTypes.VoterID().voterIDType -> {
                                OVDTypes.VoterID()
                            }

                            OVDTypes.Passport().passportType -> {
                                OVDTypes.Passport()
                            }

                            OVDTypes.DrivingLicense().drivingLicenseType -> {
                                OVDTypes.DrivingLicense()
                            }

                            OVDTypes.PAN().panType -> {
                                OVDTypes.PAN()
                            }

                            else -> {
                                OVDTypes.NONE()
                            }
                        },
                        ovdTypeError = false,
                        ovdTypeErrorMessage = UiText.StringResource(R.string.empty)
                    )
                }
                _registrationState.update {
                    it.copy(
                        ovdNumber = "",
                        ovdNumberErrorMessage = UiText.StringResource(R.string.empty),
                        isOVDNumberError = false
                    )
                }
            }

            RegistrationInputField.LastName -> {
                _registrationState.update {
                    it.copy(
                        lastName = value,
                        isLastNameError = false,
                        lastNameErrorMessage = UiText.StringResource(R.string.empty)
                    )
                }
            }

            RegistrationInputField.TwoFAOtpField -> {
                if (value.length <= 4) {
                    if (value.length == 4) {
                        _registrationState.update {
                            it.copy(
                                twoFAOTP = value,
                                twoFAOTPError = false,
                                twoFAOTPErrorMessage = UiText.DynamicString("")

                            )
                        }
                    } else {
                        _registrationState.update {
                            it.copy(
                                twoFAOTP = value,
                                twoFAOTPError = true,
                                twoFAOTPErrorMessage = UiText.DynamicString("OTP must be 4 digits")
                            )
                        }
                    }
                }
            }
        }
    }

    private fun callfetchPinApi() {
        handleFlow(
            apiCall = { fetchPinCodeUseCase.invoke(FetchPinCodeDataRequest(pin = registrationState.value.pinCode.toInt())) },
            scope = viewModelScope,
            dispatcher = Dispatchers.IO,
            builder = {
                onLoading { loading ->
                    _registrationState.update {
                        it.copy(pinCodeDataLoading = loading)
                    }
                }
                onSuccess { resp ->
                    state.value = resp?.data?.data?.state ?: ""
                    city.value = resp?.data?.data?.city ?: ""
                    _registrationState.update {
                        it.copy(pincodeData = resp?.data?.data?.city + "," + resp?.data?.data?.district + ", " + resp?.data?.data?.state)
                    }
                }
                onFailure { s, i, es, fetchPinCodeDataResponse ->
                    _registrationState.update {
                        it.copy(
                            isPinCodeError = true, pinCodeErrorMessage = UiText.DynamicString(s)
                        )
                    }
                }
            })
    }

    suspend fun hasData() = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)

    fun clearDataStore() {
        viewModelScope.launch {
            dataStoreManager.clearDataStore()
        }
    }

    fun clearData() {
        viewModelScope.launch {
            _registrationState.update {
                it.copy(
                    name = "",
                    lastName = "",
                    email = "",
                    dateOfBirth = "",
                    pincodeData = "",
                    pinCode = "",
                    referralCode = "",
                    ovdType = OVDTypes.NONE(),
                    ovdNumber = "",
                    gender = ""

                )
            }
        }
    }


}