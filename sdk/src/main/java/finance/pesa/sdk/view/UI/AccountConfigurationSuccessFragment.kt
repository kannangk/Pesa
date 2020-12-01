package finance.pesa.sdk.view.UI

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R

class AccountConfigurationSuccessFragment : Fragment() {
    private var isLogin: Boolean = false

    companion object {
        fun newInstance(
            isLogin: Boolean
        ): AccountConfigurationSuccessFragment {
            val fragment = AccountConfigurationSuccessFragment()
            fragment.isLogin = isLogin
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_configuration_success, container, false)
        Handler().postDelayed({
            PesaApplication.getChildFragmentManager().beginTransaction().replace(
                R.id.shareg_main_container,
                DashboardFragment.newInstance(1)
            ).commitAllowingStateLoss()
            try {
                val manager = PesaApplication.getChildFragmentManager()
                if (manager!!.getFragments() != null && manager.getFragments().size > 0) {
                    for (i in 0 until manager.getFragments().size) {
                        val mFragment = manager.getFragments().get(i)
                        if (mFragment != null) {
                            manager.beginTransaction().remove(mFragment!!)
                                .commitAllowingStateLoss()
                            manager.popBackStackImmediate()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 300)
        return view
    }
}