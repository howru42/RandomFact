package com.temp.randomfact.activities.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup.OnCheckedChangeListener
import com.temp.randomfact.data.model.FactInfo
import com.temp.randomfact.data.repository.State
import com.temp.randomfact.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"
    private lateinit var binding: ActivityMainBinding
    private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.Factory }
    private var selectedLang: LANGUAGE by Delegates.observable(LANGUAGE.ENGLISH) { _, oldValue, newValue ->
        homeViewModel.selectedLanguage = selectedLang.locale
        getFactsFromAPI(oldValue != newValue)
    }
    private var factInfo: FactInfo? = null

    private fun getFactsFromAPI(onLanguageChange: Boolean = false) {
        if (factInfo == null || onLanguageChange) {
            homeViewModel.getFacts(true)
            // Pull extra random facts from API for buffer
            repeat(3) {
                homeViewModel.fetchRandomFacts()
            }
        } else {
            homeViewModel.getFacts()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = homeViewModel
        binding.factInfo = factInfo
        binding.cgLanguage.setOnCheckedStateChangeListener { chipGroup, i ->
            val chip = chipGroup.findViewById<Chip>(i.first())
            if (chip != null) selectedLang = LANGUAGE.valueOf(chip.text.toString().uppercase())
        }
        homeViewModel.init()
        getFactsFromAPI()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.factsStateFlow.collect { state ->
                    when (state) {
                        is State.Loading -> if (factInfo == null) showToast("Loading")
                        is State.Error -> showToast("Failed --" + state.message)
                        is State.Success -> {
                            factInfo = state.factInfo
                            binding.factInfo = factInfo
                            binding.notifyPropertyChanged(BR.factInfo)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

enum class LANGUAGE(val locale: String) {
    ENGLISH("en"), GERMAN("de")
}