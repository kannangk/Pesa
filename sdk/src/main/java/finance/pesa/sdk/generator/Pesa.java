package finance.pesa.sdk.generator;

import android.content.Context;

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
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
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
public class Pesa extends Contract {
   // public static final String BINARY = "";

    public static final String FUNC__ACCEPTADMIN = "_acceptAdmin";

    public static final String FUNC__SETPENDINGADMIN = "_setPendingAdmin";

    public static final String FUNC__SETPESATOKEN = "_setPesaToken";

    public static final String FUNC__SUPPORTTOKENS = "_supportTokens";

    public static final String FUNC_ADMIN = "admin";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_ALLOWEDTOKENS = "allowedTokens";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_BASEPUSDBURNCAP = "basePUSDBurnCap";

    public static final String FUNC_BUY = "buy";

    public static final String FUNC_CURRENTPUSDBURNCAP = "currentPUSDBurnCap";

    public static final String FUNC_CURRENTPESAPERPUSD = "currentPesaPerPUSD";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_DOTRANSFEROUT = "doTransferOut";

    public static final String FUNC_ISSPESATOKEN = "isSPesaToken";

    public static final String FUNC_MAXIMUMPUSDBURNCAP = "maximumPUSDBurnCap";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_NEXTCAPSCALE = "nextCapScale";

    public static final String FUNC_NEXTCAPSTEP = "nextCapStep";

    public static final String FUNC_NEXTPUSDBURNCAP = "nextPUSDBurnCap";

    public static final String FUNC_NEXTPESAPERPUSDSCALE = "nextPesaPerPUSDScale";

    public static final String FUNC_PENDINGADMIN = "pendingAdmin";

    public static final String FUNC_PESATOKEN = "pesaToken";

    public static final String FUNC_SPENDPUSD = "spendPUSD";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALPUSDBURNED = "totalPUSDBurned";

