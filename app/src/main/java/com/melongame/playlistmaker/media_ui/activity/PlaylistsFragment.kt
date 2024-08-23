package com.melongame.playlistmaker.media_ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.melongame.playlistmaker.databinding.FragmentPlaylistBinding
import com.melongame.playlistmaker.media_ui.view_model.PlaylistsFragmentViewModel

class PlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = PlaylistsFragment()

    }
}