package finance.pesa.sdk.generator;

import finance.pesa.sdk.PesaApplication;
import finance.pesa.sdk.R;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.16.
 */
@SuppressWarnings("rawtypes")
public class PaymentGateway extends Contract {
    public static final String BINARY = PesaApplication.Companion.getContext().getString(R.string.payment_gateway_abi);

    public static final String FUNC_CLAIMERC20TOKEN = "claimErc20Token";

    public static final String FUNC_CLAIMETHAMOUNT = "claimEthAmount";

    public static final String FUNC_EPNTOKEN = "epnToken";

    public static final String FUNC_ESCROWERC20PAYMENTS = "escrowErc20Payments";

    public static final String FUNC_ESCROWETHPAYMENTS = "escrowEthPayments";

    public static final String FUNC_REFUNDERC20TOKEN = "refundErc20Token";

    public static final String FUNC_REFUNDETHAMOUNT = "refundEthAmount";

    public static final String FUNC_SENDERC20TOKEN = "sendErc20Token";

    public static final String FUNC_SENDERC20TOKENTOWALLET = "sendErc20TokenToWallet";

    public static final String FUNC_SENDETHAMOUNT = "sendEthAmount";

    public static final String FUNC_SENDETHAMOUNTTOWALLET = "sendEthAmountToWallet";

