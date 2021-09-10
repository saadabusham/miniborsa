package com.technzone.miniborsa.ui.core.messages.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.RowChannelBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder
import com.technzone.miniborsa.ui.base.bindingadapters.loadImage
import com.technzone.miniborsa.utils.extensions.getFullDate
import com.technzone.miniborsa.utils.extensions.getNotificationDateForamteed
import com.technzone.miniborsa.utils.extensions.loadImage
import com.technzone.miniborsa.utils.extensions.toDate
import com.twilio.chat.CallbackListener
import com.twilio.chat.Channel
import com.twilio.chat.ErrorInfo
import com.twilio.chat.Message
import org.json.JSONException

class ChannelsRecyclerAdapter constructor(
    context: Context
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
                    "fullName",
                    context.resources.getString(R.string.investor)
                )
                binding.ivProfileImage.loadImage(
                    item.attributes.jsonObject?.optString(
                        "picture",
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
            else context.resources.getString(
                R.string.chat_with_invistor
            )
        }

        fun getDateFormated(date: String): String {
            return date?.getFullDate().toDate()?.time?.getNotificationDateForamteed()
                ?: ""
        }

    }
}