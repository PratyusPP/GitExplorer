Setup instructions (how to run the app)
- You can download apk - https://drive.google.com/file/d/1uuHr9uNxJCVy7n9dxxGpYO8zCoPkLhDI/view?usp=sharing
Dependencies used
implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.savedstate)
    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Retrofit + Moshi + OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi.kotlin)

    kapt(libs.moshi.codegen)


    // Glide
    implementation(libs.glide)
    kapt(libs.glide.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

Device/emulator used for testing
- Samsung/Moto/Xiomi
- Emulator - Pixel
Short explanation of architecture choices
Mvvm Architecture and for local database - Room
