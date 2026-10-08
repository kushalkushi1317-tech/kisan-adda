package com.example.ai

import com.example.data.model.CropPrice
import com.example.data.model.PriceUnit
import com.example.localization.AppLanguage
import java.util.Locale

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

object KisanAiAssistant {

    fun answerQuery(
        query: String,
        cropPrices: List<CropPrice>,
        unit: PriceUnit,
        language: AppLanguage
    ): String {
        val q = query.trim().lowercase(Locale.ROOT)

        // 1. Check for specific crop query (e.g. Tomato, Onion, Potato, etc.)
        val cropMatch = cropPrices.firstOrNull { price ->
            q.contains(price.cropName.lowercase(Locale.ROOT))
        }

        // 2. "Highest / best market / where to sell" query
        if (q.contains("where should i sell") || q.contains("highest") || q.contains("best market") || q.contains("कहाँ बेचूं") || q.contains("सर्वोच्च") || q.contains("ಎಲ್ಲಿ ಮಾರಾಟ")) {
            if (cropMatch != null) {
                val allMatches = cropPrices.filter { it.cropName.equals(cropMatch.cropName, ignoreCase = true) }
                val best = allMatches.maxByOrNull { it.avgPrice }
                if (best != null) {
                    val formattedPrice = "₹%.1f/%s".format(best.getDisplayAvg(unit), if (unit == PriceUnit.PER_KG) "kg" else "quintal")
                    return when (language) {
                        AppLanguage.HINDI -> "सर्वश्रेष्ठ परिणाम: आप ${best.cropName} को ${best.marketName} (${best.district}, ${best.state}) में बेचें। वहां आज का उच्चतम औसत भाव $formattedPrice है!"
                        AppLanguage.KANNADA -> "ಉತ್ತಮ ಫಲಿತಾಂಶ: ನಿಮ್ಮ ${best.cropName} ಅನ್ನು ${best.marketName} (${best.district}, ${best.state}) ನಲ್ಲಿ ಮಾರಿ. ಅಲ್ಲಿ ಇಂದಿನ ಗರಿಷ್ಠ ಸರಾಸರಿ ಬೆಲೆ $formattedPrice ಆಗಿದೆ!"
                        AppLanguage.TELUGU -> "ఉత్తమ సిఫార్సు: మీ ${best.cropName} ను ${best.marketName} (${best.district}, ${best.state}) లో అమ్మండి. అక్కడ నేటి అత్యధిక సగటు ధర $formattedPrice!"
                        AppLanguage.TAMIL -> "சிறந்த பரிந்துரை: உங்கள் ${best.cropName} பயிரை ${best.marketName} சந்தையில் விற்கவும். அங்கு இன்றைய அதிகபட்ச சராசரி விலை $formattedPrice!"
                        AppLanguage.MARATHI -> "सर्वोत्तम सल्ला: तुमचे ${best.cropName} ${best.marketName} (${best.district}) मध्ये विका. तेथे आजचा सरासरी भाव $formattedPrice आहे!"
                        AppLanguage.ENGLISH -> "Best Recommendation: Sell your ${best.cropName} at ${best.marketName} (${best.district}, ${best.state}). It currently offers the highest average price of $formattedPrice!"
                    }
                }
            } else {
                val overallBest = cropPrices.maxByOrNull { it.avgPrice }
                if (overallBest != null) {
                    return "Today's highest valued crop in the mandis is ${overallBest.cropName} at ${overallBest.marketName} selling at ₹%.1f/%s.".format(
                        overallBest.getDisplayAvg(unit),
                        if (unit == PriceUnit.PER_KG) "kg" else "quintal"
                    )
                }
            }
        }

        // 3. "Trend / increased / decreased / week" query
        if (q.contains("increased") || q.contains("decreased") || q.contains("trend") || q.contains("week") || q.contains("बढ़ा") || q.contains("घटा") || q.contains("ಏರಿಕೆ")) {
            if (cropMatch != null) {
                val change = cropMatch.priceChangePercent
                val direction = if (change >= 0) "increased by +%.1f%%".format(change) else "decreased by %.1f%%".format(change)
                val advice = if (change >= 0) "Good time to consider harvesting and selling!" else "Prices are cooling off; hold inventory if storage allows."
                return "${cropMatch.cropName} prices in ${cropMatch.marketName} have $direction compared to yesterday. Current average is ₹%.1f/%s. $advice".format(
                    cropMatch.getDisplayAvg(unit),
                    if (unit == PriceUnit.PER_KG) "kg" else "quintal"
                )
            }
        }

        // 4. "Today's price / What is the price" query
        if (cropMatch != null) {
            val min = cropMatch.getDisplayMin(unit)
            val max = cropMatch.getDisplayMax(unit)
            val avg = cropMatch.getDisplayAvg(unit)
            val u = if (unit == PriceUnit.PER_KG) "kg" else "quintal"
            return when (language) {
                AppLanguage.HINDI -> "आज ${cropMatch.marketName} में ${cropMatch.cropName} का न्यूनतम भाव ₹%.1f, अधिकतम ₹%.1f और औसत भाव ₹%.1f प्रति %s है (बदलाव: %+.1f%%).".format(min, max, avg, u, cropMatch.priceChangePercent)
                AppLanguage.KANNADA -> "ಇಂದು ${cropMatch.marketName} ನಲ್ಲಿ ${cropMatch.cropName} ಕನಿಷ್ಠ ₹%.1f, ಗರಿಷ್ಠ ₹%.1f ಮತ್ತು ಸರಾಸರಿ ₹%.1f/%s ಆಗಿದೆ (ಬದಲಾವಣೆ: %+.1f%%).".format(min, max, avg, u, cropMatch.priceChangePercent)
                AppLanguage.TELUGU -> "నేడు ${cropMatch.marketName} లో ${cropMatch.cropName} కనీస ధర ₹%.1f, గరిష్ట ధర ₹%.1f, సగటు ₹%.1f/%s (మార్పు: %+.1f%%).".format(min, max, avg, u, cropMatch.priceChangePercent)
                AppLanguage.TAMIL -> "இன்று ${cropMatch.marketName} இல் ${cropMatch.cropName} குறைந்தபட்சம் ₹%.1f, அதிகபட்சம் ₹%.1f, சராசரி ₹%.1f/%s (மாற்றம்: %+.1f%%).".format(min, max, avg, u, cropMatch.priceChangePercent)
                AppLanguage.MARATHI -> "आज ${cropMatch.marketName} मध्ये ${cropMatch.cropName} चा किमान भाव ₹%.1f, कमाल ₹%.1f आणि सरासरी भाव ₹%.1f/%s आहे (बदल: %+.1f%%).".format(min, max, avg, u, cropMatch.priceChangePercent)
                AppLanguage.ENGLISH -> "Today's ${cropMatch.cropName} price at ${cropMatch.marketName} (${cropMatch.district}): Min ₹%.1f/%s, Max ₹%.1f/%s, Average ₹%.1f/%s (Change: %+.1f%%).".format(min, u, max, u, avg, u, cropMatch.priceChangePercent)
            }
        }

        // 5. General crop list or top gainers query
        if (q.contains("top") || q.contains("gainers") || q.contains("best")) {
            val topGainer = cropPrices.maxByOrNull { it.priceChangePercent }
            if (topGainer != null) {
                return "Top price increase today: ${topGainer.cropName} at ${topGainer.marketName} increased by +%.1f%% to ₹%.1f/%s.".format(
                    topGainer.priceChangePercent,
                    topGainer.getDisplayAvg(unit),
                    if (unit == PriceUnit.PER_KG) "kg" else "quintal"
                )
            }
        }

        // 6. Fallback with helpful suggestions
        return when (language) {
            AppLanguage.HINDI -> "मुझे इस फसल का डेटा नहीं मिला। आप टमाटर, प्याज, आलू, आम, चावल या गेहूं के भाव पूछ सकते हैं या 'मंडियों की तुलना करें' टैब देखें।"
            AppLanguage.KANNADA -> "ಕ್ಷಮಿಸಿ, ಈ ಬೆಳೆಯ ಮಾಹಿತಿ ಲಭ್ಯವಿಲ್ಲ. ಟೊಮೆಟೊ, ಈರುಳ್ಳಿ, ಆಲೂಗಡ್ಡೆ, ಭತ್ತ ಅಥವಾ ಗೋಧಿ ಬೆಲೆಗಳನ್ನು ಕೇಳಿ."
            AppLanguage.TELUGU -> "క్షమించండి, ఈ పంట సమాచారం అందుబాటులో లేదు. టమాట, ఉల్లి, బంగాళాదుంప లేదా బియ్యం ధరలను అడగండి."
            AppLanguage.TAMIL -> "மன்னிக்கவும், இந்த பயிர் தகவல் கிடைக்கவில்லை. தக்காளி, வெங்காயம், உருளைக்கிழங்கு அல்லது அரிசி விலைகளை கேட்கவும்."
            AppLanguage.MARATHI -> "माफ करा, या पिकाचा डेटा सापडला नाही. टोमॅटो, कांदा, बटाटा, आंबा किंवा तांदळाचे भाव विचारू शकता."
            AppLanguage.ENGLISH -> "I couldn't find specific mandi data for that query. Try asking: 'What is today's tomato price?', 'Where should I sell my onion?', or 'Which market has the highest rice rate?'."
        }
    }
}
