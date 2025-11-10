package com.example.lab_week_09

import android.os.Bundle
import android.util.Log
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
import androidx.navigation.compose.*
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
import com.example.lab_week_09.ui.theme.OnBackgroundItemText
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import androidx.navigation.NavType
import androidx.navigation.navArgument

// Moshi instance for JSON parsing
val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

// Adapter to parse List of Student objects
val jsonAdapter = moshi.adapter(List::class.java)

// Adapter for List<Student> and the Student object itself
val studentListAdapter = moshi.adapter(List::class.java).nullSafe()

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
            // Pass the JSON string to ResultContent
            ResultContent(it.arguments?.getString("listData").orEmpty())
        }
    }
}

@Composable
fun ResultContent(listData: String) {
    // Decode the JSON string into a list of students
    val decodedList = try {
        val jsonAdapter = moshi.adapter(List::class.java)
        val list = jsonAdapter.fromJson(listData)?.filterIsInstance<Student>() ?: emptyList<Student>()

        // Log the decoded list to verify the data
        Log.d("ResultContent", "Decoded list: $list") // Log the decoded list

        list
    } catch (e: Exception) {
        Log.e("ResultContent", "Error decoding JSON: ${e.message}")
        emptyList<Student>()  // Return an empty list if decoding fails
    }

    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Check if the decoded list is empty
        if (decodedList.isEmpty()) {
            Text("No data available")  // Show a message if the list is empty
        } else {
            LazyColumn {
                items(decodedList) { item ->
                    OnBackgroundItemText(text = item.name) // Access name from Student object
                }
            }
        }
    }
}


@Composable
fun Home(navigateFromHomeToResult: (String) -> Unit) {
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }
    var inputField by remember { mutableStateOf(Student("")) }

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
            // Convert the listData to JSON and log it for debugging
            val json = jsonAdapter.toJson(listData)
            Log.d("Home", "Passing JSON: $json")  // Log the JSON
            navigateFromHomeToResult(json)
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
                OnBackgroundTitleText(text = stringResource(id = R.string.enter_item))

                // Handle TextField input
                TextField(
                    value = inputField.name, // Access name from Student object
                    onValueChange = onInputValueChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

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

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home { }
}
