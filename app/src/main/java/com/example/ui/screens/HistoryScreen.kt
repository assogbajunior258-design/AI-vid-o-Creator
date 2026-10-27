package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GenerationRecord
import com.example.data.model.GenerationStatus
import com.example.ui.components.EmptyStateView
import com.example.ui.theme.*
import com.example.ui.viewmodel.VideoCreatorViewModel

@Composable
fun HistoryScreen(
    viewModel: VideoCreatorViewModel,
    modifier: Modifier = Modifier
) {
    val generations by viewModel.generations.collectAsState()
    var selectedStatus by remember { mutableStateOf<GenerationStatus?>(null) }

    val filteredList = remember(generations, selectedStatus) {
        if (selectedStatus == null) generations
        else generations.filter { it.status == selectedStatus }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Historique des générations IA",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Suivi en temps réel de tous les jobs de scénarios et de rendus",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        // Filters
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    label = { Text("Tous (${generations.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricViolet,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = selectedStatus == GenerationStatus.COMPLETED,
                    onClick = { selectedStatus = GenerationStatus.COMPLETED },
                    label = { Text("Réussis") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldSuccess,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        if (filteredList.isEmpty()) {
            item {
                EmptyStateView(
                    title = "Aucune opération dans l'historique",
                    description = "Les opérations de génération de scénarios et de vidéos apparaîtront ici.",
                    actionLabel = "Lancer une création",
                    onAction = { viewModel.startNewProject() }
                )
            }
        } else {
            items(filteredList, key = { it.id }) { item ->
                GenerationHistoryItem(record = item)
            }
        }
    }
}

@Composable
private fun GenerationHistoryItem(record: GenerationRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(record.status.colorHex).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = record.status.label,
                        color = Color(record.status.colorHex),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = "-${record.actualCostCredits} crédits",
                    color = AmberWarning,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = record.projectTitle,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fournisseur : ${record.provider} • Modèle : ${record.model}",
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Durée du traitement : ${record.durationText}",
                    color = TextMuted,
                    fontSize = 11.sp
                )
                Text(
                    text = "ID: ${record.id.take(8)}",
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}
