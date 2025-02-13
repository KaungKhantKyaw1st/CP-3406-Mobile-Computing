package com.example.healthandwellnessapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthAndWellnessApp()
        }
    }
}

enum class AppPage {
    HOME, FITNESS, HYDRATION, MEAL_PLANNER, MENTAL_WELLNESS
}

object AppColors {
    val Primary = Color(0xFF42A5F5)
    val CardBackground = Color(0xFFE3F2FD)
}

object AppDimens {
    val CardHeight = 120.dp
    val CardCornerRadius = 16.dp
    val CardElevation = 6.dp
    val IconSize = 50.dp
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthAndWellnessApp() {
    var currentPage by remember { mutableStateOf(AppPage.HOME) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Color(0xFFE3F2FD)
        )
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text("Health & Wellness Tracker", color = Color.White) },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = AppColors.Primary
                    )
                )
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                when (currentPage) {
                    AppPage.HOME -> HomeScreen(
                        onNavigateToFitness = { currentPage = AppPage.FITNESS },
                        onNavigateToMealPlanner = { currentPage = AppPage.MEAL_PLANNER },
                        onNavigateToHydration = { currentPage = AppPage.HYDRATION },
                        onNavigateToMentalWellness = { currentPage = AppPage.MENTAL_WELLNESS }
                    )
                    AppPage.FITNESS -> FitnessTrackerPage(onBack = { currentPage = AppPage.HOME })
                    AppPage.MEAL_PLANNER -> MealPlannerPage(onBack = { currentPage = AppPage.HOME })
                    AppPage.HYDRATION -> HydrationTrackerPage(onBack = { currentPage = AppPage.HOME })
                    AppPage.MENTAL_WELLNESS -> MentalWellnessPage(onBack = { currentPage = AppPage.HOME })
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onNavigateToFitness: () -> Unit,
    onNavigateToMealPlanner: () -> Unit,
    onNavigateToHydration: () -> Unit,
    onNavigateToMentalWellness: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WellnessCard(
            title = "Fitness Tracker",
            description = "Track your workouts and steps.",
            imageRes = R.drawable.running_image,
            onClick = onNavigateToFitness
        )
        WellnessCard(
            title = "Meal Planner",
            description = "Plan your meals and track daily intake.",
            imageRes = R.drawable.meal_image,
            onClick = onNavigateToMealPlanner
        )
        WellnessCard(
            title = "Mental Wellness",
            description = "Track your mood and meditate.",
            imageRes = R.drawable.mental_wellness_image,
            onClick = onNavigateToMentalWellness
        )
        WellnessCard(
            title = "Hydration Tracker",
            description = "Monitor your daily water intake.",
            imageRes = R.drawable.water_bottle,
            onClick = onNavigateToHydration
        )
    }
}

@Composable
fun WellnessCard(
    title: String,
    description: String,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(AppDimens.CardHeight),
        shape = RoundedCornerShape(AppDimens.CardCornerRadius),
        elevation = CardDefaults.cardElevation(AppDimens.CardElevation),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardBackground),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(AppDimens.IconSize)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, fontSize = 14.sp, color = Color.Gray)
            }
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Go")
        }
    }
}

@Composable
fun FitnessTrackerPage(onBack: () -> Unit) {
    var steps by remember { mutableStateOf("") }
    var stepsError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Fitness Tracker",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = steps,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    steps = newValue
                    stepsError = false
                } else {
                    stepsError = true
                }
            },
            label = { Text("Enter Steps") },
            modifier = Modifier.fillMaxWidth(),
            isError = stepsError
        )
        if (stepsError) {
            Text(
                text = "Please enter a valid number",
                color = Color.Red,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        val progress = steps.toFloatOrNull()?.div(10000) ?: 0f
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth(),
            color = AppColors.Primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Go Back")
        }
    }
}

@Composable
fun MealPlannerPage(onBack: () -> Unit) {
    var meals by remember { mutableStateOf(listOf("Breakfast: Oats", "Lunch: Salad", "Dinner: Grilled Chicken")) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Meal Planner", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        meals.forEach {
            Text(it, fontSize = 18.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Go Back")
        }
    }
}

@Composable
fun HydrationTrackerPage(onBack: () -> Unit) {
    var glasses by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Hydration Tracker", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Glasses of water: $glasses", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = { glasses++ }) {
                Text("Add Glass")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { if (glasses > 0) glasses-- }) {
                Text("Remove Glass")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Go Back")
        }
    }
}

@Composable
fun MentalWellnessPage(onBack: () -> Unit) {
    var mood by remember { mutableStateOf("🙂") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Mental Wellness", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Current Mood: $mood", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { mood = "😊" }) {
                Text("Happy")
            }
            Button(onClick = { mood = "😞" }) {
                Text("Sad")
            }
            Button(onClick = { mood = "😡" }) {
                Text("Angry")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Go Back")
        }
    }
}
