import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daveace.taskie.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimePickerTextField(
    modifier: Modifier,
    initialDateTimeString: String = "",
    onDateTimeSelected: (LocalDateTime) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var selectedDateTimeString by remember { mutableStateOf(initialDateTimeString) }

    fun setSelectedDateTimeString(dateTimeString: String) {
        selectedDateTimeString = dateTimeString
    }

    OutlinedTextField(
        value = selectedDateTimeString,
        onValueChange = {},
        label = { Text(stringResource(R.string.task_date_and_time)) },
        readOnly = true,
        shape = RoundedCornerShape(10.dp),
        trailingIcon = {
            IconButton(onClick = {

                DatePickerDialog(
                    ContextThemeWrapper(context, R.style.TaskieDateTimePickerTheme),
                    { _, year, month, dayOfMonth ->
                        TimePickerDialog(
                            ContextThemeWrapper(context, R.style.TaskieDateTimePickerTheme),
                            { _, hour, minute ->
                                val dateTime =
                                    LocalDateTime.of(year, month + 1, dayOfMonth, hour, minute)
                                setSelectedDateTimeString(
                                    dateTime.format(
                                        DateTimeFormatter.ofPattern(
                                            "HH:mm:ss, EEEE, dd/MM/yyyy"
                                        )
                                    )
                                )
                                onDateTimeSelected(dateTime)
                            },
                            calendar.get(Calendar.HOUR),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = stringResource(R.string.select_date_and_time)
                )
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimePickerTextFields(
    modifier: Modifier,
    initialDateTime: LocalDateTime = LocalDateTime.now(),
    onDateTimeSelected: (LocalDateTime) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var selectedDateString by remember {
        mutableStateOf(
            convertDateToString(
                initialDateTime,
                "dd/MM/yyyy"
            )
        )
    }
    var selectedTimeString by remember {
        mutableStateOf(
            convertDateToString(
                initialDateTime,
                "HH:mm:ss"
            )
        )
    }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var selectedDateTime by remember { mutableStateOf(LocalDateTime.now()) }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        OutlinedTextField(
            value = selectedDateString,
            onValueChange = {},
            label = { Text(stringResource(R.string.select_date)) },
            readOnly = true,
            shape = RoundedCornerShape(10.dp),
            trailingIcon = {
                IconButton(onClick = {
                    DatePickerDialog(
                        ContextThemeWrapper(
                            context,
                            R.style.TaskieDateTimePickerTheme
                        ),
                        { _, year, month, day ->
                            selectedDate = LocalDate.of(year, month + 1, day)
                            selectedDateString = selectedDate.format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            )
                            selectedDateTime = LocalDateTime.of(
                                selectedDate, LocalTime.now()
                            )
                            onDateTimeSelected(selectedDateTime)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = stringResource(R.string.select_date)
                    )
                }
            },
            modifier = modifier.weight(1F)
        )

        OutlinedTextField(
            value = selectedTimeString,
            onValueChange = {},
            label = { Text(stringResource(R.string.select_time)) },
            readOnly = true,
            shape = RoundedCornerShape(10.dp),
            trailingIcon = {
                IconButton(onClick = {
                    TimePickerDialog(
                        ContextThemeWrapper(context, R.style.TaskieDateTimePickerTheme),
                        { _, hour, minute ->
                            selectedTime = LocalTime.of(hour, minute)
                            selectedDateTime = LocalDateTime.of(selectedDate, selectedTime)
                            selectedTimeString = selectedTime.format(
                                DateTimeFormatter.ofPattern(
                                    "HH:mm:ss",
                                    Locale.getDefault()
                                )
                            )
                            onDateTimeSelected(selectedDateTime)
                        },
                        calendar.get(Calendar.HOUR),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.clock),
                        contentDescription = stringResource(R.string.select_time)
                    )
                }
            },
            modifier = modifier.weight(1F)
        )
    }
}

fun convertDateToString(date: LocalDateTime, pattern: String): String =
    date.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))