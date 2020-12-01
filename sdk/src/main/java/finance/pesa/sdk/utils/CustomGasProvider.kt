package finance.pesa.sdk.utils

import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger


class CustomGasProvider : StaticGasProvider(GAS_PRICE, GAS_LIMIT) {
    companion object {
        /*val GAS_LIMIT = BigInteger.valueOf(4500000)
        val GAS_PRICE = BigInteger.valueOf(15000000000L)*/
        val GAS_LIMIT = BigInteger.valueOf(450000)
        val GAS_PRICE = BigInteger.valueOf(15000000000L)
    }
}
