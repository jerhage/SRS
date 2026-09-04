package com.example.srs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.srs.data.SrsDataStore
import com.example.srs.data.database.AndroidDatabaseDriverFactory
import com.example.srs.feature.study.StudyViewModel
import com.example.srs.study.AndroidNewDeckFactory
import com.example.srs.study.StudyScreen

class MainActivity : ComponentActivity() {
    private lateinit var dataStore: SrsDataStore
    private lateinit var studyViewModel: StudyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        dataStore = SrsDataStore(AndroidDatabaseDriverFactory(applicationContext))
        studyViewModel = StudyViewModel(dataStore.decks, AndroidNewDeckFactory())

        setContent {
            StudyScreen(studyViewModel)
        }
    }

    override fun onDestroy() {
        studyViewModel.close()
        super.onDestroy()
    }
}
