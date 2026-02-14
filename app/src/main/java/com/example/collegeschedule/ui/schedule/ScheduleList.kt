package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.LessonDto
import com.example.collegeschedule.data.dto.LessonGroupPart
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.utils.formatDate

@Composable
fun ScheduleList(data: List<ScheduleByDateDto>) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        items(data) { day ->

            Spacer(modifier = Modifier.height(24.dp))

            // Заголовок дня
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = day.weekday.uppercase(),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = formatDate(day.lessonDate),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            day.lessons.forEach { lesson ->

                LessonItem(lesson)

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun LessonItem(lesson: LessonDto) {

    val info = lesson.groupParts[LessonGroupPart.FULL]


    Column {

        Text(
            text = "${lesson.lessonNumber} • ${lesson.time}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = info?.subject ?: "",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = info?.teacher ?: "",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = listOfNotNull(info?.building, info?.classroom)
                .joinToString(", "),
            style = MaterialTheme.typography.bodySmall
        )

    }
}


