package com.example.cameraxpractice_compose.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.rounded.CameraEnhance
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.cameraxpractice_compose.ApplicationClass
import com.example.cameraxpractice_compose.MainViewModel
import com.example.cameraxpractice_compose.ui.components.CameraPreview
import com.example.cameraxpractice_compose.ui.components.PhotoBottomSheetGallery
import com.example.ocr_app.ui.navigation.Screens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraUi(viewModel: MainViewModel, navController: NavHostController) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val cameraController = remember {
        LifecycleCameraController(ApplicationClass.context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    val bitmap by viewModel.bitmap.collectAsState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            PhotoBottomSheetGallery(bitmap = bitmap, modifier = Modifier.fillMaxWidth())
        }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CameraPreview(controller = cameraController, modifier = Modifier.fillMaxSize())

            // button in camera view to switch camera
            IconButton(
                modifier = Modifier.offset(16.dp, 16.dp),
                onClick = {
                    // switching camera
                    cameraController.cameraSelector =
                        if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else CameraSelector.DEFAULT_BACK_CAMERA
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "switch camera"
                    )
                }
            )

            // buttons for open gallery capture image and torch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                    .align(Alignment.BottomCenter),
//                    verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.Image,
                            contentDescription = "open gallery"
                        )
                    }
                )

                /*IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { *//*TODO*//* },
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.CameraEnhance,
                                contentDescription = "capture image"
                            )
                        }
                    )*/

                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 24.dp),
                    onClick = {
                        takePhoto(
                            controller = cameraController,
                            onPhotoTaken = {
                                viewModel.onTakePhoto(it)
                            }
                        )
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.CameraEnhance,
                            contentDescription = "capture image"
                        )
                    })

                IconButton(
                    onClick = {
                        /*TODO*/
                              navController.navigate(Screens.ImageCropper.route)
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.FlashlightOn,
                            contentDescription = "flesh light"
                        )
                    }
                )
            }
        }
    }
}


private fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(ApplicationClass.context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                onPhotoTaken(image.toBitmap())
                Log.i("onCaptureSuccess: ", image.toString())
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.i("onError: ", "$exception")
            }
        }
    )
}