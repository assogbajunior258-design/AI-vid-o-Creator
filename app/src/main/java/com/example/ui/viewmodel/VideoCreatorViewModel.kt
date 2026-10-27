package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.VideoCreatorRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen(val title: String, val icon: String) {
    LANDING("Accueil", "home"),
    DASHBOARD("Tableau de bord", "dashboard"),
    STUDIO("Studio de création", "movie_creation"),
    PROJECTS("Mes Projets", "video_library"),
    HISTORY("Historique", "history"),
    BILLING("Crédits & Abonnements", "account_balance_wallet"),
    SETTINGS("Paramètres", "settings"),
    ADMIN("Administration", "admin_panel_settings")
}

data class StudioWizardState(
    val currentStep: Int = 1, // 1 to 7
    val ideaPrompt: String = "",
    val selectedCategory: VideoCategory = VideoCategory.AFRICAN_STORY,
    val selectedAspectRatio: AspectRatioType = AspectRatioType.VERTICAL_9_16,
    val selectedDurationSeconds: Int = 30,
    val selectedVisualStyle: VisualStyle = VisualStyle.CINEMATIC_REALISTIC,
    val selectedTone: NarrationTone = NarrationTone.CAPTIVATING,
    val selectedLanguage: String = "Français",
    val hasVoiceover: Boolean = true,
    val hasSubtitles: Boolean = true,
    val musicStyle: String = "Inspirant & Rythmé",
    
    // Project working draft
    val editingProjectId: String? = null,
    val scriptTitle: String = "",
    val scriptHook: String = "",
    val scriptNarration: String = "",
    val scenes: List<SceneItem> = emptyList(),

    // Generation & Render Progress
    val isGeneratingScript: Boolean = false,
    val isGeneratingMedia: Boolean = false,
    val isRendering: Boolean = false,
    val renderProgressPercent: Int = 0,
    val selectedMediaProvider: String = "prov_image_gen",
    
    // Player simulation
    val isPlayingPreview: Boolean = false,
    val currentPreviewSceneIndex: Int = 0,
    val previewProgress: Float = 0f
)

data class VideoCreatorUiState(
    val currentScreen: AppScreen = AppScreen.DASHBOARD,
    val studioState: StudioWizardState = StudioWizardState(),
    val searchQuery: String = "",
    val selectedFilterStatus: ProjectStatus? = null,
    val userNotificationMessage: String? = null,
    val showConfirmDeleteId: String? = null
)

