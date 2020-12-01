package finance.pesa.sdk.view.UI

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.view.Interface.ETHBalanceUpdatedListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddFundsEPNFragment : Fragment() {

    private var ivBack: ImageView? = null
    private var walletAddress: TextView? = null
    private var copyAddress: TextView? = null
    private var moreDetails: TextView? = null
    private var btnShare: Button? = null
    private var shareQr: ImageView? = null

    companion object {
        var addFundsEPNFragment: AddFundsEPNFragment? = AddFundsEPNFragment()
        fun newInstance(): AddFundsEPNFragment {
            return addFundsEPNFragment!!
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_funds_epn, container, false)
        ivBack = view.findViewById(R.id.iv_back)
        walletAddress = view.findViewById(R.id.wallet_address)
        copyAddress = view.findViewById(R.id.copy_address)
        moreDetails = view.findViewById(R.id.more_details)
        btnShare = view.findViewById(R.id.btn_share)
        shareQr = view.findViewById(R.id.qr_image)
        showWalletAddress(Constants.getAccountAddress())
        UserInterface.changeStatusBar(activity!!,R.color.white_black)
        UserInterface.changeNavigationBar(activity!!,R.color.white_black)
        setTextViewHTML(
            moreDetails!!,
            "Please deposit at least $25 to activate your number to send & receive money. <a href=learn_more>Learn more.</a>"
        )
        Constants.setETHBalanceUpdatedListener(ethBalanceUpdatedListener)
        ivBack?.setOnClickListener { activity!!.onBackPressed() }
        copyAddress?.setOnClickListener {
            val cm = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.text = Constants.getAccountAddress()
            UserInterface.showToast(context, "Copied your wallet address")
        }

        btnShare?.setOnClickListener {
            try {
                val bitmap = getBitmapFromView(shareQr!!)
                shareImage(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return view
    }

    private fun setTextViewHTML(text: TextView, html: String) {
        val sequence = Html.fromHtml(html)
        val strBuilder = SpannableStringBuilder(sequence)
        val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(strBuilder, span)
        }
        text.text = strBuilder
        text.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable = object : ClickableSpan() {
            override fun onClick(view: View) {
                try {
                    if (span.url == "learn_more") {

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }


    override fun onDestroy() {
        super.onDestroy()
        Constants.setETHBalanceUpdatedListener(null)
        UserInterface.changeStatusBar(activity!!, R.color.app_green)
        UserInterface.changeNavigationBar(activity!!, R.color.app_green)
    }

    private val ethBalanceUpdatedListener=object : ETHBalanceUpdatedListener{
        override fun onEnableEPNActivateBalance() {
            Constants.setETHBalanceUpdatedListener(null)
            activity!!.onBackPressed()
        }

    }

    private fun showWalletAddress(qrCode: String) {
        try {
            val bitmap = encodeAsBitmap(qrCode)
            shareQr?.setImageBitmap(bitmap)
            var walAddress=qrCode
            try {
                val startValue = qrCode.substring(0, 8)
                val endValue = qrCode.substring(qrCode.length-6, qrCode.length)
                walAddress=startValue+"..."+endValue
            }catch (e:Exception){
                e.printStackTrace()
            }
            walletAddress?.text=walAddress
        } catch (e: WriterException) {
            e.printStackTrace()
        }

    }

    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    private fun shareImage(bitmap: Bitmap) {
        // save bitmap to cache directory
        try {
            val cachePath = File(activity!!.cacheDir, "images")
            cachePath.mkdirs() // don't forget to make the directory
            val stream =
                FileOutputStream(cachePath.absolutePath + "/shared_images.png") // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        val imagePath = File(activity!!.cacheDir, "images")
        val newFile = File(imagePath, "shared_images.png")
        val contentUri =
            FileProvider.getUriForFile(activity!!, activity!!.packageName + ".provider", newFile)

        if (contentUri != null) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, activity!!.contentResolver.getType(contentUri))
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.getAccountAddress())
            shareIntent.type = "image/png"
            startActivity(Intent.createChooser(shareIntent, "Choose an app"))
        }
    }


    @Throws(WriterException::class)
    private fun encodeAsBitmap(str: String): Bitmap? {
        val result: BitMatrix
        try {
            result = MultiFormatWriter().encode(
                str,
                BarcodeFormat.QR_CODE, 500, 500, null
            )
        } catch (iae: IllegalArgumentException) {
            // Unsupported format
            return null
        }

        val w = result.width
        val h = result.height
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            val offset = y * w
            for (x in 0 until w) {
                pixels[offset + x] = if (result.get(x, y)) BLACK else WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h)
        return bitmap
    }
}