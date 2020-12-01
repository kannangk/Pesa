package finance.pesa.sdk.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("unscreened_messages")
public class ScreenSMS extends ParseObject implements Comparable<ScreenSMS> {

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public int unReadCount=0;

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public String uniqueNumber;

    public ScreenSMS() {
        super();
    }

    public String getConversationId() {
        return getString("conversation_id");
    }

    public String getFrom() {
        return getString("from");
    }

    public String getTo() {
        return getString("to");
    }

   public String getScreenTo() {
        return getString("screen_to");
    }

    public String getMessage() {
        return getString("message");
    }

    public String getStatus() {
        return getString("status");
    }

    public Boolean getIsDelivery() {
        return getBoolean("is_delivery");
    }

    public Boolean getIsRead() {
        return getBoolean("is_read");
    }

    public Boolean getIsEncrypted() {
        return getBoolean("is_encrypted");
    }

    public Date getCreatedDate() {
        return getCreatedAt();
    }

    public String getChatId() {
        return getString("chat_id");
    }


    @Override
    public int compareTo(ScreenSMS o) {
        return getCreatedDate().compareTo(o.getCreatedDate());
    }
}
