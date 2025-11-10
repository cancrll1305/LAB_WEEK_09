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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
import com.example.lab_week_09.ui.theme.OnBackgroundItemText

// Student data model
data class Student(var name: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    App(navController = navController)
                }
            }
        }
    }
}

// Root Composable (App with Navigation)
@Composable
fun App(navController: NavHostController) {
    // Set up the Navigation graph
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Define the "home" route
        composable("home") {
            Home { navController.navigate("resultContent/?listData=${it}") }
        }

        // Define the "resultContent" route
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") { type = NavType.StringType })
        ) {
            ResultContent(it.arguments?.getString("listData").orEmpty())
        }
    }
}

@Composable
fun Home(navigateFromHomeToResult: (String) -> Unit) {
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
        },
        navigateFromHomeToResult = {
            navigateFromHomeToResult(listData.toList().toString())
        }
    )
}

@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit
) {
    LazyColumn {
        item {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display title using custom UI element
                OnBackgroundTitleText(text = stringResource(id = R.string.enter_item))

                // Input field for the student's name
                TextField(
                    value = inputField.name,
                    onValueChange = onInputValueChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                // Buttons for submit and navigate to result
                Row {
                    PrimaryTextButton(text = stringResource(id = R.string.button_click)) {
                        onButtonClick()
                    }
                    PrimaryTextButton(text = stringResource(id = R.string.button_navigate)) {
                        navigateFromHomeToResult()
                    }
                }
            }
        }

        // Display the list of students
        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

@Composable
fun ResultContent(listData: String) {
    // Display the result content (the listData received from the Home composable)
    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnBackgroundItemText(text = listData)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home { }
}
