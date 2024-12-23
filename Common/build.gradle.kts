plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
//    alias(libs.plugins.google.gms.google.services)


}

android {
    namespace = "com.isu.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }
////     Specifies one flavor dimension.
//    flavorDimensions += "client_type"
//    productFlavors {
//        create("sdk") {
//            // Assigns this product flavor to the "version" flavor dimension.
//            // If you are using only one dimension, this property is optional,
//            // and the plugin automatically assigns all the module's flavors to
//            // that dimension.
//            dimension = "client_type"
//
//        }
//        create("white_label") {
//            dimension = "client_type"
//            buildConfigField("String", "LOGIN_AUTHORIZATION_TOKEN", project.property("LOGIN_AUTHORIZATION_TOKEN") as String)
//
//        }
//    }
    buildTypes {
        debug {
            defaultConfig {

                minSdk = 24

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
//                query parameters and tokens
                buildConfigField(
                    "String",
                    "LOGIN_AUTHORIZATION_TOKEN",
                    project.property("LOGIN_AUTHORIZATION_TOKEN") as String
                )
                buildConfigField(
                    "String",
                    "LOGIN_GRANT_TYPE",
                    project.property("LOGIN_GRANT_TYPE") as String
                )
                buildConfigField(
                    "String",
                    "FIELD_KEY_USERNAME",
                    project.property("FIELD_KEY_USERNAME") as String
                )
                buildConfigField(
                    "String",
                    "FIELD_KEY_PASSWORD",
                    project.property("FIELD_KEY_PASSWORD") as String
                )
                buildConfigField(
                    "String",
                    "FIELD_KEY_GRANT_TYPE",
                    project.property("FIELD_KEY_GRANT_TYPE") as String
                )
                buildConfigField(
                    "String",
                    "FIELD_KEY_AUTHORIZATION",
                    project.property("FIELD_KEY_AUTHORIZATION") as String
                )
                buildConfigField(
                    "String",
                    "QUERY_USERNAME",
                    project.property("QUERY_USERNAME") as String
                )

//                urls
                buildConfigField("String", "LOGIN_URL", project.property("LOGIN_URL") as String)
                buildConfigField(
                    "String",
                    "SEND_FIRST_LOGIN_OTP",
                    project.property("SEND_FIRST_LOGIN_OTP") as String
                )
                buildConfigField(
                    "String",
                    "VERIFY_FORGOT_PASSWORD_OTP",
                    project.property("VERIFY_FORGOT_PASSWORD_OTP") as String
                )
                buildConfigField(
                    "String",
                    "SEND_FORGOT_PASSWORD_OTP",
                    project.property("SEND_FORGOT_PASSWORD_OTP") as String
                )
                buildConfigField(
                    "String",
                    "VERIFY_FIRST_LOGIN_OTP_CHANGE_PASSWORD",
                    project.property("VERIFY_FIRST_LOGIN_OTP_CHANGE_PASSWORD") as String
                )
                buildConfigField(
                    "String",
                    "DASHBOARD_USER_PROFILE",
                    project.property("DASHBOARD_USER_PROFILE") as String
                )
                buildConfigField("String", "GET_TICKETS", project.property("GET_TICKETS") as String)
                buildConfigField(
                    "String",
                    "RAISE_TICKET",
                    project.property("RAISE_TICKET") as String
                )
                buildConfigField(
                    "String",
                    "UPDATE_TICKET_STATUS",
                    project.property("UPDATE_TICKET_STATUS") as String
                )
                buildConfigField(
                    "String",
                    "SHOW_TICKET_COMMENTS",
                    project.property("SHOW_TICKET_COMMENTS") as String
                )
                buildConfigField(
                    "String",
                    "ADD_COMMENTS",
                    project.property("ADD_COMMENTS") as String
                )
                buildConfigField(
                    "String",
                    "OTP_CHANGE_PASSWORD_USING_OLD_PASSWORD",
                    project.property("OTP_CHANGE_PASSWORD_USING_OLD_PASSWORD") as String
                )
                buildConfigField(
                    "String",
                    "CHANGE_PASSWORD_USING_OLD_PASSWORD",
                    project.property("CHANGE_PASSWORD_USING_OLD_PASSWORD") as String
                )

                buildConfigField(
                    "String",
                    "FETCH_PIN_CODE",
                    project.property("FETCH_PIN_CODE") as String
                )
                buildConfigField(
                    "Boolean",
                    "IS_WHITE_LABEL",
                    project.property("IS_WHITE_LABEL") as String
                )

                buildConfigField(
                    "String",
                    "WEB_VIEW_TERMS_AND_CONDITION",
                    project.property("WEB_VIEW_TERMS_AND_CONDITION") as String
                )
                buildConfigField(
                    "String",
                    "WEB_VIEW_ABOUT_US",
                    project.property("WEB_VIEW_ABOUT_US") as String
                )
                buildConfigField(
                    "String",
                    "WEB_VIEW_PRIVACY_POLICY",
                    project.property("WEB_VIEW_PRIVACY_POLICY") as String
                )
                buildConfigField(
                    "String",
                    "STATUS_CHECK",
                    project.property("STATUS_CHECK") as String
                )


            }
        }
    }

}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)
    /* api(libs.firebase.firestore)
     api(libs.firebase.storage)
     api(libs.firebase.auth)*/
    api(libs.play.services.auth)
    testApi(libs.junit)
    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.espresso.core)
    androidTestApi(platform(libs.androidx.compose.bom))
    androidTestApi(libs.androidx.ui.test.junit4)
    debugApi(libs.androidx.ui.tooling)
    debugApi(libs.androidx.ui.test.manifest)

    // Navigation
    api(libs.navigation.compose)
    api(libs.kotlinx.serialization.json)

    // Hilt
    api(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    api(libs.hilt.navigation.compose)

    // Retrofit
    api(libs.retrofit2)
    api(libs.gson.converter)
    api(libs.logging.interceptor)
    //downloadableFont
    implementation(libs.google.font)
//datastore
    implementation(libs.accompanist.systemuicontroller)
    api(libs.androidx.datastore.preferences)
    api(libs.utility)
    //chucker
    api(libs.customchucker)
    api(libs.gms.play.services.auth)

    //biometric
    api(libs.authentication)

    api(libs.coil.compose)
    implementation(libs.jackson.module.kotlin)
    api(libs.coil3.coil.compose)
    api(libs.coil.network.okhttp)
    api(libs.androidx.core.splashscreen)


}