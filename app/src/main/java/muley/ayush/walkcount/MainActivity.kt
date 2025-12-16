package muley.ayush.walkcount

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import muley.ayush.walkcount.data.UserPreferencesRepository
import muley.ayush.walkcount.ui.composables.StepCounter
import muley.ayush.walkcount.ui.theme.WalkCountTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalkCountTheme {
                val userPreferencesRepository = UserPreferencesRepository(this)
                val mainViewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(userPreferencesRepository)
                )
                MainScreen(viewModel = mainViewModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val steps by viewModel.steps.collectAsState()
    val stepGoal by viewModel.stepGoal.collectAsState()

    // Define the permissions we need
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.POST_NOTIFICATIONS
        )
    } else
        listOf(Manifest.permission.ACTIVITY_RECOGNITION)

    var hasPermissions by remember {
        mutableStateOf(permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        })
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionResult ->
            hasPermissions = permissionResult.values.all { it }
            if (hasPermissions) {
                startWalkCountService(context)
            }
        }
    )

    // Request permissions when the composable is first launched
    LaunchedEffect(Unit) {
        if (!hasPermissions) {
            permissionLauncher.launch(permissions.toTypedArray())
        } else {
            startWalkCountService(context)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Walk Count") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { context.startActivity(Intent(context, GoalsActivity::class.java)) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            if (hasPermissions) {
                StepCounter(steps = steps, goal = stepGoal)
            } else {
                PermissionRequestUI(
                    onGrantClick = {
                        permissionLauncher.launch(permissions.toTypedArray())
                    }
                )
            }
        }
    }
}

@Composable
fun PermissionRequestUI(onGrantClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Permissions Required",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "This app needs Activity Recognition and Notification permissions to count your steps and show them in real-time."
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onGrantClick) {
            Text("Grant Permissions")
        }
    }
}

private fun startWalkCountService(context: Context) {
    val intent = Intent(context, WalkCountService::class.java)
    context.startForegroundService(intent)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WalkCountTheme {
        StepCounter(steps = 5432, goal = 10000)
    }
}
