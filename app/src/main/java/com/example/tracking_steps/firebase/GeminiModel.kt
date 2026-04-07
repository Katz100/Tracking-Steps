package com.example.tracking_steps.firebase

import android.graphics.Bitmap
import com.example.tracking_steps.data.FoodRepository
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

class GeminiModel @Inject constructor(
    private val foodRepository: FoodRepository,
) {
    companion object {
        const val CALORIES_PROMPT = "How many calories are in this food item?"
    }

    val jsonSchema = Schema.obj(
        mapOf(
            "foodName" to Schema.string(),
            "calories" to Schema.integer(),
        ),
    )

    private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(
                modelName = "gemini-3-flash-preview",
                generationConfig = generationConfig {
                    responseMimeType = "application/json"
                    responseSchema = jsonSchema
                }
            )

    val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            foodRepository.currentDayFoodItems.collect {
                Timber.d("Food item: $it")
            }
        }
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
            val foodItem = FoodItem(foodName = foodName, calories = calories)
            addFoodItemToDB(foodItem)
            return foodItem
        } else {
            null
        }
    }

    fun addFoodItemToDB(foodItem: FoodItem) {
        scope.launch {
            foodRepository.insertFoodItem(foodItem)
        }
    }
}

data class FoodItem (
    val id: Int? = null,
    val foodName: String,
    val calories: Int,
    val dateAdded: Date? = null
)