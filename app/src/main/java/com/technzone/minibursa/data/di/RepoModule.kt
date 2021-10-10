package com.technzone.minibursa.data.di


import com.technzone.minibursa.data.repos.business.BusinessRepo
import com.technzone.minibursa.data.repos.business.BusinessRepoImp
import com.technzone.minibursa.data.repos.common.CommonRepo
import com.technzone.minibursa.data.repos.common.CommonRepoImp
import com.technzone.minibursa.data.repos.configuration.ConfigurationRepo
import com.technzone.minibursa.data.repos.configuration.ConfigurationRepoImp
import com.technzone.minibursa.data.repos.investors.InvestorsRepo
import com.technzone.minibursa.data.repos.investors.InvestorsRepoImp
import com.technzone.minibursa.data.repos.searchbusiness.SearchedBusinessRepo
import com.technzone.minibursa.data.repos.searchbusiness.SearchedBusinessRepoImp
import com.technzone.minibursa.data.repos.twilio.TwilioRepo
import com.technzone.minibursa.data.repos.twilio.TwilioRepoImp
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.data.repos.user.UserRepoImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Singleton
    @Binds
    abstract fun bindConfigurationRepo(configurationRepoImp: ConfigurationRepoImp): ConfigurationRepo

    @Singleton
    @Binds
    abstract fun bindUserRepo(userRepoImp: UserRepoImp): UserRepo

    @Singleton
    @Binds
    abstract fun bindTwilioRepo(twilioRepoImp: TwilioRepoImp): TwilioRepo

    @Singleton
    @Binds
    abstract fun bindBusinessRepo(businessRepoImp: BusinessRepoImp): BusinessRepo

    @Singleton
    @Binds
    abstract fun bindInvestorRepo(investorsRepoImp: InvestorsRepoImp): InvestorsRepo

    @Singleton
    @Binds
    abstract fun bindCommonRepo(commonRepoImp: CommonRepoImp): CommonRepo

    @Singleton
    @Binds
    abstract fun bindSearchedBusinessRepo(searchedBusinessRepoImp: SearchedBusinessRepoImp): SearchedBusinessRepo

}