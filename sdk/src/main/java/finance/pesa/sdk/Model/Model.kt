package finance.pesa.sdk.Model
import android.graphics.Bitmap
import android.net.Uri
import org.web3j.abi.datatypes.Bool
import java.io.Serializable
import java.util.*


data class TelephoneNumberDetail(val count:Int,val isTrailBucket:Boolean, val numbers:List<TelephoneNumberData>):Serializable
data class TelephoneNumberData(val isTrailBucket:Boolean,val country:String, val msisdn:String, val  cost:String, val  type:String, val  features:List<String>):Serializable
data class CountryDetails( val isActive: Boolean,  val createdOn: String,  val _id: String,  val iso: String,  val name: String,  val dialCode: String):Serializable
data class VerifyModel(val success:String,val message:String):Serializable
data class JWTToken(val data:String):Serializable
data class CardDetails(val cardStripeID:String,val cardLast4:String,val cardBrand:String,val cardFunding:String,val cardCountry:String,val cardType:String,val name:String,val year:Int,val month:Int,val cardExpiresIn:Int,val default:Boolean,val zipPass:Boolean):Serializable
data class SavedCardDetails(val isAdded:Boolean,val msg:String,val data:SavedDetails):Serializable
data class SavedDetails(val id:String,val address_zip:String,val address_zip_check:String,val brand:String,val cvc_check:String,val last4:String,val exp_month:Int,val exp_year:String):Serializable
//data class SubscriptionDatas(val id:String,val name:String,val credits:String,val bonusCredits:String,val description:String,val amount:String,val planId:String,val bonusPercentage:String):Serializable
data class AvailableDevice(val status:Boolean,val walletAddress:String):Serializable
data class DeviceStatus(/*val MINT_BUY_FROM_TOKEN:List<String>,val MINT_BUY_TO_TOKEN:List<String>,*/ val allCryptoCurrencies: List<CryptoCurrenciesValue>,val authroizedNumber:String,val EPN_ID:String,val status:String,val EPNNumber:String,val isEPNEnabled:Boolean,val walletAddress:String,val message:String,val JWT_TOKEN_SECRET:String,val SUPPORT_EMAIL:String,val MAXIMUM_NUMBERS:Int,val BUFFEFR_KEY:String,val CIPHER_ALGO:String,val CIPHER_SALT:String,val ENCRYPTION_KEY:String, val NETWORK_TARIFF:String, val deviceId:String?=null, val SEARCH_NUMBER_SCREEN_TEXT:String, val isTrial:Boolean, val userName:String, val activeNumbers:List<ActiveNumbers>, val token:String, val STRIPE_PK_KEY:String, val msg_in_android:String, val registeredNumber:String, val email:String, val deviceStatus:Int, val isRegistered:Boolean, val app_update_required_in_android:Boolean):Serializable
data class CryptoCurrenciesValue(val _id:String,val symbol:String,val fiatCurrency:Double)
data class TokenServerResponse(val carrier:String,val is_cellphone:Boolean,val message:String,val success:Boolean,val uuid:String)
data class PesaError(val errorMsg:String,val errorCode: Int)
data class BasicDetails(val isLinkedPackAvailable:Boolean,val unlimited:UnLimitedDetails,val referral:Referral?=null,val deviceId:String?=null,val creditsDetails:CreditsDetails,val popupDescription:LowCreditsPopupDetails,val bannerMessgae:BannerMessgae,val nexmoUserName:String,val nexmoUserId:String,val userName:String,val activeNumbers:List<ActiveNumbers>,val email:String,val credits:Double,val isEmailVerified:Boolean,val stripeCustomerId:String):Serializable
data class UnLimitedDetails(var isUnlimitedEnable:Boolean,var remainingMinutes:Double,var remainingMessages:Double,val supportedCountry:List<String>)
data class Referral(val code:String,val message:String,val credits:Int,val description:String)
data class CreditsDetails(val OUTGOING_CALL_CREDITS:Double,val INCOMING_CALL_CREDITS:Double,val MESSAGE_CALL_CREDITS:Double)
data class LowCreditsPopupDetails(val currentStage:Int,val message:String,val title:String,val action: String)
data class BannerMessgae(val isShowBanner:Boolean,val message:String,val buttonText:String,val internalNavigation:Boolean,val action:String)
data class SubscribedDetails(val isCurrentPlan:Boolean,val status: Boolean,val iccid:String,val message: String,val totalCredits:String,val _id:String,val isSubscriptionPack:Boolean,val created_at:String?,val activatedDate:String?,val name:String,val description:String,val productId:String,val amount:String,val credits:String,val planId:String,val validity:Int,val isLifetimePack :Boolean):Serializable
data class ActiveNumbers(val activationDate:String,val isUnlimited:Boolean,val availableCredits:Double,val availableFreeMinitues:Double,val availableFreeMessages:Double,val isTrialPack:Boolean,var isDefault:Boolean,val iso:String,val dialCode:String,val number:String):Serializable
data class PlanDetails(val activePlans:List<PlanData>,val upcomingPlans:List<PlanData>,val numberPlans:List<PlanData>)
data class PlanData(val isUnlimited:Boolean, val totalFreeMinitues:Int,val totalFreeMessages:Int,val freeMinitues:Int,val freeMessages:Int, val _id:String,val dialCode :String?,val iso :String?,val expiryDate:String,var packType:Int,val planName:String,val planId:String,val productId:String,val number:String,val amount:Double,val availableCredits:Double,val totalCredits:Double,val daysToExpire:Int,val validity:Int,val active:Boolean,val isCanceled:Boolean,val platform:String,val transactionIdentifier:String,val fromStripe:Boolean,val isSubscriptionPack:Boolean,val isDefault:Boolean):Serializable
data class CallDetails(val _id:String,val from:String,val to:String,val conversation_uuid:String,val status:String,val direction:String,val uuid:String,val createdOn:String,val start_time:String,val duration:Int):Serializable
data class ContactDetails(val contactName:String,val number:String,val contactId:String,val imgURI:Uri)
data class Conversation(val msg:String, var status:String, var isDelivery:Boolean, var isRead:Boolean, val date:Date, val chat_id:String, val conversation_id:String, val from:String, val to:String,var isDeleted:Boolean?,var deletedForEveryOne:Boolean?)
//@ParseClassName("messages") class ConversationData(val message:String, var status:String, var is_delivery:Boolean, var is_read:Boolean?=false, val created_At:Date, val chat_id:String, val conversation_id:String, val from:String, val to:String) :ParseObject()
data class MessageDetails(val conversation_id:String,val value:MessageData):Serializable
data class MessageData(val is_delivery:Boolean,val is_read:Boolean,val _created_at:String,val _updated_at:String,val conversation_id:String,val from:String,val to:String,val message:String,val chat_id:String,val message_id:String):Serializable
data class Markets(val id:String,val name:String,val symbol:String,val exchangeRate:Double,val borrowRate:Double,val supplyRate:Double,val cash:Double,val collateralFactor:String,val interestRateModelAddress:String,val numberOfSuppliers:Int,val numberOfBorrowers:Int,val reserves:String,val totalSupply:String,val totalBorrows:String,val reserveFactor:String,val borrowIndex:String,val underlyingAddress:String,val underlyingName:String,val underlyingPrice:String,val underlyingSymbol:String,val underlyingDecimals:Int,val underlyingPriceUSD:Double,val pesaSpeed:String,val accrualBlockNumber:String,val __typename:String,val DistributionBorrowApy:Double,val DistributionSupplyApy:Double):Serializable
data class SubMarkets(val exchangeRate:String,val symbol:String,val __typename:String):Serializable
data class Accounts(val id:String,val countLiquidated:Int,val countLiquidator:Int,val hasBorrowed:Boolean,val __typename:String,val tokens:List<Tokens>):Serializable
data class Tokens(val id:String,val enteredMarket:Boolean,val pTokenBalance:String,val storedBorrowBalance:String,val totalUnderlyingSupplied:Double,val totalUnderlyingRedeemed:String,val totalUnderlyingBorrowed:Double,val totalUnderlyingRepaid:String,val symbol:String,val market:TokenMarkets):Serializable
data class TokenMarkets(val exchangeRate:String,val __typename:String):Serializable
data class AccountSummary(val account: Accounts,val markets:List<SubMarkets>):Serializable
data class MarketData(val markets:InvestData,val isSupply:Boolean,val isBorrow:Boolean)
data class BasicMarkets(val markets:List<InvestData>)
data class WalletData(val markets:InvestData,val wallet:String?)
data class LocalContact(val name:String,val bitmap: Bitmap?)
data class InvestDash(val markets:List<InvestData>,val overAllSupplied:Double,val overAllBorrowed:Double,val borrowedLimit:Double,val borrowedInPercentage:Double,val netAPY:Double,val supplyBarValue:Double,val borrowBarValue:Double)
data class InvestData(val id:String,val balance:Double,val __typename:String,val name:String,val symbol:String,val exchangeRate:Double,val borrowRate:Double,val supplyRate:Double,val cash:Double,val collateralFactor:Double,val interestRateModelAddress:String,val numberOfSuppliers:Int,val numberOfBorrowers:Int,
                      val reserves:String,val totalSupply:Double,val totalBorrows:Double,val reserveFactor:String,val borrowIndex:String,val underlyingAddress:String,val underlyingName:String,val underlyingPrice:String,val underlyingSymbol:String,val underlyingDecimals:Int,val underlyingPriceUSD:Double,
                      val pesaSpeed:String,val accrualBlockNumber:String,val DistributionBorrowApy:Double,val DistributionSupplyApy:Double,val enteredMarket:Boolean,val pTokenBalance:Double,val totalUnderlyingSupplied:Double,val totalUnderlyingRedeemed:Double,val totalUnderlyingRepaid:Double,
                      val totalUnderlyingBorrowed:Double,val storedBorrowBalance:Double,val totalUnderlyingSuppliedinUSD:Double,val calculatedTotalUnderlyingBorrowed:Double,val totalstoredBorrowBalanceinUSD:Double,val lifetimeSupplyInterestAccrued:Double,val lifetimeBorrowInterestAccrued:Double,val isUnderlyingApproved:Boolean):Serializable
