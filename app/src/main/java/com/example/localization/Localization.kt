package com.example.localization

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी"),
    KANNADA("kn", "Kannada", "ಕನ್ನಡ"),
    TELUGU("te", "Telugu", "తెలుగు"),
    TAMIL("ta", "Tamil", "தமிழ்"),
    MARATHI("mr", "Marathi", "मराठी")
}

object LocalizationManager {

    fun getTagline(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Know Today’s Price. Sell at the Right Price."
        AppLanguage.HINDI -> "आज का भाव जानें। सही दाम पर बेचें।"
        AppLanguage.KANNADA -> "ಇಂದಿನ ಮಾರುಕಟ್ಟೆ ದರ ತಿಳಿಯಿರಿ. ಸರಿಯಾದ ಬೆಲೆಗೆ ಮಾರಿ."
        AppLanguage.TELUGU -> "ఈరోజు ధర తెలుసుకోండి. సరైన ధరకు అమ్మండి."
        AppLanguage.TAMIL -> "இன்றைய விலையை அறிந்திடுங்கள். சரியான விலையில் விற்கவும்."
        AppLanguage.MARATHI -> "आजचा भाव जाणा. योग्य भावात विका."
    }

    fun getCategoryName(category: String, lang: AppLanguage): String {
        return when (category.lowercase()) {
            "all" -> when (lang) {
                AppLanguage.ENGLISH -> "All Crops"
                AppLanguage.HINDI -> "सभी फसलें"
                AppLanguage.KANNADA -> "ಎಲ್ಲಾ ಬೆಳೆಗಳು"
                AppLanguage.TELUGU -> "అన్ని పంటలు"
                AppLanguage.TAMIL -> "அனைத்து பயிர்கள்"
                AppLanguage.MARATHI -> "सर्व पिके"
            }
            "vegetables" -> when (lang) {
                AppLanguage.ENGLISH -> "Vegetables"
                AppLanguage.HINDI -> "सब्जियां"
                AppLanguage.KANNADA -> "ತರಕಾರಿಗಳು"
                AppLanguage.TELUGU -> "కూరగాయలు"
                AppLanguage.TAMIL -> "காய்கறிகள்"
                AppLanguage.MARATHI -> "भाज्या"
            }
            "fruits" -> when (lang) {
                AppLanguage.ENGLISH -> "Fruits"
                AppLanguage.HINDI -> "फल"
                AppLanguage.KANNADA -> "ಹಣ್ಣುಗಳು"
                AppLanguage.TELUGU -> "పండ్లు"
                AppLanguage.TAMIL -> "பழங்கள்"
                AppLanguage.MARATHI -> "फळे"
            }
            "grains", "cereals" -> when (lang) {
                AppLanguage.ENGLISH -> "Grains"
                AppLanguage.HINDI -> "अनाज"
                AppLanguage.KANNADA -> "ಧಾನ್ಯಗಳು"
                AppLanguage.TELUGU -> "ధాన్యాలు"
                AppLanguage.TAMIL -> "தானியங்கள்"
                AppLanguage.MARATHI -> "धान्य"
            }
            "pulses" -> when (lang) {
                AppLanguage.ENGLISH -> "Pulses"
                AppLanguage.HINDI -> "दालें"
                AppLanguage.KANNADA -> "ಬೇಳೆಕಾಳುಗಳು"
                AppLanguage.TELUGU -> "పప్పులు"
                AppLanguage.TAMIL -> "பருப்பு வகைகள்"
                AppLanguage.MARATHI -> "डाळी"
            }
            "spices" -> when (lang) {
                AppLanguage.ENGLISH -> "Spices"
                AppLanguage.HINDI -> "मसाले"
                AppLanguage.KANNADA -> "ಮಸಾಲೆಗಳು"
                AppLanguage.TELUGU -> "మసాలాలు"
                AppLanguage.TAMIL -> "மசாலா பொருட்கள்"
                AppLanguage.MARATHI -> "मसाले"
            }
            else -> when (lang) {
                AppLanguage.ENGLISH -> "Other Crops"
                AppLanguage.HINDI -> "अन्य फसलें"
                AppLanguage.KANNADA -> "ಇತರ ಬೆಳೆಗಳು"
                AppLanguage.TELUGU -> "ఇతర పంటలు"
                AppLanguage.TAMIL -> "பிற பயிர்கள்"
                AppLanguage.MARATHI -> "इतर पिके"
            }
        }
    }

