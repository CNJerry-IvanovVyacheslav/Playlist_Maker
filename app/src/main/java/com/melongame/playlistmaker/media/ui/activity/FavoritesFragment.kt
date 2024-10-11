package com.melongame.playlistmaker.media.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.melongame.playlistmaker.databinding.FragmentFavoriteBinding
import com.melongame.playlistmaker.media.ui.view_model.FavoritesFragmentViewModel
import com.melongame.playlistmaker.media.ui.view_model.FavoritesState
import com.melongame.playlistmaker.player.ui.activity.MediaPlayerActivity
import com.melongame.playlistmaker.search.domain.models.Track
import com.melongame.playlistmaker.search.ui.activity.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


open class FavoritesFragment : Fragment() {

    private val viewModel by viewModel<FavoritesFragmentViewModel>()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val adapter = TrackAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            setState(it)
        }
        binding.rvFavorites.adapter = adapter
        binding.rvFavorites.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter.onItemClick = { track -> onTrackClicked(track) }
    }

    private fun onTrackClicked(track: Track) {
        if (viewModel.clickDebounce()) {
            val mediaPlayerIntent = Intent(requireContext(), MediaPlayerActivity::class.java)
            mediaPlayerIntent.putExtra(KEY_TRACK, track)
            startActivity(mediaPlayerIntent)
        }
    }

    private fun showNotFound() {
        binding.rvFavorites.isVisible = false
        binding.placeholderNotFound.isVisible = true
    }

    private fun showTrackList() {
        binding.placeholderNotFound.isVisible = false
        binding.rvFavorites.isVisible = true
    }

    private fun setState(state: FavoritesState) {
        when (state) {
            is FavoritesState.Empty -> showNotFound()
            is FavoritesState.Content -> {
                showTrackList()
                adapter.tracks = state.tracks
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        const val KEY_TRACK = "track"
    }
}