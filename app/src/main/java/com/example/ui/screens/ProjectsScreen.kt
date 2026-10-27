package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.data.model.ProjectStatus
import com.example.ui.components.AspectRatioBadge
import com.example.ui.components.EmptyStateView
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.VideoCreatorViewModel

@Composable
fun ProjectsScreen(
    viewModel: VideoCreatorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val allProjects by viewModel.projects.collectAsState()

    val filteredProjects = remember(allProjects, uiState.searchQuery, uiState.selectedFilterStatus) {
        allProjects.filter { proj ->
            val matchesQuery = uiState.searchQuery.isBlank() ||
                    proj.title.contains(uiState.searchQuery, ignoreCase = true) ||
                    proj.description.contains(uiState.searchQuery, ignoreCase = true)
            val matchesStatus = uiState.selectedFilterStatus == null || proj.status == uiState.selectedFilterStatus
            matchesQuery && matchesStatus
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Bibliothèque de projets",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${allProjects.size} projets créés",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = { viewModel.startNewProject() },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Nouveau", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Rechercher un projet...", color = TextMuted) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextSecondary)
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Effacer", tint = TextSecondary)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("projects_search_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricViolet,
                    unfocusedBorderColor = StudioBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = StudioCardBg,
                    unfocusedContainerColor = StudioCardBg
                )
            )
        }

        // Status filter chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.selectedFilterStatus == null,
                    onClick = { viewModel.setFilterStatus(null) },
                    label = { Text("Tous") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricViolet,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = uiState.selectedFilterStatus == ProjectStatus.COMPLETED,
                    onClick = { viewModel.setFilterStatus(ProjectStatus.COMPLETED) },
                    label = { Text("Terminés") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldSuccess,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = uiState.selectedFilterStatus == ProjectStatus.DRAFT,
                    onClick = { viewModel.setFilterStatus(ProjectStatus.DRAFT) },
                    label = { Text("Brouillons") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StudioCardElevated,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // List of projects
        if (filteredProjects.isEmpty()) {
            item {
                EmptyStateView(
                    title = "Aucun projet trouvé",
                    description = if (uiState.searchQuery.isNotBlank()) "Aucun projet ne correspond à votre recherche '${uiState.searchQuery}'." else "Commencez dès maintenant en créant votre première vidéo.",
                    actionLabel = "Créer un projet",
                    onAction = { viewModel.startNewProject() }
                )
            }
        } else {
            items(filteredProjects, key = { it.id }) { project ->
                ProjectCardItem(
                    project = project,
                    onOpen = { viewModel.openExistingProject(project) },
                    onDuplicate = { viewModel.duplicateProject(project) },
                    onDelete = { viewModel.requestDeleteProject(project.id) }
                )
            }
        }
    }

    // Confirmation dialog for deletion
    if (uiState.showConfirmDeleteId != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelDeleteProject() },
            title = { Text("Supprimer le projet ?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("Cette action est irréversible. Le projet et ses scènes seront définitivement effacés.", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmDeleteProject() },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseError)
                ) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelDeleteProject() }) {
                    Text("Annuler", color = TextSecondary)
                }
            },
            containerColor = StudioCardBg
        )
    }
}

@Composable
private fun ProjectCardItem(
    project: Project,
    onOpen: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("project_item_${project.id}"),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = project.status)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AspectRatioBadge(aspectRatio = project.aspectRatio)
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(onClick = onDuplicate, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Dupliquer", tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Supprimer", tint = RoseError, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_sample_story),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.title,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = project.description,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${project.scenes.size} scènes • ${project.targetDurationSeconds}s • ${project.category.title}",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onOpen,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(42.dp)
            ) {
                Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (project.status == ProjectStatus.COMPLETED) "Lire la vidéo & Ouvrir studio" else "Continuer l'édition",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