    fun getLocalizedCropName(englishName: String, lang: AppLanguage): String {
        return when (englishName.lowercase()) {
            "tomato" -> when (lang) {
                AppLanguage.ENGLISH -> "Tomato"
                AppLanguage.HINDI -> "टमाटर (Tomato)"
                AppLanguage.KANNADA -> "ಟೊಮೆಟೊ (Tomato)"
                AppLanguage.TELUGU -> "టమాట (Tomato)"
                AppLanguage.TAMIL -> "தக்காளி (Tomato)"
                AppLanguage.MARATHI -> "टोमॅटो (Tomato)"
            }
            "onion" -> when (lang) {
                AppLanguage.ENGLISH -> "Onion"
                AppLanguage.HINDI -> "प्याज (Onion)"
                AppLanguage.KANNADA -> "ಈರುಳ್ಳಿ (Onion)"
                AppLanguage.TELUGU -> "ఉల్లిపాయ (Onion)"
                AppLanguage.TAMIL -> "வெங்காயம் (Onion)"
                AppLanguage.MARATHI -> "कांदा (Onion)"
            }
            "potato" -> when (lang) {
                AppLanguage.ENGLISH -> "Potato"
                AppLanguage.HINDI -> "आलू (Potato)"
                AppLanguage.KANNADA -> "ಆಲೂಗಡ್ಡೆ (Potato)"
                AppLanguage.TELUGU -> "బంగాళాదుంప (Potato)"
                AppLanguage.TAMIL -> "உருளைக்கிழங்கு (Potato)"
                AppLanguage.MARATHI -> "बटाटा (Potato)"
            }
            "rice" -> when (lang) {
                AppLanguage.ENGLISH -> "Rice / Paddy"
                AppLanguage.HINDI -> "चावल / धान (Rice)"
                AppLanguage.KANNADA -> "ಅಕ್ಕಿ / ಭತ್ತ (Rice)"
                AppLanguage.TELUGU -> "వరి / బియ్యం (Rice)"
                AppLanguage.TAMIL -> "அரிசி / நெல் (Rice)"
                AppLanguage.MARATHI -> "तांदूळ / भात (Rice)"
            }
            "wheat" -> when (lang) {
                AppLanguage.ENGLISH -> "Wheat"
                AppLanguage.HINDI -> "गेहूं (Wheat)"
                AppLanguage.KANNADA -> "ಗೋಧಿ (Wheat)"
                AppLanguage.TELUGU -> "గోధుమలు (Wheat)"
                AppLanguage.TAMIL -> "கோதுமை (Wheat)"
                AppLanguage.MARATHI -> "गहू (Wheat)"
            }
            "mango" -> when (lang) {
                AppLanguage.ENGLISH -> "Mango"
                AppLanguage.HINDI -> "आम (Mango)"
                AppLanguage.KANNADA -> "ಮಾವಿನ ಹಣ್ಣು (Mango)"
                AppLanguage.TELUGU -> "మామిడి (Mango)"
                AppLanguage.TAMIL -> "மாம்பழம் (Mango)"
                AppLanguage.MARATHI -> "आंबा (Mango)"
            }
            "banana" -> when (lang) {
                AppLanguage.ENGLISH -> "Banana"
                AppLanguage.HINDI -> "केला (Banana)"
                AppLanguage.KANNADA -> "ಬಾಳೆಹಣ್ಣು (Banana)"
                AppLanguage.TELUGU -> "అరటిపండు (Banana)"
                AppLanguage.TAMIL -> "வாழைப்பழம் (Banana)"
                AppLanguage.MARATHI -> "केळी (Banana)"
            }
            "cotton" -> when (lang) {
                AppLanguage.ENGLISH -> "Cotton"
                AppLanguage.HINDI -> "कपास (Cotton)"
                AppLanguage.KANNADA -> "ಹತ್ತಿ (Cotton)"
                AppLanguage.TELUGU -> "పత్తి (Cotton)"
                AppLanguage.TAMIL -> "பருத்தி (Cotton)"
                AppLanguage.MARATHI -> "कापूस (Cotton)"
            }
            "soybean" -> when (lang) {
                AppLanguage.ENGLISH -> "Soybean"
                AppLanguage.HINDI -> "सोयाबीन (Soybean)"
                AppLanguage.KANNADA -> "ಸೋಯಾಬೀನ್ (Soybean)"
                AppLanguage.TELUGU -> "సోయాబీన్ (Soybean)"
                AppLanguage.TAMIL -> "சோயாபீன் (Soybean)"
                AppLanguage.MARATHI -> "सोयाबीन (Soybean)"
            }
            "chilli" -> when (lang) {
                AppLanguage.ENGLISH -> "Green Chilli"
                AppLanguage.HINDI -> "हरी मिर्च (Chilli)"
                AppLanguage.KANNADA -> "ಹಸಿಮೆಣಸಿನಕಾಯಿ (Chilli)"
                AppLanguage.TELUGU -> "పచ్చిమిర్చి (Chilli)"
                AppLanguage.TAMIL -> "பச்சை மிளகாய் (Chilli)"
                AppLanguage.MARATHI -> "हिरवी मिरची (Chilli)"
            }
            "garlic" -> when (lang) {
                AppLanguage.ENGLISH -> "Garlic"
                AppLanguage.HINDI -> "लहसुन (Garlic)"
                AppLanguage.KANNADA -> "ಬೆಳ್ಳುಳ್ಳಿ (Garlic)"
                AppLanguage.TELUGU -> "వెల్లుల్లి (Garlic)"
                AppLanguage.TAMIL -> "பூண்டு (Garlic)"
                AppLanguage.MARATHI -> "लसूण (Garlic)"
            }
            "ginger" -> when (lang) {
                AppLanguage.ENGLISH -> "Ginger"
                AppLanguage.HINDI -> "अदरक (Ginger)"
                AppLanguage.KANNADA -> "ಶುಂಠಿ (Ginger)"
                AppLanguage.TELUGU -> "అల్లం (Ginger)"
                AppLanguage.TAMIL -> "இஞ்சி (Ginger)"
                AppLanguage.MARATHI -> "आले (Ginger)"
            }
            "turmeric" -> when (lang) {
                AppLanguage.ENGLISH -> "Turmeric"
                AppLanguage.HINDI -> "हल्दी (Turmeric)"
                AppLanguage.KANNADA -> "ಅರಿಶಿನ (Turmeric)"
                AppLanguage.TELUGU -> "పసుపు (Turmeric)"
                AppLanguage.TAMIL -> "மஞ்சள் (Turmeric)"
                AppLanguage.MARATHI -> "हळद (Turmeric)"
            }
            "mustard" -> when (lang) {
                AppLanguage.ENGLISH -> "Mustard"
                AppLanguage.HINDI -> "सरसों (Mustard)"
                AppLanguage.KANNADA -> "ಸಾಸಿವೆ (Mustard)"
                AppLanguage.TELUGU -> "ఆవాలు (Mustard)"
                AppLanguage.TAMIL -> "கடுகு (Mustard)"
                AppLanguage.MARATHI -> "मोहरी (Mustard)"
            }
            "maize" -> when (lang) {
                AppLanguage.ENGLISH -> "Maize / Corn"
                AppLanguage.HINDI -> "मक्का (Maize)"
                AppLanguage.KANNADA -> "ಮೆಕ್ಕೆಜೋಳ (Maize)"
                AppLanguage.TELUGU -> "మొక్కజొన్న (Maize)"
                AppLanguage.TAMIL -> "மக்காச்சோளம் (Maize)"
                AppLanguage.MARATHI -> "मका (Maize)"
            }
            else -> englishName
        }
    }

