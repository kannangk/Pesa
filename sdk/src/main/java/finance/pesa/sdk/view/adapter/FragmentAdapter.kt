package finance.pesa.sdk.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import finance.pesa.sdk.view.UI.WelcomeFirst
import finance.pesa.sdk.view.UI.WelcomeSecond
import finance.pesa.sdk.view.UI.WelcomeThird

class FragmentAdapter(fm: FragmentManager, private val count: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WelcomeFirst.newInstance()
            1 -> WelcomeSecond.newInstance()
            else -> WelcomeThird.newInstance()
        }
    }

    override fun getCount(): Int {
        return count
    }

}