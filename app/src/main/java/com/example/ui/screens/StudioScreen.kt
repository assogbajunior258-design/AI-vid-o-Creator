package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.InteractiveVideoPlayer
import com.example.ui.components.StatusBadge
import com.example.ui.components.StepIndicator
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.VideoCreatorViewModel

@Composable
fun StudioScreen(
    viewModel: VideoCreatorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val studio = uiState.studioState
    val userProfile by viewModel.userProfile.collectAsState()

    var editingSceneItem by remember { mutableStateOf<SceneItem?>(null) }
    var showExportSuccessDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
    ) {
        // Top Studio Bar
        Surface(
            color = StudioDarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour", tint = TextPrimary)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = if (studio.scriptTitle.isNotBlank()) studio.scriptTitle else "Nouveau projet vidéo",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 200.dp)
                        )
                        Text(
                            text = "Étape ${studio.currentStep} sur 7 • ${studio.selectedCategory.title}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Balance display
                Surface(
                    color = StudioCardBg,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${userProfile.creditBalance}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Stepper Navigation Strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(StudioCardBg)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            StepIndicator(
                currentStep = studio.currentStep,
                onStepClick = { step -> viewModel.setStudioStep(step) }
            )
        }

        // Main Step Content
        Box(modifier = Modifier.weight(1f)) {
            when (studio.currentStep) {
                1 -> Step1DefineIdea(viewModel)
                2 -> Step2ChooseType(viewModel)
                3 -> Step3SetParameters(viewModel)
                4 -> Step4AiScript(viewModel)
                5 -> Step5ScenesBoard(
                    viewModel = viewModel,
                    onEditScene = { editingSceneItem = it }
                )
                6 -> Step6MediaGeneration(viewModel)
                7 -> Step7FinalPreviewAndExport(
                    viewModel = viewModel,
                    onExportSuccess = { showExportSuccessDialog = true }
                )
            }
        }

        // Bottom Navigation Buttons (Suivant / Précédent)
        Surface(
            color = StudioDarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (studio.currentStep > 1) {
                    OutlinedButton(
                        onClick = { viewModel.setStudioStep(studio.currentStep - 1) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Précédent", fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                if (studio.currentStep < 7) {
                    Button(
                        onClick = {
                            if (studio.currentStep == 3 && studio.scenes.isEmpty()) {
                                viewModel.generateScriptWithAi()
                            } else {
                                viewModel.setStudioStep(studio.currentStep + 1)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("studio_next_step_button")
                    ) {
                        Text(
                            text = if (studio.currentStep == 3 && studio.scenes.isEmpty()) "Générer le script IA" else "Étape suivante",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                } else {
                    Button(
                        onClick = {
                            viewModel.saveCurrentProject(markCompleted = true)
                            viewModel.navigateTo(AppScreen.PROJECTS)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("studio_finish_project_button")
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Enregistrer & Quitter", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Modal Sheet for Editing Scene Details
    editingSceneItem?.let { scene ->
        SceneEditDialog(
            scene = scene,
            onDismiss = { editingSceneItem = null },
            onSave = { duration, narration, visualDesc, prompt ->
                viewModel.updateScene(scene.id, duration, narration, visualDesc, prompt)
                editingSceneItem = null
            }
        )
    }

    // Export Success Dialog
    if (showExportSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showExportSuccessDialog = false },
            confirmButton = {
                Button(
                    onClick = { showExportSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet)
                ) {
                    Text("Super !")
                }
            },
            title = { Text("Export Réussi", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Votre vidéo HD a été assemblée et sauvegardée avec succès dans votre bibliothèque de projets.",
                    color = TextSecondary
                )
            },
            containerColor = StudioCardBg
        )
    }
}

// ----------------------------------------------------
// STEP 1: DEFINE IDEA
// ----------------------------------------------------
@Composable
private fun Step1DefineIdea(viewModel: VideoCreatorViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val studio = uiState.studioState

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Étape 1 : Décrivez votre idée de vidéo",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Exprimez librement votre vision. L'intelligence artificielle transformera cette description en un scénario complet découpé en scènes.",
                color = TextSecondary,
                fontSize = 13.sp
            )
        }

        item {
            OutlinedTextField(
                value = studio.ideaPrompt,
                onValueChange = { viewModel.updateIdeaPrompt(it) },
                placeholder = {
                    Text("Ex: Une histoire africaine de trahison et de rédemption, avec un sage sous un baobab sacré...", color = TextMuted)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .testTag("studio_idea_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricViolet,
                    unfocusedBorderColor = StudioBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = StudioCardBg,
                    unfocusedContainerColor = StudioCardBg
                ),
                shape = RoundedCornerShape(16.dp)
            )
        }

        item {
            Text(
                text = "Idées populaires à explorer en 1 clic :",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            val suggestions = listOf(
                "Un conte traditionnel d'Afrique sur un chasseur téméraire découvrant la vérité sur la saison des pluies",
                "Une publicité percutante pour un sérum cosmétique bio hydratant à base de beurre de karité",
                "Vidéo motivationnelle virale sur la règle des 1% de progrès chaque matin",
                "Présentation éducative en 60 secondes : comment fonctionne l'IA générative"
            )

            suggestions.forEach { text ->
                Surface(
                    color = StudioCardBg,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.updateIdeaPrompt(text) }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = text, color = TextPrimary, fontSize = 12.sp, lineHeight = 16.sp)
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// STEP 2: CHOOSE VIDEO TYPE
// ----------------------------------------------------
@Composable
private fun Step2ChooseType(viewModel: VideoCreatorViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val studio = uiState.studioState
    val categories = VideoCategory.entries

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Étape 2 : Choisissez le type de vidéo",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Le type de vidéo guide la structure narrative, le rythme des scènes et le style d'accroche.",
                color = TextSecondary,
                fontSize = 13.sp
            )
        }

        items(categories) { category ->
            val isSelected = studio.selectedCategory == category
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectCategory(category) }
                    .testTag("category_card_${category.name}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) ElectricViolet.copy(alpha = 0.2f) else StudioCardBg
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (isSelected) ElectricVioletLight else StudioBorder
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = category.iconEmoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = category.title,
                            color = if (isSelected) ElectricVioletLight else TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = category.description,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Sélectionné",
                            tint = NeonCyanLight,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// STEP 3: STUDIO PARAMETERS
// ----------------------------------------------------
@Composable
private fun Step3SetParameters(viewModel: VideoCreatorViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val studio = uiState.studioState

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Étape 3 : Paramètres de réalisation",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ajustez le format d'affichage, la durée et l'esthétique générale.",
                color = TextSecondary,
                fontSize = 13.sp
            )
        }

        // Format / Aspect Ratio
        item {
            Text(text = "Format & Ratio", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AspectRatioType.entries.forEach { ratio ->
                    val isSelected = studio.selectedAspectRatio == ratio
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                viewModel.updateParameters(
                                    ratio,
                                    studio.selectedDurationSeconds,
                                    studio.selectedVisualStyle,
                                    studio.selectedTone,
                                    studio.selectedLanguage,
                                    studio.hasVoiceover,
                                    studio.hasSubtitles
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) ElectricViolet.copy(alpha = 0.25f) else StudioCardBg
                        ),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (isSelected) ElectricVioletLight else StudioBorder
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = when (ratio) {
                                    AspectRatioType.VERTICAL_9_16 -> Icons.Default.PhoneAndroid
                                    AspectRatioType.SQUARE_1_1 -> Icons.Default.CropSquare
                                    AspectRatioType.LANDSCAPE_16_9 -> Icons.Default.Tv
                                },
                                contentDescription = null,
                                tint = if (isSelected) NeonCyanLight else TextSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = when (ratio) {
                                    AspectRatioType.VERTICAL_9_16 -> "9:16 Vertical"
                                    AspectRatioType.SQUARE_1_1 -> "1:1 Carré"
                                    AspectRatioType.LANDSCAPE_16_9 -> "16:9 Paysage"
                                },
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Target Duration
        item {
            Text(text = "Durée cible", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(15, 30, 60, 90).forEach { dur ->
                    val isSelected = studio.selectedDurationSeconds == dur
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                viewModel.updateParameters(
                                    studio.selectedAspectRatio,
                                    dur,
                                    studio.selectedVisualStyle,
                                    studio.selectedTone,
                                    studio.selectedLanguage,
                                    studio.hasVoiceover,
                                    studio.hasSubtitles
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) ElectricViolet.copy(alpha = 0.25f) else StudioCardBg
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) ElectricVioletLight else StudioBorder
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${dur}s",
                                color = if (isSelected) Color.White else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Visual Style
        item {
            Text(text = "Style visuel", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                VisualStyle.entries.forEach { style ->
                    val isSelected = studio.selectedVisualStyle == style
                    Surface(
                        color = if (isSelected) ElectricViolet.copy(alpha = 0.2f) else StudioCardBg,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) ElectricVioletLight else StudioBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.updateParameters(
                                    studio.selectedAspectRatio,
                                    studio.selectedDurationSeconds,
                                    style,
                                    studio.selectedTone,
                                    studio.selectedLanguage,
                                    studio.hasVoiceover,
                                    studio.hasSubtitles
                                )
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = style.title,
                                color = if (isSelected) ElectricVioletLight else TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (isSelected) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = NeonCyanLight, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // Audio & Subtitles Switches
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Voix off IA", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("Synthèse vocale réaliste", color = TextSecondary, fontSize = 12.sp)
                        }
                        Switch(
                            checked = studio.hasVoiceover,
                            onCheckedChange = {
                                viewModel.updateParameters(
                                    studio.selectedAspectRatio,
                                    studio.selectedDurationSeconds,
                                    studio.selectedVisualStyle,
                                    studio.selectedTone,
                                    studio.selectedLanguage,
                                    it,
                                    studio.hasSubtitles
                                )
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ElectricViolet)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = StudioBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Sous-titres dynamiques", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("Affichage synchronisé à l'écran", color = TextSecondary, fontSize = 12.sp)
                        }
                        Switch(
                            checked = studio.hasSubtitles,
                            onCheckedChange = {
                                viewModel.updateParameters(
                                    studio.selectedAspectRatio,
                                    studio.selectedDurationSeconds,
                                    studio.selectedVisualStyle,
                                    studio.selectedTone,
                                    studio.selectedLanguage,
                                    studio.hasVoiceover,
                                    it
                                )
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ElectricViolet)
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// STEP 4: AI SCRIPT & BREAKDOWN
// ----------------------------------------------------
@Composable
private fun Step4AiScript(viewModel: VideoCreatorViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val studio = uiState.studioState

    if (studio.isGeneratingScript) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = ElectricViolet, strokeWidth = 3.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Génération du scénario en cours...",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "L'IA analyse le style et découpe l'histoire en scènes",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Étape 4 : Scénario IA généré",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { viewModel.generateScriptWithAi() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Régénérer", tint = NeonCyanLight)
                    }
                }
                Text(
                    text = "Vous pouvez corriger ou enrichir le texte directement avant de passer au découpage des scènes.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            item {
                Text(text = "Titre du projet", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = studio.scriptTitle,
                    onValueChange = { viewModel.updateScriptDetails(it, studio.scriptHook, studio.scriptNarration) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
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

            item {
                Text(text = "Accroche percutante (Hook)", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = studio.scriptHook,
                    onValueChange = { viewModel.updateScriptDetails(studio.scriptTitle, it, studio.scriptNarration) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
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

            item {
                Text(text = "Narration globale", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = studio.scriptNarration,
                    onValueChange = { viewModel.updateScriptDetails(studio.scriptTitle, studio.scriptHook, it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    shape = RoundedCornerShape(12.dp),
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

            item {
                Surface(
                    color = StudioCardBg,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Checklist, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "${studio.scenes.size} scènes générées et prêtes pour la personnalisation visuelle.",
                            color = TextPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// STEP 5: SCENES BOARD & EDITING
// ----------------------------------------------------
@Composable
private fun Step5ScenesBoard(
    viewModel: VideoCreatorViewModel,
    onEditScene: (SceneItem) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val studio = uiState.studioState

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
                        text = "Étape 5 : Gestion des scènes",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Modifier, supprimer ou ajouter des scènes",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = { viewModel.addScene() },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ajouter", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        itemsIndexed(studio.scenes, key = { _, s -> s.id }) { index, scene ->
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                        Surface(
                            color = ElectricViolet.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Scène ${index + 1} • ${scene.durationSeconds}s",
                                color = ElectricVioletLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Row {
                            IconButton(onClick = { onEditScene(scene) }, modifier = Modifier.size(32.dp)) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Modifier", tint = NeonCyanLight, modifier = Modifier.size(18.dp))
                            }
                            if (studio.scenes.size > 1) {
                                IconButton(onClick = { viewModel.deleteScene(scene.id) }, modifier = Modifier.size(32.dp)) {
                                    Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Supprimer", tint = RoseError, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Narration :", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = scene.narration, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Visuel :", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = scene.visualDescription, color = TextMuted, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = StudioDarkBg,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Prompt IA : ${scene.videoPrompt}",
                            color = NeonCyan.copy(alpha = 0.8f),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(8.dp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// STEP 6: MEDIA GENERATION
// ----------------------------------------------------
@Composable
private fun Step6MediaGeneration(viewModel: VideoCreatorViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val studio = uiState.studioState
    val userProfile by viewModel.userProfile.collectAsState()
    val cost = studio.scenes.size * 10

    if (studio.isGeneratingMedia) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = NeonCyan, strokeWidth = 3.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Génération des médias en cours...",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Traitement asynchrone des scènes visuelles (IA Diffusion)",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Étape 6 : Génération des Médias",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Sélectionnez le fournisseur IA et validez la déduction de vos crédits.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Devis transparent de génération", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Nombre de scènes :", color = TextSecondary, fontSize = 13.sp)
                            Text(text = "${studio.scenes.size}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Coût par scène :", color = TextSecondary, fontSize = 13.sp)
                            Text(text = "10 crédits", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total requis :", color = AmberWarning, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = "$cost crédits", color = AmberWarning, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Votre solde actuel :", color = TextSecondary, fontSize = 13.sp)
                            Text(text = "${userProfile.creditBalance} crédits", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = { viewModel.generateMediaForScenes() },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("launch_media_generation_button")
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Confirmer & Générer ($cost crédits)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ----------------------------------------------------
// STEP 7: PREVIEW & FINAL EXPORT
// ----------------------------------------------------
@Composable
private fun Step7FinalPreviewAndExport(
    viewModel: VideoCreatorViewModel,
    onExportSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val studio = uiState.studioState

    if (studio.isRendering) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                CircularProgressIndicator(
                    progress = { studio.renderProgressPercent / 100f },
                    color = NeonCyan,
                    strokeWidth = 6.dp,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Montage & Assemblage en cours...",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${studio.renderProgressPercent}% • Synchronisation audio & sous-titres",
                    color = NeonCyanLight,
                    fontSize = 13.sp
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Étape 7 : Prévisualisation & Export",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Visualisez le rendu final interactif avec sous-titres synchronisés.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            // Interactive Video Player Simulator
            item {
                InteractiveVideoPlayer(
                    title = studio.scriptTitle,
                    scenes = studio.scenes,
                    aspectRatio = studio.selectedAspectRatio
                )
            }

            // Actions bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.launchFinalRender() },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("studio_render_video_button")
                    ) {
                        Icon(imageVector = Icons.Default.MovieFilter, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Relancer rendu", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Button(
                        onClick = onExportSuccess,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("studio_download_button")
                    ) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Télécharger HD", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SceneEditDialog(
    scene: SceneItem,
    onDismiss: () -> Unit,
    onSave: (duration: Int, narration: String, visualDesc: String, prompt: String) -> Unit
) {
    var duration by remember { mutableIntStateOf(scene.durationSeconds) }
    var narration by remember { mutableStateOf(scene.narration) }
    var visualDesc by remember { mutableStateOf(scene.visualDescription) }
    var prompt by remember { mutableStateOf(scene.videoPrompt) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Modifier la Scène ${scene.sceneNumber}",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Durée (secondes)", color = TextSecondary, fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(4, 6, 8, 10).forEach { s ->
                        FilterChip(
                            selected = duration == s,
                            onClick = { duration = s },
                            label = { Text("${s}s") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricViolet,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Text(text = "Texte de narration", color = TextSecondary, fontSize = 12.sp)
                OutlinedTextField(
                    value = narration,
                    onValueChange = { narration = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = StudioCardBg,
                        unfocusedContainerColor = StudioCardBg
                    )
                )

                Text(text = "Description visuelle", color = TextSecondary, fontSize = 12.sp)
                OutlinedTextField(
                    value = visualDesc,
                    onValueChange = { visualDesc = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = StudioCardBg,
                        unfocusedContainerColor = StudioCardBg
                    )
                )

                Text(text = "Prompt IA vidéo", color = TextSecondary, fontSize = 12.sp)
                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = StudioCardBg,
                        unfocusedContainerColor = StudioCardBg
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(duration, narration, visualDesc, prompt) },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet)
            ) {
                Text("Enregistrer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler", color = TextSecondary)
            }
        },
        containerColor = StudioDarkSurface
    )
}
