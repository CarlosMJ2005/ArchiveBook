package com.example.pmdm.archivebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pmdm.archivebook.presentation.navigationroot.NavigationRoot
import com.example.pmdm.archivebook.ui.theme.ArchiveBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArchiveBookTheme {
                NavigationRoot()
            }
        }
    }
}
