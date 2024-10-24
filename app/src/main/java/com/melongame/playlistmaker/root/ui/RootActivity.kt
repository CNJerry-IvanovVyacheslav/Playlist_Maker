package com.melongame.playlistmaker.root.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.createPlaylistFragment -> hideBottomNavigationView()
                R.id.playlistViewingFragment -> hideBottomNavigationView()
                R.id.editPlaylistFragment -> hideBottomNavigationView()
                else -> showBottomNavigationView()
            }
        }
    }

    private fun hideBottomNavigationView() {
        binding.bottomNavigationView.isVisible = false
        binding.bottomNavigationSeparator.isVisible = false
    }

    private fun showBottomNavigationView() {
        binding.bottomNavigationView.isVisible = true
        binding.bottomNavigationSeparator.isVisible = true
    }

}