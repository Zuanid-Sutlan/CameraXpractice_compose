package com.example.ocr_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cameraxpractice_compose.MainViewModel
import com.example.cameraxpractice_compose.ui.CameraUi
import com.example.cameraxpractice_compose.ui.ImageCropper
import com.example.cameraxpractice_compose.ui.ImageSelectorAndCropper

@Composable
fun Navigation(viewModel: MainViewModel, navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.HomeView.route) {

        composable(Screens.HomeView.route) {
            CameraUi(viewModel = viewModel, navController = navController)
        }

        composable(Screens.CameraView.route) {
            ImageSelectorAndCropper()
        }

        composable(Screens.ImageCropper.route){
            ImageCropper()
        }


    }
}