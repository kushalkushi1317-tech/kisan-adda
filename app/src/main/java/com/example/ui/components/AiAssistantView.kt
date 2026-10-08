package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.ChatMessage
import com.example.localization.AppLanguage
import com.example.localization.LocalizationManager
import com.example.ui.theme.HarvestGreenContainer
import com.example.ui.theme.HarvestGreenPrimary
import com.example.ui.theme.SunGold
import com.example.ui.theme.SunGoldContainer

@Composable
fun AiAssistantView(
    chatMessages: List<ChatMessage>,
    language: AppLanguage,
    onSendMessage: (String) -> Unit,
    onVoiceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var queryInput by remember { mutableStateOf("") }

    val quickQuestions = when (language) {
        AppLanguage.HINDI -> listOf(
            "आज टमाटर का भाव क्या है?",
            "प्याज का सबसे अधिक भाव किस मंडी में है?",
            "क्या इस हफ्ते आलू के भाव बढ़े हैं?",
            "मुझे अपने टमाटर कहाँ बेचने चाहिए?"
        )
        AppLanguage.KANNADA -> listOf(
            "ಇಂದಿನ ಟೊಮೆಟೊ ಬೆಲೆ ಎಷ್ಟು?",
            "ಯಾವ ಮಾರುಕಟ್ಟೆಯಲ್ಲಿ ಈರುಳ್ಳಿಗೆ ಗರಿಷ್ಠ ಬೆಲೆ ಇದೆ?",
            "ಈ ವಾರ ಆಲೂಗಡ್ಡೆ ಬೆಲೆ ಹೆಚ್ಚಾಗಿದೆಯೇ?",
            "ನನ್ನ ಟೊಮೆಟೊವನ್ನು ಎಲ್ಲಿ ಮಾರಾಟ ಮಾಡಬೇಕು?"
        )
        AppLanguage.TELUGU -> listOf(
            "ఈరోజు టమాట ధర ఎంత?",
            "ఉల్లిపాయకు అత్యధిక ధర ఎక్కడ ఉంది?",
            "ఈ వారం బంగాళాదుంప ధర పెరిగిందా?",
            "నేను నా టమాటాలను ఎక్కడ అమ్మాలి?"
        )
        AppLanguage.TAMIL -> listOf(
            "இன்றைய தக்காளி விலை என்ன?",
            "எந்த சந்தையில் வெங்காயத்திற்கு அதிக விலை?",
            "இந்த வாரம் உருளைக்கிழங்கு விலை உயர்ந்ததா?",
            "நான் தக்காளியை எங்கு விற்க வேண்டும்?"
        )
        AppLanguage.MARATHI -> listOf(
            "आज टोमॅटोचा भाव काय आहे?",
            "कांद्याला सर्वात जास्त भाव कोणत्या बाजारात आहे?",
            "या आठवड्यात बटाट्याचे भाव वाढले आहेत का?",
            "मी माझे टोमॅटो कुठे विकावे?"
        )
        AppLanguage.ENGLISH -> listOf(
            "What is today's tomato price?",
            "Which market has the highest onion price?",
            "Has the price of potato increased this week?",
            "Where should I sell my tomatoes?"
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // AI Header Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = HarvestGreenContainer.copy(alpha = 0.6f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth().testTag("ai_assistant_header_card")
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(HarvestGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "AI",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "🤖 Kisan Mitra (AI Assistant)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF134E24)
                    )
                    Text(
                        text = "Instant market advice powered by live mandi prices",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF1B4332)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Suggestion Questions Row
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickQuestions.forEach { question ->
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD1D5DB)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onSendMessage(question) }
                ) {
                    Text(
                        text = question,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = HarvestGreenPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chat Message History
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .testTag("chat_messages_box")
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                reverseLayout = false
            ) {
                items(chatMessages) { msg ->
                    ChatBubble(message = msg)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Input bar with text & voice mic
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = queryInput,
                onValueChange = { queryInput = it },
                placeholder = {
                    Text("Ask price, best market, selling advice...", fontSize = 13.sp)
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = HarvestGreenPrimary
                ),
                trailingIcon = {
                    IconButton(onClick = onVoiceClick) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Input",
                            tint = HarvestGreenPrimary
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_query_input_field")
            )

            IconButton(
                onClick = {
                    if (queryInput.isNotBlank()) {
                        onSendMessage(queryInput)
                        queryInput = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(HarvestGreenPrimary)
                    .testTag("send_ai_query_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val isUser = message.isUser
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val bg = if (isUser) HarvestGreenPrimary else Color(0xFFF3F4F6)
    val textColor = if (isUser) Color.White else Color(0xFF111827)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 14.dp
            ),
            color = bg,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}
