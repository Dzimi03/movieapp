package com.example.movie.movieList.data.remote

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.movie.movieList.domain.model.Movie
import com.example.movie.movieList.data.remote.MovieApi
import com.example.movie.movieList.data.mappers.toMovie // <-- DODAJ TEN IMPORT
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import android.util.Log

fun getMoviesFromGemini(
    prompt: String,
    onResult: (List<Movie>) -> Unit
) {

    Log.d("GeminiAI", "getMoviesFromGemini called with prompt: $prompt")
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val apiKey = "AIzaSyD6CZLaXrNFTxEZRj5oy81Q7JxDjZZILkc"
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"
            val body = """
                {
                  "contents": [{
                    "parts": [{
                      "text": "I want to watch a movies about: $prompt. Suggest 5 most popular movie titles. Return only a JSON array of titles."
                    }]
                  }]
                }
            """.trimIndent()
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .post(body.toRequestBody("application/json".toMediaType()))
                .build()


            Log.d("GeminiAI", "Wysłano zapytanie do Gemini")
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            Log.d("GeminiAI", "Odpowiedź Gemini: $responseBody")



            val json = JSONObject(responseBody)

            if (json.has("error")) {
                Log.e("GeminiAI", "API error: ${json.getJSONObject("error").getString("message")}")
                withContext(Dispatchers.Main) { onResult(emptyList()) }
                return@launch
            }


            val candidates = json.getJSONArray("candidates")



            val content = candidates.getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")

// Usuń znaczniki ```json i ```
            val cleanedText = content
                .replace("```json", "")
                .replace("```", "")
                .trim()

            val titles = try {
                val arr = JSONObject("{\"arr\":$cleanedText}").getJSONArray("arr")
                List(arr.length()) { arr.getString(it) }
            } catch (e: Exception) {
                emptyList<String>()
            }




            Log.d("GeminiAI", "Titles: $titles")
            Log.d("GeminiAI", "Titles: $responseBody")

            val retrofit = Retrofit.Builder()
                .baseUrl(MovieApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val movieApi = retrofit.create(MovieApi::class.java)
            val movies = titles.mapNotNull { title ->
                try {
                    val result = movieApi.searchMovies(title)
                    result.results.firstOrNull()?.let { dto ->
                        dto.toMovie("ai") // <-- Użyj mappera
                    }
                } catch (e: Exception) { null }
            }

            withContext(Dispatchers.Main) {
                onResult(movies)
            }
        } catch (e: Exception) {
            Log.e("GeminiAI", "Błąd: ${e.message}", e)
            withContext(Dispatchers.Main) {
                onResult(emptyList())
            }
        }
    }
}