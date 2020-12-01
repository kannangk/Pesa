package finance.pesa.sdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import finance.pesa.sdk.Model.ActivityDetails
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.view.Interface.RequestActivityListerner

class AllActivityAdapter(
    context: Context,
    activityDetails: List<ActivityDetails>,
    requestActivityListerner: RequestActivityListerner
) : RecyclerView.Adapter<AllActivityAdapter.MViewHolder>() {

    var activityDetails: List<ActivityDetails>
    var context: Context? = null
    var requestActivityListerner: RequestActivityListerner? = null

    init {
        this.context = context
        this.activityDetails = activityDetails
        this.requestActivityListerner = requestActivityListerner
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activities_row, parent, false)
        return MViewHolder(view)
    }


    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val activityValue = activityDetails[position]
        try {
            vh.currencyLogo?.setImageDrawable(
                context!!.getDrawable(
                    UserInterface.getCoinIcon(
                        activityValue.cryptoSymbol
                    )
                )
            )
            vh.ivDate?.text = UserInterface.parseDateWithYear(activityValue.createdOn)
            if (Constants.getEPNId() == activityValue.from) {
                if (activityValue.isSendToEPN)
                    vh.ivName?.text = "+" + activityValue.toAddress
                else
                    UserInterface.showWalletAddress(vh.ivName!!, activityValue.toAddress)
            } else {
                vh.ivName?.text = "+" + activityValue.fromEPN
            }

            val currencyValue =
                UserInterface.round(activityValue.amount) + " " + activityValue.cryptoSymbol
            vh.ivStatus?.visibility = View.GONE
            when {
                activityValue.type == "sent" -> {
                    vh.currencyLogo?.setBackgroundResource(
                        R.drawable.ic_activity_up_red
                    )
                    vh.ivType?.text = "Sent"
                    vh.ivValue?.setTextColor(context!!.getColor(R.color.cal_red))
                    vh.ivValue?.text = "-" + currencyValue
                    if (activityValue.from == Constants.getEPNId() && activityValue.isEscrow) {
                        if ((activityValue.status == "success" || activityValue.status == "") && activityValue.isExpired) {
                            vh.currencyLogo?.setBackgroundResource(
                                R.drawable.ic_activity_left_green
                            )
                            vh.ivStatus?.visibility = View.VISIBLE
                            vh.ivStatus?.isClickable = true
                            vh.ivStatus?.text = "Claim now"
                            vh.ivStatus?.setTextColor(context!!.getColor(R.color.app_green))
                            vh.ivValue?.setTextColor(context!!.getColor(R.color.app_green))
                            vh.ivValue?.text = "+" + currencyValue
                            vh.ivStatus?.setBackgroundResource(
                                R.drawable.status_green_bg
                            )
                            vh.ivStatus?.setOnClickListener {
                                requestActivityListerner?.onCliamClicked(
                                    activityValue
                                )
                            }
                        } else if (activityValue.status == "claimed") {
                            vh.currencyLogo?.setBackgroundResource(
                                R.drawable.ic_activity_left_green
                            )
                            vh.ivStatus?.visibility = View.VISIBLE
                            vh.ivStatus?.text = "Claimed"
                            vh.ivStatus?.setTextColor(context!!.getColor(R.color.light_gray_white))
                            vh.ivValue?.setTextColor(context!!.getColor(R.color.light_gray_white))
                            vh.ivValue?.text = currencyValue
                            vh.ivStatus?.setBackgroundResource(
                                R.drawable.status_gray_bg
                            )
                        }
                    } else if (activityValue.from != Constants.getEPNId()) {
                        if ((activityValue.status == "success" || activityValue.status == "") && activityValue.isEscrow && !activityValue.isExpired) {
                            vh.currencyLogo?.setBackgroundResource(
                                R.drawable.ic_activity_right_green
                            )
                            vh.ivType?.text = "Claim Money"
                            vh.ivStatus?.visibility = View.VISIBLE
                            vh.ivStatus?.isClickable = true
                            vh.ivStatus?.text = "Claim now"
                            vh.ivStatus?.setTextColor(context!!.getColor(R.color.app_green))
                            vh.ivValue?.setTextColor(context!!.getColor(R.color.app_green))
                            vh.ivValue?.text = "+" + currencyValue
                            vh.ivStatus?.setBackgroundResource(
                                R.drawable.status_green_bg
                            )
                            vh.ivStatus?.setOnClickListener {
                                requestActivityListerner?.onCliamClicked(
                                    activityValue
                                )
                            }
                        } else if (activityValue.status == "claimed" && activityValue.isEscrow) {
                            vh.currencyLogo?.setBackgroundResource(
                                R.drawable.ic_activity_right_green
                            )
                            vh.ivType?.text = "Claim Money"
                            vh.ivStatus?.visibility = View.VISIBLE
                            vh.ivStatus?.text = "Claimed"
                            vh.ivStatus?.setTextColor(context!!.getColor(R.color.light_gray_white))
                            vh.ivValue?.setTextColor(context!!.getColor(R.color.light_gray_white))
                            vh.ivValue?.text = currencyValue
                            vh.ivStatus?.setBackgroundResource(
                                R.drawable.status_gray_bg
                            )
                        } else {
                            vh.ivType?.text = "Received"
                            vh.ivStatus?.visibility = View.GONE
                            vh.currencyLogo?.setBackgroundResource(
                                R.drawable.ic_activity_down_green
                            )
                            vh.ivValue?.setTextColor(context!!.getColor(R.color.app_green))
                            vh.ivValue?.text = "+" + currencyValue
                        }
                    }
                }
                activityValue.type == "receive" -> {
                    vh.currencyLogo?.setBackgroundResource(
                        R.drawable.ic_activity_down_green
                    )
                    vh.ivType?.text = "Received"
                    vh.ivValue?.setTextColor(context!!.getColor(R.color.app_green))
                    vh.ivValue?.text = "+" + currencyValue
                }
                activityValue.type == "request" -> {
                    vh.ivStatus?.visibility = View.VISIBLE
                    if (activityValue.from == Constants.getEPNId()) {
                        vh.currencyLogo?.setBackgroundResource(
                            R.drawable.ic_activity_left_orange
                        )
                        vh.ivStatus?.isClickable = false
                        vh.ivType?.text = "Request sent"
                        when {
                            activityValue.status == "" -> {
                                vh.ivStatus?.text = "In Review"
                                vh.ivStatus?.setTextColor(context!!.getColor(R.color.progress_select))
                                vh.ivValue?.setTextColor(context!!.getColor(R.color.progress_select))
                                vh.ivValue?.text = "+" + currencyValue
                                vh.ivStatus?.setBackgroundResource(
                                    R.drawable.status_red_bg
                                )
                            }
                            activityValue.status == "success" -> {
                                vh.ivStatus?.text = "In Review"
                                vh.ivStatus?.setTextColor(context!!.getColor(R.color.progress_select))
                                vh.ivValue?.setTextColor(context!!.getColor(R.color.progress_select))
                                vh.ivValue?.text = "+" + currencyValue
                                vh.ivStatus?.setBackgroundResource(
                                    R.drawable.status_red_bg
                                )
                            }
                            activityValue.status == "approved" -> {
                                vh.ivStatus?.text = "Approved"
                                vh.ivStatus?.setTextColor(context!!.getColor(R.color.app_green))
                                vh.ivValue?.setTextColor(context!!.getColor(R.color.app_green))
                                vh.ivValue?.text = "+" + currencyValue
                                vh.ivStatus?.setBackgroundResource(
                                    R.drawable.status_green_bg
                                )
                            }
                            activityValue.status == "declined" -> {
                                vh.currencyLogo?.setBackgroundResource(
                                    R.drawable.ic_activity_left_grey
                                )
                                vh.ivStatus?.text = "Declined"
                                vh.ivStatus?.setTextColor(context!!.getColor(R.color.light_gray_white))
                                vh.ivValue?.setTextColor(context!!.getColor(R.color.light_gray_white))
                                vh.ivValue?.text = currencyValue
                                vh.ivStatus?.setBackgroundResource(
                                    R.drawable.status_gray_bg
                                )
                            }
                            else -> {
                                vh.ivStatus?.text = activityValue.status
                                vh.ivStatus?.setTextColor(context!!.getColor(R.color.progress_select))
                                vh.ivValue?.setTextColor(context!!.getColor(R.color.progress_select))
                                vh.ivValue?.text = "+" + currencyValue
                                vh.ivStatus?.setBackgroundResource(
                                    R.drawable.status_red_bg
                                )
                            }
                        }
                    } else {
                        vh.ivStatus?.isClickable = true
                        vh.ivType?.text = "Received Request"
                        vh.currencyLogo?.setBackgroundResource(
                            R.drawable.ic_activity_right_orange
                        )
                        when {
                            activityValue.status == "success" -> {
                                vh.ivStatus?.text = "Review"
                                vh.ivStatus?.setTextColor(context!!.getColor(R.color.progress_select))
                                vh.ivValue?.setTextColor(context!!.getColor(R.color.progress_select))
                                vh.ivValue?.text = "-" + currencyValue
                                vh.ivStatus?.setBackgroundResource(
                                    R.drawable.status_red_bg
                                )
                                vh.ivStatus?.setOnClickListener {
                                    requestActivityListerner?.onRequestClicked(
                                        activityValue
                                    )
                                }
                            }
                            activityValue.status == "approved" -> {
                                vh.ivStatus?.text = "Approved"
                                vh.ivStatus?.setTextColor(context!!.getColor(R.color.progress_select))
                                vh.ivValue?.setTextColor(context!!.getColor(R.color.progress_select))
                                vh.ivValue?.text = "-" + currencyValue
                                vh.ivStatus?.setBackgroundResource(
                                    R.drawable.status_red_bg
                                )
                            }
                            activityValue.status == "declined" -> {
                                vh.ivStatus?.isClickable = false
                                vh.currencyLogo?.setBackgroundResource(
                                    R.drawable.ic_activity_right_grey
                                )
                                vh.ivStatus?.text = "Declined"
                                vh.ivStatus?.setTextColor(context!!.getColor(R.color.light_gray_white))
                                vh.ivValue?.setTextColor(context!!.getColor(R.color.light_gray_white))
                                vh.ivValue?.text = currencyValue
                                vh.ivStatus?.setBackgroundResource(
                                    R.drawable.status_gray_bg
                                )
                            }
                            else -> {
                                vh.ivStatus?.isClickable = false
                                vh.ivStatus?.text = activityValue.status
                                vh.ivStatus?.setTextColor(context!!.getColor(R.color.progress_select))
                                vh.ivValue?.setTextColor(context!!.getColor(R.color.progress_select))
                                vh.ivValue?.text = "-" + currencyValue
                                vh.ivStatus?.setBackgroundResource(
                                    R.drawable.status_red_bg
                                )
                            }
                        }
                    }
                }
                else -> {
                    vh.currencyLogo?.setBackgroundResource(
                        R.drawable.ic_activity_up_red
                    )
                    vh.ivType?.text = activityValue.type
                    vh.ivValue?.setTextColor(context!!.getColor(R.color.light_gray_white))
                    vh.ivValue?.text = currencyValue
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return activityDetails!!.size
    }

    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivDate: TextView? = view.findViewById(R.id.iv_date)
        var ivName: TextView? = view.findViewById(R.id.iv_name)
        var ivType: TextView? = view.findViewById(R.id.iv_type)
        var currencyLogo: ImageView? = view.findViewById(R.id.currency_logo)
        var ivValue: TextView? = view.findViewById(R.id.iv_value)
        var ivStatus: TextView? = view.findViewById(R.id.iv_status)
    }
}