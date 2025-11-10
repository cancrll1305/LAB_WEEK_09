package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val list = listOf("Tanu", "Tina", "Tono")
                    Home(items = list)  // Passing the list to the Home composable
                }
            }
        }
    }
}

@Composable
fun Home(
    items: List<String>  // Define a parameter to pass the list of items
) {
    // LazyColumn for efficiently displaying the list of items
    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.enter_item)  // Display the string resource
                )
                // TextField for user input
                BasicTextField(
                    value = "",
                    onValueChange = { /* Handle value change here */ },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                // Button with an onClick event
                Button(onClick = { /* Handle button click */ }) {
                    Text(text = stringResource(id = R.string.button_click))
                }
            }
        }

        // LazyColumn item to display the list of passed items
        items(items) { item ->
            Column(
                modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item)  // Display each item in the list
            }
        }
    }
}

// Preview function to show the Home composable with a sample list
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home(items = listOf("Tanu", "Tina", "Tono"))
}
