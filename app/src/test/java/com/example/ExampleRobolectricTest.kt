package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ai.KisanAiAssistant
import com.example.data.local.SampleData
import com.example.data.model.PriceUnit
import com.example.localization.AppLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read app_name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Kissan Adda", appName)
  }

  @Test
  fun `verify sample crop data and ai answers`() {
    val samplePrices = SampleData.defaultCropPrices
    assertTrue(samplePrices.isNotEmpty())

    val tomato = samplePrices.firstOrNull { it.cropName == "Tomato" }
    assertNotNull(tomato)

    val answer = KisanAiAssistant.answerQuery(
      query = "What is today's tomato price?",
      cropPrices = samplePrices,
      unit = PriceUnit.PER_KG,
      language = AppLanguage.ENGLISH
    )
    assertTrue(answer.contains("Tomato"))
    assertTrue(answer.contains("₹"))
  }

  @Test
  fun `verify unit conversion quintal vs kg`() {
    val samplePrices = SampleData.defaultCropPrices
    val tomato = samplePrices.first { it.cropName == "Tomato" }
    val kgAvg = tomato.getDisplayAvg(PriceUnit.PER_KG)
    val qtlAvg = tomato.getDisplayAvg(PriceUnit.PER_QUINTAL)

    assertEquals(kgAvg * 100.0, qtlAvg, 0.001)
  }
}
