package com.technzone.minibursa.ui.auth.language

import androidx.navigation.navGraphViewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.common.CommonEnums
import com.technzone.minibursa.databinding.FragmentLanguageBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : BaseBindingFragment<FragmentLanguageBinding>() {

    private val viewModel: LanguageViewModel by navGraphViewModels(R.id.auth_nav_graph) { defaultViewModelProviderFactory }

    override fun getLayoutId(): Int = R.layout.fragment_language

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnContinue?.setOnClickListener {
            navigationController.navigate(R.id.action_languageFragment_to_onBoardingFragment)
        }
        binding?.linArabic?.setOnClickListener {
            if (viewModel.englishSelected.value == true) {
                viewModel.englishSelected.value = false
                updateLanguage()
            }
        }
        binding?.linEnglish?.setOnClickListener {
            if (viewModel.englishSelected.value == false) {
                viewModel.englishSelected.value = true
                updateLanguage()
            }
        }

    }
    private fun updateLanguage() {
        viewModel.saveLanguage().observe(this, {
            (requireActivity() as BaseBindingActivity<*>)
                .setLanguage(if (viewModel.englishSelected.value!!)
                    CommonEnums.Languages.English.value
                else CommonEnums.Languages.Arabic.value)
        })
    }
}