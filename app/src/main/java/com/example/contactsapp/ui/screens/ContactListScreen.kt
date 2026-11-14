package com.example.contactsapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contactsapp.R
import com.example.contactsapp.data.Contact
import com.example.contactsapp.ui.components.ContactItem
import com.example.contactsapp.ui.theme.ContactsAppTheme
import com.example.contactsapp.ui.theme.Orange40
import com.example.contactsapp.ui.viewmodel.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListContent(
    contacts: List<Contact>,
    onContactClick: (Contact) -> Unit = {},
    onAddContactClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.wrapContentHeight(Alignment.CenterVertically),
                title = {
                    Box(modifier = Modifier.fillMaxHeight().wrapContentHeight(Alignment.CenterVertically)) {
                        Text(text = "Contacts", fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { Toast.makeText(context, "Contact List", Toast.LENGTH_SHORT).show() }) {
                        Icon(painter = painterResource(id = R.drawable.contacticon), contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange40,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Orange40,
                onClick = onAddContactClick
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Contacts")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(contacts) { contact ->
                ContactItem(contact = contact) {
                    onContactClick(contact)
                }
            }
        }
    }
}

@Composable
fun ContactListScreen(viewModel: ContactViewModel, navController: NavController) {
    val contacts by viewModel.allContacts.observeAsState(initial = emptyList())
    ContactListContent(
        contacts = contacts,
        onContactClick = { contact ->
            navController.navigate("contactDetail/${contact.id}")
        },
        onAddContactClick = {
            navController.navigate("addContact")
        }
    )
}

@Preview(showBackground = true, name = "Contact List UI")
@Composable
fun ContactListPreview() {
    ContactsAppTheme {
        ContactListContent(
            contacts = listOf(
                Contact(id = 1, image = "", name = "Alice Johnson", phoneNumber = "555-0101", email = "alice@example.com"),
                Contact(id = 2, image = "", name = "Bob Smith", phoneNumber = "555-0102", email = "bob@example.com"),
                Contact(id = 3, image = "", name = "Charlie Brown", phoneNumber = "555-0103", email = "charlie@example.com")
            )
        )
    }
}
