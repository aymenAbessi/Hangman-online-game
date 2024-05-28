package com.example.myapplication
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditFile : AppCompatActivity() {

    private lateinit var wordStorage: WordStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_file)

        wordStorage = WordStorage(this)

        val editTextSearch = findViewById<EditText>(R.id.editTextSearch)
        val editTextAdd = findViewById<EditText>(R.id.editTextAdd)
        val editTextDelete = findViewById<EditText>(R.id.editTextDelete)

        val buttonSearch = findViewById<Button>(R.id.buttonSearch)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)

        buttonSearch.setOnClickListener {
            val wordToSearch = editTextSearch.text.toString()
            val isWordFound = wordStorage.searchWord(wordToSearch)
            showMessage("Word '$wordToSearch' found: $isWordFound")
        }

        buttonAdd.setOnClickListener {
            val wordToAdd = editTextAdd.text.toString()
            wordStorage.addWord(wordToAdd)
            showMessage("Word '$wordToAdd' added successfully")
        }

        buttonDelete.setOnClickListener {
            val wordToDelete = editTextDelete.text.toString()
            val isWordDeleted = wordStorage.deleteWord(wordToDelete)
            if (isWordDeleted) {
                showMessage("Word '$wordToDelete' deleted successfully")
            } else {
                showMessage("Word '$wordToDelete' not found")
            }
        }
    }

    private fun showMessage(message: String) {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        println(message)
    }
}
