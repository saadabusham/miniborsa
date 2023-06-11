package com.technzone.minibursa.ui.subscription.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity
import com.oppwa.mobile.connect.checkout.meta.CheckoutActivityResult
import com.oppwa.mobile.connect.checkout.meta.CheckoutActivityResultContract
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings
import com.oppwa.mobile.connect.exception.PaymentError
import com.oppwa.mobile.connect.provider.Connect
import com.oppwa.mobile.connect.provider.Transaction
import com.oppwa.mobile.connect.provider.TransactionType
import com.technzone.minibursa.BuildConfig
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.enums.SubscriptionTypeEnums
import com.technzone.minibursa.data.models.plan.Plan
import com.technzone.minibursa.databinding.FragmentBusinessSubscriptionBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.subscription.adapter.BusinessSubscriptionRecyclerAdapter
import com.technzone.minibursa.ui.subscription.viewmodel.SubscriptionViewModel
import com.technzone.minibursa.utils.extensions.showErrorAlert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class BusinessSubscriptionActivity : BaseBindingActivity<FragmentBusinessSubscriptionBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {


    private val viewModel: SubscriptionViewModel by viewModels()

    lateinit var subscriptionRecyclerAdapter: BusinessSubscriptionRecyclerAdapter

    var subscriptionId : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.fragment_business_subscription,
            hasToolbar = true
        )
        addToolbar(
            toolbarView = binding?.layoutToolbar?.toolbar,
            tvToolbarTitleView = binding?.layoutToolbar?.tvToolbarTitle,
            hasToolbar = true,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.subscription
        )
        setUpBinding()
        setUpListeners()
        setUpSubscriptionAdapter()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnContinue?.setOnClickListener {
            subscriptionRecyclerAdapter.getSelectedItem().let {
                if (it == null) {
                    showErrorAlert(
                        getString(R.string.app_name),
                        getString(R.string.please_select_subscription)
                    )
                } else {
                    viewModel.createSubscription(
                        intent.getIntExtra(
                            Constants.BundleData.BUSINESS_ID,
                            0
                        ),
                        it.id ?: 0,
                    ).observe(this, createSubscriptionResultObserver())
                }
            }
        }
    }

    private fun setUpSubscriptionAdapter() {
        subscriptionRecyclerAdapter = BusinessSubscriptionRecyclerAdapter(this)
        binding?.recyclerView?.adapter = subscriptionRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        viewModel.getSubscription(SubscriptionTypeEnums.BUSINESS_OWNER.value).observe(this, subscriptionResultObserver())
    }

    private fun subscriptionResultObserver(): CustomObserverResponse<List<Plan>> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<List<Plan>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: List<Plan>?
                ) {
                    data?.let { subscriptionRecyclerAdapter.submitItems(it) }
                }
            })
    }

    private fun createSubscriptionResultObserver(): CustomObserverResponse<Int> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Int> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Int?
                ) {
                    subscriptionId = data
                    subscriptionRecyclerAdapter.getSelectedItem()?.let {
                        if(it.selected){
                            checkout()
                        }else{
                            setResult(RESULT_OK, Intent().apply {
                                putExtra(Constants.BundleData.SUBSCRIPTION_ID, subscriptionId)
                            })
                            finish()
                        }
                    }
                }
            })
    }

    private fun checkout() {
        subscriptionRecyclerAdapter.getSelectedItem()?.let {
            viewModel.generateCheckoutId(it.price ?: 0.0, "USD", subscriptionId?:0)
                .observe(this, observeCheckOutId())
        }
    }

    private fun observeCheckOutId(): CustomObserverResponse<String> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<String> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: String?
                ) {
                    showCheckoutUI(data ?: "")
                }
            }
        )
    }

    fun showCheckoutUI(checkoutId: String) {
        val paymentBrands: MutableSet<String> =
            LinkedHashSet()

        paymentBrands.add("VISA")
        paymentBrands.add("MASTER")
//        paymentBrands.add("GOOGLEPAY")

        val checkoutSettings =
            CheckoutSettings(checkoutId, paymentBrands, if(BuildConfig.DEBUG) Connect.ProviderMode.TEST else Connect.ProviderMode.LIVE)

        checkoutSettings.themeResId = R.style.checkoutTheme

        checkoutSettings.shopperResultUrl = Constants.PAYMENT_URL_BUSINESS + "://result"
        checkoutLauncher.launch(checkoutSettings)
    }

    private val checkoutLauncher = registerForActivityResult(
        CheckoutActivityResultContract()
    ) { result: CheckoutActivityResult ->
        this.handleCheckoutActivityResult(result)
    }

    private fun handleCheckoutActivityResult(result: CheckoutActivityResult) {
        runOnUiThread {
            if (result.isCanceled) {
                return@runOnUiThread
            }

            if (result.isErrored) {
                val error: PaymentError? = result.paymentError
                showErrorAlert(message = error?.errorMessage ?: getString(R.string.error_msg))
                return@runOnUiThread
            }

            /* Transaction completed */
            val transaction: Transaction? = result.transaction

            /* Check the transaction type */
            if (transaction != null) {
                if (transaction.transactionType === TransactionType.SYNC) {
                    viewModel.getPaymentStatus(transaction.paymentParams.checkoutId)
                        .observe(this, observePaymentStatus())
                } else {
                    /* wait for the asynchronous transaction callback in the onNewIntent() */
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.scheme == Constants.PAYMENT_URL_BUSINESS) {
            val checkoutId = intent.data!!.getQueryParameter("id")
            checkoutId?.let { viewModel.getPaymentStatus(it).observe(this, observePaymentStatus()) }
        }
    }


    private fun observePaymentStatus(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(Constants.BundleData.SUBSCRIPTION_ID, subscriptionId)
                    })
                    finish()
                }

            }
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
//        item as GeneralLookup
    }

    companion object {
        fun start(
            context: Context?,
            businessId: Int,
            resultLauncher: ActivityResultLauncher<Intent>
        ) {
            val intent = Intent(context, BusinessSubscriptionActivity::class.java).apply {
                putExtra(Constants.BundleData.BUSINESS_ID, businessId)
            }
            resultLauncher.launch(intent)
        }
    }

}