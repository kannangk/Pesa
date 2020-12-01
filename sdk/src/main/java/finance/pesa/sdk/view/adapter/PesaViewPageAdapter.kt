package finance.pesa.sdk.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.io.Serializable
import java.util.*

class PesaViewPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm), Serializable {

    private val _fragmentCollection = ArrayList<Fragment>()

    fun add(fragment: Fragment) {
        this._fragmentCollection.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return _fragmentCollection[position]
    }

    override fun getCount(): Int {
        return _fragmentCollection.size
    }

}