    public static final String FUNC_TOTALPESADISTRIBUTED = "totalPesaDistributed";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final Event ADDEDPESATOKENFORDISTRIBUTION_EVENT = new Event("AddedPesaTokenForDistribution", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event ADDEDSUPPORTEDTOKEN_EVENT = new Event("AddedSupportedToken", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event BURNPUSD_EVENT = new Event("BurnPUSD", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event DISTRIBUTEPESA_EVENT = new Event("DistributePesa", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event FAILURE_EVENT = new Event("Failure", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event MINTSPESATOKEN_EVENT = new Event("MintsPesaToken", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event NEWADMIN_EVENT = new Event("NewAdmin", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event NEWPENDINGADMIN_EVENT = new Event("NewPendingAdmin", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected Pesa(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit,Context  context) {
        super(context.getString(R.string.spesa_abi), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Pesa(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider,Context  context) {
        super(context.getString(R.string.spesa_abi), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Pesa(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,Context  context) {
        super(context.getString(R.string.spesa_abi), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Pesa(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider,Context  context) {
        super(context.getString(R.string.spesa_abi), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<AddedPesaTokenForDistributionEventResponse> getAddedPesaTokenForDistributionEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ADDEDPESATOKENFORDISTRIBUTION_EVENT, transactionReceipt);
        ArrayList<AddedPesaTokenForDistributionEventResponse> responses = new ArrayList<AddedPesaTokenForDistributionEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AddedPesaTokenForDistributionEventResponse typedResponse = new AddedPesaTokenForDistributionEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.pesaTokenAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddedPesaTokenForDistributionEventResponse> addedPesaTokenForDistributionEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AddedPesaTokenForDistributionEventResponse>() {
            @Override
            public AddedPesaTokenForDistributionEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ADDEDPESATOKENFORDISTRIBUTION_EVENT, log);
                AddedPesaTokenForDistributionEventResponse typedResponse = new AddedPesaTokenForDistributionEventResponse();
                typedResponse.log = log;
                typedResponse.pesaTokenAddress = (String) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddedPesaTokenForDistributionEventResponse> addedPesaTokenForDistributionEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDEDPESATOKENFORDISTRIBUTION_EVENT));
        return addedPesaTokenForDistributionEventFlowable(filter);
    }

    public List<AddedSupportedTokenEventResponse> getAddedSupportedTokenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ADDEDSUPPORTEDTOKEN_EVENT, transactionReceipt);
        ArrayList<AddedSupportedTokenEventResponse> responses = new ArrayList<AddedSupportedTokenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AddedSupportedTokenEventResponse typedResponse = new AddedSupportedTokenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddedSupportedTokenEventResponse> addedSupportedTokenEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AddedSupportedTokenEventResponse>() {
            @Override
            public AddedSupportedTokenEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ADDEDSUPPORTEDTOKEN_EVENT, log);
                AddedSupportedTokenEventResponse typedResponse = new AddedSupportedTokenEventResponse();
                typedResponse.log = log;
                typedResponse.tokenAddress = (String) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddedSupportedTokenEventResponse> addedSupportedTokenEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDEDSUPPORTEDTOKEN_EVENT));
        return addedSupportedTokenEventFlowable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public List<BurnPUSDEventResponse> getBurnPUSDEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(BURNPUSD_EVENT, transactionReceipt);
        ArrayList<BurnPUSDEventResponse> responses = new ArrayList<BurnPUSDEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            BurnPUSDEventResponse typedResponse = new BurnPUSDEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spendTokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BurnPUSDEventResponse> burnPUSDEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, BurnPUSDEventResponse>() {
            @Override
            public BurnPUSDEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(BURNPUSD_EVENT, log);
                BurnPUSDEventResponse typedResponse = new BurnPUSDEventResponse();
                typedResponse.log = log;
                typedResponse.spender = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.spendTokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BurnPUSDEventResponse> burnPUSDEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BURNPUSD_EVENT));
        return burnPUSDEventFlowable(filter);
    }

    public List<DistributePesaEventResponse> getDistributePesaEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(DISTRIBUTEPESA_EVENT, transactionReceipt);
        ArrayList<DistributePesaEventResponse> responses = new ArrayList<DistributePesaEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DistributePesaEventResponse typedResponse = new DistributePesaEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.pesaTokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DistributePesaEventResponse> distributePesaEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DistributePesaEventResponse>() {
            @Override
            public DistributePesaEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(DISTRIBUTEPESA_EVENT, log);
                DistributePesaEventResponse typedResponse = new DistributePesaEventResponse();
                typedResponse.log = log;
                typedResponse.spender = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.pesaTokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DistributePesaEventResponse> distributePesaEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DISTRIBUTEPESA_EVENT));
        return distributePesaEventFlowable(filter);
    }

    public List<FailureEventResponse> getFailureEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(FAILURE_EVENT, transactionReceipt);
        ArrayList<FailureEventResponse> responses = new ArrayList<FailureEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            FailureEventResponse typedResponse = new FailureEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.error = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.info = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.detail = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<FailureEventResponse> failureEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, FailureEventResponse>() {
            @Override
            public FailureEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(FAILURE_EVENT, log);
                FailureEventResponse typedResponse = new FailureEventResponse();
                typedResponse.log = log;
                typedResponse.error = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.info = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.detail = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<FailureEventResponse> failureEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FAILURE_EVENT));
        return failureEventFlowable(filter);
    }

    public List<MintsPesaTokenEventResponse> getMintsPesaTokenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MINTSPESATOKEN_EVENT, transactionReceipt);
        ArrayList<MintsPesaTokenEventResponse> responses = new ArrayList<MintsPesaTokenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MintsPesaTokenEventResponse typedResponse = new MintsPesaTokenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.buyer = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.mintAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.mintTokens = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<MintsPesaTokenEventResponse> mintsPesaTokenEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, MintsPesaTokenEventResponse>() {
            @Override
            public MintsPesaTokenEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MINTSPESATOKEN_EVENT, log);
                MintsPesaTokenEventResponse typedResponse = new MintsPesaTokenEventResponse();
                typedResponse.log = log;
                typedResponse.buyer = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.mintAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.mintTokens = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<MintsPesaTokenEventResponse> mintsPesaTokenEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MINTSPESATOKEN_EVENT));
        return mintsPesaTokenEventFlowable(filter);
    }

    public List<NewAdminEventResponse> getNewAdminEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWADMIN_EVENT, transactionReceipt);
        ArrayList<NewAdminEventResponse> responses = new ArrayList<NewAdminEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewAdminEventResponse typedResponse = new NewAdminEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldAdmin = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newAdmin = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewAdminEventResponse> newAdminEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewAdminEventResponse>() {
            @Override
            public NewAdminEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWADMIN_EVENT, log);
                NewAdminEventResponse typedResponse = new NewAdminEventResponse();
                typedResponse.log = log;
                typedResponse.oldAdmin = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newAdmin = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewAdminEventResponse> newAdminEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWADMIN_EVENT));
        return newAdminEventFlowable(filter);
    }

    public List<NewPendingAdminEventResponse> getNewPendingAdminEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWPENDINGADMIN_EVENT, transactionReceipt);
        ArrayList<NewPendingAdminEventResponse> responses = new ArrayList<NewPendingAdminEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewPendingAdminEventResponse typedResponse = new NewPendingAdminEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldPendingAdmin = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newPendingAdmin = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewPendingAdminEventResponse> newPendingAdminEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewPendingAdminEventResponse>() {
            @Override
            public NewPendingAdminEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWPENDINGADMIN_EVENT, log);
                NewPendingAdminEventResponse typedResponse = new NewPendingAdminEventResponse();
                typedResponse.log = log;
                typedResponse.oldPendingAdmin = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newPendingAdmin = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewPendingAdminEventResponse> newPendingAdminEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWPENDINGADMIN_EVENT));
        return newPendingAdminEventFlowable(filter);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> _acceptAdmin() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__ACCEPTADMIN, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setPendingAdmin(String newPendingAdmin) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETPENDINGADMIN, 
                Arrays.<Type>asList(new Address(160, newPendingAdmin)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setPesaToken(String pesaTokenAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETPESATOKEN, 
                Arrays.<Type>asList(new Address(160, pesaTokenAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _supportTokens(String allowedToken, BigInteger exchangeRate_) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SUPPORTTOKENS, 
                Arrays.<Type>asList(new Address(160, allowedToken),
                new Uint256(exchangeRate_)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> admin() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ADMIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> allowance(String owner, String spender) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new Address(160, owner),
                new Address(160, spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple3<Boolean, String, BigInteger>> allowedTokens(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ALLOWEDTOKENS, 
                Arrays.<Type>asList(new Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<Boolean, String, BigInteger>>(function,
                new Callable<Tuple3<Boolean, String, BigInteger>>() {
                    @Override
                    public Tuple3<Boolean, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<Boolean, String, BigInteger>(
                                (Boolean) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String spender, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new Address(160, spender),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String owner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new Address(160, owner)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> basePUSDBurnCap() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BASEPUSDBURNCAP, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> buy(String supplyTokenAddress, BigInteger supplyAmount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BUY, 
                Arrays.<Type>asList(new Address(160, supplyTokenAddress),
                new Uint256(supplyAmount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> currentPUSDBurnCap() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CURRENTPUSDBURNCAP, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> currentPesaPerPUSD() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CURRENTPESAPERPUSD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> doTransferOut(String suppliedTokenAddress, String to, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DOTRANSFEROUT, 
                Arrays.<Type>asList(new Address(160, suppliedTokenAddress),
                new Address(160, to),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> isSPesaToken() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISSPESATOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> maximumPUSDBurnCap() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_MAXIMUMPUSDBURNCAP, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> mint(BigInteger mintTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_MINT, 
                Arrays.<Type>asList(new Uint256(mintTokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> name() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> nextCapScale() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NEXTCAPSCALE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> nextCapStep() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NEXTCAPSTEP, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> nextPUSDBurnCap() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NEXTPUSDBURNCAP, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> nextPesaPerPUSDScale() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NEXTPESAPERPUSDSCALE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> pendingAdmin() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PENDINGADMIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> pesaToken() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PESATOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> spendPUSD(BigInteger spendToken) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SPENDPUSD, 
                Arrays.<Type>asList(new Uint256(spendToken)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> symbol() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalPUSDBurned() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALPUSDBURNED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> totalPesaDistributed() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALPESADISTRIBUTED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String dst, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new Address(160, dst),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String src, String dst, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new Address(160, src),
                new Address(160, dst),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Pesa load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit,Context  context) {
        return new Pesa(contractAddress, web3j, credentials, gasPrice, gasLimit,context);
    }

    @Deprecated
    public static Pesa load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,Context  context) {
        return new Pesa(contractAddress, web3j, transactionManager, gasPrice, gasLimit,context);
    }

    public static Pesa load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider,Context  context) {
        return new Pesa(contractAddress, web3j, credentials, contractGasProvider,context);
    }

    public static Pesa load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider,Context  context) {
        return new Pesa(contractAddress, web3j, transactionManager, contractGasProvider,context);
    }

    public static RemoteCall<Pesa> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, Context context) {
        return deployRemoteCall(Pesa.class, web3j, credentials, contractGasProvider, context.getString(R.string.spesa_abi), "");
    }

    public static RemoteCall<Pesa> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, Context context) {
        return deployRemoteCall(Pesa.class, web3j, transactionManager, contractGasProvider, context.getString(R.string.spesa_abi), "");
    }

    @Deprecated
    public static RemoteCall<Pesa> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Context context) {
        return deployRemoteCall(Pesa.class, web3j, credentials, gasPrice, gasLimit, context.getString(R.string.spesa_abi), "");
    }

    @Deprecated
    public static RemoteCall<Pesa> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Context context) {
        return deployRemoteCall(Pesa.class, web3j, transactionManager, gasPrice, gasLimit, context.getString(R.string.spesa_abi), "");
    }

    public static class AddedPesaTokenForDistributionEventResponse extends BaseEventResponse {
        public String pesaTokenAddress;
    }

    public static class AddedSupportedTokenEventResponse extends BaseEventResponse {
        public String tokenAddress;
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String spender;

        public BigInteger amount;
    }

    public static class BurnPUSDEventResponse extends BaseEventResponse {
        public String spender;

        public BigInteger spendTokens;
    }

    public static class DistributePesaEventResponse extends BaseEventResponse {
        public String spender;

        public BigInteger pesaTokens;
    }

    public static class FailureEventResponse extends BaseEventResponse {
        public BigInteger error;

        public BigInteger info;

        public BigInteger detail;
    }

    public static class MintsPesaTokenEventResponse extends BaseEventResponse {
        public String buyer;

        public BigInteger mintAmount;

        public BigInteger mintTokens;
    }

    public static class NewAdminEventResponse extends BaseEventResponse {
        public String oldAdmin;

        public String newAdmin;
    }

    public static class NewPendingAdminEventResponse extends BaseEventResponse {
        public String oldPendingAdmin;

        public String newPendingAdmin;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger amount;
    }
}
