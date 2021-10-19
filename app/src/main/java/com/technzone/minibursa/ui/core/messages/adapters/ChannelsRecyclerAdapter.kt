package com.technzone.minibursa.ui.core.messages.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.R
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.databinding.RowChannelBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder
import com.technzone.minibursa.ui.base.bindingadapters.loadImage
import com.technzone.minibursa.utils.DateTimeUtil.TWILIO_FULL_DATE_TIME_FORMATTING
import com.technzone.minibursa.utils.extensions.getNotificationDateForamteed
import com.technzone.minibursa.utils.extensions.toMillieSecconds
import com.twilio.chat.CallbackListener
import com.twilio.chat.Channel
import com.twilio.chat.ErrorInfo
import com.twilio.chat.Message
import org.json.JSONException

class ChannelsRecyclerAdapter constructor(
    context: Context,
    private val business: Boolean
) : BaseBindingRecyclerViewAdapter<Channel>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowChannelBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowChannelBinding) :
        BaseViewHolder<Channel>(binding.root) {

        override fun bind(item: Channel) {
            try {
                binding.tvName.text = item.attributes.jsonObject?.optString(
                    if (business) Constants.Twilio.TWILIO_USERNAME2 else Constants.Twilio.TWILIO_USERNAME,
                    context.resources.getString(R.string.investor)
                )
                binding.ivProfileImage.loadImage(
                    item.attributes.jsonObject?.optString(
                        if (business) Constants.Twilio.TWILIO_PICTURE2 else Constants.Twilio.TWILIO_PICTURE,
                        ""
                    ), R.drawable.ic_profile_place_holder
                )
            } catch (e: JSONException) {

            }
            item?.messages?.getLastMessages(1, object : CallbackListener<List<Message>>() {
                override fun onSuccess(p0: List<Message>?) {
                    when (p0?.size ?: 0 > 0) {
                        true -> {
                            binding?.tvMessage.text = getLastMessage(p0?.get(0))
                            binding?.tvTime?.text = p0?.get(0)?.dateCreated?.let {
                                getDateFormated(
                                    it
                                )
                            }
                        }
                        else -> {
                            binding?.tvMessage.text = context.resources.getString(
                                R.string.chat_with_invistor
                            )
                            binding?.tvTime?.text =
                                item.attributes.jsonObject?.optString("createdAt")?.let {
                                    getDateFormated(
                                        it
                                    )
                                }
                        }
                    }
                }

                override fun onError(errorInfo: ErrorInfo?) {
                    super.onError(errorInfo)
                    binding?.tvMessage.text = context.resources.getString(
                        R.string.chat_with_invistor
                    )
                    binding?.tvTime?.text =
                        item.attributes.jsonObject?.optString("createdAt")?.let {
                            getDateFormated(
                                it
                            )
                        }
                }
            })

            binding?.root?.setOnClickListener {
                itemClickListener?.onItemClick(it, adapterPosition, item)
            }

        }

        private fun getLastMessage(message: Message?): String {
            return if (message?.type == Message.Type.TEXT && !message.messageBody.isNullOrEmpty())
                message.messageBody
            else if (message?.type == Message.Type.MEDIA) {
                context.resources.getString(
                    R.string.image
                )
            } else context.resources.getString(
                R.string.chat_with_invistor
            )
        }

        fun getDateFormated(date: String): String {
            return date.toMillieSecconds(TWILIO_FULL_DATE_TIME_FORMATTING)
                .getNotificationDateForamteed()
                ?: ""
        }

    }
}