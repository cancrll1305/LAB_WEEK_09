package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

// Declare a data class called Student
data class Student(
    var name: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home()  // Pass nothing, as the state is managed within Home composable now
                }
            }
        }
    }
}

@Composable
fun Home() {
    // Create a mutable state list to store the students
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }
    // Create a mutable state to hold the input value
    var inputField by remember { mutableStateOf(Student("")) }

    // Call HomeContent and pass the listData, inputField, and handlers
    HomeContent(
        listData = listData,
        inputField = inputField,
        onInputValueChange = { newInput -> inputField = inputField.copy(name = newInput) },
        onButtonClick = {
            if (inputField.name.isNotBlank()) {
                listData.add(inputField)  // Add the input to the list
                inputField = Student("")  // Reset the input field
            }
        }
    )
}

@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit
) {
    LazyColumn {
        item {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.enter_item)) // Instruction Text
                TextField(
                    value = inputField.name, // Bind to inputField
                    onValueChange = onInputValueChange, // Update input value
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                Button(onClick = onButtonClick) {
                    Text(text = stringResource(id = R.string.button_click)) // Submit Button
                }
            }
        }

        items(listData) { item ->
            Column(
                modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item.name) // Display each student's name
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home() // Preview the Home composable
}
