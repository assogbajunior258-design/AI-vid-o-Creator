package com.example

import com.example.data.model.*
import com.example.data.repository.VideoCreatorRepository
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.VideoCreatorViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var repository: VideoCreatorRepository
    private lateinit var viewModel: VideoCreatorViewModel

    @Before
    fun setUp() {
        repository = VideoCreatorRepository()
        viewModel = VideoCreatorViewModel(repository)
    }

    @Test
    fun testInitialProjectsLoaded() {
        val projects = repository.projects.value
        assertTrue("Projects list should contain initial demo projects", projects.isNotEmpty())
        val sampleProject = projects.find { it.isSampleDemo }
        assertNotNull("Sample demo project should exist", sampleProject)
        assertTrue(sampleProject!!.scenes.isNotEmpty())
    }

    @Test
    fun testCreditDeductionAndRecharge() {
        val initialCredits = repository.userProfile.value.creditBalance
        assertTrue(initialCredits > 0)

        val deducted = repository.deductCredits(20, "Test deduction")
        assertTrue(deducted)
        assertEquals(initialCredits - 20, repository.userProfile.value.creditBalance)

        repository.addCredits(50, "Test recharge")
        assertEquals(initialCredits - 20 + 50, repository.userProfile.value.creditBalance)
    }

    @Test
    fun testProjectDuplication() {
        val original = repository.projects.value.first()
        val duplicated = repository.duplicateProject(original)

        assertNotEquals(original.id, duplicated.id)
        assertTrue(duplicated.title.contains("(Copie)"))
        assertEquals(original.scenes.size, duplicated.scenes.size)
        assertFalse(duplicated.isSampleDemo)
    }

    @Test
    fun testStudioNavigationAndState() {
        viewModel.navigateTo(AppScreen.STUDIO)
        assertEquals(AppScreen.STUDIO, viewModel.uiState.value.currentScreen)

        viewModel.setStudioStep(3)
        assertEquals(3, viewModel.uiState.value.studioState.currentStep)

        viewModel.updateIdeaPrompt("Mon idée de vidéo virale")
        assertEquals("Mon idée de vidéo virale", viewModel.uiState.value.studioState.ideaPrompt)

        viewModel.selectCategory(VideoCategory.ADVERTISING)
        assertEquals(VideoCategory.ADVERTISING, viewModel.uiState.value.studioState.selectedCategory)
    }

    @Test
    fun testPlansAndPacksAvailability() {
        val plans = repository.getPlans()
        assertTrue(plans.size >= 3)
        assertTrue(plans.any { it.isPopular })

        val creditPacks = repository.getCreditPacks()
        assertTrue(creditPacks.size >= 3)

        val providers = repository.getAiProviders()
        assertTrue(providers.any { it.category == ProviderCategory.TEXT })
        assertTrue(providers.any { it.category == ProviderCategory.IMAGE })
    }
}
