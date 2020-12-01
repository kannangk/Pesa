package finance.pesa.sdk.generator;

import android.content.Context;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint224;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import finance.pesa.sdk.R;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

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
public class PController extends Contract {
   // public static final String BINARY = ;

    public static final String FUNC__ADDPESAMARKETS = "_addPesaMarkets";

    public static final String FUNC__BECOME = "_become";

    public static final String FUNC__BORROWGUARDIANPAUSED = "_borrowGuardianPaused";

    public static final String FUNC__DROPPESAMARKET = "_dropPesaMarket";

    public static final String FUNC__MINTGUARDIANPAUSED = "_mintGuardianPaused";

    public static final String FUNC__SETBORROWPAUSED = "_setBorrowPaused";

    public static final String FUNC__SETCLOSEFACTOR = "_setCloseFactor";

    public static final String FUNC__SETCOLLATERALFACTOR = "_setCollateralFactor";

    public static final String FUNC__SETLIQUIDATIONINCENTIVE = "_setLiquidationIncentive";

    public static final String FUNC__SETMAXASSETS = "_setMaxAssets";

    public static final String FUNC__SETMINTPAUSED = "_setMintPaused";

    public static final String FUNC__SETPAUSEGUARDIAN = "_setPauseGuardian";

    public static final String FUNC__SETPESARATE = "_setPesaRate";

    public static final String FUNC__SETPRICEORACLE = "_setPriceOracle";

    public static final String FUNC__SETSEIZEPAUSED = "_setSeizePaused";

    public static final String FUNC__SETTRANSFERPAUSED = "_setTransferPaused";

    public static final String FUNC__SUPPORTMARKET = "_supportMarket";

    public static final String FUNC_ACCOUNTASSETS = "accountAssets";

    public static final String FUNC_ADMIN = "admin";

    public static final String FUNC_ALLMARKETS = "allMarkets";

    public static final String FUNC_BORROWALLOWED = "borrowAllowed";

    public static final String FUNC_BORROWGUARDIANPAUSED = "borrowGuardianPaused";

    public static final String FUNC_BORROWVERIFY = "borrowVerify";

    public static final String FUNC_CHECKMEMBERSHIP = "checkMembership";

    public static final String FUNC_claimPesa = "claimPesa";

    public static final String FUNC_CLOSEFACTORMANTISSA = "closeFactorMantissa";

    public static final String FUNC_ENTERMARKETS = "enterMarkets";

    public static final String FUNC_EXITMARKET = "exitMarket";

    public static final String FUNC_GETACCOUNTLIQUIDITY = "getAccountLiquidity";

    public static final String FUNC_GETALLMARKETS = "getAllMarkets";

    public static final String FUNC_GETASSETSIN = "getAssetsIn";

    public static final String FUNC_GETBLOCKNUMBER = "getBlockNumber";

    public static final String FUNC_GETHYPOTHETICALACCOUNTLIQUIDITY = "getHypotheticalAccountLiquidity";

    public static final String FUNC_GETPESAADDRESS = "getPesaAddress";

    public static final String FUNC_IMPLEMENTATION = "implementation";

    public static final String FUNC_ISPCONTROLLER = "isPController";

    public static final String FUNC_LIQUIDATEBORROWALLOWED = "liquidateBorrowAllowed";

    public static final String FUNC_LIQUIDATEBORROWVERIFY = "liquidateBorrowVerify";

    public static final String FUNC_LIQUIDATECALCULATESEIZETOKENS = "liquidateCalculateSeizeTokens";

    public static final String FUNC_LIQUIDATIONINCENTIVEMANTISSA = "liquidationIncentiveMantissa";

    public static final String FUNC_MARKETS = "markets";

    public static final String FUNC_MAXASSETS = "maxAssets";

    public static final String FUNC_MINTALLOWED = "mintAllowed";

    public static final String FUNC_MINTGUARDIANPAUSED = "mintGuardianPaused";

    public static final String FUNC_MINTVERIFY = "mintVerify";

    public static final String FUNC_ORACLE = "oracle";

    public static final String FUNC_PAUSEGUARDIAN = "pauseGuardian";

    public static final String FUNC_PENDINGADMIN = "pendingAdmin";

    public static final String FUNC_PENDINGIMPLEMENTATION = "pendingImplementation";

    public static final String FUNC_PESAACCRUED = "pesaAccrued";

    public static final String FUNC_PESABORROWSTATE = "pesaBorrowState";

    public static final String FUNC_PESABORROWERINDEX = "pesaBorrowerIndex";

    public static final String FUNC_PESACLAIMTHRESHOLD = "pesaClaimThreshold";

