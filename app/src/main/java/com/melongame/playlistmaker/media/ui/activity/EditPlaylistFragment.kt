package com.melongame.playlistmaker.media.ui.activity

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.media.ui.view_model.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistFragment() {

    private val editPlaylistViewModel: EditPlaylistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createButton.text = getString(R.string.save)
        binding.screenTitle.text = getString(R.string.edit_playlist)

        val playlistId = arguments?.getLong(PLAYLIST_ID) ?: return
        editPlaylistViewModel.loadPlaylist(playlistId)
        editPlaylistViewModel.name.observe(viewLifecycleOwner) { name ->
            binding.nameEditText.setText(name)
        }

        editPlaylistViewModel.description.observe(viewLifecycleOwner) { description ->
            binding.descriptionEditText.setText(description)
        }

        editPlaylistViewModel.coverImageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                Glide.with(this)
                    .load(it)
                    .apply(RequestOptions().override(ONE_HUNDRED, ONE_HUNDRED))
                    .transform(CenterCrop(), RoundedCorners(ROUNDED_CORNERS_EIGHT))
                    .into(binding.placeForCover)
            }
            binding.playerTrackArtwork.isVisible = uri == null
        }
        binding.createButton.setOnClickListener {
            if (binding.nameEditText.text?.isNotEmpty() == true) {
                val coverUriToSave = selectedImageUri?.toString()
                    ?: editPlaylistViewModel.coverImageUri.value?.toString()
                editPlaylistViewModel.savePlaylist(
                    binding.nameEditText.text.toString(),
                    binding.descriptionEditText.text.toString(),
                    coverUriToSave
                )
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    PLAYLIST_UPDATE,
                    true
                )
                findNavController().popBackStack()
            }
        }

        editPlaylistViewModel.playlistCreated.observe(viewLifecycleOwner) { success ->
            if (success) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        binding.playerTrackArtwork.isVisible = editPlaylistViewModel.coverImageUri.value != null
    }

    override fun handleBackNavigation() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    companion object {
        private const val PLAYLIST_ID = "playlistId"
        private const val PLAYLIST_UPDATE = "playlist_update"
        private const val ONE_HUNDRED = 1000
        private const val ROUNDED_CORNERS_EIGHT = 8
    }
}