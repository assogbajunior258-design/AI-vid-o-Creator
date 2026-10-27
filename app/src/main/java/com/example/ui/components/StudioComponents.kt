package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AspectRatioType
import com.example.data.model.ProjectStatus
import com.example.data.model.SceneItem
import com.example.ui.theme.*

@Composable
fun StatusBadge(status: ProjectStatus, modifier: Modifier = Modifier) {
    val (bg, fg) = when (status) {
        ProjectStatus.COMPLETED -> Pair(EmeraldSuccessBg, EmeraldSuccess)
        ProjectStatus.SCRIPT_READY -> Pair(Color(0x338B5CF6), ElectricVioletLight)
        ProjectStatus.SCRIPT_GENERATING,
        ProjectStatus.MEDIA_GENERATING,
        ProjectStatus.RENDERING -> Pair(AmberWarningBg, AmberWarning)
        ProjectStatus.FAILED -> Pair(RoseErrorBg, RoseError)
        else -> Pair(Color(0x2294A3B8), TextSecondary)
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, fg.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(fg)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = status.label,
                color = fg,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun AspectRatioBadge(aspectRatio: AspectRatioType, modifier: Modifier = Modifier) {
    Surface(
        color = StudioCardElevated,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = when (aspectRatio) {
                    AspectRatioType.VERTICAL_9_16 -> Icons.Default.PhoneAndroid
                    AspectRatioType.SQUARE_1_1 -> Icons.Default.CropSquare
                    AspectRatioType.LANDSCAPE_16_9 -> Icons.Default.Tv
                },
                contentDescription = aspectRatio.label,
                tint = NeonCyan,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = when (aspectRatio) {
                    AspectRatioType.VERTICAL_9_16 -> "9:16"
                    AspectRatioType.SQUARE_1_1 -> "1:1"
                    AspectRatioType.LANDSCAPE_16_9 -> "16:9"
                },
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = title, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = value, color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, color = TextMuted, fontSize = 11.sp)
        }
    }
}

@Composable
fun StepIndicator(
    currentStep: Int,
    onStepClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        1 to "Idée",
        2 to "Type",
        3 to "Paramètres",
        4 to "Scénario",
        5 to "Scènes",
        6 to "Médias",
        7 to "Rendu & Player"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEach { (stepNum, label) ->
                val isCurrent = stepNum == currentStep
                val isCompleted = stepNum < currentStep

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onStepClick(stepNum) }
                        .padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCurrent -> ElectricViolet
                                    isCompleted -> EmeraldSuccess
                                    else -> StudioCardElevated
                                }
                            )
                            .border(
                                1.dp,
                                if (isCurrent) ElectricVioletLight else StudioBorder,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Validé",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        } else {
                            Text(
                                text = "$stepNum",
                                color = if (isCurrent) Color.White else TextMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = label,
                        color = if (isCurrent) ElectricVioletLight else if (isCompleted) TextPrimary else TextMuted,
                        fontSize = 9.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun InteractiveVideoPlayer(
    title: String,
    scenes: List<SceneItem>,
    aspectRatio: AspectRatioType,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentSceneIndex by remember { mutableIntStateOf(0) }
    var progress by remember { mutableFloatStateOf(0.15f) }
    var showSubtitles by remember { mutableStateOf(true) }

    val currentScene = scenes.getOrNull(currentSceneIndex) ?: scenes.firstOrNull()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_video_player"),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, StudioBorder)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Video Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        when (aspectRatio) {
                            AspectRatioType.VERTICAL_9_16 -> 360.dp
                            AspectRatioType.SQUARE_1_1 -> 280.dp
                            AspectRatioType.LANDSCAPE_16_9 -> 220.dp
                        }
                    )
                    .background(Color(0xFF070A10)),
                contentAlignment = Alignment.Center
            ) {
                // Background visual asset
                Image(
                    painter = painterResource(id = R.drawable.img_sample_story),
                    contentDescription = "Rendu vidéo en prévisualisation",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Cinematic Gradient Shadow Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.75f)
                                )
                            )
                        )
                )

                // Top Badge Overlay (Format & Scene)
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AspectRatioBadge(aspectRatio = aspectRatio)

                    Surface(
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Scène ${currentSceneIndex + 1} / ${scenes.size.coerceAtLeast(1)}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Subtitles Overlay
                if (showSubtitles && currentScene != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
                            .background(
                                Color.Black.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = currentScene.narration,
                            color = NeonCyanLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Play / Pause central button overlay
                IconButton(
                    onClick = {
                        isPlaying = !isPlaying
                        if (isPlaying && currentSceneIndex >= scenes.size - 1) {
                            currentSceneIndex = 0
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(ElectricViolet.copy(alpha = 0.85f))
                        .testTag("player_play_pause_button")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Mettre en pause" else "Lire la vidéo",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Timeline Controls Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StudioCardBg)
                    .padding(12.dp)
            ) {
                // Scrubber line
                Slider(
                    value = progress,
                    onValueChange = {
                        progress = it
                        val targetScene = (it * scenes.size).toInt().coerceIn(0, (scenes.size - 1).coerceAtLeast(0))
                        currentSceneIndex = targetScene
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = ElectricViolet,
                        activeTrackColor = ElectricVioletLight,
                        inactiveTrackColor = StudioBorder
                    ),
                    modifier = Modifier.fillMaxWidth().height(24.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                if (currentSceneIndex > 0) currentSceneIndex--
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipPrevious,
                                contentDescription = "Scène précédente",
                                tint = TextPrimary
                            )
                        }

                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Lecture",
                                tint = NeonCyan
                            )
                        }

                        IconButton(
                            onClick = {
                                if (currentSceneIndex < scenes.size - 1) currentSceneIndex++
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Scène suivante",
                                tint = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "00:${(progress * 30).toInt().toString().padStart(2, '0')} / 00:30",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Subtitle toggle
                        IconButton(
                            onClick = { showSubtitles = !showSubtitles },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (showSubtitles) Icons.Default.Subtitles else Icons.Default.SubtitlesOff,
                                contentDescription = "Sous-titres",
                                tint = if (showSubtitles) NeonCyan else TextMuted
                            )
                        }

                        // Quality badge
                        Surface(
                            color = StudioCardElevated,
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                        ) {
                            Text(
                                text = "1080p HD",
                                color = TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateView(
    title: String,
    description: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(ElectricViolet.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.VideoLibrary,
                    contentDescription = null,
                    tint = ElectricViolet,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                color = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("empty_state_action_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = actionLabel, fontWeight = FontWeight.Bold)
            }
        }
    }
}
