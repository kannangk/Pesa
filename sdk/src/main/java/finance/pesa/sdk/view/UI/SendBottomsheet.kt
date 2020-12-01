package finance.pesa.sdk.view.UI

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import finance.pesa.sdk.R


class SendBottomsheet : BottomSheetDialogFragment() {
    private var closeBtn: Button? = null
    private var height = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, R.style.CoffeeDialog)
        super.onCreate(savedInstanceState)

    }

    companion object {
        fun newInstance(
            height: Int
        ): SendBottomsheet {
            val fragment = SendBottomsheet()
            fragment.height = height
            return fragment
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
            val dialogc = dialog as BottomSheetDialog
            val bottomSheet = dialogc.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
            bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return dialog
    }


    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams

        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight - (height)
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        return if (activity != null) {
            val displayMetrics = DisplayMetrics()
            if (activity!!.windowManager != null) {
                activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.heightPixels
            } else
                600
        } else
            600
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(context, R.layout.send, null)
        closeBtn = view.findViewById(R.id.btn_cancel)
        closeBtn!!.setOnClickListener {
            dismiss()
        }
        return view
    }

}