package finance.pesa.devapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import finance.pesa.sdk.Pesa
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.utils.UserInterface.Companion.dismissSnack


class MainActivity : AppCompatActivity() {

    var keyword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            if (intent.hasExtra("redirect"))
                keyword = intent.getStringExtra("redirect")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        initialize(intent)
        PesaApplication.currentActivity = this
    }


    private fun initialize(intent: Intent) {
        try {
            val fragment = Pesa.newInstance(keyword)
            supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        try {
            if (PesaApplication.getChildFragmentManager() != null) {
                val mFragmentManager = PesaApplication.getChildFragmentManager()
                if (mFragmentManager.backStackEntryCount > 0) {
                    dismissSnack()
                    mFragmentManager.popBackStackImmediate()
                } else
                    finish()
            } else
                finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
