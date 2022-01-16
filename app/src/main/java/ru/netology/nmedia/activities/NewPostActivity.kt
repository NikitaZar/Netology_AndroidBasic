package ru.netology.nmedia.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editText.requestFocus()
        binding.addButton.setOnClickListener {
            val intent = Intent()
            if (binding.editText.text.isBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val text = binding.editText.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, text)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }
}