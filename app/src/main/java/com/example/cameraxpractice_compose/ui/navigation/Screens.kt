package com.example.ocr_app.ui.navigation

sealed class Screens(val route: String) {

    data object HomeView: Screens("home-view")

    data object ImageCropper: Screens("scanned-text-view")

    data object CameraView: Screens("camera-view")

}