class VideoCreatorViewModel(
    private val repository: VideoCreatorRepository = VideoCreatorRepository()
) : ViewModel() {

    val projects = repository.projects
    val userProfile = repository.userProfile
    val generations = repository.generations
    val transactions = repository.transactions
    val plans = repository.getPlans()
    val creditPacks = repository.getCreditPacks()
    val aiProviders = repository.getAiProviders()

    private val _uiState = MutableStateFlow(VideoCreatorUiState())
    val uiState: StateFlow<VideoCreatorUiState> = _uiState.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _uiState.update { it.copy(currentScreen = screen, userNotificationMessage = null) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setFilterStatus(status: ProjectStatus?) {
        _uiState.update { it.copy(selectedFilterStatus = status) }
    }

    fun clearNotification() {
        _uiState.update { it.copy(userNotificationMessage = null) }
    }

    fun showToast(message: String) {
        _uiState.update { it.copy(userNotificationMessage = message) }
    }

    fun startNewProject(presetIdea: String? = null, presetCategory: VideoCategory? = null) {
        _uiState.update {
            it.copy(
                currentScreen = AppScreen.STUDIO,
                studioState = StudioWizardState(
                    currentStep = 1,
                    ideaPrompt = presetIdea ?: "",
                    selectedCategory = presetCategory ?: VideoCategory.AFRICAN_STORY
                )
            )
        }
    }

    fun openExistingProject(project: Project) {
        _uiState.update {
            it.copy(
                currentScreen = AppScreen.STUDIO,
                studioState = StudioWizardState(
                    currentStep = if (project.status == ProjectStatus.COMPLETED) 7 else 5,
                    ideaPrompt = project.description,
                    selectedCategory = project.category,
                    selectedAspectRatio = project.aspectRatio,
                    selectedDurationSeconds = project.targetDurationSeconds,
                    selectedVisualStyle = project.visualStyle,
                    selectedTone = project.narrationTone,
                    selectedLanguage = project.language,
                    hasVoiceover = project.hasVoiceover,
                    hasSubtitles = project.hasSubtitles,
                    editingProjectId = project.id,
                    scriptTitle = project.title,
                    scriptHook = project.hook,
                    scriptNarration = project.fullNarration,
                    scenes = project.scenes
                )
            )
        }
    }

    fun setStudioStep(step: Int) {
        if (step in 1..7) {
            _uiState.update { it.copy(studioState = it.studioState.copy(currentStep = step)) }
        }
    }

    fun updateIdeaPrompt(prompt: String) {
        _uiState.update { it.copy(studioState = it.studioState.copy(ideaPrompt = prompt)) }
    }

    fun selectCategory(category: VideoCategory) {
        _uiState.update { it.copy(studioState = it.studioState.copy(selectedCategory = category)) }
    }

    fun updateParameters(
        aspectRatio: AspectRatioType,
        durationSeconds: Int,
        visualStyle: VisualStyle,
        tone: NarrationTone,
        language: String,
        hasVoiceover: Boolean,
        hasSubtitles: Boolean
    ) {
        _uiState.update {
            it.copy(
                studioState = it.studioState.copy(
                    selectedAspectRatio = aspectRatio,
                    selectedDurationSeconds = durationSeconds,
                    selectedVisualStyle = visualStyle,
                    selectedTone = tone,
                    selectedLanguage = language,
                    hasVoiceover = hasVoiceover,
                    hasSubtitles = hasSubtitles
                )
            )
        }
    }

    fun updateScriptDetails(title: String, hook: String, fullNarration: String) {
        _uiState.update {
            it.copy(
                studioState = it.studioState.copy(
                    scriptTitle = title,
                    scriptHook = hook,
                    scriptNarration = fullNarration
                )
            )
        }
    }

    fun generateScriptWithAi() {
        val studio = _uiState.value.studioState
        val promptText = studio.ideaPrompt.ifBlank { "Une histoire inspirante sur la persévérance" }
        val category = studio.selectedCategory
        val style = studio.selectedVisualStyle

        _uiState.update { it.copy(studioState = it.studioState.copy(isGeneratingScript = true)) }

        viewModelScope.launch {
            // Intelligent generation simulation based on chosen category & prompt
            delay(1600) // realistic AI inference delay

            val title: String
            val hook: String
            val narration: String
            val generatedScenes: List<SceneItem>
            val tempProjId = studio.editingProjectId ?: UUID.randomUUID().toString()

            when (category) {
                VideoCategory.AFRICAN_STORY -> {
                    title = if (promptText.contains("baobab", true)) "Le Secret du Baobab Sacré" else "L'Héritage du Royaume Oublié"
                    hook = "Ce que les ancêtres ont caché dans la pierre va tout changer..."
                    narration = "Au cœur de la savane, l'écho des tambours annonçait le retour du prince exilé. Avec courage et sagesse, il réconcilia les deux villages rivaux sous l'arbre sacré."
                    generatedScenes = listOf(
                        SceneItem(
                            projectId = tempProjId,
                            sceneNumber = 1,
                            durationSeconds = 6,
                            narration = "Le soleil couchant illumine les remparts de terre de l'ancienne cité.",
                            visualDescription = "Vue panoramique dorée sur un village fortifié en banco au soleil couchant.",
                            videoPrompt = "cinematic wide drone shot of magnificent historical West African fortress city at sunset, 8k realistic"
                        ),
                        SceneItem(
                            projectId = tempProjId,
                            sceneNumber = 2,
                            durationSeconds = 8,
                            narration = "Un chant puissant retentit, guidant les pas d'un cavalier solitaire.",
                            visualDescription = "Gros plan sur un cavalier noble en tissu indigo brodé chevauchant vers l'arche sacrée.",
                            videoPrompt = "dramatic close up of noble African rider in indigo regal robe, golden hour dust, slow motion 60fps"
                        ),
                        SceneItem(
                            projectId = tempProjId,
                            sceneNumber = 3,
                            durationSeconds = 8,
                            narration = "Devant le conseil des sages, le sceptre d'or révèle sa lumière.",
                            visualDescription = "Le sceptre royal brille d'une lueur dorée mystique au centre de la cour royale.",
                            videoPrompt = "ancient golden artifact glowing with spiritual energy in hands of elder, magical cinematic lighting"
                        ),
                        SceneItem(
                            projectId = tempProjId,
                            sceneNumber = 4,
                            durationSeconds = 8,
                            narration = "La paix est restaurée, et la légende continue de traverser les siècles.",
                            visualDescription = "Grand rassemblement festif au clair de lune, sourires et feux de joie ancestraux.",
                            videoPrompt = "joyous cultural celebration around bonfire under starry night sky, emotional community triumph, cinematic"
                        )
                    )
                }
                VideoCategory.ADVERTISING -> {
                    title = "Impact & Élégance : Votre Marque en Lumière"
                    hook = "Arrêtez de gaspiller votre temps. Voici la solution que vous attendiez."
                    narration = "Un design d'exception. Une performance inégalée. Découvrez dès aujourd'hui l'expérience qui redéfinit vos standards."
                    generatedScenes = listOf(
                        SceneItem(
                            projectId = tempProjId,
                            sceneNumber = 1,
                            durationSeconds = 5,
                            narration = "Le problème que tout le monde ignore...",
                            visualDescription = "Scène dynamique et contrastée illustrant la frustration courante des consommateurs.",
                            videoPrompt = "sleek high fashion aesthetic commercial shot, dramatic moody lighting, modern product showcase"
                        ),
                        SceneItem(
                            projectId = tempProjId,
                            sceneNumber = 2,
                            durationSeconds = 5,
                            narration = "La révélation : une formule pensée pour vous.",
                            visualDescription = "Animation 3D fluide et élégante mettant en valeur les détails du produit haut de gamme.",
                            videoPrompt = "ultra crisp product reveal with liquid physics, glowing neon accents, 4k advertising standard"
                        ),
                        SceneItem(
                            projectId = tempProjId,
                            sceneNumber = 3,
                            durationSeconds = 5,
                            narration = "Offre exclusive disponible ce mois-ci.",
                            visualDescription = "Appel à l'action percutant avec typographie néon lumineuse et packaging soigné.",
                            videoPrompt = "bold call-to-action screen with luxury packaging, stylish studio light reflections"
                        )
                    )
                }
                else -> {
                    title = promptText.take(40).ifBlank { "Création Studio IA" }
                    hook = "Voici pourquoi 99% des gens passent à côté de cette vérité..."
                    narration = "Chaque grand voyage commence par une décision courageuse. Quand vous refusez d'abandonner, l'univers conspire à votre réussite."
                    generatedScenes = listOf(
                        SceneItem(
                            projectId = tempProjId,
                            sceneNumber = 1,
                            durationSeconds = 6,
                            narration = "Le point de départ : l'étincelle qui change tout.",
                            visualDescription = "Plan cinématographique moderne avec éclairage néon violet et cyan.",
                            videoPrompt = "${style.promptModifier}, visionary modern cinematic scene with neon violet and blue tones"
                        ),
                        SceneItem(
                            projectId = tempProjId,
                            sceneNumber = 2,
                            durationSeconds = 8,
                            narration = "L'effort constant dans l'ombre.",
                            visualDescription = "Montage accéléré d'un créateur en plein travail inspiré.",
                            videoPrompt = "${style.promptModifier}, focused creator working on groundbreaking vision, dramatic atmospheric shadows"
                        ),
                        SceneItem(
                            projectId = tempProjId,
                            sceneNumber = 3,
                            durationSeconds = 8,
                            narration = "L'aboutissement : le succès mérité.",
                            visualDescription = "Victoire éclatante, horizon ouvert vers l'avenir.",
                            videoPrompt = "${style.promptModifier}, grand inspiring climax looking at the future, sunrise over futuristic skyline"
                        )
                    )
                }
            }

            _uiState.update {
                it.copy(
                    studioState = it.studioState.copy(
                        isGeneratingScript = false,
                        currentStep = 4, // move to script review
                        scriptTitle = title,
                        scriptHook = hook,
                        scriptNarration = narration,
                        scenes = generatedScenes,
                        editingProjectId = tempProjId
                    ),
                    userNotificationMessage = "Scénario généré avec succès par l'IA !"
                )
            }
        }
    }

    fun updateScene(sceneId: String, duration: Int, narration: String, visualDesc: String, prompt: String) {
        val currentScenes = _uiState.value.studioState.scenes.toMutableList()
        val index = currentScenes.indexOfFirst { it.id == sceneId }
        if (index != -1) {
            val scene = currentScenes[index]
            currentScenes[index] = scene.copy(
                durationSeconds = duration,
                narration = narration,
                visualDescription = visualDesc,
                videoPrompt = prompt
            )
            _uiState.update { it.copy(studioState = it.studioState.copy(scenes = currentScenes)) }
        }
    }

    fun addScene() {
        val studio = _uiState.value.studioState
        val projId = studio.editingProjectId ?: UUID.randomUUID().toString()
        val newNumber = studio.scenes.size + 1
        val newScene = SceneItem(
            projectId = projId,
            sceneNumber = newNumber,
            durationSeconds = 6,
            narration = "Nouvelle narration pour la scène $newNumber...",
            visualDescription = "Description visuelle cinématographique de la scène.",
            videoPrompt = "cinematic 8k shot, studio lighting, detailed atmosphere"
        )
        _uiState.update { it.copy(studioState = it.studioState.copy(scenes = studio.scenes + newScene)) }
    }

    fun deleteScene(sceneId: String) {
        val studio = _uiState.value.studioState
        val filtered = studio.scenes.filterNot { it.id == sceneId }
            .mapIndexed { idx, item -> item.copy(sceneNumber = idx + 1) }
        _uiState.update { it.copy(studioState = it.studioState.copy(scenes = filtered)) }
    }

    fun generateMediaForScenes() {
        val studio = _uiState.value.studioState
        val cost = studio.scenes.size * 10

        if (repository.userProfile.value.creditBalance < cost) {
            showToast("Solde insuffisant ($cost crédits requis). Veuillez recharger vos crédits.")
            return
        }

        _uiState.update { it.copy(studioState = it.studioState.copy(isGeneratingMedia = true)) }

        viewModelScope.launch {
            repository.deductCredits(cost, "Génération média pour '${studio.scriptTitle}' (${studio.scenes.size} scènes)")
            delay(1800)

            val updatedScenes = studio.scenes.map { it.copy(status = SceneStatus.READY) }

            repository.recordGeneration(
                projectId = studio.editingProjectId ?: "temp",
                projectTitle = studio.scriptTitle,
                provider = "Studio Image & Video Engine",
                model = "motion-v2.1",
                cost = cost,
                status = GenerationStatus.COMPLETED
            )

            _uiState.update {
                it.copy(
                    studioState = it.studioState.copy(
                        isGeneratingMedia = false,
                        scenes = updatedScenes,
                        currentStep = 7 // Move to render / final preview
                    ),
                    userNotificationMessage = "Génération des médias terminée avec succès !"
                )
            }
        }
    }

    fun launchFinalRender() {
        _uiState.update { it.copy(studioState = it.studioState.copy(isRendering = true, renderProgressPercent = 5)) }

        viewModelScope.launch {
            for (p in listOf(20, 45, 70, 90, 100)) {
                delay(400)
                _uiState.update { it.copy(studioState = it.studioState.copy(renderProgressPercent = p)) }
            }
            delay(300)

            saveCurrentProject(markCompleted = true)

            _uiState.update {
                it.copy(
                    studioState = it.studioState.copy(
                        isRendering = false,
                        renderProgressPercent = 100,
                        currentStep = 7
                    ),
                    userNotificationMessage = "Vidéo montée et rendue avec succès !"
                )
            }
        }
    }

    fun saveCurrentProject(markCompleted: Boolean = false): Project {
        val studio = _uiState.value.studioState
        val projId = studio.editingProjectId ?: UUID.randomUUID().toString()
        val totalDuration = studio.scenes.sumOf { it.durationSeconds }.coerceAtLeast(15)

        val project = Project(
            id = projId,
            title = studio.scriptTitle.ifBlank { "Projet ${studio.selectedCategory.title}" },
            description = studio.ideaPrompt.ifBlank { "Vidéo ${studio.selectedCategory.title}" },
            category = studio.selectedCategory,
            aspectRatio = studio.selectedAspectRatio,
            targetDurationSeconds = totalDuration,
            visualStyle = studio.selectedVisualStyle,
            narrationTone = studio.selectedTone,
            language = studio.selectedLanguage,
            hasVoiceover = studio.hasVoiceover,
            hasSubtitles = studio.hasSubtitles,
            status = if (markCompleted) ProjectStatus.COMPLETED else ProjectStatus.SCRIPT_READY,
            hook = studio.scriptHook,
            fullNarration = studio.scriptNarration,
            scenes = studio.scenes,
            isSampleDemo = false
        )

        repository.saveProject(project)
        _uiState.update { it.copy(studioState = it.studioState.copy(editingProjectId = projId)) }
        return project
    }

    fun duplicateProject(project: Project) {
        val copy = repository.duplicateProject(project)
        showToast("Projet dupliqué : '${copy.title}'")
    }

    fun requestDeleteProject(projectId: String) {
        _uiState.update { it.copy(showConfirmDeleteId = projectId) }
    }

    fun confirmDeleteProject() {
        val id = _uiState.value.showConfirmDeleteId ?: return
        repository.deleteProject(id)
        _uiState.update { it.copy(showConfirmDeleteId = null, userNotificationMessage = "Projet supprimé") }
    }

    fun cancelDeleteProject() {
        _uiState.update { it.copy(showConfirmDeleteId = null) }
    }

    fun purchaseCredits(pack: CreditPackage, paymentMethod: String) {
        repository.addCredits(pack.credits + pack.bonusCredits, "Recharge ${pack.credits} crédits via $paymentMethod")
        showToast("Recharge réussie ! +${pack.credits + pack.bonusCredits} crédits ajoutés via $paymentMethod.")
    }

    fun subscribePlan(plan: SubscriptionPlan) {
        repository.addCredits(plan.creditsMonthly, "Activation formule ${plan.name}")
        showToast("Formule ${plan.name} activée avec succès (+${plan.creditsMonthly} crédits) !")
    }
}
