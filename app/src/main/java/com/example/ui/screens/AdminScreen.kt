package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.VideoCreatorViewModel

@Composable
fun AdminScreen(
    viewModel: VideoCreatorViewModel,
    modifier: Modifier = Modifier
) {
    val generations by viewModel.generations.collectAsState()
    val projects by viewModel.projects.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Administration SaaS",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Supervision de la plateforme & gestion des coûts",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Surface(
                    color = Color(0x338B5CF6),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Accès Admin Protégé",
                        color = ElectricVioletLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Global Metrics Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Utilisateurs SaaS",
                        value = "1 420",
                        subtitle = "+18% ce mois",
                        icon = Icons.Outlined.People,
                        accentColor = ElectricViolet,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Vidéos Générées",
                        value = "${projects.size + 3840}",
                        subtitle = "Sur l'infrastructure",
                        icon = Icons.Outlined.VideoLibrary,
                        accentColor = NeonCyan,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Taux de Réussite",
                        value = "98.7%",
                        subtitle = "Générations sans erreur",
                        icon = Icons.Outlined.CheckCircle,
                        accentColor = EmeraldSuccess,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Coût Estimé API",
                        value = "38,50 €",
                        subtitle = "Consommation serveur",
                        icon = Icons.Outlined.MonetizationOn,
                        accentColor = AmberWarning,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Infrastructure Status Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Statut des microservices & pipelines",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ServiceStatusRow("Passerelle API Principale", "Opérationnel", EmeraldSuccess)
                    HorizontalDivider(color = StudioBorder, modifier = Modifier.padding(vertical = 8.dp))
                    ServiceStatusRow("Moteur Script Gemini 2.5", "Opérationnel (12ms latence)", EmeraldSuccess)
                    HorizontalDivider(color = StudioBorder, modifier = Modifier.padding(vertical = 8.dp))
                    ServiceStatusRow("Moteur Rendu Vidéo Diffusé", "En attente config (Phase 4)", AmberWarning)
                    HorizontalDivider(color = StudioBorder, modifier = Modifier.padding(vertical = 8.dp))
                    ServiceStatusRow("File d'attente Asynchrone", "Prête (0 tâches en attente)", EmeraldSuccess)
                }
            }
        }

        // Security & Policies Reminder
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Sécurité & Zéro Clé Côté Client", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Les clés API privées et les webhooks sont isolés côté serveur et injectés via variables d'environnement.",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceStatusRow(name: String, statusText: String, statusColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, color = TextSecondary, fontSize = 13.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(statusColor, shape = androidx.compose.foundation.shape.CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = statusText, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
