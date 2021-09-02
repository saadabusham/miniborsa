package com.technzone.miniborsa.ui.auth.language

import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.common.CommonEnums
import com.technzone.miniborsa.databinding.FragmentLanguageBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : BaseBindingFragment<FragmentLanguageBinding>() {

    private val viewModel: LanguageViewModel by navGraphViewModels(R.id.auth_nav_graph) { defaultViewModelProviderFactory }

    override fun getLayoutId(): Int = R.layout.fragment_language

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        if (viewModel.languageSettled) {
            navigationController.navigate(R.id.action_languageFragment_to_onBoardingFragment)
        }
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnContinue?.setOnClickListener {
            viewModel.saveLanguage(CommonEnums.Languages.English.value).observe(viewLifecycleOwner,
                {
                    activity?.let {
                        if (!viewModel.getIsLanguageSelected(CommonEnums.Languages.English.value))
                            (it as BaseBindingActivity<*>).setLanguage(CommonEnums.Languages.English.value)
                        navigationController.navigate(R.id.action_languageFragment_to_onBoardingFragment)
                    }
                })
        }

    }
}