    fun getText(key: String, lang: AppLanguage): String {
        return when (key) {
            "search_hint" -> when (lang) {
                AppLanguage.ENGLISH -> "Search tomato, onion, rice, mango..."
                AppLanguage.HINDI -> "टमाटर, प्याज, चावल, आम खोजें..."
                AppLanguage.KANNADA -> "ಟೊಮೆಟೊ, ಈರುಳ್ಳಿ, ಭತ್ತ, ಮಾವು ಹುಡುಕಿ..."
                AppLanguage.TELUGU -> "టమాట, ఉల్లి, బియ్యం, మామిడి వెతకండి..."
                AppLanguage.TAMIL -> "தக்காளி, வெங்காயம், அரிசி தேடுங்கள்..."
                AppLanguage.MARATHI -> "टोमॅटो, कांदा, तांदूळ, आंबा शोधा..."
            }
            "today_top_prices" -> when (lang) {
                AppLanguage.ENGLISH -> "Today's Top Prices"
                AppLanguage.HINDI -> "आज के उच्चतम भाव"
                AppLanguage.KANNADA -> "ಇಂದಿನ ಗರಿಷ್ಠ ದರಗಳು"
                AppLanguage.TELUGU -> "ఈరోజు అత్యధిక ధరలు"
                AppLanguage.TAMIL -> "இன்றைய அதிகபட்ச விலைகள்"
                AppLanguage.MARATHI -> "आजचे उच्चांकी दर"
            }
            "price_increased" -> when (lang) {
                AppLanguage.ENGLISH -> "Price Increased"
                AppLanguage.HINDI -> "भाव में बढ़त"
                AppLanguage.KANNADA -> "ಬೆಲೆ ಏರಿಕೆ"
                AppLanguage.TELUGU -> "ధర పెరిగింది"
                AppLanguage.TAMIL -> "விலை அதிகரிப்பு"
                AppLanguage.MARATHI -> "भाव वाढले"
            }
            "price_decreased" -> when (lang) {
                AppLanguage.ENGLISH -> "Price Decreased"
                AppLanguage.HINDI -> "भाव में गिरावट"
                AppLanguage.KANNADA -> "ಬೆಲೆ ಇಳಿಕೆ"
                AppLanguage.TELUGU -> "ధర తగ్గింది"
                AppLanguage.TAMIL -> "விலை குறைவு"
                AppLanguage.MARATHI -> "भाव घसरले"
            }
            "compare_markets" -> when (lang) {
                AppLanguage.ENGLISH -> "Compare Markets"
                AppLanguage.HINDI -> "मंडियों की तुलना करें"
                AppLanguage.KANNADA -> "ಮಾರುಕಟ್ಟೆಗಳ ಹೋಲಿಕೆ"
                AppLanguage.TELUGU -> "మార్కెట్లను పోల్చండి"
                AppLanguage.TAMIL -> "சந்தைகளை ஒப்பிடுக"
                AppLanguage.MARATHI -> "बाजारांची तुलना करा"
            }
            "price_trends" -> when (lang) {
                AppLanguage.ENGLISH -> "Price Trends"
                AppLanguage.HINDI -> "भाव के रुझान (ट्रेंड)"
                AppLanguage.KANNADA -> "ಬೆಲೆ ಪ್ರವೃತ್ತಿ"
                AppLanguage.TELUGU -> "ధరల పోకడలు"
                AppLanguage.TAMIL -> "விலைப் போக்குகள்"
                AppLanguage.MARATHI -> "भावाचा कल"
            }
            "my_crops" -> when (lang) {
                AppLanguage.ENGLISH -> "My Crops"
                AppLanguage.HINDI -> "मेरी फसलें"
                AppLanguage.KANNADA -> "ನನ್ನ ಬೆಳೆಗಳು"
                AppLanguage.TELUGU -> "నా పంటలు"
                AppLanguage.TAMIL -> "என் பயிர்கள்"
                AppLanguage.MARATHI -> "माझी पिके"
            }
            "best_market_to_sell" -> when (lang) {
                AppLanguage.ENGLISH -> "Best Market to Sell"
                AppLanguage.HINDI -> "बेचने के लिए सर्वश्रेष्ठ मंडी"
                AppLanguage.KANNADA -> "ಮಾರಾಟಕ್ಕೆ ಅತ್ಯುತ್ತಮ ಮಾರುಕಟ್ಟೆ"
                AppLanguage.TELUGU -> "అమ్మడానికి ఉత్తమ మార్కెట్"
                AppLanguage.TAMIL -> "விற்பனைக்கு சிறந்த சந்தை"
                AppLanguage.MARATHI -> "विक्रीसाठी सर्वोत्तम बाजार"
            }
            "min_price" -> when (lang) {
                AppLanguage.ENGLISH -> "Min"
                AppLanguage.HINDI -> "न्यूनतम"
                AppLanguage.KANNADA -> "ಕನಿಷ್ಠ"
                AppLanguage.TELUGU -> "కనిష్ట"
                AppLanguage.TAMIL -> "குறைந்தபட்சம்"
                AppLanguage.MARATHI -> "किमान"
            }
            "max_price" -> when (lang) {
                AppLanguage.ENGLISH -> "Max"
                AppLanguage.HINDI -> "अधिकतम"
                AppLanguage.KANNADA -> "ಗರಿಷ್ಠ"
                AppLanguage.TELUGU -> "గరిష్ట"
                AppLanguage.TAMIL -> "அதிகபட்சம்"
                AppLanguage.MARATHI -> "कमाल"
            }
            "avg_price" -> when (lang) {
                AppLanguage.ENGLISH -> "Average"
                AppLanguage.HINDI -> "औसत भाव"
                AppLanguage.KANNADA -> "ಸರಾಸರಿ"
                AppLanguage.TELUGU -> "సగటు"
                AppLanguage.TAMIL -> "சராசரி"
                AppLanguage.MARATHI -> "सरासरी"
            }
            "sample_data_banner" -> when (lang) {
                AppLanguage.ENGLISH -> "Sample Mandi Data (Agmarknet Simulated Format)"
                AppLanguage.HINDI -> "नमूना मंडी डेटा (एगमार्कनेट प्रारूप)"
                AppLanguage.KANNADA -> "ಮಾದರಿ ಮಂಡಿ ಮಾಹಿತಿ (ಆಗ್ಮಾರ್ಕ್ನೆಟ್ ಮಾದರಿ)"
                AppLanguage.TELUGU -> "నమూనా మార్కెట్ డేటా (ఆగ్మార్క్‌నెట్)"
                AppLanguage.TAMIL -> "மாதிரி சந்தை தரவு (Agmarknet வடிவம்)"
                AppLanguage.MARATHI -> "नमुना बाजार डेटा (एगमार्कनेट फॉरमॅट)"
            }
            "voice_search" -> when (lang) {
                AppLanguage.ENGLISH -> "Voice Search"
                AppLanguage.HINDI -> "आवाज से खोजें"
                AppLanguage.KANNADA -> "ಧ್ವನಿ ಹುಡುಕಾಟ"
                AppLanguage.TELUGU -> "వాయిస్ శోధన"
                AppLanguage.TAMIL -> "குரல் தேடல்"
                AppLanguage.MARATHI -> "आवाज शोध"
            }
            "share_price" -> when (lang) {
                AppLanguage.ENGLISH -> "Share Price"
                AppLanguage.HINDI -> "भाव साझा करें"
                AppLanguage.KANNADA -> "ಬೆಲೆ ಹಂಚಿಕೊಳ್ಳಿ"
                AppLanguage.TELUGU -> "ధర పంచుకోండి"
                AppLanguage.TAMIL -> "விலையை பகிர்க"
                AppLanguage.MARATHI -> "भाव शेअर करा"
            }
            "ai_assistant" -> when (lang) {
                AppLanguage.ENGLISH -> "AI Market Assistant"
                AppLanguage.HINDI -> "किसान मित्र (AI सहायक)"
                AppLanguage.KANNADA -> "ಕಿಸಾನ್ ಮಿತ್ರ (AI ಸಹಾಯಕ)"
                AppLanguage.TELUGU -> "కిసాన్ మిత్ర (AI అసిస్టెంట్)"
                AppLanguage.TAMIL -> "கிசான் மித்ரா (AI உதவியாளர்)"
                AppLanguage.MARATHI -> "किसान मित्र (AI सहाय्यक)"
            }
            "admin_panel" -> when (lang) {
                AppLanguage.ENGLISH -> "Admin Panel"
                AppLanguage.HINDI -> "प्रबंधक पैनल"
                AppLanguage.KANNADA -> "ಆಡಳಿತ ಫಲಕ"
                AppLanguage.TELUGU -> "అడ్మిన్ ప్యానెల్"
                AppLanguage.TAMIL -> "நிர்வாக குழு"
                AppLanguage.MARATHI -> "व्यवस्थापक पॅनेल"
            }
            else -> key
        }
    }
}
