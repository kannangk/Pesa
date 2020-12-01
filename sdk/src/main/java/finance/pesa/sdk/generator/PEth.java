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
public class PEth extends Contract {
    public static final String BINARY = PesaApplication.Companion.getContext().getString(R.string.p_eth_abi);

    public static final String FUNC__ACCEPTADMIN = "_acceptAdmin";

    public static final String FUNC__CHANGEEPNCLAIMSTATUS = "_changeEPNClaimStatus";

    public static final String FUNC__REDUCERESERVES = "_reduceReserves";

    public static final String FUNC__SETEPN = "_setEPN";

    public static final String FUNC__SETINTERESTRATEMODEL = "_setInterestRateModel";

    public static final String FUNC__SETMINIMUMCLAIMVALUE = "_setMinimumClaimValue";

    public static final String FUNC__SETPCONTROLLER = "_setPController";

    public static final String FUNC__SETPENDINGADMIN = "_setPendingAdmin";

    public static final String FUNC__SETRESERVEFACTOR = "_setReserveFactor";

    public static final String FUNC_ACCRUALBLOCKNUMBER = "accrualBlockNumber";

    public static final String FUNC_ACCRUEINTEREST = "accrueInterest";

    public static final String FUNC_ADMIN = "admin";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_BALANCEOFUNDERLYING = "balanceOfUnderlying";

    public static final String FUNC_BORROW = "borrow";

    public static final String FUNC_BORROWBALANCECURRENT = "borrowBalanceCurrent";

    public static final String FUNC_BORROWBALANCESTORED = "borrowBalanceStored";

    public static final String FUNC_BORROWINDEX = "borrowIndex";

    public static final String FUNC_BORROWRATEPERBLOCK = "borrowRatePerBlock";

    public static final String FUNC_CLAIMEPN = "claimEPN";

    public static final String FUNC_CLAIMEPNWITHENS = "claimEPNwithENS";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_EPNTOKEN = "epnToken";

    public static final String FUNC_EXCHANGERATECURRENT = "exchangeRateCurrent";

    public static final String FUNC_EXCHANGERATESTORED = "exchangeRateStored";

    public static final String FUNC_GETACCOUNTSNAPSHOT = "getAccountSnapshot";

    public static final String FUNC_GETCASH = "getCash";

    public static final String FUNC_INITIALIZE = "initialize";

    public static final String FUNC_INTERESTRATEMODEL = "interestRateModel";

    public static final String FUNC_ISEPNCLAIMENABLED = "isEPNClaimEnabled";

    public static final String FUNC_ISPTOKEN = "isPToken";

    public static final String FUNC_LIQUIDATEBORROW = "liquidateBorrow";

    public static final String FUNC_MINIMUMCLAIMVALUE = "minimumClaimValue";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_PCONTROLLER = "pController";

    public static final String FUNC_PENDINGADMIN = "pendingAdmin";

    public static final String FUNC_REDEEM = "redeem";

    public static final String FUNC_REDEEMUNDERLYING = "redeemUnderlying";

    public static final String FUNC_REPAYBORROW = "repayBorrow";

    public static final String FUNC_REPAYBORROWBEHALF = "repayBorrowBehalf";

    public static final String FUNC_RESERVEFACTORMANTISSA = "reserveFactorMantissa";

    public static final String FUNC_SEIZE = "seize";

    public static final String FUNC_SUPPLYRATEPERBLOCK = "supplyRatePerBlock";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALBORROWS = "totalBorrows";

    public static final String FUNC_TOTALBORROWSCURRENT = "totalBorrowsCurrent";