data class MarketValues(val markets:InvestData,val walletBalance: String,var isAllowance: Boolean):Serializable
data class PendingTransaction(val hash:String,val message:String,var status:String)
data class EPNAuthData(val status:Boolean,val data:List<String>)
data class MnemonicData(var isSelected:Boolean,val word:String,var updatedWord:String)
data class ResponseStatus(val status:Boolean,val message:String):Serializable
data class EPNCreateStatus(val isClaimEnable:Boolean,val status:Boolean,val message:String):Serializable
data class CredentialStatus(val deviceStatus:Int,val isRegistered:Boolean,val message:String,val EPNNumber:String,val EPN_ID:String,val isEPNEnabled:Boolean):Serializable
data class ActivitiesData(val send:List<ActivityDetails>,val received:List<ActivityDetails>,val all:List<ActivityDetails>,val request:List<ActivityDetails>,val escrow:List<ActivityDetails>):Serializable
data class ActivityDetails(val usdAmount:Double,val isExpired:Boolean,val id:String,val fromEPN:String,val toAddress:String,val status:String,val deviceId:String,val txHash:String,val from:String,val to:String,val cryptoSymbol:String,val createdOn:String,val txnFee:Double,val amount:Double,val isToken:Boolean,val isSendToEPN:Boolean,val isEscrow:Boolean,val notes:String,val type:String):Serializable
