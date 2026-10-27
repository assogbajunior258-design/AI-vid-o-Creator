package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Project
import com.example.data.model.VideoCategory
import com.example.ui.components.AspectRatioBadge
import com.example.ui.components.EmptyStateView
import com.example.ui.components.StatCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.VideoCreatorViewModel

@Composable
fun DashboardScreen(
    viewModel: VideoCreatorViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val generations by viewModel.generations.collectAsState()

    val completedCount = projects.count { it.status == com.example.data.model.ProjectStatus.COMPLETED }
    val totalScenes = projects.sumOf { it.scenes.size }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Welcome Header & Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(ElectricViolet.copy(alpha = 0.25f))
                                    .border(1.5.dp, ElectricVioletLight, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userProfile.fullName.take(2).uppercase(),
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Bonjour, ${userProfile.fullName.substringBefore(" ")} 👋",
                                    color = TextPrimary,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = userProfile.role,
                                    color = NeonCyanLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Credits chip
                        Surface(
                            color = StudioCardElevated,
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.6f)),
                            modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.BILLING) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = AmberWarning,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${userProfile.creditBalance} crédits",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Primary CTA
                    Button(
                        onClick = { viewModel.startNewProject() },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("dashboard_create_video_button")
                    ) {
                        Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Créer une nouvelle vidéo", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Stats Grid
        item {
            Column {
                Text(
                    text = "Performances du studio",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Projets créés",
                        value = "${projects.size}",
                        subtitle = "$completedCount terminés",
                        icon = Icons.Outlined.VideoLibrary,
                        accentColor = ElectricViolet,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Scènes générées",
                        value = "$totalScenes",
                        subtitle = "Sur l'ensemble",
                        icon = Icons.Outlined.MovieFilter,
                        accentColor = NeonCyan,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Opérations IA",
                        value = "${generations.size}",
                        subtitle = "Scripts & Médias",
                        icon = Icons.Outlined.AutoAwesome,
                        accentColor = AmberWarning,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Solde disponible",
                        value = "${userProfile.creditBalance}",
                        subtitle = "Crédits actifs",
                        icon = Icons.Outlined.AccountBalanceWallet,
                        accentColor = EmeraldSuccess,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Ideas Presets
        item {
            Column {
                Text(
                    text = "Démarrage rapide par inspiration",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickPresetChip(
                        emoji = "🌍",
                        title = "Histoire Africaine",
                        onClick = {
                            viewModel.startNewProject(
                                presetIdea = "Un conte africain ancestral sur la découverte d'un trésor caché sous le baobab millénaire",
                                presetCategory = VideoCategory.AFRICAN_STORY
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                    QuickPresetChip(
                        emoji = "🛍️",
                        title = "Publicité Produit",
                        onClick = {
                            viewModel.startNewProject(
                                presetIdea = "Une publicité percutante pour un sérum de beauté bio à base de karité et hibiscus",
                                presetCategory = VideoCategory.ADVERTISING
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickPresetChip(
                        emoji = "🔥",
                        title = "Motivation TikTok",
                        onClick = {
                            viewModel.startNewProject(
                                presetIdea = "Une vidéo courte inspirante sur la persévérance quotidienne et la discipline",
                                presetCategory = VideoCategory.MOTIVATION
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                    QuickPresetChip(
                        emoji = "🎓",
                        title = "Tuto Business",
                        onClick = {
                            viewModel.startNewProject(
                                presetIdea = "Comment lancer son business en ligne avec un petit budget en 2026",
                                presetCategory = VideoCategory.EDUCATION
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Recent Projects Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Projets récents",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { viewModel.navigateTo(AppScreen.PROJECTS) }) {
                    Text(text = "Voir tout (${projects.size})", color = NeonCyanLight, fontSize = 12.sp)
                }
            }
        }

        // Projects list or empty
        if (projects.isEmpty()) {
            item {
                EmptyStateView(
                    title = "Aucun projet pour le moment",
                    description = "Créez votre première vidéo assistée par IA en quelques clics.",
                    actionLabel = "Créer une vidéo",
                    onAction = { viewModel.startNewProject() }
                )
            }
        } else {
            items(projects.take(4), key = { it.id }) { project ->
                DashboardProjectCard(
                    project = project,
                    onOpen = { viewModel.openExistingProject(project) }
                )
            }
        }
    }
}

@Composable
private fun QuickPresetChip(
    emoji: String,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = StudioCardBg,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DashboardProjectCard(
    project: Project,
    onOpen: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
            .testTag("dashboard_project_card_${project.id}"),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail / Icon box
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_sample_story),
                    contentDescription = project.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Ouvrir",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusBadge(status = project.status)
                    if (project.isSampleDemo) {
                        Surface(
                            color = Color(0x33F59E0B),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Démo",
                                color = AmberWarning,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = project.title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AspectRatioBadge(aspectRatio = project.aspectRatio)
                    Text(
                        text = "${project.scenes.size} scènes • ${project.targetDurationSeconds}s",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(onClick = onOpen) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Éditer",
                    tint = TextSecondary
                )
            }
        }
    }
}