    public static final String FUNC_TOTALRESERVES = "totalReserves";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final Event ACCRUEINTERESTTOKEN_EVENT = new Event("AccrueInterestToken", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ALLOWEDTESTEVENT_EVENT = new Event("AllowedTestEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event BORROWTOKEN_EVENT = new Event("BorrowToken", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event FAILURE_EVENT = new Event("Failure", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event LIQUIDATEBORROWTOKEN_EVENT = new Event("LiquidateBorrowToken", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event MINTTOKEN_EVENT = new Event("MintToken", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event NEWADMIN_EVENT = new Event("NewAdmin", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event NEWEPNTOKEN_EVENT = new Event("NewEPNToken", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event NEWISEPNCLAIMSTATUS_EVENT = new Event("NewIsEPNClaimStatus", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event NEWMARKETTOKENINTERESTRATEMODEL_EVENT = new Event("NewMarketTokenInterestRateModel", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event NEWMINIMUMCLAIMVALUE_EVENT = new Event("NewMinimumClaimValue", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event NEWPCONTROLLER_EVENT = new Event("NewPController", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event NEWPENDINGADMIN_EVENT = new Event("NewPendingAdmin", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event NEWTOKENRESERVEFACTOR_EVENT = new Event("NewTokenReserveFactor", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event REDEEMTOKEN_EVENT = new Event("RedeemToken", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event REPAYBORROWTOKEN_EVENT = new Event("RepayBorrowToken", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event RESERVESADDED_EVENT = new Event("ReservesAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event RESERVESREDUCED_EVENT = new Event("ReservesReduced", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected PEth(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PEth(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PEth(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PEth(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<AccrueInterestTokenEventResponse> getAccrueInterestTokenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ACCRUEINTERESTTOKEN_EVENT, transactionReceipt);
        ArrayList<AccrueInterestTokenEventResponse> responses = new ArrayList<AccrueInterestTokenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AccrueInterestTokenEventResponse typedResponse = new AccrueInterestTokenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.cashPrior = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.interestAccumulated = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.borrowIndex = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.totalBorrows = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AccrueInterestTokenEventResponse> accrueInterestTokenEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AccrueInterestTokenEventResponse>() {
            @Override
            public AccrueInterestTokenEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ACCRUEINTERESTTOKEN_EVENT, log);
                AccrueInterestTokenEventResponse typedResponse = new AccrueInterestTokenEventResponse();
                typedResponse.log = log;
                typedResponse.cashPrior = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.interestAccumulated = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.borrowIndex = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.totalBorrows = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AccrueInterestTokenEventResponse> accrueInterestTokenEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCRUEINTERESTTOKEN_EVENT));
        return accrueInterestTokenEventFlowable(filter);
    }

    public List<AllowedTestEventEventResponse> getAllowedTestEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ALLOWEDTESTEVENT_EVENT, transactionReceipt);
        ArrayList<AllowedTestEventEventResponse> responses = new ArrayList<AllowedTestEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AllowedTestEventEventResponse typedResponse = new AllowedTestEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.pToken = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.errorCode = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AllowedTestEventEventResponse> allowedTestEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AllowedTestEventEventResponse>() {
            @Override
            public AllowedTestEventEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ALLOWEDTESTEVENT_EVENT, log);
                AllowedTestEventEventResponse typedResponse = new AllowedTestEventEventResponse();
                typedResponse.log = log;
                typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.pToken = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.errorCode = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AllowedTestEventEventResponse> allowedTestEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ALLOWEDTESTEVENT_EVENT));
        return allowedTestEventEventFlowable(filter);
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

