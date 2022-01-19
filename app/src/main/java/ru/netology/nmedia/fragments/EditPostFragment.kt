package ru.netology.nmedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.androidUtils.StringArg
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.viewModel.PostViewModel

class EditPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentEditPostBinding.inflate(layoutInflater, container, false)

        arguments?.textArg?.let {
            binding.editText.setText(it)
        }
        binding.editText.requestFocus()

        binding.cancelEditButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addButton.setOnClickListener {
            val text = binding.editText.text.toString()
            if (text.isNotBlank()) {
                viewModel.changePostText(text)
                viewModel.save()
            }
            findNavController().navigateUp()
        }
        return binding.root
    }
}