plugins {
    id("com.google.devtools.ksp")

    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

}

android {
    namespace = "mx.edu.utez.ciudadsecreta"
    compileSdk = 36

    defaultConfig {
        applicationId = "mx.edu.utez.ciudadsecreta"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/DEPENDENCIES"
            excludes +=  "META-INF/io.netty.versions.properties"
        }
    }
}

dependencies {
    // --- RETROFIT + GSON ---
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // --- OKHTTP LOGGING ---
    implementation(libs.okhttp.logging)


    // --- DEPENDENCIAS COMPOSE Y BÁSICAS ---
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.0")

    // COMPOSE BOM (Base de versiones)
    implementation(platform(libs.androidx.compose.bom))

    // UI Compose Components (Limpiado de duplicados, se mantienen las referencias 'libs.')
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Lifecycle y ViewModel
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1") // Mantengo esta por si la versión es clave
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navegación
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // --- ROOM (Base de Datos) ---
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler) // Compilador KSP

    // --- RED Y LOCALIZACIÓN ---
    //implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.play.services.location)
    implementation(libs.osmdroid.android)

    // --- TEST Y DEBUG ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // --- OTROS (Revisa esta línea si da error) ---
    // Si 'firebase.appdistribution.gradle' es un plugin, debe ir en el bloque 'plugins'.
    implementation(libs.firebase.appdistribution.gradle)
}