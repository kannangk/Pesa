package finance.pesa.sdk.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import finance.pesa.sdk.view.UI.SupplyFragment

class SupplyWithdrawAdapter (fm:FragmentManager, private val count: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            SupplyFragment()
        } else  {
            Fragment()
        }
    }

    override fun getCount(): Int {
        return count
    }

}