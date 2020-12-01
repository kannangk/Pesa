package finance.pesa.sdk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import finance.pesa.sdk.view.UI.SplashFragment

class Pesa : Fragment() {

    private var keyWord = ""
    companion object {
        fun newInstance(keyWord: String): Pesa {
            val fagment = Pesa()
            fagment.keyWord = keyWord
            return fagment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         PesaApplication.setChildFragmentManager(
            childFragmentManager
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
            .add(R.id.shareg_main_container, SplashFragment.newInstance(keyWord)).commitAllowingStateLoss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pesa, container, false)
    }
}