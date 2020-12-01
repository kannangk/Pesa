package finance.pesa.sdk.utils

import android.Manifest
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Vibrator
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.hbb20.CountryCodePicker
import finance.pesa.sdk.Model.LocalContact
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.view.UI.ActivitiesFragment.Companion.activitiesFragment
import finance.pesa.sdk.view.UI.DashboardFragment
import finance.pesa.sdk.view.UI.DashboardFragment.Companion.dashboardFragment
import finance.pesa.sdk.view.UI.MessageHistory
import finance.pesa.sdk.view.UI.SendRequestFragment.Companion.sendRequestFragment
import finance.pesa.sdk.view.UI.SplashFragment
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.security.MessageDigest
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToInt

class UserInterface {
    companion object {
        var rateUsDialog: Dialog? = null
        private var progressDialog: Dialog? = null
        private val CONTACT_PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS)
        private var snack: Snackbar? = null

        //Show Transaction Progress bar
        fun showTransactionProgress(title: String, message: String, context: Context?) {
            if (context != null) {
                try {
                    if (!isShowing()) {
                        progressDialog = Dialog(context, R.style.CustomDialog)
                        progressDialog!!.setContentView(R.layout.transaction_progress)
                        val d = ColorDrawable(Color.TRANSPARENT)
                        progressDialog!!.window!!.setBackgroundDrawable(d)
                        progressDialog!!.setCancelable(false)
                        val msg = progressDialog!!.findViewById<TextView>(R.id.msg)
                        val tvTitle = progressDialog!!.findViewById<TextView>(R.id.title)
                        tvTitle.text = title
                        msg.text = message
                        progressDialog!!.show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        //Show Progress bar
        fun showProgress(message: String, context: Context?) {
            if (context != null) {
                try {
                    if (!isShowing()) {
                        progressDialog = Dialog(context, R.style.CustomDialog)
                        progressDialog!!.setContentView(R.layout.custom_progress_dialog)
                        val d = ColorDrawable(Color.TRANSPARENT)
                        progressDialog!!.window!!.setBackgroundDrawable(d)
                        progressDialog!!.setCancelable(false)
                        val msg = progressDialog!!.findViewById<TextView>(R.id.msg)
                        msg.text = message
                        progressDialog!!.show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        //Show Progress bar
        fun showCancellableProgress(message: String, context: Context?) {
            if (context != null) {
                try {
                    if (!isShowing()) {
                        progressDialog = Dialog(context, R.style.CustomDialog)
                        progressDialog!!.setContentView(R.layout.custom_progress_dialog)
                        val d = ColorDrawable(Color.TRANSPARENT)
                        progressDialog!!.window!!.setBackgroundDrawable(d)
                        progressDialog!!.setCancelable(true)
                        val msg = progressDialog!!.findViewById<TextView>(R.id.msg)
                        msg.text = message
                        progressDialog!!.show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        fun getCurrentDateTime(): String {
            try {
                val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm aa", Locale.getDefault())
                return sdf.format(Date())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }


        fun isShowing(): Boolean {
            return if (progressDialog != null)
                progressDialog!!.isShowing()
            else
                false
        }

        //Hide Progress bar
        fun hideProgress(context: Context?) {
            try {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing())
                        progressDialog!!.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun showSnack(
            text: String,
            indefenite: Boolean,
            view: View?,
            actionText: String,
            onClickListener: View.OnClickListener
        ) {
            try {
                if (view != null) {
                    snack = Snackbar.make(
                        view,
                        text,
                        if (indefenite) BaseTransientBottomBar.LENGTH_INDEFINITE else BaseTransientBottomBar.LENGTH_LONG
                    ).setAction(actionText, onClickListener)
                    snack!!.view.setBackgroundColor(Color.BLACK)

                    try {
                        val mActionView = snack!!.view.findViewById<View>(R.id.snackbar_text)
                        mActionView.setBackgroundColor(Color.BLACK)
                    } catch (e: Exception) {
                        // ignore
                    }

                    snack!!.setActionTextColor(Color.WHITE)
                    snack!!.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun showSnack(
            text: String,
            indefenite: Boolean,
            view: View?
        ) {
            try {
                if (view != null) {
                    snack = Snackbar.make(
                        view,
                        text,
                        if (indefenite) BaseTransientBottomBar.LENGTH_INDEFINITE else BaseTransientBottomBar.LENGTH_LONG
                    )
                    snack!!.view.setBackgroundColor(Color.BLACK)

                    try {
                        val mActionView = snack!!.view.findViewById<View>(R.id.snackbar_text)
                        mActionView.setBackgroundColor(Color.BLACK)
                    } catch (e: Exception) {
                        // ignore
                    }

                    snack!!.setActionTextColor(Color.WHITE)
                    snack!!.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun dismissSnack() {
            if (snack != null)
                snack!!.dismiss()
        }


        fun showToast(context: Context?, message: String) {
            if (context != null) {
                val inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val layout = inflater.inflate(R.layout.custom_toast, null, false)
                val image = layout.findViewById(R.id.image) as ImageView
                image.setImageResource(R.drawable.ic_tick_white)
                val text = layout.findViewById(R.id.msg) as TextView
                text.text = message
                val toast = Toast(context)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                toast.duration = Toast.LENGTH_SHORT
                toast.view = layout
                toast.show()
            }
        }

        fun showOKAlert(
            c: Context?,
            message: String,
            cancelable: Boolean,
            listener: DialogInterface.OnClickListener
        ) {
            // TODO Auto-generated constructor stub
            if (c != null) {
                val b = AlertDialog.Builder(c)
                b.setTitle(c.resources.getString(R.string.info))
                b.setMessage(message)
                b.setPositiveButton(c.resources.getString(R.string.ok), listener)
                b.setCancelable(cancelable)
                val d = b.create()
                d.show()
            }
        }

        fun getFlagMasterResID(code: String): Int {
            when (code.toLowerCase()) {
                //this should be sorted based on country name code.
                "ad" //andorra
                -> return R.drawable.flag_andorra
                "ae" //united arab emirates
                -> return R.drawable.flag_uae
                "af" //afghanistan
                -> return R.drawable.flag_afghanistan
                "ag" //antigua & barbuda
                -> return R.drawable.flag_antigua_and_barbuda
                "ai" //anguilla // Caribbean Islands
                -> return R.drawable.flag_anguilla
                "al" //albania
                -> return R.drawable.flag_albania
                "am" //armenia
                -> return R.drawable.flag_armenia
                "ao" //angola
                -> return R.drawable.flag_angola
                "aq" //antarctica // custom
                -> return R.drawable.flag_antarctica
                "ar" //argentina
                -> return R.drawable.flag_argentina
                "as" //American Samoa
                -> return R.drawable.flag_american_samoa
                "at" //austria
                -> return R.drawable.flag_austria
                "au" //australia
                -> return R.drawable.flag_australia
                "aw" //aruba
                -> return R.drawable.flag_aruba
                "ax" //alan islands
                -> return R.drawable.flag_aland
                "az" //azerbaijan
                -> return R.drawable.flag_azerbaijan
                "ba" //bosnia and herzegovina
                -> return R.drawable.flag_bosnia
                "bb" //barbados
                -> return R.drawable.flag_barbados
                "bd" //bangladesh
                -> return R.drawable.flag_bangladesh
                "be" //belgium
                -> return R.drawable.flag_belgium
                "bf" //burkina faso
                -> return R.drawable.flag_burkina_faso
                "bg" //bulgaria
                -> return R.drawable.flag_bulgaria
                "bh" //bahrain
                -> return R.drawable.flag_bahrain
                "bi" //burundi
                -> return R.drawable.flag_burundi
                "bj" //benin
                -> return R.drawable.flag_benin
                "bl" //saint barthélemy
                -> return R.drawable.flag_saint_barthelemy// custom
                "bm" //bermuda
                -> return R.drawable.flag_bermuda
                "bn" //brunei darussalam // custom
                -> return R.drawable.flag_brunei
                "bo" //bolivia, plurinational state of
                -> return R.drawable.flag_bolivia
                "bq" //bonaire, Sint Eustatius and Saba
                -> return R.drawable.flag_bonaire
                "br" //brazil
                -> return R.drawable.flag_brazil
                "bs" //bahamas
                -> return R.drawable.flag_bahamas
                "bt" //bhutan
                -> return R.drawable.flag_bhutan
                "bw" //botswana
                -> return R.drawable.flag_botswana
                "by" //belarus
                -> return R.drawable.flag_belarus
                "bz" //belize
                -> return R.drawable.flag_belize
                "ca" //canada
                -> return R.drawable.flag_canada
                "cc" //cocos (keeling) islands
                -> return R.drawable.flag_cocos// custom
                "cd" //congo, the democratic republic of the
                -> return R.drawable.flag_democratic_republic_of_the_congo
                "cf" //central african republic
                -> return R.drawable.flag_central_african_republic
                "cg" //congo
                -> return R.drawable.flag_republic_of_the_congo
                "ch" //switzerland
                -> return R.drawable.flag_switzerland
                "ci" //côte d\'ivoire
                -> return R.drawable.flag_cote_divoire
                "ck" //cook islands
                -> return R.drawable.flag_cook_islands
                "cl" //chile
                -> return R.drawable.flag_chile
                "cm" //cameroon
                -> return R.drawable.flag_cameroon
                "cn" //china
                -> return R.drawable.flag_china
                "co" //colombia
                -> return R.drawable.flag_colombia
                "cr" //costa rica
                -> return R.drawable.flag_costa_rica
                "cu" //cuba
                -> return R.drawable.flag_cuba
                "cv" //cape verde
                -> return R.drawable.flag_cape_verde
                "cx" //christmas island
                -> return R.drawable.flag_christmas_island
                "cy" //cyprus
                -> return R.drawable.flag_cyprus
                "cz" //czech republic
                -> return R.drawable.flag_czech_republic
                "de" //germany
                -> return R.drawable.flag_germany
                "dj" //djibouti
                -> return R.drawable.flag_djibouti
                "dk" //denmark
                -> return R.drawable.flag_denmark
                "dm" //dominica
                -> return R.drawable.flag_dominica
                "do" //dominican republic
                -> return R.drawable.flag_dominican_republic
                "dz" //algeria
                -> return R.drawable.flag_algeria
                "ec" //ecuador
                -> return R.drawable.flag_ecuador
                "ee" //estonia
                -> return R.drawable.flag_estonia
                "eg" //egypt
                -> return R.drawable.flag_egypt
                "er" //eritrea
                -> return R.drawable.flag_eritrea
                "es" //spain
                -> return R.drawable.flag_spain
                "et" //ethiopia
                -> return R.drawable.flag_ethiopia
                "fi" //finland
                -> return R.drawable.flag_finland
                "fj" //fiji
                -> return R.drawable.flag_fiji
                "fk" //falkland islands (malvinas)
                -> return R.drawable.flag_falkland_islands
                "fm" //micronesia, federated states of
                -> return R.drawable.flag_micronesia
                "fo" //faroe islands
                -> return R.drawable.flag_faroe_islands
                "fr" //france
                -> return R.drawable.flag_france
                "ga" //gabon
                -> return R.drawable.flag_gabon
                "gb" //united kingdom
                -> return R.drawable.flag_united_kingdom
                "gd" //grenada
                -> return R.drawable.flag_grenada
                "ge" //georgia
                -> return R.drawable.flag_georgia
                "gf" //guyane
                -> return R.drawable.flag_guyane
                "gg" //Guernsey
                -> return R.drawable.flag_guernsey
                "gh" //ghana
                -> return R.drawable.flag_ghana
                "gi" //gibraltar
                -> return R.drawable.flag_gibraltar
                "gl" //greenland
                -> return R.drawable.flag_greenland
                "gm" //gambia
                -> return R.drawable.flag_gambia
                "gn" //guinea
                -> return R.drawable.flag_guinea
                "gp" //guadeloupe
                -> return R.drawable.flag_guadeloupe
                "gq" //equatorial guinea
                -> return R.drawable.flag_equatorial_guinea
                "gr" //greece
                -> return R.drawable.flag_greece
                "gt" //guatemala
                -> return R.drawable.flag_guatemala
                "gu" //Guam
                -> return R.drawable.flag_guam
                "gw" //guinea-bissau
                -> return R.drawable.flag_guinea_bissau
                "gy" //guyana
                -> return R.drawable.flag_guyana
                "hk" //hong kong
                -> return R.drawable.flag_hong_kong
                "hn" //honduras
                -> return R.drawable.flag_honduras
                "hr" //croatia
                -> return R.drawable.flag_croatia
                "ht" //haiti
                -> return R.drawable.flag_haiti
                "hu" //hungary
                -> return R.drawable.flag_hungary
                "id" //indonesia
                -> return R.drawable.flag_indonesia
                "ie" //ireland
                -> return R.drawable.flag_ireland
                "il" //israel
                -> return R.drawable.flag_israel
                "im" //isle of man
                -> return R.drawable.flag_isleof_man // custom
                "is" //Iceland
                -> return R.drawable.flag_iceland
                "in" //india
                -> return R.drawable.flag_india
                "io" //British indian ocean territory
                -> return R.drawable.flag_british_indian_ocean_territory
                "iq" //iraq
                -> return R.drawable.flag_iraq_new
                "ir" //iran, islamic republic of
                -> return R.drawable.flag_iran
                "it" //italy
                -> return R.drawable.flag_italy
                "je" //Jersey
                -> return R.drawable.flag_jersey
                "jm" //jamaica
                -> return R.drawable.flag_jamaica
                "jo" //jordan
                -> return R.drawable.flag_jordan
                "jp" //japan
                -> return R.drawable.flag_japan
                "ke" //kenya
                -> return R.drawable.flag_kenya
                "kg" //kyrgyzstan
                -> return R.drawable.flag_kyrgyzstan
                "kh" //cambodia
                -> return R.drawable.flag_cambodia
                "ki" //kiribati
                -> return R.drawable.flag_kiribati
                "km" //comoros
                -> return R.drawable.flag_comoros
                "kn" //st kitts & nevis
                -> return R.drawable.flag_saint_kitts_and_nevis
                "kp" //north korea
                -> return R.drawable.flag_north_korea
                "kr" //south korea
                -> return R.drawable.flag_south_korea
                "kw" //kuwait
                -> return R.drawable.flag_kuwait
                "ky" //Cayman_Islands
                -> return R.drawable.flag_cayman_islands
                "kz" //kazakhstan
                -> return R.drawable.flag_kazakhstan
                "la" //lao people\'s democratic republic
                -> return R.drawable.flag_laos
                "lb" //lebanon
                -> return R.drawable.flag_lebanon
                "lc" //st lucia
                -> return R.drawable.flag_saint_lucia
                "li" //liechtenstein
                -> return R.drawable.flag_liechtenstein
                "lk" //sri lanka
                -> return R.drawable.flag_sri_lanka
                "lr" //liberia
                -> return R.drawable.flag_liberia
                "ls" //lesotho
                -> return R.drawable.flag_lesotho
                "lt" //lithuania
                -> return R.drawable.flag_lithuania
                "lu" //luxembourg
                -> return R.drawable.flag_luxembourg
                "lv" //latvia
                -> return R.drawable.flag_latvia
                "ly" //libya
                -> return R.drawable.flag_libya
                "ma" //morocco
                -> return R.drawable.flag_morocco
                "mc" //monaco
                -> return R.drawable.flag_monaco
                "md" //moldova, republic of
                -> return R.drawable.flag_moldova
                "me" //montenegro
                -> return R.drawable.flag_of_montenegro// custom
                "mf" -> return R.drawable.flag_saint_martin
                "mg" //madagascar
                -> return R.drawable.flag_madagascar
                "mh" //marshall islands
                -> return R.drawable.flag_marshall_islands
                "mk" //macedonia, the former yugoslav republic of
                -> return R.drawable.flag_macedonia
                "ml" //mali
                -> return R.drawable.flag_mali
                "mm" //myanmar
                -> return R.drawable.flag_myanmar
                "mn" //mongolia
                -> return R.drawable.flag_mongolia
                "mo" //macao
                -> return R.drawable.flag_macao
                "mp" // Northern mariana islands
                -> return R.drawable.flag_northern_mariana_islands
                "mq" //martinique
                -> return R.drawable.flag_martinique
                "mr" //mauritania
                -> return R.drawable.flag_mauritania
                "ms" //montserrat
                -> return R.drawable.flag_montserrat
                "mt" //malta
                -> return R.drawable.flag_malta
                "mu" //mauritius
                -> return R.drawable.flag_mauritius
                "mv" //maldives
                -> return R.drawable.flag_maldives
                "mw" //malawi
                -> return R.drawable.flag_malawi
                "mx" //mexico
                -> return R.drawable.flag_mexico
                "my" //malaysia
                -> return R.drawable.flag_malaysia
                "mz" //mozambique
                -> return R.drawable.flag_mozambique
                "na" //namibia
                -> return R.drawable.flag_namibia
                "nc" //new caledonia
                -> return R.drawable.flag_new_caledonia// custom
                "ne" //niger
                -> return R.drawable.flag_niger
                "nf" //Norfolk
                -> return R.drawable.flag_norfolk_island
                "ng" //nigeria
                -> return R.drawable.flag_nigeria
                "ni" //nicaragua
                -> return R.drawable.flag_nicaragua
                "nl" //netherlands
                -> return R.drawable.flag_netherlands
                "no" //norway
                -> return R.drawable.flag_norway
                "np" //nepal
                -> return R.drawable.flag_nepal
                "nr" //nauru
                -> return R.drawable.flag_nauru
                "nu" //niue
                -> return R.drawable.flag_niue
                "nz" //new zealand
                -> return R.drawable.flag_new_zealand
                "om" //oman
                -> return R.drawable.flag_oman
                "pa" //panama
                -> return R.drawable.flag_panama
                "pe" //peru
                -> return R.drawable.flag_peru
                "pf" //french polynesia
                -> return R.drawable.flag_french_polynesia
                "pg" //papua new guinea
                -> return R.drawable.flag_papua_new_guinea
                "ph" //philippines
                -> return R.drawable.flag_philippines
                "pk" //pakistan
                -> return R.drawable.flag_pakistan
                "pl" //poland
                -> return R.drawable.flag_poland
                "pm" //saint pierre and miquelon
                -> return R.drawable.flag_saint_pierre
                "pn" //pitcairn
                -> return R.drawable.flag_pitcairn_islands
                "pr" //puerto rico
                -> return R.drawable.flag_puerto_rico
                "ps" //palestine
                -> return R.drawable.flag_palestine
                "pt" //portugal
                -> return R.drawable.flag_portugal
                "pw" //palau
                -> return R.drawable.flag_palau
                "py" //paraguay
                -> return R.drawable.flag_paraguay
                "qa" //qatar
                -> return R.drawable.flag_qatar
                "re" //la reunion
                -> return R.drawable.flag_martinique // no exact flag found
                "ro" //romania
                -> return R.drawable.flag_romania
                "rs" //serbia
                -> return R.drawable.flag_serbia // custom
                "ru" //russian federation
                -> return R.drawable.flag_russian_federation
                "rw" //rwanda
                -> return R.drawable.flag_rwanda
                "sa" //saudi arabia
                -> return R.drawable.flag_saudi_arabia
                "sb" //solomon islands
                -> return R.drawable.flag_soloman_islands
                "sc" //seychelles
                -> return R.drawable.flag_seychelles
                "sd" //sudan
                -> return R.drawable.flag_sudan
                "se" //sweden
                -> return R.drawable.flag_sweden
                "sg" //singapore
                -> return R.drawable.flag_singapore
                "sh" //saint helena, ascension and tristan da cunha
                -> return R.drawable.flag_saint_helena // custom
                "si" //slovenia
                -> return R.drawable.flag_slovenia
                "sk" //slovakia
                -> return R.drawable.flag_slovakia
                "sl" //sierra leone
                -> return R.drawable.flag_sierra_leone
                "sm" //san marino
                -> return R.drawable.flag_san_marino
                "sn" //senegal
                -> return R.drawable.flag_senegal
                "so" //somalia
                -> return R.drawable.flag_somalia
                "sr" //suriname
                -> return R.drawable.flag_suriname
                "st" //sao tome and principe
                -> return R.drawable.flag_sao_tome_and_principe
                "sv" //el salvador
                -> return R.drawable.flag_el_salvador
                "sx" //sint maarten
                -> return R.drawable.flag_sint_maarten
                "sy" //syrian arab republic
                -> return R.drawable.flag_syria
                "sz" //swaziland
                -> return R.drawable.flag_swaziland
                "tc" //turks & caicos islands
                -> return R.drawable.flag_turks_and_caicos_islands
                "td" //chad
                -> return R.drawable.flag_chad
                "tg" //togo
                -> return R.drawable.flag_togo
                "th" //thailand
                -> return R.drawable.flag_thailand
                "tj" //tajikistan
                -> return R.drawable.flag_tajikistan
                "tk" //tokelau
                -> return R.drawable.flag_tokelau // custom
                "tl" //timor-leste
                -> return R.drawable.flag_timor_leste
                "tm" //turkmenistan
                -> return R.drawable.flag_turkmenistan
                "tn" //tunisia
                -> return R.drawable.flag_tunisia
                "to" //tonga
                -> return R.drawable.flag_tonga
                "tr" //turkey
                -> return R.drawable.flag_turkey
                "tt" //trinidad & tobago
                -> return R.drawable.flag_trinidad_and_tobago
                "tv" //tuvalu
                -> return R.drawable.flag_tuvalu
                "tw" //taiwan, province of china
                -> return R.drawable.flag_taiwan
                "tz" //tanzania, united republic of
                -> return R.drawable.flag_tanzania
                "ua" //ukraine
                -> return R.drawable.flag_ukraine
                "ug" //uganda
                -> return R.drawable.flag_uganda
                "us" //united states
                -> return R.drawable.flag_united_states_of_america
                "uy" //uruguay
                -> return R.drawable.flag_uruguay
                "uz" //uzbekistan
                -> return R.drawable.flag_uzbekistan
                "va" //holy see (vatican city state)
                -> return R.drawable.flag_vatican_city
                "vc" //st vincent & the grenadines
                -> return R.drawable.flag_saint_vicent_and_the_grenadines
                "ve" //venezuela, bolivarian republic of
                -> return R.drawable.flag_venezuela
                "vg" //british virgin islands
                -> return R.drawable.flag_british_virgin_islands
                "vi" //us virgin islands
                -> return R.drawable.flag_us_virgin_islands
                "vn" //vietnam
                -> return R.drawable.flag_vietnam
                "vu" //vanuatu
                -> return R.drawable.flag_vanuatu
                "wf" //wallis and futuna
                -> return R.drawable.flag_wallis_and_futuna
                "ws" //samoa
                -> return R.drawable.flag_samoa
                "xk" //kosovo
                -> return R.drawable.flag_kosovo
                "ye" //yemen
                -> return R.drawable.flag_yemen
                "yt" //mayotte
                -> return R.drawable.flag_martinique // no exact flag found
                "za" //south africa
                -> return R.drawable.flag_south_africa
                "zm" //zambia
                -> return R.drawable.flag_zambia
                "zw" //zimbabwe
                -> return R.drawable.flag_zimbabwe
                else -> return R.drawable.flag_transparent
            }
        }


        fun showOKTitleAlert(
            c: Context?,
            title: String,
            message: String,
            cancelable: Boolean,
            listener: DialogInterface.OnClickListener
        ) {
            // TODO Auto-generated constructor stub
            if (c != null) {
                val b = AlertDialog.Builder(c)
                b.setTitle(title)
                b.setMessage(message)
                b.setPositiveButton("OK", listener)
                b.setCancelable(cancelable)
                val d = b.create()
                d.show()
            }
        }

        fun showLowCreditsAlert(
            c: Context?,
            title: String,
            message: String,
            cancelable: Boolean,
            positiveButton: String,
            negativeButton: String,
            positiveListener: DialogInterface.OnClickListener
        ) {
            // TODO Auto-generated constructor stub
            if (c != null) {
                var d: AlertDialog? = null
                val b = AlertDialog.Builder(c)
                b.setTitle(title)
                b.setMessage(message)
                b.setPositiveButton(positiveButton, positiveListener)
                b.setNegativeButton(negativeButton) { dialogInterface, which ->
                    // d.dismiss()
                }
                b.setCancelable(cancelable)
                d = b.create()
                d.show()

            }
        }


        fun showOkCancelAlert(
            c: Context?,
            title: String,
            message: String,
            okListener: DialogInterface.OnClickListener,
            okListenerNegative: DialogInterface.OnClickListener
        ) {
            // TODO Auto-generated constructor stub
            if (c != null) {
                val b = AlertDialog.Builder(c)
                b.setTitle(title)
                b.setMessage(message)
                b.setPositiveButton("OK", okListener)
                b.setNegativeButton("Cancel", okListenerNegative)
                val d = b.create()
                d.show()
                val titleView = d.findViewById<TextView>(
                    c.resources.getIdentifier(
                        "alertTitle",
                        "id",
                        "android"
                    )
                )
                if (titleView != null) {
                    titleView.gravity = Gravity.CENTER_HORIZONTAL
                }
            }
        }

        fun hideKeyboard(context: Context, view: View) {
            val `in` = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            `in`.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

        // Check Email Id is Valid or Not
        fun isValidEmail(target: CharSequence): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }

        fun showLongToast(context: Context?, message: String) {
            if (context != null) {
                val inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val layout = inflater.inflate(R.layout.custom_toast, null, false)
                val image = layout.findViewById<View>(R.id.image) as ImageView
                image.setImageResource(R.drawable.ic_tick_white)
                val text = layout.findViewById<View>(R.id.msg) as TextView
                text.text = message
                val toast = Toast(context)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                toast.duration = Toast.LENGTH_LONG
                toast.view = layout
                toast.show()
            }
        }


        fun goToMyApp(context: Context) {//true if Google Play, false if Amazone Store
            if (context != null) {
                ////////////////////////////////
                val uri = Uri.parse("market://details?id=" + context.packageName)
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                goToMarket.addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
                try {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    context.startActivity(goToMarket)
                }


            }
        }


        fun parseDateWithTimeYear(time: String): String {
            val incomingFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outGoingFormat = SimpleDateFormat("dd MMM yyyy, hh:mm aa")
            try {
                val date = incomingFormat.parse(time)
                return outGoingFormat.format(date!!)
            } catch (pe: ParseException) {
                pe.printStackTrace()
            }

            return ""
        }

        fun parseDateWithYear(time: String): String {
            val incomingFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outGoingFormat = SimpleDateFormat("MMM dd, yyyy")
            try {
                val date = incomingFormat.parse(time)
                return outGoingFormat.format(date!!)
            } catch (pe: ParseException) {
                pe.printStackTrace()
            }

            return ""
        }


        fun getParseTime(date: Date): String {
            val outGoingFormat = SimpleDateFormat("dd MMM yyyy")
            try {
                return outGoingFormat.format(date)
            } catch (pe: Exception) {
                pe.printStackTrace()
            }

            return ""
        }

        fun formateddate(dateString: String): String {
            try {
                val incomingFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val date = incomingFormat.parse(dateString)
                return when {
                    isToday(date) -> parseDate(date)
                    isYesterday(date) -> "Yesterday"
                    else -> parseDate(
                        date
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun isToday(date: Date): Boolean {
            return DateUtils.isToday(date.time)
        }

        fun isYesterday(d: Date): Boolean {
            return DateUtils.isToday(d.time + DateUtils.DAY_IN_MILLIS)
        }

        fun parseDate(date: Date): String {
            val outGoingFormat = SimpleDateFormat("MMM dd")
            try {
                return outGoingFormat.format(date)
            } catch (pe: Exception) {
                pe.printStackTrace()
            }

            return ""
        }

        fun getChatTime(date: Date): String {
            val outGoingFormat = SimpleDateFormat("hh:mm aa")
            try {
                return outGoingFormat.format(date)
            } catch (pe: Exception) {
                pe.printStackTrace()
            }

            return ""
        }

        fun getChatDate(date: Date): String {
            val outGoingFormat = SimpleDateFormat("dd MMM yyyy")
            try {
                return outGoingFormat.format(date)
            } catch (pe: Exception) {
                pe.printStackTrace()
            }

            return ""
        }

        fun getTime(date: Date): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            try {
                val c = Calendar.getInstance()
                c.time = date!!
                c.timeZone = TimeZone.getDefault()
                return convertToHumanReadable(c.timeInMillis, date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }

        fun getTime(started: String): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            try {
                val date = simpleDateFormat.parse(started)
                val c = Calendar.getInstance()
                c.time = date!!
                c.timeZone = TimeZone.getDefault()
                return convertToHumanReadable(c.timeInMillis, date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }

        private fun convertToHumanReadable(milliseconds: Long, date: Date?): String {
            val today = Calendar.getInstance()
            val postedDay = Calendar.getInstance()
            postedDay.timeInMillis = milliseconds
            val day = 86400000L
            val hour = 3600000L
            val minute = 60000L
            val month = 2628000000L
            val year = 31536000000L
            val time: Int

            return when {
                isToday(date!!) -> {
                    when {
                        today.timeInMillis - postedDay.timeInMillis > hour -> {
                            time = ((today.timeInMillis - postedDay.timeInMillis) / hour).toFloat()
                                .roundToInt()
                            time.toString() + if (time == 1) "h" else "h"
                        }
                        else -> {
                            time =
                                ((today.timeInMillis - postedDay.timeInMillis) / minute).toFloat()
                                    .roundToInt()
                            if (time == 0) "Now" else time.toString() + "m"
                        }
                    }
                }
                isYesterday(date) -> "1 Day"
                else -> parseDate(
                    date
                )
            }

        }

        private fun convertToHumanReadable(milliseconds: Long): String {
            val today = Calendar.getInstance()
            val postedDay = Calendar.getInstance()
            postedDay.timeInMillis = milliseconds
            val day = 86400000L
            val hour = 3600000L
            val minute = 60000L
            val month = 2628000000L
            val year = 31536000000L
            val time: Int

            if (today.timeInMillis - postedDay.timeInMillis > year) {
                time = Math.round(((today.timeInMillis - postedDay.timeInMillis) / year).toFloat())
                return time.toString() + if (time == 1) "yr" else "yr"
            } else if (today.timeInMillis - postedDay.timeInMillis > month) {
                time = Math.round(((today.timeInMillis - postedDay.timeInMillis) / month).toFloat())
                return time.toString() + if (time == 1) "mo" else "mo"
            } else if (today.timeInMillis - postedDay.timeInMillis > day) {
                time = Math.round(((today.timeInMillis - postedDay.timeInMillis) / day).toFloat())
                return time.toString() + if (time == 1) "Day" else "Day"
            } else if (today.timeInMillis - postedDay.timeInMillis > hour) {
                time = Math.round(((today.timeInMillis - postedDay.timeInMillis) / hour).toFloat())
                return time.toString() + if (time == 1) "h" else "h"
            } else {
                time =
                    Math.round(((today.timeInMillis - postedDay.timeInMillis) / minute).toFloat())
                return time.toString() + if (time == 1) " m" else " m"
            }
        }

        private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            permission
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return false
                    }
                }
            }
            return true
        }


        private fun isContactVerify(contactNumber: String, number: String): Boolean {
            if ((contactNumber.trim().replace("+", "").replace("-", "").replace(
                    " ",
                    ""
                )) == (number.trim().replace("+", "").replace("-", "").replace(" ", ""))
            )
                return true
            return false
        }

        private fun getDefaultProfile(context: Context): Drawable {
            return context.resources.getDrawable(R.drawable.ic_avatar, null)
        }


        fun formatNumberCompat(rawPhone: String?, countryIso: String = ""): String {
            if (rawPhone == null) return ""

            var countryName = countryIso
            if (countryName.isBlank()) {
                countryName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Resources.getSystem().configuration.locales[0].country
                } else {
                    Resources.getSystem().configuration.locale.country
                }
            }

            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                PhoneNumberUtils.formatNumber(rawPhone)
            } else {
                PhoneNumberUtils.formatNumber(rawPhone, countryName)
            }
        }


        fun errorMessage(message: String, errorBody: ResponseBody?): String {
            try {
                val errorResponse =
                    JSONObject(errorBody!!.string())
                val errorMsg = errorResponse.getString("message")
                return if (errorMsg.contains("No address associated with hostname"))
                    "No internet connection. please try again."
                else
                    errorMsg
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return message
        }

        fun errorMessage(errorMsg: String): String {
            try {
                return if (errorMsg.contains("No address associated with hostname"))
                    "No internet connection. please try again."
                else
                    errorMsg
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return errorMsg
        }

        fun formatPhone(Phone: String): String {
            var formattedNumber = Phone
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    formattedNumber = PhoneNumberUtils.formatNumber(Phone, "US")
                    return formattedNumber?.replace("-", " ") ?: Phone
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return Phone
        }

        fun getCountryCode(phNo: String, context: Context): String {
            var countryCode = "+1"
            try {
                val ccp_loadFullNumber = CountryCodePicker(context)
                ccp_loadFullNumber!!.fullNumber = phNo
                return ccp_loadFullNumber?.selectedCountryCode
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return countryCode
        }


        fun isNullOrEmpty(string: String?): Boolean {
            return string == null || string.isEmpty()
        }


        fun showInvalidNumberAlert(
            c: Context?,
            title: String,
            message: String
        ) {
            try {
                if (c != null) {
                    var d: AlertDialog? = null
                    val b = AlertDialog.Builder(c)
                    b.setTitle(title)
                    b.setMessage(message)
                    b.setPositiveButton("OK", null)
                    b.setCancelable(true)
                    d = b.create()
                    d.show()

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun toHexStrings(array: ByteArray): String {
            return org.apache.commons.codec.binary.Hex.encodeHex(array).toString()
            //return DatatypeConverter.printHexBinary(array);
        }

        fun toByteArray(s: String): ByteArray {
            return org.apache.commons.codec.binary.Hex.decodeHex(s.toCharArray())
            // return DatatypeConverter.parseHexBinary(s);
        }


        fun toHexString(array: ByteArray): String {
            return android.util.Base64.encodeToString(array, 16)
            //return DatatypeConverter.printHexBinary(array);
        }

        @Throws(Exception::class)
        fun encrypt(content: String, context: Context): String {
            val input = content.toByteArray(charset("utf-8"))
            val iv = IvParameterSpec(Constants.getBufferKey(context).toByteArray(charset("utf-8")))
            val md = MessageDigest.getInstance("SHA-256")
            val seed = Constants.getEncryptedKey(context)
            val keyb = seed.toByteArray(charset("utf-8"))
            val thedigest = md.digest(keyb)
            val skc = SecretKeySpec(thedigest, Constants.getCipherAlgo(context))
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, skc, iv)

            val cipherText = ByteArray(cipher.getOutputSize(input.size))
            var ctLength = cipher.update(input, 0, input.size, cipherText, 0)
            ctLength += cipher.doFinal(cipherText, ctLength)
            return toHexString(cipherText)
        }

        fun round(value: Double, places: Int): Double {
            try {
                require(places >= 0)

                var bd: BigDecimal? = BigDecimal(value)
                bd = bd!!.setScale(places, RoundingMode.HALF_UP)
                return bd?.toDouble() ?: 0.00
            } catch (e: Exception) {
                e.printStackTrace()
                return 0.00
            }

        }

        fun UnitDivide(number: BigDecimal, factor: Int): BigDecimal {
            val weiFactor = BigDecimal.TEN.pow(factor)
            return number.divide(weiFactor)
        }

        fun UnitDivide(number: BigInteger, factor: Int): BigInteger {
            val weiFactor = BigInteger.TEN.pow(factor)
            return number.divide(weiFactor)
        }

        fun UnitMultiply(number: BigInteger, factor: Int): BigInteger {
            val weiFactor = BigInteger.TEN.pow(factor)
            return number.multiply(weiFactor)
        }

        fun UnitMultiply(number: BigDecimal, factor: Int): BigDecimal {
            val weiFactor = BigDecimal.TEN.pow(factor)
            return number.multiply(weiFactor)
        }

        fun UnitMultiply(number: Long, factor: Int): Long {
            val weiFactor = BigInteger.TEN.pow(factor)
            return number * weiFactor.toLong()
        }

        fun round(d: Double): String {
            return String.format("%.4f", d)
        }

        fun roundWallet(d: Double): String {
            return String.format("%.8f", d)
        }

        fun roundTwo(d: Double): String {
            return String.format("%.2f", d)
        }

        fun roundEight(d: Double): String {
            return String.format("%.8f", d)
        }

        fun getCoinIcon(symbol: String): Int {
            when (symbol.toLowerCase()) {
                "link"
                -> return R.drawable.ic_link
                "usdc"
                -> return R.drawable.ic_usdc
                "usdt"
                -> return R.drawable.ic_usdt
                "wbtc"
                -> return R.drawable.ic_wbtc
                "zrx"
                -> return R.drawable.ic_zrx
                "spesa"
                -> return R.drawable.ic_s_pesa_round_logo
                "eth"
                -> return R.drawable.ic_ethereum
                "bat"
                -> return R.drawable.ic_bat
                else
                -> return R.drawable.ic_s_pesa_round_logo
            }
        }

        fun getFormatedNumber(count: Double): String {
            if (count < 1000) return String.format("%.4f", count)
            val exp = (ln(count) / ln(1000.0)).toInt()
            return String.format("%.2f %c", count / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])
        }

        //change statusBar color
        fun changeStatusBar(activity: Activity, color: Int) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val startColor = activity.window.statusBarColor
                    val endColor = ContextCompat.getColor(activity, color)
                    ObjectAnimator.ofArgb(activity.window, "statusBarColor", startColor, endColor)
                        .start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //change navigation bar color
        fun changeNavigationBar(activity: Activity, color: Int) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val startColor = activity.window.navigationBarColor
                    val endColor = ContextCompat.getColor(activity, color)
                    ObjectAnimator.ofArgb(
                        activity.window,
                        "navigationBarColor",
                        startColor,
                        endColor
                    )
                        .start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun readContactNumbers(context: Context) {
            if (hasPermissions(context, *CONTACT_PERMISSIONS)) {
                var contactReadService = ContactReadService()
                contactReadService.readAllContact(context!!)
            }
        }

        fun retrieveContactPhoto(context: Context, number: String): Bitmap? {
            if (!hasPermissions(context, *CONTACT_PERMISSIONS)) {
                return null
            }
            if (Constants.getAllLocalContact()!!.containsKey(number)) {
                return if (Constants.getAllLocalContact()!![number] != null)
                    Constants.getAllLocalContact()!![number]!!.bitmap
                else
                    null
            }
            val contentResolver = context.contentResolver
            var contactId: String? = null
            val uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number)
            )

            val projection =
                arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID)

            val cursor = contentResolver.query(
                uri,
                projection, null, null, null
            )

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    contactId =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID))
                }
                cursor.close()
            }

            var photo = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.ic_avatar
            )

            try {
                val contactUri = ContentUris.withAppendedId(
                    ContactsContract.Contacts.CONTENT_URI,
                    java.lang.Long.valueOf(contactId!!)
                )
                val input = ContactsContract.Contacts.openContactPhotoInputStream(
                    context.contentResolver,
                    contactUri
                )
                photo = BitmapFactory.decodeStream(input)


                /* Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactId));
            Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);

            AssetFileDescriptor fd =
                    context.getContentResolver().openAssetFileDescriptor(displayPhotoUri, "r");
            InputStream inputStream = fd.createInputStream();
*/

                /*  if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
            }

            assert inputStream != null;
            inputStream.close();*/
                //  input.close();

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return photo
        }

        fun retrieveContactName(context: Context, number: String): String? {
            if (!hasPermissions(context, *CONTACT_PERMISSIONS)) {
                return null
            }

            if (Constants.getAllLocalContact()!!.containsKey(number)) {
                return if (Constants.getAllLocalContact()!![number] != null)
                    Constants.getAllLocalContact()!![number]!!.name
                else
                    null
            }
            val contentResolver = context.contentResolver
            var contactName: String? = null
            val uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number)
            )

            val projection =
                arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID)

            val cursor = contentResolver.query(
                uri,
                projection, null, null, null
            )

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    contactName = cursor.getString(
                        cursor.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME
                        )
                    )
                }
                cursor.close()
            }


            return contactName
        }

        fun retrieveContactPhoto(
            context: Context,
            number: String,
            iv_user: ImageView,
            username: TextView
        ) {
            try {
                if (number == null) {
                    iv_user.setImageDrawable(getDefaultProfile(context))
                    username.text = "Unknown"
                    return
                }
                if (!hasPermissions(context, *CONTACT_PERMISSIONS)) {
                    iv_user.setImageDrawable(getDefaultProfile(context))
                    username.text = "Unknown"
                    return
                }

                if (Constants.getAllLocalContact()!!.containsKey(number)) {
                    if (Constants.getAllLocalContact()!![number] == null) {
                        iv_user.setImageDrawable(getDefaultProfile(context))
                        username.text = "Unknown"
                        return
                    }
                    username.text = Constants.getAllLocalContact()!![number]!!.name
                    if (Constants.getAllLocalContact()!![number]!!.bitmap != null)
                        iv_user.setImageBitmap(Constants.getAllLocalContact()!![number]!!.bitmap)
                    else
                        iv_user.setImageDrawable(getDefaultProfile(context))
                    return
                }
                var contactId: String? = null
                var contactName: String? = null


                /*if (Constants.getAllContact().isNotEmpty()) {
                    for (contactDetail in Constants.getAllContact()) {
                        if (isContactVerify(contactDetail.number, number)) {
                            contactId = contactDetail.contactId
                            contactName = contactDetail.contactName
                        }
                    }
                }else{*/
                val contentResolver = context.contentResolver
                val uri = Uri.withAppendedPath(
                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(number)
                )

                val projection = arrayOf(
                    ContactsContract.PhoneLookup.DISPLAY_NAME,
                    ContactsContract.PhoneLookup._ID
                )

                val cursor = contentResolver.query(
                    uri,
                    projection, null, null, null
                )

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        contactId =
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID))
                        contactName = cursor.getString(
                            cursor.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME
                            )
                        )
                    }
                    cursor.close()
                }
                //}

                var photo: Bitmap? = BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_avatar
                )

                if (contactId == null || contactName == null) {
                    iv_user.setImageDrawable(getDefaultProfile(context))
                    username.text = "Unknown"
                    Constants.setLocalContact(number, null)
                    return
                }


                try {
                    val contactUri = ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI,
                        java.lang.Long.valueOf(contactId!!)
                    )

                    /* Glide.with(context)
                         .load(contactUri)
                         .listener(object : RequestListener<Drawable> {
                             override fun onLoadFailed(
                                 e: GlideException?,
                                 model: Any?,
                                 target: Target<Drawable>?,
                                 isFirstResource: Boolean
                             ): Boolean {
                                 iv_user.setImageDrawable(getDefaultProfile(context))
                                 return false
                             }

                             override fun onResourceReady(
                                 resource: Drawable?,
                                 model: Any?,
                                 target: Target<Drawable>?,
                                 dataSource: DataSource?,
                                 isFirstResource: Boolean
                             ): Boolean {
                                 Log.d("Glide", "OnResourceReady")
                                 //do something when picture already loaded
                                 return false
                             }
                         })
                         .into(iv_user)*/
                    val input = ContactsContract.Contacts.openContactPhotoInputStream(
                        context.contentResolver,
                        contactUri
                    )
                    photo = BitmapFactory.decodeStream(input)
                    Constants.setLocalContact(number, LocalContact(contactName, photo))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (contactName != null) {
                    username.text = contactName
                } else {
                    username.text = "Unknown"
                }
                if (photo != null) {
                    iv_user.setImageBitmap(photo)
                } else {
                    iv_user.setImageDrawable(getDefaultProfile(context))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun retrieveContactPhotoWithName(
            context: Context,
            number: String,
            iv_user: ImageView,
            username: TextView
        ) {
            try {
                if (number == null) {
                    iv_user.setImageDrawable(getDefaultProfile(context))
                    username.text = "Unknown"
                    return
                }
                if (!hasPermissions(context, *CONTACT_PERMISSIONS)) {
                    iv_user.setImageDrawable(getDefaultProfile(context))
                    username.text = formatPhone(number)
                    return
                }

                if (Constants.getAllLocalContact()!!.containsKey(number)) {
                    if (Constants.getAllLocalContact()!![number] == null) {
                        iv_user.setImageDrawable(getDefaultProfile(context))
                        username.text = formatPhone(number)
                        return
                    }
                    username.text = Constants.getAllLocalContact()!![number]!!.name
                    if (Constants.getAllLocalContact()!![number]!!.bitmap != null)
                        iv_user.setImageBitmap(Constants.getAllLocalContact()!![number]!!.bitmap)
                    else
                        iv_user.setImageDrawable(getDefaultProfile(context))
                    return
                }
                var contactId: String? = null
                var contactName: String? = null


                /* if (Constants.getAllContact().isNotEmpty()) {
                     for (contactDetail in Constants.getAllContact()) {
                         if (isContactVerify(contactDetail.number, number)) {
                             contactId = contactDetail.contactId
                             contactName = contactDetail.contactName
                         }
                     }
                 } else {*/
                val contentResolver = context.contentResolver
                val uri = Uri.withAppendedPath(
                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(number)
                )

                val projection = arrayOf(
                    ContactsContract.PhoneLookup.DISPLAY_NAME,
                    ContactsContract.PhoneLookup._ID
                )

                val cursor = contentResolver.query(
                    uri,
                    projection, null, null, null
                )

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        contactId =
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID))
                        contactName = cursor.getString(
                            cursor.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME
                            )
                        )
                    }
                    cursor.close()
                }
                // }
                if (contactId == null || contactName == null) {
                    iv_user.setImageDrawable(getDefaultProfile(context))
                    username.text = formatPhone(number)
                    Constants.setLocalContact(number, null)
                    return
                }

                var photo: Bitmap? = BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_avatar
                )

                try {
                    val contactUri = ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI,
                        java.lang.Long.valueOf(contactId!!)
                    )

                    /* Glide.with(context)
                         .load(contactUri)
                         .listener(object : RequestListener<Drawable> {
                             override fun onLoadFailed(
                                 e: GlideException?,
                                 model: Any?,
                                 target: Target<Drawable>?,
                                 isFirstResource: Boolean
                             ): Boolean {
                                 iv_user.setImageDrawable(getDefaultProfile(context))
                                 return false
                             }

                             override fun onResourceReady(
                                 resource: Drawable?,
                                 model: Any?,
                                 target: Target<Drawable>?,
                                 dataSource: DataSource?,
                                 isFirstResource: Boolean
                             ): Boolean {
                                 Log.d("Glide", "OnResourceReady")
                                 //do something when picture already loaded
                                 return false
                             }
                         })
                         .into(iv_user)*/
                    val input = ContactsContract.Contacts.openContactPhotoInputStream(
                        context.contentResolver,
                        contactUri
                    )
                    photo = BitmapFactory.decodeStream(input)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (contactName != null) {
                    username.text = contactName
                } else {
                    username.text = formatPhone(number)
                }
                if (photo != null) {
                    iv_user.setImageBitmap(photo)
                } else {
                    iv_user.setImageDrawable(getDefaultProfile(context))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun cyberDecrypt(encrypted: String, context: Context): String {


            try {
                val cryptLib = finance.pesa.sdk.utils.CryptLib()
                return cryptLib.decryptCipherText(
                    encrypted,
                    Constants.getEncryptedKey(context),
                    Constants.getBufferKey(context)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }

        fun cyberEncrypt(value: String, context: Context): String {
            try {
                val cryptLib = finance.pesa.sdk.utils.CryptLib()
                return cryptLib.encryptPlainText(
                    value,
                    Constants.getEncryptedKey(context),
                    Constants.getBufferKey(context)
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return ""
        }

        fun onRefreshMessageHistory() {
            try {
                if (MessageHistory.messageHistory != null)
                    MessageHistory.messageHistory!!.chatHistoryData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getAllowedMsg(callee: String?, context: Context?): Boolean? {
            try {
                var toCaller = callee
                if (!callee!!.startsWith("+"))
                    toCaller = "+$callee"
                val phoneUtil = PhoneNumberUtil.getInstance()
                val swissNumberProto = phoneUtil.parse(toCaller, "US")
                val isValid = phoneUtil.isValidNumber(swissNumberProto)
                return if (isValid) {
                    return true
                } else {
                    showInvalidNumberAlert(context, "Alert", "Invalid number")
                    null
                }
            } catch (e: NumberParseException) {
                System.err.println("NumberParseException was thrown: " + e.toString())
            }
            showInvalidNumberAlert(context, "Alert", "Invalid number")
            return null
        }

        fun loadWalletDatas(context: Context, isForce: Boolean) {
            try {
                var basicDetailsService = FetchDataBackgroungService()
                basicDetailsService.loadAllMarkets(1, context!!)
                // basicDetailsService.getRefreshWallet(context!!, isForce)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun unAuthorized(context: Context, errorCode: Int) {
            try {
                val pref =
                    PreferenceManager.getDefaultSharedPreferences(context)
                val ed = pref.edit()
                if (errorCode == 402 || errorCode == 403) {
                    ed.putString(Constants.PARSE_AUTH_PHONE, null)
                }
                ed.apply()
                PesaApplication.getChildFragmentManager().beginTransaction()
                    .replace(R.id.shareg_main_container, SplashFragment())
                    .commitAllowingStateLoss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun randomString(stringLength: Int): String {
            val list = "1234567890".toCharArray()
            var randomS = ""
            for (i in 1..stringLength) {
                randomS += list[getRandomNumber(0, list.size - 1)]
            }
            return randomS
        }

        fun getRandomNumber(min: Int, max: Int): Int {
            return Random().nextInt((max - min) + 1) + min
        }

        fun errorShow(view: View?, context: Context?) {
            try {
                view?.startAnimation(shakeError())
                val vibe: Vibrator =
                    context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibe.vibrate(100)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun shakeError(): TranslateAnimation {
            val shake = TranslateAnimation(0f, 10f, 0f, 0f)
            shake.duration = 500
            shake.interpolator = CycleInterpolator(7f)
            return shake
        }

        fun getAllowedNumber(
            callee: String?
        ): Boolean? {
            try {
                var toCaller = callee
                if (!callee!!.startsWith("+"))
                    toCaller = "+$callee"
                val phoneUtil = PhoneNumberUtil.getInstance()
                val swissNumberProto = phoneUtil.parse(toCaller, "US")
                val isValid = phoneUtil.isValidNumber(swissNumberProto)
                if (isValid) {
                    return true
                }
                return false
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        fun showTransactionFailedDialog(msg: String, context: Context) {
            try {
                val failedDialog = Dialog(context!!, R.style.CustomDialog)
                val inflater =
                    context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val dialogLayout = inflater.inflate(R.layout.transaction_failed, null, false)
                val errorMessage = dialogLayout.findViewById<TextView>(R.id.error_message)
                val statusIcon = dialogLayout.findViewById<ImageView>(R.id.status_icon)
                val statusBg = dialogLayout.findViewById<LinearLayout>(R.id.status_bg)
                val done = dialogLayout.findViewById<Button>(R.id.done)
                failedDialog.setContentView(dialogLayout)
                failedDialog.setCancelable(false)
                val d = ColorDrawable(Color.TRANSPARENT)
                failedDialog.window!!.setBackgroundDrawable(d)
                val drawable =
                    context.getDrawable(R.drawable.animated_cross) as AnimatedVectorDrawable
                statusIcon.setImageDrawable(drawable)
                drawable.start()
                errorMessage.text = msg
                statusBg.background = context.getDrawable(R.drawable.circle_red_bg)

                done.setOnClickListener { v -> failedDialog.dismiss() }
                failedDialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun activitiesRefresh() {
            try {
                if (activitiesFragment != null) {
                    activitiesFragment!!.isViewGenerated = false
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun showWalletAddress(tvText: TextView, qrCode: String) {
            try {
                var walAddress = qrCode
                try {
                    val startValue = qrCode.substring(0, 8)
                    val endValue = qrCode.substring(qrCode.length - 6, qrCode.length)
                    walAddress = startValue + "..." + endValue
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                tvText?.text = walAddress
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun redirectToEscrow() {
            try {
                if (DashboardFragment.dashboardFragment != null) {
                    DashboardFragment.dashboardFragment!!.viewPager!!.currentItem = 2
                    if (activitiesFragment != null) {
                        Handler().postDelayed({
                            try {
                                activitiesFragment!!.viewpager!!.currentItem = 4
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }, 200)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun redirectToActivity() {
            try {
                if (DashboardFragment.dashboardFragment != null) {
                    DashboardFragment.dashboardFragment!!.viewPager!!.currentItem = 2
                    if (activitiesFragment != null) {
                        Handler().postDelayed({
                            try {
                                activitiesFragment!!.viewpager!!.currentItem = 0
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }, 200)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun refreshActivities() {
            try {
                if (dashboardFragment != null) {
                    if (dashboardFragment!!.viewPager!!.currentItem == 2)
                        if (activitiesFragment != null)
                            activitiesFragment!!.loadAllActivities(1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun updateNotification(isPending:Boolean) {
            try {
                if (sendRequestFragment != null) {
                    sendRequestFragment!!.updateNotificationIcon(isPending)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}