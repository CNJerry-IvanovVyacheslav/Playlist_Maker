package com.melongame.playlistmaker.media.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.databinding.FragmentPlaylistBinding
import com.melongame.playlistmaker.media.ui.view_model.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel: PlaylistsFragmentViewModel by viewModel()
    private lateinit var playlistAdapter: PlaylistAdapter

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistAdapter = PlaylistAdapter(mutableListOf())
        binding.playlist.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = playlistAdapter
        }

        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            if (playlists.isNotEmpty()) {
                binding.playlist.visibility = View.VISIBLE
                binding.placeholderNotFound.visibility = View.GONE
                playlistAdapter.setPlaylists(playlists)
            } else {
                binding.playlist.visibility = View.GONE
                binding.placeholderNotFound.visibility = View.VISIBLE
            }
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.createPlaylistFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsFragment()

    }

}