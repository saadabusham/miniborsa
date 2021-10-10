package com.technzone.minibursa.ui.core.faqs

import androidx.lifecycle.liveData
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.repos.common.CommonRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
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