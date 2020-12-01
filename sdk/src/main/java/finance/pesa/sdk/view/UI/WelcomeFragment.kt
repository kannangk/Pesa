package finance.pesa.sdk.view.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.viewpagerindicator.CirclePageIndicator
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.view.adapter.FragmentAdapter

class WelcomeFragment : Fragment() {
    var getStarted: RelativeLayout? = null


    companion object {
        fun newInstance(): WelcomeFragment {
            return WelcomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.welcome_main, container, false)
        val viewPager = view.findViewById<ViewPager>(R.id.view_pager)
        val adapter = FragmentAdapter(childFragmentManager, 3)
        val indicator = view.findViewById<CirclePageIndicator>(R.id.indicator)
        getStarted = view.findViewById(R.id.get_started)
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val edit = prefs.edit()
        edit.putBoolean(Constants.PREFS_RUN_FIRST, true)
        edit.commit()
        viewPager!!.adapter = adapter
        indicator.setViewPager(viewPager)
        getStarted?.setOnClickListener {
            PesaApplication.getChildFragmentManager().beginTransaction()
                .replace(R.id.shareg_main_container, EnterMobileNumber())
                .commitAllowingStateLoss()
        }
        return view
    }


}

class WelcomeFirst : Fragment() {
    companion object {
        fun newInstance(): WelcomeFirst {
            val fragment = WelcomeFirst()
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.welcome_1, container, false)
        return view
    }
}

class WelcomeSecond : Fragment() {
    companion object {
        fun newInstance(): WelcomeSecond {
            val fragment = WelcomeSecond()
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.welcome_1, container, false)
        return view
    }
}

class WelcomeThird : Fragment() {
    companion object {
        fun newInstance(): WelcomeThird {
            val fragment = WelcomeThird()
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.welcome_1, container, false)
        return view
    }
}