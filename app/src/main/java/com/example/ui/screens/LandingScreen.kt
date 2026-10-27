package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.VideoCreatorViewModel

@Composable
fun LandingScreen(
    viewModel: VideoCreatorViewModel,
    modifier: Modifier = Modifier
) {
    var expandedFaqIndex by remember { mutableIntStateOf(-1) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_studio),
                    contentDescription = "Studio IA Création Vidéo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    StudioDarkBg.copy(alpha = 0.85f),
                                    StudioDarkBg
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Surface(
                        color = ElectricViolet.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricVioletLight)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyanLight, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Studio SaaS Vidéo IA Pro", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Transformez vos idées en vidéos virales grâce à l'IA",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 28.sp
                    )
                }
            }
        }

        // Subtitle & Action buttons
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Générez des scripts captivants, découpez automatiquement vos scènes, produisez vos visuels et exportez des vidéos prêtes pour TikTok, Reels et YouTube Shorts.",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.startNewProject() },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("landing_start_button")
                    ) {
                        Icon(imageVector = Icons.Default.MovieCreation, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Créer une vidéo", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    OutlinedButton(
                        onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(text = "Tableau de bord", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Section: Comment ça marche (5 étapes claires)
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Comment ça marche ?",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "De l'idée brute au téléchargement final en 5 minutes",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                val steps = listOf(
                    Triple("1. Décrivez votre idée", "Tapez une simple phrase ou utilisez nos suggestions (conte africain, pub e-commerce, motivation).", Icons.Default.EditNote),
                    Triple("2. Choix du format & style", "Formats TikTok 9:16, Carré 1:1 ou Paysage 16:9 avec des styles cinématographiques ou 3D.", Icons.Default.Tune),
                    Triple("3. Scénarisation IA", "L'IA génère un titre percutant, une accroche accrocheuse et un découpage scène par scène.", Icons.Default.Psychology),
                    Triple("4. Visuels & Voix off", "Générez les rendus visuels correspondants et ajoutez une voix de synthèse captivante.", Icons.Default.AutoAwesome),
                    Triple("5. Montage & Export", "Prévisualisez le résultat final avec sous-titres animés et téléchargez la vidéo HD.", Icons.Default.Download)
                )

                steps.forEachIndexed { idx, (title, desc, icon) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ElectricViolet.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = desc, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                            }
                        }
                    }
                }
            }
        }

        // Section: Cas d'usage réels
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Conçu pour tous les créateurs",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    UseCaseCard(
                        emoji = "🌍",
                        title = "Contes & Histoires",
                        desc = "Légendes africaines, fables et récits immersifs viraux.",
                        modifier = Modifier.weight(1f)
                    )
                    UseCaseCard(
                        emoji = "🛍️",
                        title = "Publicités E-commerce",
                        desc = "Vidéos de vente pour cosmétiques, vêtements, produits.",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    UseCaseCard(
                        emoji = "🔥",
                        title = "Shorts TikTok",
                        desc = "Mindset, motivation et citations à fort taux de rétention.",
                        modifier = Modifier.weight(1f)
                    )
                    UseCaseCard(
                        emoji = "🎓",
                        title = "Formations & Tutos",
                        desc = "Conseils entrepreneuriat et explications claires.",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section: FAQ
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Questions Fréquentes (FAQ)",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                val faqs = listOf(
                    "Comment fonctionne le calcul des crédits ?" to "La génération d'un script coûte 10 à 15 crédits. Chaque scène visuelle générée consomme 10 crédits. Le montage et l'export sont inclus dans le coût de la génération.",
                    "Puis-je modifier le texte des scènes générées ?" to "Oui, absolument ! Le scénario ainsi que la narration et les prompts de chaque scène sont entièrement modifiables avant le rendu.",
                    "Quels moyens de paiement sont supportés ?" to "Nous intégrons les cartes bancaires ainsi que les paiements Mobile Money (Orange Money, MTN MoMo, Wave) très populaires en Afrique francophone.",
                    "Ai-je les droits commerciaux sur les vidéos ?" to "Oui, toutes les vidéos créées avec les formules Pro et Studio peuvent être monétisées librement sur vos réseaux sociaux et vos boutiques."
                )

                faqs.forEachIndexed { index, (q, a) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                expandedFaqIndex = if (expandedFaqIndex == index) -1 else index
                            },
                        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = q,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = if (expandedFaqIndex == index) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            AnimatedVisibility(visible = expandedFaqIndex == index) {
                                Column {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = a,
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UseCaseCard(
    emoji: String,
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = desc, color = TextSecondary, fontSize = 11.sp, lineHeight = 15.sp)
        }
    }
}
