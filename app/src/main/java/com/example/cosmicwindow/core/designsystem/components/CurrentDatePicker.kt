package com.example.cosmicwindow.core.designsystem.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
fun CurrentDatePicker(onDateSelected: (LocalDate) -> Unit, onDismiss: () -> Unit) {
    val state =
        rememberDatePickerState(
            initialSelectedDateMillis =
                LocalDate
                    .now()
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli(),
            selectableDates =
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                        utcTimeMillis <=
                            LocalDate
                                .now()
                                .atStartOfDay(ZoneOffset.UTC)
                                .toInstant()
                                .toEpochMilli()

                    override fun isSelectableYear(year: Int): Boolean = year <= LocalDate.now().year
                },
        )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                state.selectedDateMillis?.let { millis ->
                    onDateSelected(
                        Instant
                            .ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate(),
                    )
                }
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    ) {
        DatePicker(state = state)
    }
}
