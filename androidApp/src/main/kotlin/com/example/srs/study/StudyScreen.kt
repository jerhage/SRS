package com.example.srs.study

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.srs.feature.study.StudyState
import com.example.srs.feature.study.StudyViewModel
import kotlinx.coroutines.launch

@Composable
fun StudyScreen(viewModel: StudyViewModel) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        viewModel.refresh()
    }

    StudyScreen(
        state = state,
        onDeckNameChanged = viewModel::onDeckNameChanged,
        onCreateDeck = { scope.launch { viewModel.createDeck() } },
        onRefresh = { scope.launch { viewModel.refresh() } },
    )
}

@Composable
private fun StudyScreen(
    state: StudyState,
    onDeckNameChanged: (String) -> Unit,
    onCreateDeck: () -> Unit,
    onRefresh: () -> Unit,
) {
    val errorMessage = state.errorMessage

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Text("Study", style = MaterialTheme.typography.headlineMedium)

        TextField(
            value = state.deckName,
            onValueChange = onDeckNameChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("New deck name") },
            isError = state.deckNameError != null,
            supportingText = state.deckNameError?.let { error -> { Text(error) } },
            singleLine = true,
        )

        Button(
            onClick = onCreateDeck,
            enabled = !state.isCreatingDeck,
        ) {
            Text(if (state.isCreatingDeck) "Creating…" else "Create deck")
        }

        when {
            state.isLoading -> Text("Loading decks…")
            errorMessage != null -> Text(errorMessage)
            state.decks.isEmpty() -> Text("No decks yet.")
            else -> Column(modifier = Modifier.fillMaxWidth()) {
                state.decks.forEach { deck -> Text(deck.name, style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Button(onClick = onRefresh, enabled = !state.isLoading) {
            Text("Refresh")
        }
    }
}
