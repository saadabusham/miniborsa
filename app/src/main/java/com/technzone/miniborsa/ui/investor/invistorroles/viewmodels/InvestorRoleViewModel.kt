package com.technzone.miniborsa.ui.investor.invistorroles.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.common.Constants.MAX_PRICE_LENGTH
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.models.auth.login.UserRoles
import com.technzone.miniborsa.data.repos.configuration.ConfigurationRepo
import com.technzone.miniborsa.data.repos.investors.InvestorsRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvestorRoleViewModel @Inject constructor(
    private val configurationRepo: ConfigurationRepo,
    private val investorsRepo: InvestorsRepo,
    private val userRepo: UserRepo
) : BaseViewModel() {
    val defaultMinValue: Int = 1000
    val defaultMaxValue: Int = 1000000
    val maxLength: Int = MAX_PRICE_LENGTH
    val budgetOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)
    val investmentPrice: MutableLiveData<Double> = MutableLiveData(1000.0)
    val jobTitle: MutableLiveData<String> = MutableLiveData("")
    val bio: MutableLiveData<String> = MutableLiveData("")

    fun getCountries() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getCountries(
            pageSize = 1000,
            pageNumber = 1
        )
        emit(response)
    }

    fun getCategories() = liveData {
        emit(APIResource.loading())
        val response =
            configurationRepo.getCategories(
                pageSize = 1000,
                pageNumber = 1
            )
        emit(response)
    }

    fun becomeInvestor(
        countries: List<Int>,
        categories: List<Int>
    ) = liveData {
        emit(APIResource.loading())
        val response =
            investorsRepo.becomeInvestor(
                jobTitle.value,
                bio.value,
                investmentPrice.value ?: 0.0,
                budgetOnRequest.value ?: false,
                countries,
                categories,
                true
            )
        emit(response)
    }

    fun addInvestorRole() {
        userRepo.getUser()?.apply {
            roles?.add(UserRoles(role = UserRoleEnums.INVESTOR_ROLE.value))
        }?.let {
            userRepo.setUser(
                it
            )
        }
    }

    fun setUserRole(role: String) {
        return userRepo.setCurrentRole(role)
    }

}