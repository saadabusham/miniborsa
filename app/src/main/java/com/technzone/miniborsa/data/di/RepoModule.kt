package com.technzone.miniborsa.data.di


import com.technzone.miniborsa.data.repos.business.BusinessRepo
import com.technzone.miniborsa.data.repos.business.BusinessRepoImp
import com.technzone.miniborsa.data.repos.common.CommonRepo
import com.technzone.miniborsa.data.repos.common.CommonRepoImp
import com.technzone.miniborsa.data.repos.configuration.ConfigurationRepo
import com.technzone.miniborsa.data.repos.configuration.ConfigurationRepoImp
import com.technzone.miniborsa.data.repos.investors.InvestorsRepo
import com.technzone.miniborsa.data.repos.investors.InvestorsRepoImp
import com.technzone.miniborsa.data.repos.twilio.TwilioRepo
import com.technzone.miniborsa.data.repos.twilio.TwilioRepoImp
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.data.repos.user.UserRepoImp
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

}