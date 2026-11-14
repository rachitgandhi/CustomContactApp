package com.example.contactsapp.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.contactsapp.R
import com.example.contactsapp.data.Contact
import com.example.contactsapp.ui.theme.ContactsAppTheme
import com.example.contactsapp.ui.theme.Orange40
import com.example.contactsapp.ui.viewmodel.ContactViewModel
import com.example.contactsapp.util.ImageUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactContent(
    image: Any? = null,
    name: String = "",
    phoneNumber: String = "",
    email: String = "",
    onNameChange: (String) -> Unit = {},
    onPhoneNumberChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onChooseImageClick: () -> Unit = {},
    onCaptureDetailsClick: () -> Unit = {},
    onPasteDetailsClick: () -> Unit = {},
    onUpdateContactClick: () -> Unit = {}
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.wrapContentHeight(Alignment.CenterVertically),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight(Alignment.CenterVertically)
                    ) {
                        Text(text = "Edit Contact", fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { Toast.makeText(context, "Edit Contact", Toast.LENGTH_SHORT).show() }) {
                        Icon(painter = painterResource(id = R.drawable.editcontact), contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange40,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(image),
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onChooseImageClick,
                colors = ButtonDefaults.buttonColors(Orange40)
            ) {
                Text(text = "Choose Image")
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text(text = "Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                label = { Text(text = "Phone Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text(text = "Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onCaptureDetailsClick,
                    colors = ButtonDefaults.buttonColors(Orange40)
                ) {
                    Text(text = "Capture details")
                }
                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onPasteDetailsClick,
                    colors = ButtonDefaults.buttonColors(Orange40)
                ) {
                    Text(text = "Paste details")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onUpdateContactClick,
                colors = ButtonDefaults.buttonColors(Orange40)
            ) {
                Text(text = "Update Contact")
            }
        }
    }
}

@Composable
fun EditContactScreen(
    contact: Contact,
    viewModel: ContactViewModel,
    navController: NavController,
    onRequestCameraPermission: () -> Unit = {},
    recognizedText: String = ""
) {
    val context = LocalContext.current.applicationContext
    var imageUri by remember { mutableStateOf(contact.image) }
    var name by remember { mutableStateOf(contact.name) }
    var phonenumber by remember { mutableStateOf(contact.phoneNumber) }
    var email by remember { mutableStateOf(contact.email) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { newUri ->
            val internalPath = ImageUtils.copyUriToInternalStorage(context, newUri, "$name.jpg")
            internalPath?.let { path -> imageUri = path }
        }
    }

    EditContactContent(
        image = imageUri,
        name = name,
        phoneNumber = phonenumber,
        email = email,
        onNameChange = { name = it },
        onPhoneNumberChange = { phonenumber = it },
        onEmailChange = { email = it },
        onChooseImageClick = { launcher.launch("image/*") },
        onCaptureDetailsClick = onRequestCameraPermission,
        onPasteDetailsClick = {
            if (recognizedText.isNotEmpty()) {
                email = recognizedText
            }
        },
        onUpdateContactClick = {
            val updateContact = contact.copy(image = imageUri, name = name, phoneNumber = phonenumber, email = email)
            viewModel.updateContact(updateContact)
            navController.navigate("contactList") {
                popUpTo(0)
            }
        }
    )
}

@Preview(showBackground = true, name = "Edit Contact UI")
@Composable
fun EditContactPreview() {
    ContactsAppTheme {
        EditContactContent(
            name = "Alice Johnson",
            phoneNumber = "555-0101",
            email = "alice@example.com"
        )
    }
}