    public static final Event CLAIMERC20TOKENTRANSFERED_EVENT = new Event("ClaimErc20TokenTransfered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event CLAIMETHTRANSFERED_EVENT = new Event("ClaimEthTransfered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ERC20TOKENADDEDTOESCROW_EVENT = new Event("Erc20TokenAddedToEscrow", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ERC20TOKENTRANSFERED_EVENT = new Event("Erc20TokenTransfered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ERC20TOKENTRANSFEREDTOWALLET_EVENT = new Event("Erc20TokenTransferedToWallet", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ETHADDEDTOESCROW_EVENT = new Event("EthAddedToEscrow", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ETHTRANSFERED_EVENT = new Event("EthTransfered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ETHTRANSFEREDTOWALLET_EVENT = new Event("EthTransferedToWallet", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event REFUNDERC20TOKENTRANSFERED_EVENT = new Event("RefundErc20TokenTransfered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event REFUNDETHTRANSFERED_EVENT = new Event("RefundEthTransfered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected PaymentGateway(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PaymentGateway(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PaymentGateway(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PaymentGateway(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ClaimErc20TokenTransferedEventResponse> getClaimErc20TokenTransferedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(CLAIMERC20TOKENTRANSFERED_EVENT, transactionReceipt);
        ArrayList<ClaimErc20TokenTransferedEventResponse> responses = new ArrayList<ClaimErc20TokenTransferedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ClaimErc20TokenTransferedEventResponse typedResponse = new ClaimErc20TokenTransferedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.itemId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.tokenAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ClaimErc20TokenTransferedEventResponse> claimErc20TokenTransferedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ClaimErc20TokenTransferedEventResponse>() {
            @Override
            public ClaimErc20TokenTransferedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(CLAIMERC20TOKENTRANSFERED_EVENT, log);
                ClaimErc20TokenTransferedEventResponse typedResponse = new ClaimErc20TokenTransferedEventResponse();
                typedResponse.log = log;
                typedResponse.itemId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.tokenAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ClaimErc20TokenTransferedEventResponse> claimErc20TokenTransferedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CLAIMERC20TOKENTRANSFERED_EVENT));
        return claimErc20TokenTransferedEventFlowable(filter);
    }

    public List<ClaimEthTransferedEventResponse> getClaimEthTransferedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(CLAIMETHTRANSFERED_EVENT, transactionReceipt);
        ArrayList<ClaimEthTransferedEventResponse> responses = new ArrayList<ClaimEthTransferedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ClaimEthTransferedEventResponse typedResponse = new ClaimEthTransferedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.itemId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ClaimEthTransferedEventResponse> claimEthTransferedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ClaimEthTransferedEventResponse>() {
            @Override
            public ClaimEthTransferedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(CLAIMETHTRANSFERED_EVENT, log);
                ClaimEthTransferedEventResponse typedResponse = new ClaimEthTransferedEventResponse();
                typedResponse.log = log;
                typedResponse.itemId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ClaimEthTransferedEventResponse> claimEthTransferedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CLAIMETHTRANSFERED_EVENT));
        return claimEthTransferedEventFlowable(filter);
    }

    public List<Erc20TokenAddedToEscrowEventResponse> getErc20TokenAddedToEscrowEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ERC20TOKENADDEDTOESCROW_EVENT, transactionReceipt);
        ArrayList<Erc20TokenAddedToEscrowEventResponse> responses = new ArrayList<Erc20TokenAddedToEscrowEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            Erc20TokenAddedToEscrowEventResponse typedResponse = new Erc20TokenAddedToEscrowEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.itemId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.tokenAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.expiresOn = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<Erc20TokenAddedToEscrowEventResponse> erc20TokenAddedToEscrowEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, Erc20TokenAddedToEscrowEventResponse>() {
            @Override
            public Erc20TokenAddedToEscrowEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ERC20TOKENADDEDTOESCROW_EVENT, log);
                Erc20TokenAddedToEscrowEventResponse typedResponse = new Erc20TokenAddedToEscrowEventResponse();
                typedResponse.log = log;
                typedResponse.itemId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.tokenAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.expiresOn = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<Erc20TokenAddedToEscrowEventResponse> erc20TokenAddedToEscrowEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ERC20TOKENADDEDTOESCROW_EVENT));
        return erc20TokenAddedToEscrowEventFlowable(filter);
    }

    public List<Erc20TokenTransferedEventResponse> getErc20TokenTransferedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ERC20TOKENTRANSFERED_EVENT, transactionReceipt);
        ArrayList<Erc20TokenTransferedEventResponse> responses = new ArrayList<Erc20TokenTransferedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            Erc20TokenTransferedEventResponse typedResponse = new Erc20TokenTransferedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tokenAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<Erc20TokenTransferedEventResponse> erc20TokenTransferedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, Erc20TokenTransferedEventResponse>() {
            @Override
            public Erc20TokenTransferedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ERC20TOKENTRANSFERED_EVENT, log);
                Erc20TokenTransferedEventResponse typedResponse = new Erc20TokenTransferedEventResponse();
                typedResponse.log = log;
                typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.tokenAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<Erc20TokenTransferedEventResponse> erc20TokenTransferedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ERC20TOKENTRANSFERED_EVENT));
        return erc20TokenTransferedEventFlowable(filter);
    }

    public List<Erc20TokenTransferedToWalletEventResponse> getErc20TokenTransferedToWalletEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ERC20TOKENTRANSFEREDTOWALLET_EVENT, transactionReceipt);
        ArrayList<Erc20TokenTransferedToWalletEventResponse> responses = new ArrayList<Erc20TokenTransferedToWalletEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            Erc20TokenTransferedToWalletEventResponse typedResponse = new Erc20TokenTransferedToWalletEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.destinationAddress = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tokenAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<Erc20TokenTransferedToWalletEventResponse> erc20TokenTransferedToWalletEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, Erc20TokenTransferedToWalletEventResponse>() {
            @Override
            public Erc20TokenTransferedToWalletEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ERC20TOKENTRANSFEREDTOWALLET_EVENT, log);
                Erc20TokenTransferedToWalletEventResponse typedResponse = new Erc20TokenTransferedToWalletEventResponse();
                typedResponse.log = log;
                typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.destinationAddress = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.tokenAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<Erc20TokenTransferedToWalletEventResponse> erc20TokenTransferedToWalletEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ERC20TOKENTRANSFEREDTOWALLET_EVENT));
        return erc20TokenTransferedToWalletEventFlowable(filter);
    }

    public List<EthAddedToEscrowEventResponse> getEthAddedToEscrowEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ETHADDEDTOESCROW_EVENT, transactionReceipt);
        ArrayList<EthAddedToEscrowEventResponse> responses = new ArrayList<EthAddedToEscrowEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            EthAddedToEscrowEventResponse typedResponse = new EthAddedToEscrowEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.itemId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.expiresOn = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<EthAddedToEscrowEventResponse> ethAddedToEscrowEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, EthAddedToEscrowEventResponse>() {
            @Override
            public EthAddedToEscrowEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ETHADDEDTOESCROW_EVENT, log);
                EthAddedToEscrowEventResponse typedResponse = new EthAddedToEscrowEventResponse();
                typedResponse.log = log;
                typedResponse.itemId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.expiresOn = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<EthAddedToEscrowEventResponse> ethAddedToEscrowEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ETHADDEDTOESCROW_EVENT));
        return ethAddedToEscrowEventFlowable(filter);
    }

    public List<EthTransferedEventResponse> getEthTransferedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ETHTRANSFERED_EVENT, transactionReceipt);
        ArrayList<EthTransferedEventResponse> responses = new ArrayList<EthTransferedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            EthTransferedEventResponse typedResponse = new EthTransferedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<EthTransferedEventResponse> ethTransferedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, EthTransferedEventResponse>() {
            @Override
            public EthTransferedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ETHTRANSFERED_EVENT, log);
                EthTransferedEventResponse typedResponse = new EthTransferedEventResponse();
                typedResponse.log = log;
                typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<EthTransferedEventResponse> ethTransferedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ETHTRANSFERED_EVENT));
        return ethTransferedEventFlowable(filter);
    }

    public List<EthTransferedToWalletEventResponse> getEthTransferedToWalletEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ETHTRANSFEREDTOWALLET_EVENT, transactionReceipt);
        ArrayList<EthTransferedToWalletEventResponse> responses = new ArrayList<EthTransferedToWalletEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            EthTransferedToWalletEventResponse typedResponse = new EthTransferedToWalletEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.destinationAddress = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<EthTransferedToWalletEventResponse> ethTransferedToWalletEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, EthTransferedToWalletEventResponse>() {
            @Override
            public EthTransferedToWalletEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ETHTRANSFEREDTOWALLET_EVENT, log);
                EthTransferedToWalletEventResponse typedResponse = new EthTransferedToWalletEventResponse();
                typedResponse.log = log;
                typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.destinationAddress = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<EthTransferedToWalletEventResponse> ethTransferedToWalletEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ETHTRANSFEREDTOWALLET_EVENT));
        return ethTransferedToWalletEventFlowable(filter);
    }

    public List<RefundErc20TokenTransferedEventResponse> getRefundErc20TokenTransferedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(REFUNDERC20TOKENTRANSFERED_EVENT, transactionReceipt);
        ArrayList<RefundErc20TokenTransferedEventResponse> responses = new ArrayList<RefundErc20TokenTransferedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RefundErc20TokenTransferedEventResponse typedResponse = new RefundErc20TokenTransferedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.itemId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.tokenAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RefundErc20TokenTransferedEventResponse> refundErc20TokenTransferedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RefundErc20TokenTransferedEventResponse>() {
            @Override
            public RefundErc20TokenTransferedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(REFUNDERC20TOKENTRANSFERED_EVENT, log);
                RefundErc20TokenTransferedEventResponse typedResponse = new RefundErc20TokenTransferedEventResponse();
                typedResponse.log = log;
                typedResponse.itemId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.tokenAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RefundErc20TokenTransferedEventResponse> refundErc20TokenTransferedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REFUNDERC20TOKENTRANSFERED_EVENT));
        return refundErc20TokenTransferedEventFlowable(filter);
    }

    public List<RefundEthTransferedEventResponse> getRefundEthTransferedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(REFUNDETHTRANSFERED_EVENT, transactionReceipt);
        ArrayList<RefundEthTransferedEventResponse> responses = new ArrayList<RefundEthTransferedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RefundEthTransferedEventResponse typedResponse = new RefundEthTransferedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.itemId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RefundEthTransferedEventResponse> refundEthTransferedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RefundEthTransferedEventResponse>() {
            @Override
            public RefundEthTransferedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(REFUNDETHTRANSFERED_EVENT, log);
                RefundEthTransferedEventResponse typedResponse = new RefundEthTransferedEventResponse();
                typedResponse.log = log;
                typedResponse.itemId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.senderId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.destinationId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RefundEthTransferedEventResponse> refundEthTransferedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REFUNDETHTRANSFERED_EVENT));
        return refundEthTransferedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> claimErc20Token(BigInteger senderId, BigInteger destinationId, String token) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CLAIMERC20TOKEN, 
                Arrays.<Type>asList(new Uint256(senderId),
                new Uint256(destinationId),
                new Address(160, token)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> claimEthAmount(BigInteger senderId, BigInteger destinationId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CLAIMETHAMOUNT, 
                Arrays.<Type>asList(new Uint256(senderId),
                new Uint256(destinationId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> epnToken() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_EPNTOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>> escrowErc20Payments(BigInteger param0, String param1) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ESCROWERC20PAYMENTS, 
                Arrays.<Type>asList(new Uint256(param0),
                new Address(160, param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple4<BigInteger, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>> escrowEthPayments(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ESCROWETHPAYMENTS, 
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple4<BigInteger, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> refundErc20Token(BigInteger senderId, BigInteger destinationId, String token) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REFUNDERC20TOKEN, 
                Arrays.<Type>asList(new Uint256(senderId),
                new Uint256(destinationId),
                new Address(160, token)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> refundEthAmount(BigInteger senderId, BigInteger destinationId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REFUNDETHAMOUNT, 
                Arrays.<Type>asList(new Uint256(senderId),
                new Uint256(destinationId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> sendErc20Token(BigInteger senderId, BigInteger destinationId, String token, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SENDERC20TOKEN, 
                Arrays.<Type>asList(new Uint256(senderId),
                new Uint256(destinationId),
                new Address(160, token),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> sendErc20TokenToWallet(BigInteger senderId, String destinationAddress, String token, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SENDERC20TOKENTOWALLET, 
                Arrays.<Type>asList(new Uint256(senderId),
                new Address(160, destinationAddress),
                new Address(160, token),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> sendEthAmount(BigInteger weiAmount,BigInteger senderId, BigInteger destinationId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SENDETHAMOUNT, 
                Arrays.<Type>asList(new Uint256(senderId),
                new Uint256(destinationId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function,weiAmount);
    }

    public RemoteFunctionCall<TransactionReceipt> sendEthAmountToWallet(BigInteger weiAmount,BigInteger senderId, String destinationAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SENDETHAMOUNTTOWALLET, 
                Arrays.<Type>asList(new Uint256(senderId),
                new Address(160, destinationAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function,weiAmount);
    }

    @Deprecated
    public static PaymentGateway load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PaymentGateway(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PaymentGateway load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PaymentGateway(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PaymentGateway load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new PaymentGateway(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PaymentGateway load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PaymentGateway(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PaymentGateway> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String epnToken_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, epnToken_)));
        return deployRemoteCall(PaymentGateway.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<PaymentGateway> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String epnToken_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, epnToken_)));
        return deployRemoteCall(PaymentGateway.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<PaymentGateway> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String epnToken_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, epnToken_)));
        return deployRemoteCall(PaymentGateway.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<PaymentGateway> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String epnToken_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, epnToken_)));
        return deployRemoteCall(PaymentGateway.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class ClaimErc20TokenTransferedEventResponse extends BaseEventResponse {
        public BigInteger itemId;

        public BigInteger senderId;

        public BigInteger destinationId;

        public String tokenAddress;

        public BigInteger amount;
    }

    public static class ClaimEthTransferedEventResponse extends BaseEventResponse {
        public BigInteger itemId;

        public BigInteger senderId;

        public BigInteger destinationId;

        public BigInteger amount;
    }

    public static class Erc20TokenAddedToEscrowEventResponse extends BaseEventResponse {
        public BigInteger itemId;

        public BigInteger senderId;

        public BigInteger destinationId;

        public String tokenAddress;

        public BigInteger amount;

        public BigInteger expiresOn;
    }

    public static class Erc20TokenTransferedEventResponse extends BaseEventResponse {
        public BigInteger senderId;

        public BigInteger destinationId;

        public String tokenAddress;

        public BigInteger amount;
    }

    public static class Erc20TokenTransferedToWalletEventResponse extends BaseEventResponse {
        public BigInteger senderId;

        public String destinationAddress;

        public String tokenAddress;

        public BigInteger amount;
    }

    public static class EthAddedToEscrowEventResponse extends BaseEventResponse {
        public BigInteger itemId;

        public BigInteger senderId;

        public BigInteger destinationId;

        public BigInteger amount;

        public BigInteger expiresOn;
    }

    public static class EthTransferedEventResponse extends BaseEventResponse {
        public BigInteger senderId;

        public BigInteger destinationId;

        public BigInteger amount;
    }

    public static class EthTransferedToWalletEventResponse extends BaseEventResponse {
        public BigInteger senderId;

        public String destinationAddress;

        public BigInteger amount;
    }

    public static class RefundErc20TokenTransferedEventResponse extends BaseEventResponse {
        public BigInteger itemId;

        public BigInteger senderId;

        public BigInteger destinationId;

        public String tokenAddress;

        public BigInteger amount;
    }

    public static class RefundEthTransferedEventResponse extends BaseEventResponse {
        public BigInteger itemId;

        public BigInteger senderId;

        public BigInteger destinationId;

        public BigInteger amount;
    }
}
