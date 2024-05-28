package com.example.myapplication
import android.content.Context
import java.io.*


class WordStorage(private val context: Context) {
    private val wordsList: MutableList<String> = mutableListOf()

    init {
        checkAndCopyRawFile()
        loadWordsFromFile(getInternalFilePath())
    }

    private fun checkAndCopyRawFile() {
        val internalFilePath = getInternalFilePath()
        if (!File(internalFilePath).exists()) {
            // copy the raw file to internal storage
            copyRawFileToInternalStorage(R.raw.words, internalFilePath)
        }
    }

    private fun copyRawFileToInternalStorage(rawResourceId: Int, internalFilePath: String) {
        try {
            val inputStream = context.resources.openRawResource(rawResourceId)
            val outputStream = FileOutputStream(File(internalFilePath))

            val buffer = ByteArray(1024)
            var length: Int

            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getInternalFilePath(): String {
        return context.filesDir.path + File.separator + "words.txt"
    }

    private fun loadWordsFromFile(filePath: String) {
        try {
            val reader = BufferedReader(FileReader(filePath))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (!line.isNullOrBlank()) {
                    val words = line?.split("\\s+".toRegex()) ?: emptyList()
                    wordsList.addAll(words)
                }
            }

            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getWords(): List<String> {
        return wordsList
    }

    fun searchWord(word: String): Boolean {
        return wordsList.contains(word)
    }

    fun deleteWord(word: String): Boolean {
        val removed = wordsList.remove(word)
        if (removed) {
            saveToFile(getInternalFilePath())
        }
        return removed
    }

    fun addWord(word: String) {
        wordsList.add(word)
        saveToFile(getInternalFilePath())
        println(getInternalFilePath())
    }

    private fun saveToFile(filePath: String) {
        try {
            val writer = BufferedWriter(FileWriter(filePath))
            for (line in wordsList) {
                writer.write(line + "\n")
            }
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
