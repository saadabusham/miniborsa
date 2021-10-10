package com.technzone.minibursa.data.repos.configuration

import com.technzone.minibursa.common.CommonEnums
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseHandler
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.daos.remote.configuration.ConfigurationRemoteDao
import com.technzone.minibursa.data.models.business.business.PropertiesItem
import com.technzone.minibursa.data.models.configuration.ConfigurationWrapperResponse
import com.technzone.minibursa.data.models.country.Country
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.data.models.investor.investors.CategoriesItem
import com.technzone.minibursa.data.pref.configuration.ConfigurationPref
import com.technzone.minibursa.data.repos.base.BaseRepo
import javax.inject.Inject

class ConfigurationRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val configurationRemoteDao: ConfigurationRemoteDao,
    private val configurationPref: ConfigurationPref
) : BaseRepo(responseHandler), ConfigurationRepo {

    override fun setAppLanguage(selectedLanguage: CommonEnums.Languages) {
        configurationPref.setAppLanguageValue(selectedLanguage.value)
    }

    override fun getAppLanguage(): CommonEnums.Languages {
        return CommonEnums.Languages.getLanguageByValue(configurationPref.getAppLanguageValue())
    }

    override suspend fun loadConfigurationData():
            APIResource<ResponseWrapper<ConfigurationWrapperResponse>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getAppConfiguration())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getCountries(
        name:String?,
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<Country>>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getCountries(name,pageSize,pageNumber))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getCategories(
        parentId:Int?,
        name:String?,
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<CategoriesItem>>> {
        return try {
            responseHandle.handleSuccess(
                configurationRemoteDao.getCategories(
                   parentId, name, pageSize, pageNumber
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getInvestorCategories(
        parentId: Int?,
        name: String?,
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<CategoriesItem>>> {
        return try {
            responseHandle.handleSuccess(
                configurationRemoteDao.getInvestorCategories(
                    parentId, name, pageSize, pageNumber
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getProperties(
        parentId:Int?,
        name:String?,
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<PropertiesItem>>> {
        return try {
            responseHandle.handleSuccess(
                configurationRemoteDao.getProperty(
                    parentId, name, pageSize, pageNumber
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }    }

}