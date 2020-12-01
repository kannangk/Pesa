package finance.pesa.sdk.utils

import android.app.IntentService
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.provider.ContactsContract
import finance.pesa.sdk.Model.ContactDetails
import org.json.JSONArray
import org.json.JSONObject
import com.parse.ParseObject
import com.parse.ParseQuery


class ContactReadService : IntentService("ContactReadService") {
    var isStarted = false
    internal var mContext: Context? = null

    override fun onHandleIntent(intent: Intent?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        isStarted = false
    }

    override fun onCreate() {
        super.onCreate()
        isStarted = true
    }

    fun readAllContact(context: Context) {
        this.mContext = context
        AsyncContactImportTask().execute()
    }

    private fun getAllContacts(): List<ContactDetails> {
        val contactsJsonArray = JSONArray()
        val phoneList: ArrayList<ContactDetails> = arrayListOf()
        val cr = mContext!!.contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if ((if (cur != null) cur!!.count else 0) > 0) {
            while (cur != null && cur!!.moveToNext()) {
                try {
                    val id = cur!!.getString(
                        cur!!.getColumnIndex(ContactsContract.Contacts._ID)
                    )
                    val name = cur!!.getString(
                        cur!!.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME
                        )
                    )
                    val contactUri = ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI,
                        java.lang.Long.valueOf(id!!)
                    )

                    if (cur!!.getInt(cur!!.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        val pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf<String>(id), null
                        )
                        while (pCur!!.moveToNext()) {
                            val phoneNo = pCur!!.getString(
                                pCur!!.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                            if (phoneNo != null) {
                                val contactObj = JSONObject()
                                contactObj.put("name", name)
                                contactObj.put("phoneNumber", phoneNo!!.replace(" ", "").replace("+", ""))
                                contactsJsonArray.put(contactObj)
                                phoneList.add(
                                    ContactDetails(
                                        name,
                                        phoneNo!!.replace(" ", ""),
                                        id,
                                        contactUri
                                    )
                                )
                            }
                        }
                        pCur!!.close()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (cur != null) {
            cur!!.close()
        }
        updateContacts(contactsJsonArray)
        return phoneList
    }

    //Update Contacts to Backend
    private fun updateContacts(datas: JSONArray) {
        try {
            if (datas.length() == 0)
                return
            if (Constants.getDeviceIdFromLocal(mContext!!) != null) {
                val query = ParseQuery.getQuery<ParseObject>("user_contacts")
                query.whereEqualTo("deviceId",Constants.getDeviceIdFromLocal(mContext!!)!!)
                query.findInBackground { objects, e ->
                    if (e == null) {
                        if (objects.isEmpty()) {
                            val gameScore = ParseObject("user_contacts")
                            gameScore.put("deviceId", Constants.getDeviceIdFromLocal(mContext!!)!!)
                            gameScore.put("contactInfo", datas)
                            gameScore.saveInBackground()
                        } else {
                            objects[0].put(
                                "contactInfo",
                                Constants.getDeviceIdFromLocal(mContext!!)!!
                            )
                            objects[0].saveInBackground()
                        }
                    } else {
                        // something went wrong
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Import Contact
    private inner class AsyncContactImportTask :
        AsyncTask<Boolean, Boolean, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
            Constants.setAllContact(emptyList())
        }

        override fun doInBackground(vararg params: Boolean?): Boolean {
            try {
                Constants.setAllContact(getAllContacts())
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return false
        }

        override fun onPostExecute(isUpdated: Boolean) {
            super.onPostExecute(isUpdated)
            if (Constants.getContactUpdatedListeners()!!.size > 0) {
                for (contactUpdateListener in Constants.getContactUpdatedListeners()!!)
                    contactUpdateListener.onContactUpdated()
            }
        }
    }
}