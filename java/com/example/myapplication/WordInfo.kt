package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class WordInfo : AppCompatActivity() {

    private val client = OkHttpClient()

    @Throws(IOException::class)
    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("WordInfo", "Request failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseBody = response.body?.string()
                    Log.i("WordInfo", "Response JSON: $responseBody")

                    // Process the JSON data as needed
                    if (response.isSuccessful && responseBody != null) {
                        val jsonArray = JSONArray(responseBody)
                        if (jsonArray.length() > 0) {
                            val jsonObject = jsonArray.getJSONObject(0)
                            // Do something with the jsonObject
                            // For example, update the UI with the parsed data
                            updateUI(jsonObject)
                        } else {
                            Log.e("WordInfo", "Empty JSON array")
                        }
                    } else {
                        Log.e("WordInfo", "Unsuccessful response")
                    }

                } catch (e: JSONException) {
                    Log.e("WordInfo", "Error parsing JSON: ${e.message}")
                }
            }

        })
    }

    private fun updateUI(jsonObject: JSONObject) {
        try {
            val word = jsonObject.getString("word")

            val phonetic = if (jsonObject.has("phonetic")) {
                jsonObject.getString("phonetic")
            } else {
                "Phonetic information not available"
            }

            val phoneticsArray = jsonObject.getJSONArray("phonetics")
            val meaningsArray = jsonObject.getJSONArray("meanings")

            val stringBuilder = StringBuilder()

            // Word and Phonetic
            stringBuilder.append("Word: $word\n")
            stringBuilder.append("Phonetic: $phonetic\n\n")

            // Phonetics
            if (phoneticsArray.length() > 0) {
                stringBuilder.append("Phonetics:\n")
                for (k in 0 until phoneticsArray.length()) {
                    val phoneticObject = phoneticsArray.getJSONObject(k)
                    val phoneticText = phoneticObject.optString("text", "")
                    stringBuilder.append("- Text: $phoneticText\n")
                }
                stringBuilder.append("\n")
            }

            // Meanings
            if (meaningsArray.length() > 0) {
                stringBuilder.append("Meanings:\n")
                for (i in 0 until meaningsArray.length()) {
                    val meaningObject = meaningsArray.getJSONObject(i)
                    val partOfSpeech = meaningObject.getString("partOfSpeech")

                    val definitionsArray = meaningObject.getJSONArray("definitions")
                    for (j in 0 until definitionsArray.length()) {
                        val definitionObject = definitionsArray.getJSONObject(j)
                        val definition = definitionObject.getString("definition")
                        val example = definitionObject.optString("example", "")

                        // Definition details
                        stringBuilder.append("  Part of Speech: $partOfSpeech\n")
                        stringBuilder.append("  Definition: $definition\n")
                        if (example.isNotEmpty()) {
                            stringBuilder.append("  Example: $example\n")
                        }
                    }
                    stringBuilder.append("-------------------------\n")
                }
            }

            // Update the TextView with the extracted information
            runOnUiThread {
                val textView: TextView = findViewById(R.id.wordinfos)
                textView.text = stringBuilder.toString()
            }

        } catch (e: JSONException) {
            Log.e("WordInfo", "Error parsing JSON: ${e.message}")
        }
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_info)

        var word = intent.getStringExtra("word").toString()
        val text: TextView = findViewById(R.id.def)
        text.text = word

        run("https://api.dictionaryapi.dev/api/v2/entries/en/${word}")
    }
}
