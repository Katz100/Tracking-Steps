package com.example.tracking_steps.firebase

import android.graphics.Bitmap
import androidx.lifecycle.DefaultLifecycleObserver
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import org.json.JSONObject
import javax.inject.Inject

class GeminiModel @Inject constructor(): DefaultLifecycleObserver {
    companion object {
        const val CALORIES_PROMPT = "How many calories are in this food item?"
    }
    private val model by lazy {
        val jsonSchema = Schema.obj(
                    mapOf(
                        "calories" to Schema.string()
                    ),
                )
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(
                modelName = "gemini-3-flash-preview",
                generationConfig = generationConfig {
                    responseMimeType = "application/json"
                    responseSchema = jsonSchema
                }
            )
    }

    suspend fun generateContentFromImage(imageBitmap: Bitmap): String? {
        val promptToSend = content {
            image(imageBitmap)
            text(CALORIES_PROMPT)
        }

        val response = model.generateContent(promptToSend).text

        return if (response != null) {
            JSONObject(response).getString("calories")
        } else {
            null
        }
    }
}