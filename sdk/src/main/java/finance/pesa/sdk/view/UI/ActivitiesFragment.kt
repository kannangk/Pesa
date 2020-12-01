package finance.pesa.sdk.view.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.Model.ActivitiesData
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.NoSwipeViewPager
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.view.UI.SendRequestFragment.Companion.sendRequestFragment
import finance.pesa.sdk.view.adapter.ViewPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivitiesFragment : Fragment() {
    var isViewGenerated: Boolean? = false
    private var tabLayout: TabLayout? = null
    var viewpager: ViewPager? = null
    var activitiesData: ActivitiesData? = null

    companion object {
        var activitiesFragment: ActivitiesFragment? = ActivitiesFragment()
        fun newInstance(): ActivitiesFragment {
            return activitiesFragment!!
        }
    }

    fun onTabSelected() {
        try {
            // if (!isViewGenerated!!) {
            UserInterface.changeStatusBar(activity!!, R.color.white_black)
            isViewGenerated = true
            loadAllActivities(1)
            //  }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun onSelectFromOutSide(pos: Int) {
        viewpager?.currentItem = pos
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            if (DashboardFragment.dashboardFragment!!.viewPager!!.currentItem == 2) {
                onTabSelected()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activities_main, container, false)
        tabLayout = view.findViewById(R.id.tabLayout)
        viewpager = view.findViewById(R.id.viewpager)
        isViewGenerated = false
        setupViewPager(viewpager!!)
        tabLayout?.setupWithViewPager(viewpager)
        viewpager?.offscreenPageLimit = 5
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager?.currentItem = tab.position
                when (tab.position) {
                    0 -> {
                    }
                }// TODO
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        return view
    }


    private fun setupViewPager(viewPager: ViewPager) {

        val adapter = ViewPagerAdapter(
            childFragmentManager
        )
        adapter.addFrag(
            AllActivitiesFragment.newInstance(Constants.ALL), "All"
        )
        adapter.addFrag(
            AllActivitiesFragment.newInstance(Constants.SENT), "Sent"
        )
        adapter.addFrag(
            AllActivitiesFragment.newInstance(Constants.RECEIVED), "Received"
        )
        adapter.addFrag(
            AllActivitiesFragment.newInstance(Constants.REQUEST), "Request"
        )
        adapter.addFrag(
            AllActivitiesFragment.newInstance(Constants.ESCROW), "Escrow"
        )
        viewPager.adapter = adapter
    }

    //get all activities
    fun loadAllActivities(retryCount: Int) {
        Constants.setIsPendingNotification(false,context)
        UserInterface.updateNotification(false)
        var call: Call<ActivitiesData>? =
            ApiClient.build()?.getAllActivities(Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<ActivitiesData> {
            override fun onFailure(call: Call<ActivitiesData>, t: Throwable) {
                if (retryCount < 3) {
                    val retryCountNew = retryCount + 1
                    loadAllActivities(retryCountNew)
                }
            }

            override fun onResponse(
                call: Call<ActivitiesData>,
                response: Response<ActivitiesData>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        try {
                            activitiesData = it
                            if (Constants.getActivitiesUpdateListeners() != null) {
                                for (listener in Constants.getActivitiesUpdateListeners()!!) {
                                    listener.onActivityUpdate(it)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (retryCount < 3) {
                            val retryCountNew = retryCount + 1
                            loadAllActivities(retryCountNew)
                        }
                    }
                }
            }

        })

    }
}