package com.example.redmuriapp.main_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.example.redmuriapp.MainActivity
import com.example.redmuriapp.databinding.FragmentMainPageBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainPageFragment : Fragment() {

    private var _binding: FragmentMainPageBinding? = null
    private val binding: FragmentMainPageBinding
        get() = _binding ?: throw RuntimeException("FragmentMainPageBinding == null")

    private val mainPageViewModel by lazy {
        ViewModelProvider(requireActivity())[MainPageViewModel::class.java]
    }

    private var errorToast: Toast? = null

    private val adapterLatestItems by lazy {
        LatestItemsAdapter(requireActivity().application)
    }
    private val adapterFlashSaleItems by lazy {
        FlashSaleItemsAdapter(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserData()
        getBothItems()
        observeViewModel()
        setupRecyclerViews()
        bindClickListeners()
    }

    private fun bindClickListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            setUserData()
            getBothItems()
            observeViewModel()
            setupRecyclerViews()
        }
    }

    private fun setupRecyclerViews() {
        binding.rvLatest.adapter = adapterLatestItems
        binding.rvFlashSale.adapter = adapterFlashSaleItems
    }

    private fun getBothItems() {
        mainPageViewModel.getBothItems()
    }

    private fun observeViewModel() {
        with(mainPageViewModel) {
            userData.observe(viewLifecycleOwner) {
                binding.tvLocation.text = it.location
            }
            bothItems.observe(viewLifecycleOwner) {
                adapterLatestItems.submitList(it.first)
                adapterFlashSaleItems.submitList(it.second)
            }
            mainPageState.observe(viewLifecycleOwner) {
                binding.progressBar.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
                when (it) {
                    is MainSuccess -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is MainProgress -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MainError -> {
                        showError(it.errorCode)
                    }
                }
            }
        }
    }

    private fun showError(errorCode: Int) {
        when (errorCode) {
            MainPageViewModel.ERROR_USER_NOT_FOUND -> {
                showErrorToast("Some error occurred. Try logging in again")
            }
            MainPageViewModel.ERROR_LOADING_ITEMS -> {
                showErrorToast("Something went wrong. Check your internet connection")
            }
        }
    }

    private fun showErrorToast(errorText: String) {
        errorToast?.cancel()
        errorToast = Toast.makeText(
            requireContext(),
            errorText, Toast.LENGTH_SHORT
        )
        errorToast?.show()
    }

    private fun setUserData() {
        val firstName = getFirstNameFromPref()
        mainPageViewModel.getUser(firstName)
    }

    private fun getFirstNameFromPref(): String? {
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return pref.getString(MainActivity.USER_FIRST_NAME, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
