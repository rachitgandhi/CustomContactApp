package com.example.contactsapp

import android.Manifest
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.contactsapp.data.ContactDatabase
import com.example.contactsapp.data.ContactRepository
import com.example.contactsapp.ui.screens.AddContactScreen
import com.example.contactsapp.ui.screens.ContactDetailScreen
import com.example.contactsapp.ui.screens.ContactListScreen
import com.example.contactsapp.ui.screens.EditContactScreen
import com.example.contactsapp.ui.theme.ContactsAppTheme
import com.example.contactsapp.ui.viewmodel.ContactViewModel
import com.example.contactsapp.ui.viewmodel.ContactViewModelFactory
import com.example.contactsapp.util.ImageUtils

class MainActivity : ComponentActivity() {

    private var currentPhotoPath: String? = null
    private var recognizeTextFromImage by mutableStateOf("")

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contact_database"
        ).build()

        val repository = ContactRepository(database.contactDao())
        val viewModel: ContactViewModel by viewModels { ContactViewModelFactory(repository) }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                captureImage()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                currentPhotoPath?.let { path ->
                    val bitmap = BitmapFactory.decodeFile(path)
                    ImageUtils.recognizeText(bitmap, applicationContext) { text ->
                        recognizeTextFromImage = text
                    }
                }
            }
        }

        setContent {
            ContactsAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "contactList") {
                    composable("contactList") {
                        ContactListScreen(viewModel = viewModel, navController = navController)
                    }
                    composable("addContact") {
                        AddContactScreen(
                            viewModel = viewModel,
                            navController = navController,
                            onRequestCameraPermission = {
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            },
                            recognizedText = recognizeTextFromImage
                        )
                    }
                    composable("contactDetail/{contactId}") { backStackEntry ->
                        val contactId = backStackEntry.arguments?.getString("contactId")?.toIntOrNull()
                        val contacts by viewModel.allContacts.observeAsState(initial = emptyList())
                        val contact = contacts.find { it.id == contactId }
                        contact?.let {
                            ContactDetailScreen(
                                contact = it,
                                viewModel = viewModel,
                                navController = navController
                            )
                        }
                    }
                    composable("editContact/{contactId}") { backStackEntry ->
                        val contactId = backStackEntry.arguments?.getString("contactId")?.toIntOrNull()
                        val contacts by viewModel.allContacts.observeAsState(initial = emptyList())
                        val contact = contacts.find { it.id == contactId }
                        contact?.let {
                            EditContactScreen(
                                contact = it,
                                viewModel = viewModel,
                                navController = navController,
                                onRequestCameraPermission = {
                                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                                },
                                recognizedText = recognizeTextFromImage
                            )
                        }
                    }
                }
            }
        }
    }

    private fun captureImage() {
        val result = ImageUtils.createImageFile(this)
        if (result != null) {
            val (photoFile, path) = result
            currentPhotoPath = path
            val photoUri: Uri = FileProvider.getUriForFile(this, "${packageName}.provider", photoFile)
            takePictureLauncher.launch(photoUri)
        }
    }
}
