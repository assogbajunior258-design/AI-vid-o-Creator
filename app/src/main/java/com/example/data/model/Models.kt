package com.example.data.model

import java.util.UUID

enum class ProjectStatus(val label: String, val badgeColorHex: Long) {
    DRAFT("Brouillon", 0xFF64748B),
    SCRIPT_GENERATING("Génération du script...", 0xFF3B82F6),
    SCRIPT_READY("Scénario prêt", 0xFF8B5CF6),
    MEDIA_GENERATING("Génération des médias...", 0xFFF59E0B),
    RENDERING("Montage & Rendu...", 0xFF06B6D4),
    COMPLETED("Terminé", 0xFF10B981),
    FAILED("Échec", 0xFFEF4444),
    CANCELLED("Annulé", 0xFF94A3B8)
}

enum class AspectRatioType(val label: String, val ratioWidth: Float, val ratioHeight: Float, val iconName: String) {
    VERTICAL_9_16("Vertical 9:16 (TikTok, Shorts, Reels)", 9f, 16f, "phone_iphone"),
    SQUARE_1_1("Carré 1:1 (Instagram, Feed)", 1f, 1f, "crop_square"),
    LANDSCAPE_16_9("Paysage 16:9 (YouTube, Web)", 16f, 9f, "tv")
}

enum class VideoCategory(val title: String, val description: String, val iconEmoji: String) {
    STORYTELLING("Storytelling", "Récits captivants, fables et aventures immersives", "📖"),
    AFRICAN_STORY("Histoire Africaine", "Contes traditionnels, légendes et récits d'Afrique", "🌍"),
    ADVERTISING("Publicité & Produit", "Vidéos de vente pour cosmétiques, e-commerce, marques", "🛍️"),
    MOTIVATION("Motivation & Mindset", "Discours puissants, citations inspirantes pour réseaux", "🔥"),
    EDUCATION("Éducation & Tuto", "Explications pédagogiques, astuces business, cours", "🎓"),
    NEWS("Actualité & Décryptage", "Synthèses courtes de faits marquants et analyses", "📰"),
    COMEDY("Contenu Humoristique", "Sketchs, anecdotes drôles et divertissement", "🎭"),
    BUSINESS("Présentation d'Entreprise", "Pitches corporate, valorisation de services", "💼"),
    CUSTOM("Contenu Personnalisé", "Création libre adaptée à votre créativité", "✨")
}

enum class VisualStyle(val title: String, val promptModifier: String) {
    CINEMATIC_REALISTIC("Cinématographique réaliste", "ultra-realistic, 8k cinematic shot, dramatic lighting, 35mm lens, atmospheric depth"),
    ANIMATION_3D("Animation 3D moderne", "Pixar style, 3D animated render, vibrant colors, expressive characters, soft studio lighting"),
    DOCUMENTARY("Documentaire soigné", "high-end National Geographic documentary footage, natural lighting, vivid detail"),
    PRO_AD("Publicité professionnelle", "commercial advertising aesthetic, sleek product focus, high contrast, studio light setup"),
    CARTOON("Dessin animé", "vibrant 2D illustrated cartoon aesthetic, crisp outlines, bold expressive colors"),
    ILLUSTRATED("Art Illustré", "digital concept art, painterly textures, warm color harmony"),
    EDUCATIONAL("Vidéo Éducative", "clean infographics, crisp visuals, clear focal points, modern tech aesthetic"),
    CUSTOM_STYLE("Style sur-mesure", "custom creative vision")
}

enum class NarrationTone(val title: String) {
    CAPTIVATING("Captivant & Mystérieux"),
    INSPIRING("Inspirant & Énergique"),
    PROFESSIONAL("Professionnel & Posé"),
    DRAMATIC("Dramatique & Intense"),
    FRIENDLY("Chaleureux & Amical"),
    HUMOROUS("Humoristique & Décalé")
}

data class SceneItem(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val sceneNumber: Int,
    var durationSeconds: Int = 5,
    var narration: String,
    var visualDescription: String,
    var videoPrompt: String,
    var imageUrl: String? = null,
    var videoUrl: String? = null,
    var status: SceneStatus = SceneStatus.READY
)

enum class SceneStatus(val label: String) {
    DRAFT("Brouillon"),
    GENERATING("En cours..."),
    READY("Prêt"),
    FAILED("Échec")
}

data class Project(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "usr_creator_01",
    var title: String,
    var description: String,
    var category: VideoCategory,
    var aspectRatio: AspectRatioType = AspectRatioType.VERTICAL_9_16,
    var targetDurationSeconds: Int = 30,
    var visualStyle: VisualStyle = VisualStyle.CINEMATIC_REALISTIC,
    var narrationTone: NarrationTone = NarrationTone.CAPTIVATING,
    var language: String = "Français",
    var hasVoiceover: Boolean = true,
    var hasSubtitles: Boolean = true,
    var status: ProjectStatus = ProjectStatus.DRAFT,
    val createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis(),
    var hook: String = "",
    var fullNarration: String = "",
    var scenes: List<SceneItem> = emptyList(),
    var renderedVideoUrl: String? = null,
    var isSampleDemo: Boolean = false
)

data class GenerationRecord(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "usr_creator_01",
    val projectId: String,
    val projectTitle: String,
    val sceneId: String? = null,
    val provider: String,
    val model: String,
    val status: GenerationStatus,
    val estimatedCostCredits: Int,
    val actualCostCredits: Int = estimatedCostCredits,
    val externalJobId: String? = null,
    val errorMessage: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val durationText: String = "22s"
)

enum class GenerationStatus(val label: String, val colorHex: Long) {
    COMPLETED("Terminé", 0xFF10B981),
    RUNNING("En cours", 0xFF3B82F6),
    FAILED("Échoué", 0xFFEF4444),
    CANCELLED("Annulé", 0xFF94A3B8)
}

data class UserProfile(
    val id: String = "usr_creator_01",
    val fullName: String = "Junior Assogba",
    val email: String = "assogbajunior258@gmail.com",
    val role: String = "Créateur Pro",
    val planId: String = "plan_pro",
    val creditBalance: Int = 180,
    val avatarUrl: String? = null
)

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val priceMonthlyCfa: String,
    val priceMonthlyEur: String,
    val creditsMonthly: Int,
    val features: List<String>,
    val isPopular: Boolean = false,
    val badgeLabel: String? = null
)

data class CreditPackage(
    val id: String,
    val credits: Int,
    val priceCfa: String,
    val priceEur: String,
    val bonusCredits: Int = 0,
    val isBestValue: Boolean = false
)

data class CreditTransaction(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "usr_creator_01",
    val amount: Int, // positive for top-up, negative for usage
    val transactionType: String,
    val description: String,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ProviderCategory(val label: String) {
    TEXT("Génération Scénarios"),
    IMAGE("Génération Images"),
    VIDEO("Génération Vidéos"),
    VOICE("Synthèse Vocale"),
    SUBTITLES("Sous-titrage"),
    RENDER("Moteur d'Assemblage")
}

data class AiProviderInfo(
    val id: String,
    val name: String,
    val category: ProviderCategory,
    val defaultModel: String,
    val isConfigured: Boolean,
    val statusNote: String,
    val readinessPhase: String
)
