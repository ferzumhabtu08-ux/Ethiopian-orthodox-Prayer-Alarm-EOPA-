package com.ethiopianorthodox.prayeralarm

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

data class Prayer(
    val name: String,
    val amharic: String,
    val hour: Int,
    val minute: Int
)

val prayers = listOf(
    Prayer("Morning Prayer", "የንጋት ጸሎት", 6, 0),
    Prayer("Prime", "የጠዋት ጸሎት", 9, 0),
    Prayer("Terce", "የሶስተኛው ሰዓት", 12, 0),
    Prayer("Sext", "የስድስተኛው ሰዓት", 15, 0),
    Prayer("None", "የዘጠነኛው ሰዓት", 18, 0),
    Prayer("Vespers", "የምሽት ጸሎት", 19, 0),
    Prayer("Compline", "የሌሊት ጸሎት", 21, 0)
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                PrayerHome()
            }
        }
    }
}

@Composable
fun PrayerHome() {

    val context = LocalContext.current

    var alarmsEnabled by remember {
        mutableStateOf(true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {

            Text(
                text = "የጸሎት ደወል",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Ethiopian Orthodox Prayer Bell",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "የጸሎት ማንቂያ",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "በተወሰነው የጸሎት ጊዜ የደወል ማሳወቂያ ይሰጣል።"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "ማንቂያዎች",
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = alarmsEnabled,
                            onCheckedChange = {

                                alarmsEnabled = it

                                if (it) {
                                    AlarmScheduler.scheduleAll(context)
                                } else {
                                    AlarmScheduler.cancelAll(context)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "የዕለቱ የጸሎት ጊዜያት",
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(prayers) { prayer ->

            PrayerCard(prayer)
        }

        item {

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    val alarmManager =
                        context.getSystemService(
                            Context.ALARM_SERVICE
                        ) as AlarmManager

                    if (
                        android.os.Build.VERSION.SDK_INT >= 31 &&
                        !alarmManager.canScheduleExactAlarms()
                    ) {

                        context.startActivity(
                            Intent(
                                android.provider.Settings
                                    .ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                            )
                        )

                    } else {

                        AlarmScheduler.scheduleAll(context)
                    }
                }
            ) {

                Text("🔔 የጸሎት ደወሎችን አንቃ")
            }
        }
    }
}

@Composable
fun PrayerCard(prayer: Prayer) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = prayer.amharic,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = prayer.name
                )
            }

            Text(
                text = String.format(
                    "%02d:%02d",
                    prayer.hour,
                    prayer.minute
                ),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
