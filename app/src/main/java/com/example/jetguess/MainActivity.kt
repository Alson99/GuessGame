package com.example.jetguess
import android.os.Bundle
import android.widget.HorizontalScrollView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetguess.ui.theme.JetGuessTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetGuessTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GameScreen()
                }
            }
        }
    }
  }


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameScreen(){

    val min = 1
    val max = 66
    val numbers = min..max
    val scrollState = rememberScrollState()
    val gradient = Brush.horizontalGradient(
        listOf(Color.Red, Color.Blue, Color.Green), 0.0f, 10000.0f, TileMode.Repeated
    )

    var generatedNumber by remember {
        mutableStateOf(Random.nextInt(min, max))
    }

    var message by remember {
        mutableStateOf("")
    }

    var answer by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                .horizontalScroll(scrollState)
                .size(width = 10000.dp, height = 200.dp)
                .background(brush = gradient)
                .fillMaxWidth()
                .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){
                Text(text = "Guess the value ", fontSize = 25.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = answer.ifEmpty { "X" }, fontSize = 35.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = message, fontSize = 25.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(10.dp))
            LazyVerticalGrid(cells = GridCells.Fixed(6) , content = {
                items(numbers.toList()) { number ->
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Button(
                            onClick = {
                                message = guestGame(
                                    providedNumber = number,
                                    generatedNumber = generatedNumber,
                                    min = min,
                                    max = max,
                                )
                                if (message.contains("Congrats ! you won the game", true)) {
                                    answer = number.toString()
                                }
                            },
                            modifier = Modifier.padding(2.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                        ) {
                            Text(
                                text = number.toString(),
                                modifier = Modifier.padding(2.dp),
                                color = Color.White,
                                fontSize = 15.sp)
                        }
                    }
                }
            }, modifier = Modifier
                .weight(7f)
                .fillMaxWidth())
        }
    }
    if (answer.isNotEmpty()){
        DialogView(message) {
            message = ""
            answer = ""
            generatedNumber = Random.nextInt(min, max)
        }
    }

}


@Composable
fun DialogView(message: String, resetGame: () -> Unit) {

    var showDialog by remember {
        mutableStateOf(true)
    }
    if (showDialog) {
        AlertDialog(onDismissRequest = { /*TODO*/ },
            title = { Text(text = "Super!") },
            text = { Text(text = message) },
            confirmButton = {
                Button(onClick = {
                    resetGame.invoke()
                    showDialog = false
                }) {
                    Text(text = "Restart")
                }
            })
        }
}

fun guestGame(providedNumber: Int, generatedNumber: Int, min: Int, max: Int) : String {
    var count = 0
    val k = 2

    while (count < k){
        if ( providedNumber !in (min..max)){
            return "The chosen number doesn't exist in Difficult level"
        }else if (providedNumber > generatedNumber){
            return "You've chosen the number which is greater than the generated one !"
        }else if (providedNumber < generatedNumber) {
            return "You've chosen the number which is less than the generated one !"
        }
        count++
        if (providedNumber == generatedNumber){
            return "Congrats ! you won the game after $count trials ! "
        }
    }
    if (count ==  k){
        return "You've made $k trials and the generatedNumber is $generatedNumber"
    }
    return " "
}



