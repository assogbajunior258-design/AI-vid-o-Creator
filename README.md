# AI Video Creator 🎬✨

Studio SaaS moderne de création vidéo assistée par intelligence artificielle, conçu pour les créateurs de contenu TikTok, YouTube Shorts, Instagram Reels, conteurs et entrepreneurs.

Application native développée en **Kotlin** avec **Jetpack Compose** et **Material Design 3**.

---

## 📌 Pourquoi ce projet ne se déploie pas sur Vercel ?

**Vercel** est une plateforme d'hébergement dédiée exclusivement aux **applications Web** (Next.js, React Web, HTML/CSS, Node.js). 

Ce projet est une **application mobile native Android** (`.apk` / `.aab`) compilée avec Gradle et le SDK Android. Un hébergeur web comme Vercel ne dispose pas de l'environnement d'exécution Android (JVM, Gradle daemon, Android SDK, simulateur ARM/x86) nécessaire pour exécuter ou servir une application native.

### 🚀 Comment déployer ou utiliser cette application ?

1. **Test en direct dans Google AI Studio :**
   - Utilisez le **Streaming Emulator** intégré dans votre navigateur pour tester immédiatement toutes les fonctionnalités de l'application en temps réel.
2. **Export vers GitHub & ZIP :**
   - Depuis le menu de Google AI Studio en haut à droite, vous pouvez synchroniser directement ce projet avec votre compte GitHub ou télécharger une archive ZIP.
3. **Génération de l'APK / AAB pour smartphone :**
   - Vous pouvez générer directement le fichier `.apk` (pour installation directe sur n'importe quel téléphone Android) ou `.aab` (pour publication sur le Google Play Store).
4. **Ouverture dans Android Studio :**
   - Clonez le dépôt et ouvrez le dossier directement dans **Android Studio Ladybug / Meerkat** (ou version plus récente). Lancez ensuite sur votre téléphone physique en activant le débogage USB ou sur un émulateur.

---

## 🌟 Fonctionnalités Principales (Phase 1)

- 🎨 **Direction Artistique Studio Dark :** Thème sombre graphite/bleu nuit avec accents violet électrique et cyan néon, respectant les directives Material You et l'adaptabilité tablettes/mobiles (`NavigationRail` et `NavigationBar`).
- 🧭 **Studio en 7 Étapes Guidées :**
  1. **Idée :** Saisie libre avec suggestions d'inspiration prêtes à l'emploi (contes africains, pub e-commerce, motivation, tutoriels).
  2. **Type de vidéo :** Choix thématique (Storytelling, Histoire Africaine, Publicité, Motivation, Éducation, etc.).
  3. **Paramètres :** Formats 9:16 vertical, 1:1 carré, 16:9 paysage, durées (15s à 90s), styles visuels et bascules voix off / sous-titres.
  4. **Scénario IA :** Génération du titre, accroche (*hook*) et narration globale modifiable.
  5. **Découpage des scènes :** Cartes de scènes avec durée, narration, prompts IA, édition en direct, ajout et suppression.
  6. **Devis Médias :** Calcul transparent du coût en crédits par scène avant génération.
  7. **Rendu & Lecteur interactif :** Lecteur vidéo avec barre de progression, scrubbing de timeline, sous-titres animés et boutons d'export/téléchargement.
- 📊 **Tableau de bord :** Statistiques en direct, solde de crédits, raccourcis d'inspiration et créations récentes.
- 📁 **Bibliothèque de projets :** Recherche instantanée, filtres de statut, duplication et suppression sécurisée.
- 💳 **Crédits & Abonnements :** Packs de crédits avec tarifs en **FCFA** et **EUR**, intégration prête pour Mobile Money (Orange Money, MTN MoMo, Wave) et Cartes bancaires.
- ⚙️ **Paramètres & Supervision :** Tableaux d'architecture des providers IA (Gemini, Diffusion, Neural Video, ElevenLabs, FFmpeg) et console d'administration SaaS.

---

## 🛠️ Stack Technique

- **Langage :** Kotlin 2.2
- **Interface UI :** Jetpack Compose (BOM 2024.09), Material 3
- **Architecture :** MVVM (Model - View - ViewModel) + StateFlow réactif
- **Gestionnaire de dépendances :** Gradle (Kotlin DSL `.gradle.kts`) avec Version Catalog (`libs.versions.toml`)
- **Tests :** JUnit 4 + Robolectric pour les tests unitaires et composants

---

## 🧪 Exécution des Tests

Pour exécuter la suite de tests unitaires et Robolectric :
```bash
gradle :app:testDebugUnitTest
```
