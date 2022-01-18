package ru.netology.nmedia.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postText = intent.getStringExtra(Intent.EXTRA_TEXT)
        binding.editText.setText(postText)
        binding.editText.requestFocus()

        binding.cancelEditButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }

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