    public List<BorrowTokenEventResponse> getBorrowTokenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(BORROWTOKEN_EVENT, transactionReceipt);
        ArrayList<BorrowTokenEventResponse> responses = new ArrayList<BorrowTokenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            BorrowTokenEventResponse typedResponse = new BorrowTokenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.borrower = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.borrowAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.accountBorrows = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.totalBorrows = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BorrowTokenEventResponse> borrowTokenEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, BorrowTokenEventResponse>() {
            @Override
            public BorrowTokenEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(BORROWTOKEN_EVENT, log);
                BorrowTokenEventResponse typedResponse = new BorrowTokenEventResponse();
                typedResponse.log = log;
                typedResponse.borrower = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.borrowAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.accountBorrows = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.totalBorrows = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BorrowTokenEventResponse> borrowTokenEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BORROWTOKEN_EVENT));
        return borrowTokenEventFlowable(filter);
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

    public List<LiquidateBorrowTokenEventResponse> getLiquidateBorrowTokenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(LIQUIDATEBORROWTOKEN_EVENT, transactionReceipt);
        ArrayList<LiquidateBorrowTokenEventResponse> responses = new ArrayList<LiquidateBorrowTokenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LiquidateBorrowTokenEventResponse typedResponse = new LiquidateBorrowTokenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.liquidator = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.borrower = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.repayAmount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.cTokenCollateral = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.seizeTokens = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LiquidateBorrowTokenEventResponse> liquidateBorrowTokenEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, LiquidateBorrowTokenEventResponse>() {
            @Override
            public LiquidateBorrowTokenEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(LIQUIDATEBORROWTOKEN_EVENT, log);
                LiquidateBorrowTokenEventResponse typedResponse = new LiquidateBorrowTokenEventResponse();
                typedResponse.log = log;
                typedResponse.liquidator = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.borrower = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.repayAmount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.cTokenCollateral = (String) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.seizeTokens = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LiquidateBorrowTokenEventResponse> liquidateBorrowTokenEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LIQUIDATEBORROWTOKEN_EVENT));
        return liquidateBorrowTokenEventFlowable(filter);
    }

    public List<MintTokenEventResponse> getMintTokenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MINTTOKEN_EVENT, transactionReceipt);
        ArrayList<MintTokenEventResponse> responses = new ArrayList<MintTokenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MintTokenEventResponse typedResponse = new MintTokenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.minter = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.mintAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.mintTokens = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<MintTokenEventResponse> mintTokenEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, MintTokenEventResponse>() {
            @Override
            public MintTokenEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MINTTOKEN_EVENT, log);
                MintTokenEventResponse typedResponse = new MintTokenEventResponse();
                typedResponse.log = log;
                typedResponse.minter = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.mintAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.mintTokens = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<MintTokenEventResponse> mintTokenEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MINTTOKEN_EVENT));
        return mintTokenEventFlowable(filter);
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

    public List<NewEPNTokenEventResponse> getNewEPNTokenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWEPNTOKEN_EVENT, transactionReceipt);
        ArrayList<NewEPNTokenEventResponse> responses = new ArrayList<NewEPNTokenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewEPNTokenEventResponse typedResponse = new NewEPNTokenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldEPNToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newEPNToken = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewEPNTokenEventResponse> newEPNTokenEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewEPNTokenEventResponse>() {
            @Override
            public NewEPNTokenEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWEPNTOKEN_EVENT, log);
                NewEPNTokenEventResponse typedResponse = new NewEPNTokenEventResponse();
                typedResponse.log = log;
                typedResponse.oldEPNToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newEPNToken = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewEPNTokenEventResponse> newEPNTokenEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWEPNTOKEN_EVENT));
        return newEPNTokenEventFlowable(filter);
    }

    public List<NewIsEPNClaimStatusEventResponse> getNewIsEPNClaimStatusEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWISEPNCLAIMSTATUS_EVENT, transactionReceipt);
        ArrayList<NewIsEPNClaimStatusEventResponse> responses = new ArrayList<NewIsEPNClaimStatusEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewIsEPNClaimStatusEventResponse typedResponse = new NewIsEPNClaimStatusEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldStatus = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newStatus = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewIsEPNClaimStatusEventResponse> newIsEPNClaimStatusEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewIsEPNClaimStatusEventResponse>() {
            @Override
            public NewIsEPNClaimStatusEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWISEPNCLAIMSTATUS_EVENT, log);
                NewIsEPNClaimStatusEventResponse typedResponse = new NewIsEPNClaimStatusEventResponse();
                typedResponse.log = log;
                typedResponse.oldStatus = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newStatus = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewIsEPNClaimStatusEventResponse> newIsEPNClaimStatusEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWISEPNCLAIMSTATUS_EVENT));
        return newIsEPNClaimStatusEventFlowable(filter);
    }

    public List<NewMarketTokenInterestRateModelEventResponse> getNewMarketTokenInterestRateModelEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWMARKETTOKENINTERESTRATEMODEL_EVENT, transactionReceipt);
        ArrayList<NewMarketTokenInterestRateModelEventResponse> responses = new ArrayList<NewMarketTokenInterestRateModelEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewMarketTokenInterestRateModelEventResponse typedResponse = new NewMarketTokenInterestRateModelEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldInterestRateModel = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newInterestRateModel = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewMarketTokenInterestRateModelEventResponse> newMarketTokenInterestRateModelEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewMarketTokenInterestRateModelEventResponse>() {
            @Override
            public NewMarketTokenInterestRateModelEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWMARKETTOKENINTERESTRATEMODEL_EVENT, log);
                NewMarketTokenInterestRateModelEventResponse typedResponse = new NewMarketTokenInterestRateModelEventResponse();
                typedResponse.log = log;
                typedResponse.oldInterestRateModel = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newInterestRateModel = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewMarketTokenInterestRateModelEventResponse> newMarketTokenInterestRateModelEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWMARKETTOKENINTERESTRATEMODEL_EVENT));
        return newMarketTokenInterestRateModelEventFlowable(filter);
    }

    public List<NewMinimumClaimValueEventResponse> getNewMinimumClaimValueEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWMINIMUMCLAIMVALUE_EVENT, transactionReceipt);
        ArrayList<NewMinimumClaimValueEventResponse> responses = new ArrayList<NewMinimumClaimValueEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewMinimumClaimValueEventResponse typedResponse = new NewMinimumClaimValueEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldMinimumClaimValue = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newMinimumClaimValue = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewMinimumClaimValueEventResponse> newMinimumClaimValueEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewMinimumClaimValueEventResponse>() {
            @Override
            public NewMinimumClaimValueEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWMINIMUMCLAIMVALUE_EVENT, log);
                NewMinimumClaimValueEventResponse typedResponse = new NewMinimumClaimValueEventResponse();
                typedResponse.log = log;
                typedResponse.oldMinimumClaimValue = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newMinimumClaimValue = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewMinimumClaimValueEventResponse> newMinimumClaimValueEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWMINIMUMCLAIMVALUE_EVENT));
        return newMinimumClaimValueEventFlowable(filter);
    }

    public List<NewPControllerEventResponse> getNewPControllerEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWPCONTROLLER_EVENT, transactionReceipt);
        ArrayList<NewPControllerEventResponse> responses = new ArrayList<NewPControllerEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewPControllerEventResponse typedResponse = new NewPControllerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldPController = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newPController = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewPControllerEventResponse> newPControllerEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewPControllerEventResponse>() {
            @Override
            public NewPControllerEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWPCONTROLLER_EVENT, log);
                NewPControllerEventResponse typedResponse = new NewPControllerEventResponse();
                typedResponse.log = log;
                typedResponse.oldPController = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newPController = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewPControllerEventResponse> newPControllerEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWPCONTROLLER_EVENT));
        return newPControllerEventFlowable(filter);
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

    public List<NewTokenReserveFactorEventResponse> getNewTokenReserveFactorEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWTOKENRESERVEFACTOR_EVENT, transactionReceipt);
        ArrayList<NewTokenReserveFactorEventResponse> responses = new ArrayList<NewTokenReserveFactorEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewTokenReserveFactorEventResponse typedResponse = new NewTokenReserveFactorEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldReserveFactorMantissa = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newReserveFactorMantissa = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewTokenReserveFactorEventResponse> newTokenReserveFactorEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewTokenReserveFactorEventResponse>() {
            @Override
            public NewTokenReserveFactorEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWTOKENRESERVEFACTOR_EVENT, log);
                NewTokenReserveFactorEventResponse typedResponse = new NewTokenReserveFactorEventResponse();
                typedResponse.log = log;
                typedResponse.oldReserveFactorMantissa = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newReserveFactorMantissa = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewTokenReserveFactorEventResponse> newTokenReserveFactorEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWTOKENRESERVEFACTOR_EVENT));
        return newTokenReserveFactorEventFlowable(filter);
    }

    public List<RedeemTokenEventResponse> getRedeemTokenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(REDEEMTOKEN_EVENT, transactionReceipt);
        ArrayList<RedeemTokenEventResponse> responses = new ArrayList<RedeemTokenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RedeemTokenEventResponse typedResponse = new RedeemTokenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.redeemer = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.redeemAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.redeemTokens = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RedeemTokenEventResponse> redeemTokenEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RedeemTokenEventResponse>() {
            @Override
            public RedeemTokenEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(REDEEMTOKEN_EVENT, log);
                RedeemTokenEventResponse typedResponse = new RedeemTokenEventResponse();
                typedResponse.log = log;
                typedResponse.redeemer = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.redeemAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.redeemTokens = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RedeemTokenEventResponse> redeemTokenEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REDEEMTOKEN_EVENT));
        return redeemTokenEventFlowable(filter);
    }

    public List<RepayBorrowTokenEventResponse> getRepayBorrowTokenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(REPAYBORROWTOKEN_EVENT, transactionReceipt);
        ArrayList<RepayBorrowTokenEventResponse> responses = new ArrayList<RepayBorrowTokenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RepayBorrowTokenEventResponse typedResponse = new RepayBorrowTokenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.payer = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.borrower = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.repayAmount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.accountBorrows = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.totalBorrows = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RepayBorrowTokenEventResponse> repayBorrowTokenEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RepayBorrowTokenEventResponse>() {
            @Override
            public RepayBorrowTokenEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(REPAYBORROWTOKEN_EVENT, log);
                RepayBorrowTokenEventResponse typedResponse = new RepayBorrowTokenEventResponse();
                typedResponse.log = log;
                typedResponse.payer = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.borrower = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.repayAmount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.accountBorrows = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.totalBorrows = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RepayBorrowTokenEventResponse> repayBorrowTokenEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REPAYBORROWTOKEN_EVENT));
        return repayBorrowTokenEventFlowable(filter);
    }

    public List<ReservesAddedEventResponse> getReservesAddedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(RESERVESADDED_EVENT, transactionReceipt);
        ArrayList<ReservesAddedEventResponse> responses = new ArrayList<ReservesAddedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ReservesAddedEventResponse typedResponse = new ReservesAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.benefactor = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.addAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.newTotalReserves = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ReservesAddedEventResponse> reservesAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ReservesAddedEventResponse>() {
            @Override
            public ReservesAddedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(RESERVESADDED_EVENT, log);
                ReservesAddedEventResponse typedResponse = new ReservesAddedEventResponse();
                typedResponse.log = log;
                typedResponse.benefactor = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.addAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.newTotalReserves = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ReservesAddedEventResponse> reservesAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(RESERVESADDED_EVENT));
        return reservesAddedEventFlowable(filter);
    }

    public List<ReservesReducedEventResponse> getReservesReducedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(RESERVESREDUCED_EVENT, transactionReceipt);
        ArrayList<ReservesReducedEventResponse> responses = new ArrayList<ReservesReducedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ReservesReducedEventResponse typedResponse = new ReservesReducedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.admin = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.reduceAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.newTotalReserves = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ReservesReducedEventResponse> reservesReducedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ReservesReducedEventResponse>() {
            @Override
            public ReservesReducedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(RESERVESREDUCED_EVENT, log);
                ReservesReducedEventResponse typedResponse = new ReservesReducedEventResponse();
                typedResponse.log = log;
                typedResponse.admin = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.reduceAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.newTotalReserves = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ReservesReducedEventResponse> reservesReducedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(RESERVESREDUCED_EVENT));
        return reservesReducedEventFlowable(filter);
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

    public RemoteFunctionCall<TransactionReceipt> _changeEPNClaimStatus() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__CHANGEEPNCLAIMSTATUS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _reduceReserves(BigInteger reduceAmount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__REDUCERESERVES, 
                Arrays.<Type>asList(new Uint256(reduceAmount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setEPN(String newEPNToken) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETEPN, 
                Arrays.<Type>asList(new Address(160, newEPNToken)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setInterestRateModel(String newInterestRateModel) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETINTERESTRATEMODEL, 
                Arrays.<Type>asList(new Address(160, newInterestRateModel)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setMinimumClaimValue(BigInteger newMinimumClaimValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETMINIMUMCLAIMVALUE, 
                Arrays.<Type>asList(new Uint256(newMinimumClaimValue)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> _setPController(String newPController) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETPCONTROLLER, 
                Arrays.<Type>asList(new Address(160, newPController)),
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

    public RemoteFunctionCall<TransactionReceipt> _setReserveFactor(BigInteger newReserveFactorMantissa) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__SETRESERVEFACTOR, 
                Arrays.<Type>asList(new Uint256(newReserveFactorMantissa)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> accrualBlockNumber() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ACCRUALBLOCKNUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> accrueInterest() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ACCRUEINTEREST, 
                Arrays.<Type>asList(), 
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

    public RemoteFunctionCall<TransactionReceipt> balanceOfUnderlying(String owner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BALANCEOFUNDERLYING, 
                Arrays.<Type>asList(new Address(160, owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> borrow(BigInteger borrowAmount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BORROW, 
                Arrays.<Type>asList(new Uint256(borrowAmount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> borrowBalanceCurrent(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BORROWBALANCECURRENT, 
                Arrays.<Type>asList(new Address(160, account)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> borrowBalanceStored(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BORROWBALANCESTORED, 
                Arrays.<Type>asList(new Address(160, account)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> borrowIndex() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BORROWINDEX, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> borrowRatePerBlock() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BORROWRATEPERBLOCK, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> claimEPN(BigInteger id, String owner, BigInteger phoneNumber) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CLAIMEPN, 
                Arrays.<Type>asList(new Uint256(id),
                new Address(160, owner),
                new Uint256(phoneNumber)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> claimEPNwithENS(BigInteger id, String owner, BigInteger phoneNumber, String ensName) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CLAIMEPNWITHENS, 
                Arrays.<Type>asList(new Uint256(id),
                new Address(160, owner),
                new Uint256(phoneNumber),
                new Utf8String(ensName)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> epnToken() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_EPNTOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> exchangeRateCurrent() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_EXCHANGERATECURRENT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> exchangeRateStored() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_EXCHANGERATESTORED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>> getAccountSnapshot(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETACCOUNTSNAPSHOT, 
                Arrays.<Type>asList(new Address(160, account)),
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

    public RemoteFunctionCall<BigInteger> getCash() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETCASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> initialize(String pController_, String interestRateModel_, BigInteger initialExchangeRateMantissa_, String name_, String symbol_, BigInteger decimals_) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INITIALIZE, 
                Arrays.<Type>asList(new Address(160, pController_),
                new Address(160, interestRateModel_),
                new Uint256(initialExchangeRateMantissa_),
                new Utf8String(name_),
                new Utf8String(symbol_),
                new Uint8(decimals_)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> interestRateModel() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_INTERESTRATEMODEL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isEPNClaimEnabled() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISEPNCLAIMENABLED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isPToken() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISPTOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> liquidateBorrow(String borrower, String cTokenCollateral, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_LIQUIDATEBORROW, 
                Arrays.<Type>asList(new Address(160, borrower),
                new Address(160, cTokenCollateral)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<BigInteger> minimumClaimValue() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_MINIMUMCLAIMVALUE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> mint(BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_MINT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<String> name() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> pController() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PCONTROLLER, 
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

    public RemoteFunctionCall<TransactionReceipt> redeem(BigInteger redeemTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REDEEM, 
                Arrays.<Type>asList(new Uint256(redeemTokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> redeemUnderlying(BigInteger redeemAmount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REDEEMUNDERLYING, 
                Arrays.<Type>asList(new Uint256(redeemAmount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> repayBorrow(BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REPAYBORROW, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> repayBorrowBehalf(String borrower, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REPAYBORROWBEHALF, 
                Arrays.<Type>asList(new Address(160, borrower)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<BigInteger> reserveFactorMantissa() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_RESERVEFACTORMANTISSA, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> seize(String liquidator, String borrower, BigInteger seizeTokens) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SEIZE, 
                Arrays.<Type>asList(new Address(160, liquidator),
                new Address(160, borrower),
                new Uint256(seizeTokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> supplyRatePerBlock() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SUPPLYRATEPERBLOCK, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> symbol() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalBorrows() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALBORROWS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> totalBorrowsCurrent() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TOTALBORROWSCURRENT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> totalReserves() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALRESERVES, 
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
    public static PEth load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PEth(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PEth load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PEth(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PEth load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new PEth(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PEth load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PEth(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PEth> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String pController_, String interestRateModel_, BigInteger initialExchangeRateMantissa_, String name_, String symbol_, BigInteger decimals_, String admin_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, pController_),
                new Address(160, interestRateModel_),
                new Uint256(initialExchangeRateMantissa_),
                new Utf8String(name_),
                new Utf8String(symbol_),
                new Uint8(decimals_),
                new Address(160, admin_)));
        return deployRemoteCall(PEth.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<PEth> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String pController_, String interestRateModel_, BigInteger initialExchangeRateMantissa_, String name_, String symbol_, BigInteger decimals_, String admin_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, pController_),
                new Address(160, interestRateModel_),
                new Uint256(initialExchangeRateMantissa_),
                new Utf8String(name_),
                new Utf8String(symbol_),
                new Uint8(decimals_),
                new Address(160, admin_)));
        return deployRemoteCall(PEth.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<PEth> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String pController_, String interestRateModel_, BigInteger initialExchangeRateMantissa_, String name_, String symbol_, BigInteger decimals_, String admin_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, pController_),
                new Address(160, interestRateModel_),
                new Uint256(initialExchangeRateMantissa_),
                new Utf8String(name_),
                new Utf8String(symbol_),
                new Uint8(decimals_),
                new Address(160, admin_)));
        return deployRemoteCall(PEth.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<PEth> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String pController_, String interestRateModel_, BigInteger initialExchangeRateMantissa_, String name_, String symbol_, BigInteger decimals_, String admin_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, pController_),
                new Address(160, interestRateModel_),
                new Uint256(initialExchangeRateMantissa_),
                new Utf8String(name_),
                new Utf8String(symbol_),
                new Uint8(decimals_),
                new Address(160, admin_)));
        return deployRemoteCall(PEth.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class AccrueInterestTokenEventResponse extends BaseEventResponse {
        public BigInteger cashPrior;

        public BigInteger interestAccumulated;

        public BigInteger borrowIndex;

        public BigInteger totalBorrows;
    }

    public static class AllowedTestEventEventResponse extends BaseEventResponse {
        public String user;

        public String pToken;

        public BigInteger errorCode;
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String spender;

        public BigInteger amount;
    }

    public static class BorrowTokenEventResponse extends BaseEventResponse {
        public String borrower;

        public BigInteger borrowAmount;

        public BigInteger accountBorrows;

        public BigInteger totalBorrows;
    }

    public static class FailureEventResponse extends BaseEventResponse {
        public BigInteger error;

        public BigInteger info;

        public BigInteger detail;
    }

    public static class LiquidateBorrowTokenEventResponse extends BaseEventResponse {
        public String liquidator;

        public String borrower;

        public BigInteger repayAmount;

        public String cTokenCollateral;

        public BigInteger seizeTokens;
    }

    public static class MintTokenEventResponse extends BaseEventResponse {
        public String minter;

        public BigInteger mintAmount;

        public BigInteger mintTokens;
    }

    public static class NewAdminEventResponse extends BaseEventResponse {
        public String oldAdmin;

        public String newAdmin;
    }

    public static class NewEPNTokenEventResponse extends BaseEventResponse {
        public String oldEPNToken;

        public String newEPNToken;
    }

    public static class NewIsEPNClaimStatusEventResponse extends BaseEventResponse {
        public Boolean oldStatus;

        public Boolean newStatus;
    }

    public static class NewMarketTokenInterestRateModelEventResponse extends BaseEventResponse {
        public String oldInterestRateModel;

        public String newInterestRateModel;
    }

    public static class NewMinimumClaimValueEventResponse extends BaseEventResponse {
        public BigInteger oldMinimumClaimValue;

        public BigInteger newMinimumClaimValue;
    }

    public static class NewPControllerEventResponse extends BaseEventResponse {
        public String oldPController;

        public String newPController;
    }

    public static class NewPendingAdminEventResponse extends BaseEventResponse {
        public String oldPendingAdmin;

        public String newPendingAdmin;
    }

    public static class NewTokenReserveFactorEventResponse extends BaseEventResponse {
        public BigInteger oldReserveFactorMantissa;

        public BigInteger newReserveFactorMantissa;
    }

    public static class RedeemTokenEventResponse extends BaseEventResponse {
        public String redeemer;

        public BigInteger redeemAmount;

        public BigInteger redeemTokens;
    }

    public static class RepayBorrowTokenEventResponse extends BaseEventResponse {
        public String payer;

        public String borrower;

        public BigInteger repayAmount;

        public BigInteger accountBorrows;

        public BigInteger totalBorrows;
    }

    public static class ReservesAddedEventResponse extends BaseEventResponse {
        public String benefactor;

        public BigInteger addAmount;

        public BigInteger newTotalReserves;
    }

    public static class ReservesReducedEventResponse extends BaseEventResponse {
        public String admin;

        public BigInteger reduceAmount;

        public BigInteger newTotalReserves;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger amount;
    }
}