    public static final String FUNC_PESAINITIALINDEX = "pesaInitialIndex";

    public static final String FUNC_PESARATE = "pesaRate";

    public static final String FUNC_PESASPEEDS = "pesaSpeeds";

    public static final String FUNC_PESASUPPLIERINDEX = "pesaSupplierIndex";

    public static final String FUNC_PESASUPPLYSTATE = "pesaSupplyState";

    public static final String FUNC_REDEEMALLOWED = "redeemAllowed";

    public static final String FUNC_REDEEMVERIFY = "redeemVerify";

    public static final String FUNC_REFRESHPESASPEEDS = "refreshPesaSpeeds";

    public static final String FUNC_REPAYBORROWALLOWED = "repayBorrowAllowed";

    public static final String FUNC_REPAYBORROWVERIFY = "repayBorrowVerify";

    public static final String FUNC_SEIZEALLOWED = "seizeAllowed";

    public static final String FUNC_SEIZEGUARDIANPAUSED = "seizeGuardianPaused";

    public static final String FUNC_SEIZEVERIFY = "seizeVerify";

    public static final String FUNC_TRANSFERALLOWED = "transferAllowed";

    public static final String FUNC_TRANSFERGUARDIANPAUSED = "transferGuardianPaused";

    public static final String FUNC_TRANSFERVERIFY = "transferVerify";

