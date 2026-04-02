package com.example.tracking_steps.firebase

import android.graphics.Bitmap
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class GeminiModel @Inject constructor() {
    companion object {
        const val CALORIES_PROMPT = "How many calories are in this food item?"
    }
    private val model by lazy {
        Timber.i("Initializing model")
        val jsonSchema = Schema.obj(
                    mapOf(
                        "foodName" to Schema.string(),
                        "calories" to Schema.integer(),
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

    suspend fun generateContentFromImage(imageBitmap: Bitmap): FoodItem? {
        val promptToSend = content {
            image(imageBitmap)
            text(CALORIES_PROMPT)
        }

        val response = model.generateContent(promptToSend).text

        return if (response != null) {
            val calories = JSONObject(response).getInt("calories")
            val foodName = JSONObject(response).getString("foodName")
            FoodItem(foodName, calories)
        } else {
            null
        }
    }
}

data class FoodItem (
    val foodName: String,
    val calories: Int,
)