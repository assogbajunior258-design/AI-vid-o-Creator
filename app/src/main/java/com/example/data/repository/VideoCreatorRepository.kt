package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class VideoCreatorRepository {

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            id = "usr_creator_01",
            fullName = "Junior Assogba",
            email = "assogbajunior258@gmail.com",
            role = "Créateur Pro",
            planId = "plan_pro",
            creditBalance = 240
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _generations = MutableStateFlow<List<GenerationRecord>>(emptyList())
    val generations: StateFlow<List<GenerationRecord>> = _generations.asStateFlow()

    private val _transactions = MutableStateFlow<List<CreditTransaction>>(emptyList())
    val transactions: StateFlow<List<CreditTransaction>> = _transactions.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val demoProjectId = "proj_demo_african_story"
        val sampleAfricanProject = Project(
            id = demoProjectId,
            title = "Le Secret du Baobab Sacré",
            description = "Un jeune chasseur brave l'interdit des anciens et découvre la vérité sur la disparition de la pluie.",
            category = VideoCategory.AFRICAN_STORY,
            aspectRatio = AspectRatioType.VERTICAL_9_16,
            targetDurationSeconds = 30,
            visualStyle = VisualStyle.CINEMATIC_REALISTIC,
            narrationTone = NarrationTone.CAPTIVATING,
            language = "Français",
            hasVoiceover = true,
            hasSubtitles = true,
            status = ProjectStatus.COMPLETED,
            hook = "Il y a 300 ans, un secret a été enterré sous le baobab... jusqu'à ce matin.",
            fullNarration = "Dans les terres rouges du Sahel, la sécheresse menaçait le village de Koumi. Seul Kofi osait s'approcher du grand baobab millénaire. Les racines murmurèrent son nom, dévoilant la source d'eau sacrée cachée sous la roche.",
            isSampleDemo = true,
            scenes = listOf(
                SceneItem(
                    id = "sc_01",
                    projectId = demoProjectId,
                    sceneNumber = 1,
                    durationSeconds = 6,
                    narration = "Le soleil brûlant assèche la terre rouge d'un village ancien.",
                    visualDescription = "Vue cinématographique aérienne d'une terre craquelée au coucher du soleil, cases traditionnelles à l'horizon.",
                    videoPrompt = "cinematic aerial shot of dry red soil African landscape at sunset, golden dust in the air, 8k dramatic lighting",
                    status = SceneStatus.READY
                ),
                SceneItem(
                    id = "sc_02",
                    projectId = demoProjectId,
                    sceneNumber = 2,
                    durationSeconds = 8,
                    narration = "Seul Kofi ose franchir le cercle des esprits vers le grand baobab.",
                    visualDescription = "Gros plan sur un jeune chasseur déterminé marchant vers un gigantesque baobab aux racines luisantes.",
                    videoPrompt = "heroic low angle shot of young determined African hunter walking towards a colossal glowing ancient baobab tree, cinematic 35mm",
                    status = SceneStatus.READY
                ),
                SceneItem(
                    id = "sc_03",
                    projectId = demoProjectId,
                    sceneNumber = 3,
                    durationSeconds = 8,
                    narration = "Au contact de son écorce, une lumière turquoise jaillit des profondeurs.",
                    visualDescription = "Kofi pose sa main sur l'écorce, des veines d'eau luminescente scintillent et l'eau jaillit sous ses pieds.",
                    videoPrompt = "magical moment hunter touches glowing sacred tree, vibrant cyan luminous water springing from dry earth, cinematic visual fx",
                    status = SceneStatus.READY
                ),
                SceneItem(
                    id = "sc_04",
                    projectId = demoProjectId,
                    sceneNumber = 4,
                    durationSeconds = 8,
                    narration = "Le village est sauvé, mais la prophétie ne fait que commencer.",
                    visualDescription = "Pluie bienfaisante sur les villageois émerveillés, Kofi observant l'horizon avec bravoure.",
                    videoPrompt = "celebration of life rain falling in African village, joyful people, young hero looking at the horizon, emotional cinematic climax",
                    status = SceneStatus.READY
                )
            )
        )

        val sampleAdProject = Project(
            id = "proj_demo_cosmetic_ad",
            title = "Sérum Végétal Karité & Hibiscus",
            description = "Publicité e-commerce percutante format TikTok pour cosmétique naturel bio.",
            category = VideoCategory.ADVERTISING,
            aspectRatio = AspectRatioType.VERTICAL_9_16,
            targetDurationSeconds = 15,
            visualStyle = VisualStyle.PRO_AD,
            narrationTone = NarrationTone.INSPIRING,
            status = ProjectStatus.SCRIPT_READY,
            hook = "Votre peau mérite le secret le mieux gardé de la nature.",
            fullNarration = "Découvrez le sérum bio Karité & Hibiscus. Hydratation éclat 24h sans effet gras. Commandez dès maintenant !",
            isSampleDemo = true,
            scenes = listOf(
                SceneItem(
                    id = "sc_ad_01",
                    projectId = "proj_demo_cosmetic_ad",
                    sceneNumber = 1,
                    durationSeconds = 5,
                    narration = "Votre peau tiraille dès le matin ?",
                    visualDescription = "Gros plan élégant sur une goutte dorée de sérum perlant sur une fleur d'hibiscus fraîche.",
                    videoPrompt = "macro commercial shot glowing golden serum drop dripping on tropical hibiscus flower, luxury beauty ad lighting",
                    status = SceneStatus.READY
                ),
                SceneItem(
                    id = "sc_ad_02",
                    projectId = "proj_demo_cosmetic_ad",
                    sceneNumber = 2,
                    durationSeconds = 5,
                    narration = "Une texture soyeuse infusée de beurre de karité pur.",
                    visualDescription = "Application du produit sur peau lumineuse et radieuse, effet velours naturel.",
                    videoPrompt = "model with glowing flawless skin applying silky serum, soft morning radiance studio aesthetic, 4k 60fps",
                    status = SceneStatus.READY
                ),
                SceneItem(
                    id = "sc_ad_03",
                    projectId = "proj_demo_cosmetic_ad",
                    sceneNumber = 3,
                    durationSeconds = 5,
                    narration = "-20% cette semaine avec le code ECLAT.",
                    visualDescription = "Packshot dynamique du flacon cosmétique avec texte call to action percutant.",
                    videoPrompt = "luxury cosmetic bottle spinning on sleek dark reflective pedestal, premium neon violet rim light, commercial 3D render",
                    status = SceneStatus.READY
                )
            )
        )

        val sampleMotivationProject = Project(
            id = "proj_demo_motivation",
            title = "La Règle des 1% Infaillible",
            description = "Vidéo motivationnelle virale sur la discipline et le travail acharné.",
            category = VideoCategory.MOTIVATION,
            aspectRatio = AspectRatioType.VERTICAL_9_16,
            targetDurationSeconds = 30,
            visualStyle = VisualStyle.CINEMATIC_REALISTIC,
            narrationTone = NarrationTone.INSPIRING,
            status = ProjectStatus.DRAFT,
            hook = "Ne cherche pas à devenir parfait en 1 jour. Fais 1% de plus aujourd'hui.",
            fullNarration = "Les plus grands empires ne se sont pas construits sur un coup de chance. Chaque matin où tu te réveilles avant les doutes, tu gagnes une bataille.",
            isSampleDemo = true,
            scenes = listOf(
                SceneItem(
                    id = "sc_mot_01",
                    projectId = "proj_demo_motivation",
                    sceneNumber = 1,
                    durationSeconds = 6,
                    narration = "Le réveil sonne à 5h00. La plupart renoncent.",
                    visualDescription = "Plan serré sur des baskets de course lacées dans la pénombre, silhouette d'un athlète.",
                    videoPrompt = "dramatic morning workout preparation, athlete lacing running shoes in dark room with street lamp glow outside window",
                    status = SceneStatus.READY
                ),
                SceneItem(
                    id = "sc_mot_02",
                    projectId = "proj_demo_motivation",
                    sceneNumber = 2,
                    durationSeconds = 8,
                    narration = "C'est dans l'obscurité que se forgent les plus éclatantes victoires.",
                    visualDescription = "Course sous la pluie dans les rues désertes d'une métropole moderne.",
                    videoPrompt = "cinematic runner in rain through empty neon-lit city street at dawn, slow motion sweat drops, high motivation video",
                    status = SceneStatus.READY
                )
            )
        )

        _projects.value = listOf(sampleAfricanProject, sampleAdProject, sampleMotivationProject)

        _generations.value = listOf(
            GenerationRecord(
                projectId = demoProjectId,
                projectTitle = "Le Secret du Baobab Sacré",
                provider = "Gemini AI Script & StoryEngine",
                model = "gemini-2.5-flash",
                status = GenerationStatus.COMPLETED,
                estimatedCostCredits = 15,
                actualCostCredits = 15,
                durationText = "4.2s"
            ),
            GenerationRecord(
                projectId = demoProjectId,
                projectTitle = "Le Secret du Baobab Sacré (Scene 1-4)",
                provider = "VideoDiffusion Neural Render",
                model = "motion-v2.1",
                status = GenerationStatus.COMPLETED,
                estimatedCostCredits = 60,
                actualCostCredits = 60,
                durationText = "48s"
            ),
            GenerationRecord(
                projectId = "proj_demo_cosmetic_ad",
                projectTitle = "Sérum Végétal Karité & Hibiscus",
                provider = "Gemini AI Script Engine",
                model = "gemini-2.5-flash",
                status = GenerationStatus.COMPLETED,
                estimatedCostCredits = 10,
                actualCostCredits = 10,
                durationText = "3.1s"
            )
        )

        _transactions.value = listOf(
            CreditTransaction(
                amount = 250,
                transactionType = "Souscription Formule Pro",
                description = "Renouvellement mensuel pack Pro Créateur (+250 crédits)"
            ),
            CreditTransaction(
                amount = -75,
                transactionType = "Génération Vidéo",
                description = "Scénario + 4 Scènes vidéo pour 'Le Secret du Baobab Sacré'"
            ),
            CreditTransaction(
                amount = -10,
                transactionType = "Génération Script",
                description = "Génération du script 'Sérum Végétal Karité & Hibiscus'"
            ),
            CreditTransaction(
                amount = 75,
                transactionType = "Pack Rechargement MoMo",
                description = "Achat direct via MTN Mobile Money"
            )
        )
    }

    fun saveProject(project: Project): Project {
        val current = _projects.value.toMutableList()
        val index = current.indexOfFirst { it.id == project.id }
        if (index != -1) {
            current[index] = project.copy(updatedAt = System.currentTimeMillis())
        } else {
            current.add(0, project)
        }
        _projects.value = current
        return project
    }

    fun deleteProject(projectId: String) {
        _projects.value = _projects.value.filterNot { it.id == projectId }
    }

    fun duplicateProject(project: Project): Project {
        val newId = UUID.randomUUID().toString()
        val duplicated = project.copy(
            id = newId,
            title = "${project.title} (Copie)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            isSampleDemo = false,
            scenes = project.scenes.map { it.copy(id = UUID.randomUUID().toString(), projectId = newId) }
        )
        val current = _projects.value.toMutableList()
        current.add(0, duplicated)
        _projects.value = current
        return duplicated
    }

    fun deductCredits(amount: Int, reason: String): Boolean {
        val current = _userProfile.value
        if (current.creditBalance < amount) return false

        _userProfile.value = current.copy(creditBalance = current.creditBalance - amount)
        val tx = CreditTransaction(
            amount = -amount,
            transactionType = "Consommation Studio",
            description = reason
        )
        _transactions.value = listOf(tx) + _transactions.value
        return true
    }

    fun addCredits(amount: Int, methodDescription: String) {
        val current = _userProfile.value
        _userProfile.value = current.copy(creditBalance = current.creditBalance + amount)
        val tx = CreditTransaction(
            amount = amount,
            transactionType = "Recharge Crédits",
            description = methodDescription
        )
        _transactions.value = listOf(tx) + _transactions.value
    }

    fun recordGeneration(
        projectId: String,
        projectTitle: String,
        provider: String,
        model: String,
        cost: Int,
        status: GenerationStatus,
        errorMessage: String? = null
    ) {
        val record = GenerationRecord(
            projectId = projectId,
            projectTitle = projectTitle,
            provider = provider,
            model = model,
            status = status,
            estimatedCostCredits = cost,
            actualCostCredits = cost,
            errorMessage = errorMessage
        )
        _generations.value = listOf(record) + _generations.value
    }

    fun getPlans(): List<SubscriptionPlan> = listOf(
        SubscriptionPlan(
            id = "plan_free",
            name = "Découverte Gratuite",
            priceMonthlyCfa = "0 FCFA",
            priceMonthlyEur = "0 €",
            creditsMonthly = 30,
            features = listOf(
                "30 crédits de test offerts",
                "Format vertical 9:16 & carré 1:1",
                "Génération de scripts IA français",
                "Export vidéo 720p avec filigrane",
                "Support communautaire"
            )
        ),
        SubscriptionPlan(
            id = "plan_pro",
            name = "Pro Créateur",
            priceMonthlyCfa = "9 500 FCFA",
            priceMonthlyEur = "15 €",
            creditsMonthly = 350,
            isPopular = true,
            badgeLabel = "Le plus populaire",
            features = listOf(
                "350 crédits par mois",
                "Tous formats : 9:16, 1:1 et 16:9",
                "Rendu vidéo HD 1080p sans filigrane",
                "Styles Cinématique, 3D & Publicité",
                "Voix off IA réaliste multilingue",
                "Sous-titres dynamiques synchronisés",
                "Support prioritaire WhatsApp & Email"
            )
        ),
        SubscriptionPlan(
            id = "plan_agency",
            name = "Agence & Studio",
            priceMonthlyCfa = "29 000 FCFA",
            priceMonthlyEur = "45 €",
            creditsMonthly = 1200,
            features = listOf(
                "1 200 crédits par mois",
                "Rendu 4K Ultra-HD illimité",
                "Générations parallèles ultra-rapides",
                "Accès prioritaire aux nouveaux modèles IA",
                "Sous-titrage personnalisé avec charte graphique",
                "Gestionnaire de compte dédié"
            )
        )
    )

    fun getCreditPacks(): List<CreditPackage> = listOf(
        CreditPackage(
            id = "pack_starter",
            credits = 60,
            priceCfa = "2 000 FCFA",
            priceEur = "3 €",
            bonusCredits = 0
        ),
        CreditPackage(
            id = "pack_creator",
            credits = 250,
            priceCfa = "7 500 FCFA",
            priceEur = "12 €",
            bonusCredits = 25,
            isBestValue = true
        ),
        CreditPackage(
            id = "pack_studio",
            credits = 800,
            priceCfa = "22 000 FCFA",
            priceEur = "35 €",
            bonusCredits = 150
        )
    )

    fun getAiProviders(): List<AiProviderInfo> = listOf(
        AiProviderInfo(
            id = "prov_text_gemini",
            name = "Google Gemini 2.5 Flash",
            category = ProviderCategory.TEXT,
            defaultModel = "gemini-2.5-flash",
            isConfigured = true,
            statusNote = "Prêt & Actif - Intégration officielle AI Studio",
            readinessPhase = "Phase 1 / 3"
        ),
        AiProviderInfo(
            id = "prov_image_gen",
            name = "Studio Image Diffusion Engine",
            category = ProviderCategory.IMAGE,
            defaultModel = "imagen-3.0",
            isConfigured = true,
            statusNote = "Prêt - Rendu des miniatures & concept art",
            readinessPhase = "Phase 1 / 4"
        ),
        AiProviderInfo(
            id = "prov_video_gen",
            name = "Video Neural Pipeline",
            category = ProviderCategory.VIDEO,
            defaultModel = "kling-v1.5 / runway-gen3",
            isConfigured = false,
            statusNote = "En attente de clé API fournisseur vidéo (Phase 4)",
            readinessPhase = "Phase 4"
        ),
        AiProviderInfo(
            id = "prov_voice_synth",
            name = "Neural Voiceover TTS",
            category = ProviderCategory.VOICE,
            defaultModel = "elevenlabs-multilingual-v2",
            isConfigured = false,
            statusNote = "Voix off de synthèse planifiée pour la Phase 5",
            readinessPhase = "Phase 5"
        ),
        AiProviderInfo(
            id = "prov_render_engine",
            name = "FFmpeg Cloud Composer",
            category = ProviderCategory.RENDER,
            defaultModel = "h264-high-profile",
            isConfigured = false,
            statusNote = "Assemblage final & sous-titrage synchronisé (Phase 5)",
            readinessPhase = "Phase 5"
        )
    )
}
