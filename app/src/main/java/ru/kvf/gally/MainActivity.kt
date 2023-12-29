package ru.kvf.gally

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.kvf.gally.core.theme.GallyTheme
import ru.kvf.gally.feature.root.RootHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            GallyTheme {
                RootHost(navController)
            }
        }
    }
}

@Composable
fun A(
    vm: AViewModel = koinViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val state by vm.collectAsState()

    vm.collectSideEffect {
        when (it) {
            is ASideEffect.Toast -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = state.a.toString())

        Button(onClick = vm::aClick) {
            Text(text = "Increase A")
        }

        Button(onClick = vm::toast) {
            Text(text = "Toast")
        }

        Button(onClick = { navController.navigate("B") }) {
            Text(text = "Navigate")
        }
    }
}

data class AState(
    val a: Int = 0
)

sealed interface ASideEffect {
    data class Toast(val message: String) : ASideEffect
}

class AViewModel : ViewModel(), ContainerHost<AState, ASideEffect> {
    override val container: Container<AState, ASideEffect> = container(AState())

    fun aClick() = intent {
        reduce {
            state.copy(a = state.a + 1)
        }
    }

    fun toast() = intent {
        postSideEffect(ASideEffect.Toast("Hello"))
    }
}

@Composable
fun B() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
    }
}
