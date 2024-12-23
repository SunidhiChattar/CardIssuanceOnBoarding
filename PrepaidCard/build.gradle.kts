plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.isu.prepaidcard"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

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
    buildFeatures{
        buildConfig = true
    }
    buildTypes{
        debug {
            defaultConfig{
                minSdk = 24
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")

                //        urls
                buildConfigField("String", "VIEW_CARD_DATA_BY_MOBILE_NUMBER", project.property("VIEW_CARD_DATA_BY_MOBILE_NUMBER") as String)
                buildConfigField("String", "VIEW_CARD_BALANCE", project.property("VIEW_CARD_BALANCE") as String)
                buildConfigField("String", "LOAD_CARD", project.property("LOAD_CARD") as String)
                buildConfigField("String", "ORDER_PHYSICAL_CARD", project.property("ORDER_PHYSICAL_CARD") as String)
                buildConfigField("String", "VIEW_CARD_DATA_BY_REF_ID", project.property("VIEW_CARD_DATA_BY_REF_ID") as String)
                buildConfigField("String", "CHANGE_CARD_STATUS", project.property("CHANGE_CARD_STATUS") as String)
                buildConfigField("String", "VIEW_CARD_CVV", project.property("VIEW_CARD_CVV") as String)
                buildConfigField("String", "CHANGE_MCC", project.property("CHANGE_MCC") as String)
                buildConfigField("String", "MODIFY_PIN", project.property("MODIFY_PIN") as String)
                buildConfigField("String", "ADD_ON_CARD", project.property("ADD_ON_CARD") as String)
                buildConfigField("String", "MINI_STATEMENT", project.property("MINI_STATEMENT") as String)
                buildConfigField("String", "DETAILED_STATEMENT", project.property("DETAILED_STATEMENT") as String)
            }
        }
    }
}

dependencies {
    implementation(project(":Common"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}