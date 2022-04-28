package ru.qveex.godudego.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


@Composable
fun AlertDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    Column {
        val openDialog = remember { mutableStateOf(true) }

        if (openDialog.value) {
            androidx.compose.material.AlertDialog(
                onDismissRequest = { },
                title = { Text(text = title) },
                text = { Text(text = text) },
                confirmButton = {
                    Button(
                        onClick = {
                            onConfirm()
                            openDialog.value = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                            onDismiss()
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}