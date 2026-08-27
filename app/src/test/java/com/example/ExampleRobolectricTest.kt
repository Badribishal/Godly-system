package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.engine.PersonalityEvaluationEngine
import com.example.data.model.RecordInput
import com.example.data.model.ShadowType
import com.example.data.model.SoulIdentity
import com.example.data.model.VirtueType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Godly System", appName)
  }

  @Test
  fun `test personality evaluation engine constructive shadow conversion`() {
    val initial = SoulIdentity.initial()
    val input = RecordInput(
      emotion = "Envy and competitiveness",
      primaryShadow = ShadowType.ENVY,
      primaryVirtue = VirtueType.DILIGENCE,
      situation = "Witnessed a competitor succeed",
      intention = "Channel envy into rigorous practice and self-improvement",
      action = "Trained for 3 hours and analyzed technique",
      consequence = "Increased personal skill and clarity",
      reflection = "The rival's fire pushed me further."
    )

    val (updatedSoul, result) = PersonalityEvaluationEngine.evaluateRecord(input, initial, 1)

    assertNotNull(updatedSoul)
    assertNotNull(result)
    assertTrue(updatedSoul.virtueScores[VirtueType.DILIGENCE]!! > initial.virtueScores[VirtueType.DILIGENCE]!!)
    assertTrue(updatedSoul.shadowScores[ShadowType.ENVY]!! > initial.shadowScores[ShadowType.ENVY]!!)
  }

  @Test
  fun `test cosmetic effects and soul shards unlock`() {
    val initial = SoulIdentity.initial().copy(soulShards = 150)
    val effectToUnlock = com.example.data.model.CosmeticCatalog.getEffectById("effect_aurora")

    assertTrue(initial.soulShards >= effectToUnlock.cost)
    val afterPurchase = initial.copy(
      soulShards = initial.soulShards - effectToUnlock.cost,
      unlockedEffectIds = initial.unlockedEffectIds + effectToUnlock.id,
      equippedEffectId = effectToUnlock.id
    )

    assertEquals(100, afterPurchase.soulShards)
    assertTrue(afterPurchase.unlockedEffectIds.contains("effect_aurora"))
    assertEquals("effect_aurora", afterPurchase.equippedEffectId)
  }

  @Test
  fun `test achievement catalog definitions exist`() {
    val definitions = com.example.data.model.AchievementCatalog.DEFINITIONS
    assertTrue(definitions.size >= 1000)
    val firstRecord = definitions.find { it.id == "first_record" }
    assertNotNull(firstRecord)
    assertEquals(30, firstRecord?.rewardShards)
  }

  @Test
  fun `test soul progression engine level and tier calculations`() {
    val initialSoul = SoulIdentity.initial()
    assertEquals(1, initialSoul.soulLevel)
    assertEquals(0, initialSoul.soulExp)

    // Apply 500 EXP
    val (triplet, outcome) = com.example.data.engine.SoulProgressionEngine.applyExpGain(
      currentLevel = initialSoul.soulLevel,
      currentExp = initialSoul.soulExp,
      totalExp = initialSoul.totalSoulExp,
      gainedExp = 500,
      alreadyUnlockedArchetypeIds = initialSoul.unlockedArchetypeIds.toSet()
    )

    val (newLevel, newExp, newTotalExp) = triplet
    assertTrue(newLevel > 1)
    assertEquals(500, newTotalExp)
    assertNotNull(outcome)
    assertTrue(outcome!!.levelsGained > 0)
    assertTrue(outcome.newlyUnlockedArchetypes.isNotEmpty())
  }

  @Test
  fun `test advanced archetype catalog retrieval and tier mapping`() {
    val starter = com.example.data.model.AdvancedArchetypesCatalog.getArchetypeById("arch_seeker")
    assertEquals("The Awakening Vessel", starter.name)
    assertEquals(1, starter.requiredLevel)

    val allArchetypes = com.example.data.model.AdvancedArchetypesCatalog.ALL_ARCHETYPES
    assertTrue(allArchetypes.size >= 20)
  }

  @Test
  fun `test brighter palettes available in theme engine`() {
    val radiantSolar = com.example.ui.theme.RarePalette.valueOf("RADIANT_SOLAR")
    assertNotNull(radiantSolar)
    assertEquals("Radiant Solar Dawn", radiantSolar.title)

    val prismaticOpal = com.example.ui.theme.RarePalette.valueOf("PRISMATIC_OPAL")
    assertNotNull(prismaticOpal)
    assertEquals("Prismatic Opal Light", prismaticOpal.title)
  }

  @Test
  fun `test daily login reward state progression`() {
    val state = com.example.ui.viewmodel.DailyLoginRewardState(
      streakDay = 1,
      isClaimedToday = false,
      todayRewardShards = 30
    )
    assertEquals(1, state.streakDay)
    assertEquals(false, state.isClaimedToday)
    assertEquals(30, state.todayRewardShards)
    assertEquals(7, state.rewardsList.size)
    assertEquals(250, state.rewardsList[6])
  }
}