    public static final Event ACTIONPAUSED_EVENT = new Event("ActionPaused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
    ;


    public static final Event DISTRIBUTEDBORROWERPESA_EVENT = new Event("DistributedBorrowerPesa", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event DISTRIBUTEDSUPPLIERPESA_EVENT = new Event("DistributedSupplierPesa", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event FAILURE_EVENT = new Event("Failure", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event MARKETENTERED_EVENT = new Event("MarketEntered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event MARKETEXITED_EVENT = new Event("MarketExited", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event MARKETLISTED_EVENT = new Event("MarketListed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event MARKETPESA_EVENT = new Event("MarketPesa", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event NEWCLOSEFACTOR_EVENT = new Event("NewCloseFactor", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event NEWCOLLATERALFACTOR_EVENT = new Event("NewCollateralFactor", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event NEWLIQUIDATIONINCENTIVE_EVENT = new Event("NewLiquidationIncentive", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event NEWMAXASSETS_EVENT = new Event("NewMaxAssets", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event NEWPAUSEGUARDIAN_EVENT = new Event("NewPauseGuardian", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event NEWPESARATE_EVENT = new Event("NewPesaRate", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event NEWPRICEORACLE_EVENT = new Event("NewPriceOracle", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event PESASPEEDUPDATED_EVENT = new Event("PesaSpeedUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected PController(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Context context) {
        super(context.getString(R.string.p_controller_abi), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PController(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, Context context) {
        super(context.getString(R.string.p_controller_abi), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PController(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Context context) {
        super(context.getString(R.string.p_controller_abi), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PController(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, Context context) {
        super(context.getString(R.string.p_controller_abi), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ActionPausedEventResponse> getActionPausedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ACTIONPAUSED_EVENT, transactionReceipt);
        ArrayList<ActionPausedEventResponse> responses = new ArrayList<ActionPausedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ActionPausedEventResponse typedResponse = new ActionPausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.action = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.pauseState = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ActionPausedEventResponse> actionPausedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ActionPausedEventResponse>() {
            @Override
            public ActionPausedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ACTIONPAUSED_EVENT, log);
                ActionPausedEventResponse typedResponse = new ActionPausedEventResponse();
                typedResponse.log = log;
                typedResponse.action = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.pauseState = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ActionPausedEventResponse> actionPausedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACTIONPAUSED_EVENT));
        return actionPausedEventFlowable(filter);
    }


    public List<DistributedBorrowerPesaEventResponse> getDistributedBorrowerPesaEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(DISTRIBUTEDBORROWERPESA_EVENT, transactionReceipt);
        ArrayList<DistributedBorrowerPesaEventResponse> responses = new ArrayList<DistributedBorrowerPesaEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DistributedBorrowerPesaEventResponse typedResponse = new DistributedBorrowerPesaEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.pToken = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.borrower = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.pesaDelta = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.pesaBorrowIndex = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DistributedBorrowerPesaEventResponse> distributedBorrowerPesaEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DistributedBorrowerPesaEventResponse>() {
            @Override
            public DistributedBorrowerPesaEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(DISTRIBUTEDBORROWERPESA_EVENT, log);
                DistributedBorrowerPesaEventResponse typedResponse = new DistributedBorrowerPesaEventResponse();
                typedResponse.log = log;
                typedResponse.pToken = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.borrower = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.pesaDelta = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.pesaBorrowIndex = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DistributedBorrowerPesaEventResponse> distributedBorrowerPesaEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DISTRIBUTEDBORROWERPESA_EVENT));
        return distributedBorrowerPesaEventFlowable(filter);
    }

    public List<DistributedSupplierPesaEventResponse> getDistributedSupplierPesaEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(DISTRIBUTEDSUPPLIERPESA_EVENT, transactionReceipt);
        ArrayList<DistributedSupplierPesaEventResponse> responses = new ArrayList<DistributedSupplierPesaEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DistributedSupplierPesaEventResponse typedResponse = new DistributedSupplierPesaEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.pToken = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.supplier = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.pesaDelta = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.pesaSupplyIndex = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DistributedSupplierPesaEventResponse> distributedSupplierPesaEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DistributedSupplierPesaEventResponse>() {
            @Override
            public DistributedSupplierPesaEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(DISTRIBUTEDSUPPLIERPESA_EVENT, log);
                DistributedSupplierPesaEventResponse typedResponse = new DistributedSupplierPesaEventResponse();
                typedResponse.log = log;
                typedResponse.pToken = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.supplier = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.pesaDelta = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.pesaSupplyIndex = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DistributedSupplierPesaEventResponse> distributedSupplierPesaEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DISTRIBUTEDSUPPLIERPESA_EVENT));
        return distributedSupplierPesaEventFlowable(filter);
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

    public List<MarketEnteredEventResponse> getMarketEnteredEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MARKETENTERED_EVENT, transactionReceipt);
        ArrayList<MarketEnteredEventResponse> responses = new ArrayList<MarketEnteredEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MarketEnteredEventResponse typedResponse = new MarketEnteredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.pToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<MarketEnteredEventResponse> marketEnteredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, MarketEnteredEventResponse>() {
            @Override
            public MarketEnteredEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MARKETENTERED_EVENT, log);
                MarketEnteredEventResponse typedResponse = new MarketEnteredEventResponse();
                typedResponse.log = log;
                typedResponse.pToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.account = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<MarketEnteredEventResponse> marketEnteredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MARKETENTERED_EVENT));
        return marketEnteredEventFlowable(filter);
    }

    public List<MarketExitedEventResponse> getMarketExitedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MARKETEXITED_EVENT, transactionReceipt);
        ArrayList<MarketExitedEventResponse> responses = new ArrayList<MarketExitedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MarketExitedEventResponse typedResponse = new MarketExitedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.pToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<MarketExitedEventResponse> marketExitedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, MarketExitedEventResponse>() {
            @Override
            public MarketExitedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MARKETEXITED_EVENT, log);
                MarketExitedEventResponse typedResponse = new MarketExitedEventResponse();
                typedResponse.log = log;
                typedResponse.pToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.account = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<MarketExitedEventResponse> marketExitedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MARKETEXITED_EVENT));
        return marketExitedEventFlowable(filter);
    }

    public List<MarketListedEventResponse> getMarketListedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MARKETLISTED_EVENT, transactionReceipt);
        ArrayList<MarketListedEventResponse> responses = new ArrayList<MarketListedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MarketListedEventResponse typedResponse = new MarketListedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.pToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<MarketListedEventResponse> marketListedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, MarketListedEventResponse>() {
            @Override
            public MarketListedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MARKETLISTED_EVENT, log);
                MarketListedEventResponse typedResponse = new MarketListedEventResponse();
                typedResponse.log = log;
                typedResponse.pToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<MarketListedEventResponse> marketListedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MARKETLISTED_EVENT));
        return marketListedEventFlowable(filter);
    }

    public List<MarketPesaEventResponse> getMarketPesaEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MARKETPESA_EVENT, transactionReceipt);
        ArrayList<MarketPesaEventResponse> responses = new ArrayList<MarketPesaEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MarketPesaEventResponse typedResponse = new MarketPesaEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.pToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.isPesa = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<MarketPesaEventResponse> marketPesaEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, MarketPesaEventResponse>() {
            @Override
            public MarketPesaEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MARKETPESA_EVENT, log);
                MarketPesaEventResponse typedResponse = new MarketPesaEventResponse();
                typedResponse.log = log;
                typedResponse.pToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.isPesa = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<MarketPesaEventResponse> marketPesaEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MARKETPESA_EVENT));
        return marketPesaEventFlowable(filter);
    }

    public List<NewCloseFactorEventResponse> getNewCloseFactorEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWCLOSEFACTOR_EVENT, transactionReceipt);
        ArrayList<NewCloseFactorEventResponse> responses = new ArrayList<NewCloseFactorEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewCloseFactorEventResponse typedResponse = new NewCloseFactorEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldCloseFactorMantissa = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newCloseFactorMantissa = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewCloseFactorEventResponse> newCloseFactorEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewCloseFactorEventResponse>() {
            @Override
            public NewCloseFactorEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWCLOSEFACTOR_EVENT, log);
                NewCloseFactorEventResponse typedResponse = new NewCloseFactorEventResponse();
                typedResponse.log = log;
                typedResponse.oldCloseFactorMantissa = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newCloseFactorMantissa = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewCloseFactorEventResponse> newCloseFactorEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWCLOSEFACTOR_EVENT));
        return newCloseFactorEventFlowable(filter);
    }

    public List<NewCollateralFactorEventResponse> getNewCollateralFactorEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWCOLLATERALFACTOR_EVENT, transactionReceipt);
        ArrayList<NewCollateralFactorEventResponse> responses = new ArrayList<NewCollateralFactorEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewCollateralFactorEventResponse typedResponse = new NewCollateralFactorEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.pToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.oldCollateralFactorMantissa = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.newCollateralFactorMantissa = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewCollateralFactorEventResponse> newCollateralFactorEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewCollateralFactorEventResponse>() {
            @Override
            public NewCollateralFactorEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWCOLLATERALFACTOR_EVENT, log);
                NewCollateralFactorEventResponse typedResponse = new NewCollateralFactorEventResponse();
                typedResponse.log = log;
                typedResponse.pToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.oldCollateralFactorMantissa = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.newCollateralFactorMantissa = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewCollateralFactorEventResponse> newCollateralFactorEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWCOLLATERALFACTOR_EVENT));
        return newCollateralFactorEventFlowable(filter);
    }

    public List<NewLiquidationIncentiveEventResponse> getNewLiquidationIncentiveEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWLIQUIDATIONINCENTIVE_EVENT, transactionReceipt);
        ArrayList<NewLiquidationIncentiveEventResponse> responses = new ArrayList<NewLiquidationIncentiveEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewLiquidationIncentiveEventResponse typedResponse = new NewLiquidationIncentiveEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldLiquidationIncentiveMantissa = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newLiquidationIncentiveMantissa = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewLiquidationIncentiveEventResponse> newLiquidationIncentiveEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewLiquidationIncentiveEventResponse>() {
            @Override
            public NewLiquidationIncentiveEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWLIQUIDATIONINCENTIVE_EVENT, log);
                NewLiquidationIncentiveEventResponse typedResponse = new NewLiquidationIncentiveEventResponse();
                typedResponse.log = log;
                typedResponse.oldLiquidationIncentiveMantissa = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newLiquidationIncentiveMantissa = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewLiquidationIncentiveEventResponse> newLiquidationIncentiveEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWLIQUIDATIONINCENTIVE_EVENT));
        return newLiquidationIncentiveEventFlowable(filter);
    }

    public List<NewMaxAssetsEventResponse> getNewMaxAssetsEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWMAXASSETS_EVENT, transactionReceipt);
        ArrayList<NewMaxAssetsEventResponse> responses = new ArrayList<NewMaxAssetsEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewMaxAssetsEventResponse typedResponse = new NewMaxAssetsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldMaxAssets = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newMaxAssets = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewMaxAssetsEventResponse> newMaxAssetsEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewMaxAssetsEventResponse>() {
            @Override
            public NewMaxAssetsEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWMAXASSETS_EVENT, log);
                NewMaxAssetsEventResponse typedResponse = new NewMaxAssetsEventResponse();
                typedResponse.log = log;
                typedResponse.oldMaxAssets = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newMaxAssets = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewMaxAssetsEventResponse> newMaxAssetsEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWMAXASSETS_EVENT));
        return newMaxAssetsEventFlowable(filter);
    }

    public List<NewPauseGuardianEventResponse> getNewPauseGuardianEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWPAUSEGUARDIAN_EVENT, transactionReceipt);
        ArrayList<NewPauseGuardianEventResponse> responses = new ArrayList<NewPauseGuardianEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewPauseGuardianEventResponse typedResponse = new NewPauseGuardianEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldPauseGuardian = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newPauseGuardian = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewPauseGuardianEventResponse> newPauseGuardianEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewPauseGuardianEventResponse>() {
            @Override
            public NewPauseGuardianEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWPAUSEGUARDIAN_EVENT, log);
                NewPauseGuardianEventResponse typedResponse = new NewPauseGuardianEventResponse();
                typedResponse.log = log;
                typedResponse.oldPauseGuardian = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newPauseGuardian = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewPauseGuardianEventResponse> newPauseGuardianEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWPAUSEGUARDIAN_EVENT));
        return newPauseGuardianEventFlowable(filter);
    }

    public List<NewPesaRateEventResponse> getNewPesaRateEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWPESARATE_EVENT, transactionReceipt);
        ArrayList<NewPesaRateEventResponse> responses = new ArrayList<NewPesaRateEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewPesaRateEventResponse typedResponse = new NewPesaRateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldPesaRate = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newPesaRate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewPesaRateEventResponse> newPesaRateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewPesaRateEventResponse>() {
            @Override
            public NewPesaRateEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWPESARATE_EVENT, log);
                NewPesaRateEventResponse typedResponse = new NewPesaRateEventResponse();
                typedResponse.log = log;
                typedResponse.oldPesaRate = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newPesaRate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewPesaRateEventResponse> newPesaRateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWPESARATE_EVENT));
        return newPesaRateEventFlowable(filter);
    }

    public List<NewPriceOracleEventResponse> getNewPriceOracleEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWPRICEORACLE_EVENT, transactionReceipt);
        ArrayList<NewPriceOracleEventResponse> responses = new ArrayList<NewPriceOracleEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewPriceOracleEventResponse typedResponse = new NewPriceOracleEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldPriceOracle = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newPriceOracle = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewPriceOracleEventResponse> newPriceOracleEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewPriceOracleEventResponse>() {
            @Override
            public NewPriceOracleEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWPRICEORACLE_EVENT, log);
                NewPriceOracleEventResponse typedResponse = new NewPriceOracleEventResponse();
                typedResponse.log = log;
                typedResponse.oldPriceOracle = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newPriceOracle = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewPriceOracleEventResponse> newPriceOracleEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWPRICEORACLE_EVENT));
        return newPriceOracleEventFlowable(filter);
    }

    public List<PesaSpeedUpdatedEventResponse> getPesaSpeedUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PESASPEEDUPDATED_EVENT, transactionReceipt);
        ArrayList<PesaSpeedUpdatedEventResponse> responses = new ArrayList<PesaSpeedUpdatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PesaSpeedUpdatedEventResponse typedResponse = new PesaSpeedUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.pToken = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newSpeed = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PesaSpeedUpdatedEventResponse> pesaSpeedUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PesaSpeedUpdatedEventResponse>() {
            @Override
            public PesaSpeedUpdatedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(PESASPEEDUPDATED_EVENT, log);
                PesaSpeedUpdatedEventResponse typedResponse = new PesaSpeedUpdatedEventResponse();
                typedResponse.log = log;
                typedResponse.pToken = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newSpeed = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PesaSpeedUpdatedEventResponse> pesaSpeedUpdatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PESASPEEDUPDATED_EVENT));
        return pesaSpeedUpdatedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> _addPesaMarkets(List<String> pTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__ADDPESAMARKETS, 
                Arrays.<Type>asList(new DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(pTokens, Address.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _become(String pesaCore) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__BECOME, 
                Arrays.<Type>asList(new Address(160, pesaCore)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> _borrowGuardianPaused() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC__BORROWGUARDIANPAUSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> _dropPesaMarket(String pToken) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__DROPPESAMARKET, 
                Arrays.<Type>asList(new Address(160, pToken)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> _mintGuardianPaused() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC__MINTGUARDIANPAUSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> _setBorrowPaused(String pToken, Boolean state) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETBORROWPAUSED, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Bool(state)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setCloseFactor(BigInteger newCloseFactorMantissa) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETCLOSEFACTOR, 
                Arrays.<Type>asList(new Uint256(newCloseFactorMantissa)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setCollateralFactor(String pToken, BigInteger newCollateralFactorMantissa) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETCOLLATERALFACTOR, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Uint256(newCollateralFactorMantissa)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setLiquidationIncentive(BigInteger newLiquidationIncentiveMantissa) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETLIQUIDATIONINCENTIVE, 
                Arrays.<Type>asList(new Uint256(newLiquidationIncentiveMantissa)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setMaxAssets(BigInteger newMaxAssets) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETMAXASSETS, 
                Arrays.<Type>asList(new Uint256(newMaxAssets)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setMintPaused(String pToken, Boolean state) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETMINTPAUSED, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Bool(state)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setPauseGuardian(String newPauseGuardian) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETPAUSEGUARDIAN, 
                Arrays.<Type>asList(new Address(160, newPauseGuardian)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setPesaRate(BigInteger pesaRate_) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETPESARATE, 
                Arrays.<Type>asList(new Uint256(pesaRate_)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setPriceOracle(String newOracle) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETPRICEORACLE, 
                Arrays.<Type>asList(new Address(160, newOracle)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setSeizePaused(Boolean state) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETSEIZEPAUSED, 
                Arrays.<Type>asList(new Bool(state)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setTransferPaused(Boolean state) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETTRANSFERPAUSED, 
                Arrays.<Type>asList(new Bool(state)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _supportMarket(String pToken) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SUPPORTMARKET, 
                Arrays.<Type>asList(new Address(160, pToken)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> accountAssets(String param0, BigInteger param1) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ACCOUNTASSETS, 
                Arrays.<Type>asList(new Address(160, param0),
                new Uint256(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> admin() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ADMIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> allMarkets(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ALLMARKETS, 
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> borrowAllowed(String pToken, String borrower, BigInteger borrowAmount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BORROWALLOWED, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Address(160, borrower),
                new Uint256(borrowAmount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> borrowGuardianPaused(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BORROWGUARDIANPAUSED, 
                Arrays.<Type>asList(new Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> borrowVerify(String pToken, String borrower, BigInteger borrowAmount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BORROWVERIFY, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Address(160, borrower),
                new Uint256(borrowAmount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> checkMembership(String account, String pToken) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CHECKMEMBERSHIP, 
                Arrays.<Type>asList(new Address(160, account),
                new Address(160, pToken)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> claimPesa(List<String> holders, List<String> pTokens, Boolean borrowers, Boolean suppliers) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_claimPesa, 
                Arrays.<Type>asList(new DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(holders, Address.class)),
                new DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(pTokens, Address.class)),
                new Bool(borrowers),
                new Bool(suppliers)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> claimPesa(String holder) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_claimPesa, 
                Arrays.<Type>asList(new Address(160, holder)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> claimPesa(String holder, List<String> pTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_claimPesa, 
                Arrays.<Type>asList(new Address(160, holder),
                new DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(pTokens, Address.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> closeFactorMantissa() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CLOSEFACTORMANTISSA, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> enterMarkets(List<String> pTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ENTERMARKETS, 
                Arrays.<Type>asList(new DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(pTokens, Address.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> exitMarket(String pTokenAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_EXITMARKET, 
                Arrays.<Type>asList(new Address(160, pTokenAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<BigInteger, BigInteger, BigInteger>> getAccountLiquidity(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETACCOUNTLIQUIDITY, 
                Arrays.<Type>asList(new Address(160, account)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple3<BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple3<BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<List> getAllMarkets() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETALLMARKETS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> getAssetsIn(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETASSETSIN, 
                Arrays.<Type>asList(new Address(160, account)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getBlockNumber() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETBLOCKNUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple3<BigInteger, BigInteger, BigInteger>> getHypotheticalAccountLiquidity(String account, String pTokenModify, BigInteger redeemTokens, BigInteger borrowAmount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETHYPOTHETICALACCOUNTLIQUIDITY, 
                Arrays.<Type>asList(new Address(160, account),
                new Address(160, pTokenModify),
                new Uint256(redeemTokens),
                new Uint256(borrowAmount)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple3<BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple3<BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> getPesaAddress() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETPESAADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> implementation() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_IMPLEMENTATION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isPController() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISPCONTROLLER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> liquidateBorrowAllowed(String pTokenBorrowed, String pTokenCollateral, String liquidator, String borrower, BigInteger repayAmount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_LIQUIDATEBORROWALLOWED, 
                Arrays.<Type>asList(new Address(160, pTokenBorrowed),
                new Address(160, pTokenCollateral),
                new Address(160, liquidator),
                new Address(160, borrower),
                new Uint256(repayAmount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> liquidateBorrowVerify(String pTokenBorrowed, String pTokenCollateral, String liquidator, String borrower, BigInteger actualRepayAmount, BigInteger seizeTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_LIQUIDATEBORROWVERIFY, 
                Arrays.<Type>asList(new Address(160, pTokenBorrowed),
                new Address(160, pTokenCollateral),
                new Address(160, liquidator),
                new Address(160, borrower),
                new Uint256(actualRepayAmount),
                new Uint256(seizeTokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple2<BigInteger, BigInteger>> liquidateCalculateSeizeTokens(String pTokenBorrowed, String pTokenCollateral, BigInteger actualRepayAmount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_LIQUIDATECALCULATESEIZETOKENS, 
                Arrays.<Type>asList(new Address(160, pTokenBorrowed),
                new Address(160, pTokenCollateral),
                new Uint256(actualRepayAmount)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<BigInteger, BigInteger>>(function,
                new Callable<Tuple2<BigInteger, BigInteger>>() {
                    @Override
                    public Tuple2<BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> liquidationIncentiveMantissa() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_LIQUIDATIONINCENTIVEMANTISSA, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple3<Boolean, BigInteger, Boolean>> markets(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_MARKETS, 
                Arrays.<Type>asList(new Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple3<Boolean, BigInteger, Boolean>>(function,
                new Callable<Tuple3<Boolean, BigInteger, Boolean>>() {
                    @Override
                    public Tuple3<Boolean, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<Boolean, BigInteger, Boolean>(
                                (Boolean) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> maxAssets() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_MAXASSETS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> mintAllowed(String pToken, String minter, BigInteger mintAmount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_MINTALLOWED, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Address(160, minter),
                new Uint256(mintAmount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> mintGuardianPaused(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_MINTGUARDIANPAUSED, 
                Arrays.<Type>asList(new Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> mintVerify(String pToken, String minter, BigInteger actualMintAmount, BigInteger mintTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_MINTVERIFY, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Address(160, minter),
                new Uint256(actualMintAmount),
                new Uint256(mintTokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> oracle() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ORACLE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> pauseGuardian() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PAUSEGUARDIAN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> pendingAdmin() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PENDINGADMIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> pendingImplementation() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PENDINGIMPLEMENTATION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> pesaAccrued(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PESAACCRUED, 
                Arrays.<Type>asList(new Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple2<BigInteger, BigInteger>> pesaBorrowState(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PESABORROWSTATE, 
                Arrays.<Type>asList(new Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint224>() {}, new TypeReference<Uint32>() {}));
        return new RemoteFunctionCall<Tuple2<BigInteger, BigInteger>>(function,
                new Callable<Tuple2<BigInteger, BigInteger>>() {
                    @Override
                    public Tuple2<BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> pesaBorrowerIndex(String param0, String param1) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PESABORROWERINDEX, 
                Arrays.<Type>asList(new Address(160, param0),
                new Address(160, param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> pesaClaimThreshold() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PESACLAIMTHRESHOLD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> pesaInitialIndex() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PESAINITIALINDEX, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint224>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> pesaRate() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PESARATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> pesaSpeeds(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PESASPEEDS, 
                Arrays.<Type>asList(new Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> pesaSupplierIndex(String param0, String param1) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PESASUPPLIERINDEX, 
                Arrays.<Type>asList(new Address(160, param0),
                new Address(160, param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple2<BigInteger, BigInteger>> pesaSupplyState(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PESASUPPLYSTATE, 
                Arrays.<Type>asList(new Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint224>() {}, new TypeReference<Uint32>() {}));
        return new RemoteFunctionCall<Tuple2<BigInteger, BigInteger>>(function,
                new Callable<Tuple2<BigInteger, BigInteger>>() {
                    @Override
                    public Tuple2<BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> redeemAllowed(String pToken, String redeemer, BigInteger redeemTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REDEEMALLOWED, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Address(160, redeemer),
                new Uint256(redeemTokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> redeemVerify(String pToken, String redeemer, BigInteger redeemAmount, BigInteger redeemTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REDEEMVERIFY, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Address(160, redeemer),
                new Uint256(redeemAmount),
                new Uint256(redeemTokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> refreshPesaSpeeds() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REFRESHPESASPEEDS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> repayBorrowAllowed(String pToken, String payer, String borrower, BigInteger repayAmount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REPAYBORROWALLOWED, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Address(160, payer),
                new Address(160, borrower),
                new Uint256(repayAmount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> repayBorrowVerify(String pToken, String payer, String borrower, BigInteger actualRepayAmount, BigInteger borrowerIndex) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REPAYBORROWVERIFY, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Address(160, payer),
                new Address(160, borrower),
                new Uint256(actualRepayAmount),
                new Uint256(borrowerIndex)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> seizeAllowed(String pTokenCollateral, String pTokenBorrowed, String liquidator, String borrower, BigInteger seizeTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SEIZEALLOWED, 
                Arrays.<Type>asList(new Address(160, pTokenCollateral),
                new Address(160, pTokenBorrowed),
                new Address(160, liquidator),
                new Address(160, borrower),
                new Uint256(seizeTokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> seizeGuardianPaused() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SEIZEGUARDIANPAUSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> seizeVerify(String pTokenCollateral, String pTokenBorrowed, String liquidator, String borrower, BigInteger seizeTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SEIZEVERIFY, 
                Arrays.<Type>asList(new Address(160, pTokenCollateral),
                new Address(160, pTokenBorrowed),
                new Address(160, liquidator),
                new Address(160, borrower),
                new Uint256(seizeTokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferAllowed(String pToken, String src, String dst, BigInteger transferTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFERALLOWED, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Address(160, src),
                new Address(160, dst),
                new Uint256(transferTokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> transferGuardianPaused() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TRANSFERGUARDIANPAUSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferVerify(String pToken, String src, String dst, BigInteger transferTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFERVERIFY, 
                Arrays.<Type>asList(new Address(160, pToken),
                new Address(160, src),
                new Address(160, dst),
                new Uint256(transferTokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static PController load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit,Context context) {
        return new PController(contractAddress, web3j, credentials, gasPrice, gasLimit,context);
    }

    @Deprecated
    public static PController load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,Context context) {
        return new PController(contractAddress, web3j, transactionManager, gasPrice, gasLimit,context);
    }

    public static PController load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider,Context context) {
        return new PController(contractAddress, web3j, credentials, contractGasProvider,context);
    }

    public static PController load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider,Context context) {
        return new PController(contractAddress, web3j, transactionManager, contractGasProvider,context);
    }

    public static RemoteCall<PController> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider,Context context) {
        return deployRemoteCall(PController.class, web3j, credentials, contractGasProvider, context.getString(R.string.p_controller_abi), "");
    }

    public static RemoteCall<PController> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider,Context context) {
        return deployRemoteCall(PController.class, web3j, transactionManager, contractGasProvider, context.getString(R.string.p_controller_abi), "");
    }

    @Deprecated
    public static RemoteCall<PController> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit,Context context) {
        return deployRemoteCall(PController.class, web3j, credentials, gasPrice, gasLimit, context.getString(R.string.p_controller_abi), "");
    }

    @Deprecated
    public static RemoteCall<PController> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,Context context) {
        return deployRemoteCall(PController.class, web3j, transactionManager, gasPrice, gasLimit, context.getString(R.string.p_controller_abi), "");
    }

    public static class ActionPausedEventResponse extends BaseEventResponse {
        public String pToken;

        public String action;

        public Boolean pauseState;
    }

    public static class DistributedBorrowerPesaEventResponse extends BaseEventResponse {
        public String pToken;

        public String borrower;

        public BigInteger pesaDelta;

        public BigInteger pesaBorrowIndex;
    }

    public static class DistributedSupplierPesaEventResponse extends BaseEventResponse {
        public String pToken;

        public String supplier;

        public BigInteger pesaDelta;

        public BigInteger pesaSupplyIndex;
    }

    public static class FailureEventResponse extends BaseEventResponse {
        public BigInteger error;

        public BigInteger info;

        public BigInteger detail;
    }

    public static class MarketEnteredEventResponse extends BaseEventResponse {
        public String pToken;

        public String account;
    }

    public static class MarketExitedEventResponse extends BaseEventResponse {
        public String pToken;

        public String account;
    }

    public static class MarketListedEventResponse extends BaseEventResponse {
        public String pToken;
    }

    public static class MarketPesaEventResponse extends BaseEventResponse {
        public String pToken;

        public Boolean isPesa;
    }

    public static class NewCloseFactorEventResponse extends BaseEventResponse {
        public BigInteger oldCloseFactorMantissa;

        public BigInteger newCloseFactorMantissa;
    }

    public static class NewCollateralFactorEventResponse extends BaseEventResponse {
        public String pToken;

        public BigInteger oldCollateralFactorMantissa;

        public BigInteger newCollateralFactorMantissa;
    }

    public static class NewLiquidationIncentiveEventResponse extends BaseEventResponse {
        public BigInteger oldLiquidationIncentiveMantissa;

        public BigInteger newLiquidationIncentiveMantissa;
    }

    public static class NewMaxAssetsEventResponse extends BaseEventResponse {
        public BigInteger oldMaxAssets;

        public BigInteger newMaxAssets;
    }

    public static class NewPauseGuardianEventResponse extends BaseEventResponse {
        public String oldPauseGuardian;

        public String newPauseGuardian;
    }

    public static class NewPesaRateEventResponse extends BaseEventResponse {
        public BigInteger oldPesaRate;

        public BigInteger newPesaRate;
    }

    public static class NewPriceOracleEventResponse extends BaseEventResponse {
        public String oldPriceOracle;

        public String newPriceOracle;
    }

    public static class PesaSpeedUpdatedEventResponse extends BaseEventResponse {
        public String pToken;

        public BigInteger newSpeed;
    }
}
