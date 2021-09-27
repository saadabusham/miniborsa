package com.technzone.miniborsa.ui.core.faqs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.repos.common.CommonRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FaqsViewModel @Inject constructor(
    private val commonRepo: CommonRepo
) : BaseViewModel() {

    fun getFAQs() = liveData {
        emit(APIResource.loading())
        val response = commonRepo.getFaqs(pageSize = 1000000, pageNumber = 1)
        emit(response)
    }


}