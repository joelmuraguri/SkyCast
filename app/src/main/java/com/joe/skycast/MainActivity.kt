package com.joe.skycast

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.joe.skycast.navigation.BottomNavigationBar
import com.joe.skycast.navigation.SkyCastNavGraph
import com.joe.skycast.ui.theme.SkyCastTheme
import com.joe.sync.worker.SYNC_WORK_NAME
import com.joe.sync.worker.SyncViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val snackbarHostState = SnackbarHostState()
    private val scope = CoroutineScope(Dispatchers.Main)

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        Log.d(SYNC_WORK_NAME, "---------------> Permissions: $permissions")
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            syncViewModel.requestSync()
        } else {
            scope.launch {
                showSnackbar("Location permissions are required to fetch current location.")
            }
        }
    }

    private val syncViewModel: SyncViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val activity = context as? Activity

            SkyCastTheme {
                val navController = rememberNavController()
                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { if (bottomBarState.value) BottomNavigationBar(navController) },
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    containerColor = Color(0xFF142036)
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .padding(padding)
                    ) {
                        SkyCastNavGraph(
                            navHostController = navController,
                            updateBottomBarState = { bottomBarState.value = it },
                            activity = activity!!
                        )
                    }
                }
            }
        }
    }

    private fun requestPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private suspend fun showSnackbar(message: String) {
        snackbarHostState.showSnackbar(message)
    }
}

