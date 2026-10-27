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
import com.example.data.model.AiProviderInfo
import com.example.ui.theme.*
import com.example.ui.viewmodel.VideoCreatorViewModel

@Composable
fun SettingsScreen(
    viewModel: VideoCreatorViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val aiProviders = viewModel.aiProviders

    var emailNotifications by remember { mutableStateOf(true) }
    var renderAlerts by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Paramètres & Intégrations",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Gérez votre compte et consultez l'état des moteurs d'intelligence artificielle",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        // User Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Profil Utilisateur", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = userProfile.fullName,
                        onValueChange = {},
                        label = { Text("Nom complet") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = TextPrimary,
                            disabledContainerColor = StudioDarkSurface,
                            disabledBorderColor = StudioBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = userProfile.email,
                        onValueChange = {},
                        label = { Text("Adresse email") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = TextPrimary,
                            disabledContainerColor = StudioDarkSurface,
                            disabledBorderColor = StudioBorder
                        )
                    )
                }
            }
        }

        // AI Provider Status Section (Section 6 & 7 Specifications)
        item {
            Text(
                text = "Architecture des Fournisseurs d'IA",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Couche d'abstraction modulaire prête pour les phases successives de déploiement.",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        items(aiProviders) { provider ->
            ProviderStatusCard(provider = provider)
        }

        // Notification Preferences
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Préférences des Notifications", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Alertes de fin de rendu vidéo", color = TextSecondary, fontSize = 13.sp)
                        Switch(
                            checked = renderAlerts,
                            onCheckedChange = { renderAlerts = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ElectricViolet)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = StudioBorder)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Rapports d'utilisation des crédits", color = TextSecondary, fontSize = 13.sp)
                        Switch(
                            checked = emailNotifications,
                            onCheckedChange = { emailNotifications = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ElectricViolet)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProviderStatusCard(provider: AiProviderInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (provider.isConfigured) EmeraldSuccess.copy(alpha = 0.5f) else StudioBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = provider.name, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                Surface(
                    color = if (provider.isConfigured) EmeraldSuccessBg else Color(0x22F59E0B),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (provider.isConfigured) "Actif" else provider.readinessPhase,
                        color = if (provider.isConfigured) EmeraldSuccess else AmberWarning,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Type : ${provider.category.label} • Modèle : ${provider.defaultModel}",
                color = NeonCyanLight,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = provider.statusNote,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}
