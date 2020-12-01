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
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint64;
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
public class RecoveryManager extends Contract {
   //public static final String BINARY = "";

    public static final String FUNC_ADDMODULE = "addModule";

    public static final String FUNC_CANCELRECOVERY = "cancelRecovery";

    public static final String FUNC_EXECUTE = "execute";

    public static final String FUNC_EXECUTERECOVERY = "executeRecovery";

    public static final String FUNC_FINALIZERECOVERY = "finalizeRecovery";

    public static final String FUNC_GETNONCE = "getNonce";

    public static final String FUNC_GETRECOVERY = "getRecovery";

    public static final String FUNC_GETREQUIREDSIGNATURES = "getRequiredSignatures";

    public static final String FUNC_GUARDIANSTORAGE = "guardianStorage";

    public static final String FUNC_INIT = "init";

    public static final String FUNC_LOCKPERIOD = "lockPeriod";

    public static final String FUNC_RECOVERTOKEN = "recoverToken";

    public static final String FUNC_RECOVERYPERIOD = "recoveryPeriod";

    public static final String FUNC_RELAYER = "relayer";

    public static final String FUNC_SECURITYPERIOD = "securityPeriod";

    public static final String FUNC_SECURITYWINDOW = "securityWindow";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event MODULECREATED_EVENT = new Event("ModuleCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
    ;

    public static final Event MODULEINITIALISED_EVENT = new Event("ModuleInitialised", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERED_EVENT = new Event("OwnershipTransfered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event RECOVERYCANCELED_EVENT = new Event("RecoveryCanceled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event RECOVERYEXECUTED_EVENT = new Event("RecoveryExecuted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint64>() {}));
    ;

    public static final Event RECOVERYFINALIZED_EVENT = new Event("RecoveryFinalized", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event TRANSACTIONEXECUTED_EVENT = new Event("TransactionExecuted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Bool>(true) {}, new TypeReference<Bytes32>() {}));
    ;

    @Deprecated
    protected RecoveryManager(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Context context) {
        super(context.getString(R.string.recovery_abi), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected RecoveryManager(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, Context context) {
        super(context.getString(R.string.recovery_abi), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected RecoveryManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Context context) {
        super(context.getString(R.string.recovery_abi), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected RecoveryManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, Context context) {
        super(context.getString(R.string.recovery_abi), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ModuleCreatedEventResponse> getModuleCreatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MODULECREATED_EVENT, transactionReceipt);
        ArrayList<ModuleCreatedEventResponse> responses = new ArrayList<ModuleCreatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ModuleCreatedEventResponse typedResponse = new ModuleCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.name = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ModuleCreatedEventResponse> moduleCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ModuleCreatedEventResponse>() {
            @Override
            public ModuleCreatedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MODULECREATED_EVENT, log);
                ModuleCreatedEventResponse typedResponse = new ModuleCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.name = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ModuleCreatedEventResponse> moduleCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MODULECREATED_EVENT));
        return moduleCreatedEventFlowable(filter);
    }

    public List<ModuleInitialisedEventResponse> getModuleInitialisedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MODULEINITIALISED_EVENT, transactionReceipt);
        ArrayList<ModuleInitialisedEventResponse> responses = new ArrayList<ModuleInitialisedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ModuleInitialisedEventResponse typedResponse = new ModuleInitialisedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ModuleInitialisedEventResponse> moduleInitialisedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ModuleInitialisedEventResponse>() {
            @Override
            public ModuleInitialisedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MODULEINITIALISED_EVENT, log);
                ModuleInitialisedEventResponse typedResponse = new ModuleInitialisedEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ModuleInitialisedEventResponse> moduleInitialisedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MODULEINITIALISED_EVENT));
        return moduleInitialisedEventFlowable(filter);
    }

    public List<OwnershipTransferedEventResponse> getOwnershipTransferedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferedEventResponse> responses = new ArrayList<OwnershipTransferedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OwnershipTransferedEventResponse typedResponse = new OwnershipTransferedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferedEventResponse> ownershipTransferedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferedEventResponse>() {
            @Override
            public OwnershipTransferedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERED_EVENT, log);
                OwnershipTransferedEventResponse typedResponse = new OwnershipTransferedEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferedEventResponse> ownershipTransferedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERED_EVENT));
        return ownershipTransferedEventFlowable(filter);
    }

    public List<RecoveryCanceledEventResponse> getRecoveryCanceledEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(RECOVERYCANCELED_EVENT, transactionReceipt);
        ArrayList<RecoveryCanceledEventResponse> responses = new ArrayList<RecoveryCanceledEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RecoveryCanceledEventResponse typedResponse = new RecoveryCanceledEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._recovery = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RecoveryCanceledEventResponse> recoveryCanceledEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RecoveryCanceledEventResponse>() {
            @Override
            public RecoveryCanceledEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(RECOVERYCANCELED_EVENT, log);
                RecoveryCanceledEventResponse typedResponse = new RecoveryCanceledEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._recovery = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RecoveryCanceledEventResponse> recoveryCanceledEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(RECOVERYCANCELED_EVENT));
        return recoveryCanceledEventFlowable(filter);
    }

    public List<RecoveryExecutedEventResponse> getRecoveryExecutedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(RECOVERYEXECUTED_EVENT, transactionReceipt);
        ArrayList<RecoveryExecutedEventResponse> responses = new ArrayList<RecoveryExecutedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RecoveryExecutedEventResponse typedResponse = new RecoveryExecutedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._recovery = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.executeAfter = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RecoveryExecutedEventResponse> recoveryExecutedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RecoveryExecutedEventResponse>() {
            @Override
            public RecoveryExecutedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(RECOVERYEXECUTED_EVENT, log);
                RecoveryExecutedEventResponse typedResponse = new RecoveryExecutedEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._recovery = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.executeAfter = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RecoveryExecutedEventResponse> recoveryExecutedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(RECOVERYEXECUTED_EVENT));
        return recoveryExecutedEventFlowable(filter);
    }

    public List<RecoveryFinalizedEventResponse> getRecoveryFinalizedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(RECOVERYFINALIZED_EVENT, transactionReceipt);
        ArrayList<RecoveryFinalizedEventResponse> responses = new ArrayList<RecoveryFinalizedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RecoveryFinalizedEventResponse typedResponse = new RecoveryFinalizedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._recovery = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RecoveryFinalizedEventResponse> recoveryFinalizedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RecoveryFinalizedEventResponse>() {
            @Override
            public RecoveryFinalizedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(RECOVERYFINALIZED_EVENT, log);
                RecoveryFinalizedEventResponse typedResponse = new RecoveryFinalizedEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._recovery = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RecoveryFinalizedEventResponse> recoveryFinalizedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(RECOVERYFINALIZED_EVENT));
        return recoveryFinalizedEventFlowable(filter);
    }

    public List<TransactionExecutedEventResponse> getTransactionExecutedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSACTIONEXECUTED_EVENT, transactionReceipt);
        ArrayList<TransactionExecutedEventResponse> responses = new ArrayList<TransactionExecutedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransactionExecutedEventResponse typedResponse = new TransactionExecutedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.success = (Boolean) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.signedHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransactionExecutedEventResponse> transactionExecutedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransactionExecutedEventResponse>() {
            @Override
            public TransactionExecutedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSACTIONEXECUTED_EVENT, log);
                TransactionExecutedEventResponse typedResponse = new TransactionExecutedEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.success = (Boolean) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.signedHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransactionExecutedEventResponse> transactionExecutedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSACTIONEXECUTED_EVENT));
        return transactionExecutedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addModule(String _wallet, String _module) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDMODULE, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new Address(160, _module)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> cancelRecovery(String _wallet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CANCELRECOVERY, 
                Arrays.<Type>asList(new Address(160, _wallet)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> execute(String _wallet, byte[] _data, BigInteger _nonce, byte[] _signatures, BigInteger _gasPrice, BigInteger _gasLimit) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_EXECUTE, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new org.web3j.abi.datatypes.DynamicBytes(_data), 
                new Uint256(_nonce),
                new org.web3j.abi.datatypes.DynamicBytes(_signatures), 
                new Uint256(_gasPrice),
                new Uint256(_gasLimit)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> executeRecovery(String _wallet, String _recovery) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_EXECUTERECOVERY, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new Address(160, _recovery)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> finalizeRecovery(String _wallet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_FINALIZERECOVERY, 
                Arrays.<Type>asList(new Address(160, _wallet)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getNonce(String _wallet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETNONCE, 
                Arrays.<Type>asList(new Address(160, _wallet)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>> getRecovery(String _wallet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETRECOVERY, 
                Arrays.<Type>asList(new Address(160, _wallet)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint64>() {}, new TypeReference<Uint32>() {}));
        return new RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>>(function,
                new Callable<Tuple3<String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple3<String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getRequiredSignatures(String _wallet, byte[] _data) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETREQUIREDSIGNATURES, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new org.web3j.abi.datatypes.DynamicBytes(_data)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> guardianStorage() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GUARDIANSTORAGE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> init(String _wallet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INIT, 
                Arrays.<Type>asList(new Address(160, _wallet)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> lockPeriod() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_LOCKPERIOD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> recoverToken(String _token) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RECOVERTOKEN, 
                Arrays.<Type>asList(new Address(160, _token)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> recoveryPeriod() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_RECOVERYPERIOD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> relayer(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_RELAYER, 
                Arrays.<Type>asList(new Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> securityPeriod() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SECURITYPERIOD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> securityWindow() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SECURITYWINDOW, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String _wallet, String _newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new Address(160, _newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static RecoveryManager load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Context context) {
        return new RecoveryManager(contractAddress, web3j, credentials, gasPrice, gasLimit, context);
    }

    @Deprecated
    public static RecoveryManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Context context) {
        return new RecoveryManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit, context);
    }

    public static RecoveryManager load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, Context context) {
        return new RecoveryManager(contractAddress, web3j, credentials, contractGasProvider, context);
    }

    public static RecoveryManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, Context context) {
        return new RecoveryManager(contractAddress, web3j, transactionManager, contractGasProvider, context);
    }

    public static RemoteCall<RecoveryManager> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _registry, String _guardianStorage, BigInteger _recoveryPeriod, BigInteger _lockPeriod, BigInteger _securityPeriod, BigInteger _securityWindow, Context context) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, _registry),
                new Address(160, _guardianStorage),
                new Uint256(_recoveryPeriod),
                new Uint256(_lockPeriod),
                new Uint256(_securityPeriod),
                new Uint256(_securityWindow)));
        return deployRemoteCall(RecoveryManager.class, web3j, credentials, contractGasProvider, context.getString(R.string.recovery_abi), encodedConstructor);
    }

    public static RemoteCall<RecoveryManager> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _registry, String _guardianStorage, BigInteger _recoveryPeriod, BigInteger _lockPeriod, BigInteger _securityPeriod, BigInteger _securityWindow, Context context) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, _registry),
                new Address(160, _guardianStorage),
                new Uint256(_recoveryPeriod),
                new Uint256(_lockPeriod),
                new Uint256(_securityPeriod),
                new Uint256(_securityWindow)));
        return deployRemoteCall(RecoveryManager.class, web3j, transactionManager, contractGasProvider, context.getString(R.string.recovery_abi), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<RecoveryManager> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _registry, String _guardianStorage, BigInteger _recoveryPeriod, BigInteger _lockPeriod, BigInteger _securityPeriod, BigInteger _securityWindow, Context context) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, _registry),
                new Address(160, _guardianStorage),
                new Uint256(_recoveryPeriod),
                new Uint256(_lockPeriod),
                new Uint256(_securityPeriod),
                new Uint256(_securityWindow)));
        return deployRemoteCall(RecoveryManager.class, web3j, credentials, gasPrice, gasLimit, context.getString(R.string.recovery_abi), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<RecoveryManager> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _registry, String _guardianStorage, BigInteger _recoveryPeriod, BigInteger _lockPeriod, BigInteger _securityPeriod, BigInteger _securityWindow, Context context) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, _registry),
                new Address(160, _guardianStorage),
                new Uint256(_recoveryPeriod),
                new Uint256(_lockPeriod),
                new Uint256(_securityPeriod),
                new Uint256(_securityWindow)));
        return deployRemoteCall(RecoveryManager.class, web3j, transactionManager, gasPrice, gasLimit, context.getString(R.string.recovery_abi), encodedConstructor);
    }

    public static class ModuleCreatedEventResponse extends BaseEventResponse {
        public byte[] name;
    }

    public static class ModuleInitialisedEventResponse extends BaseEventResponse {
        public String wallet;
    }

    public static class OwnershipTransferedEventResponse extends BaseEventResponse {
        public String wallet;

        public String _newOwner;
    }

    public static class RecoveryCanceledEventResponse extends BaseEventResponse {
        public String wallet;

        public String _recovery;
    }

    public static class RecoveryExecutedEventResponse extends BaseEventResponse {
        public String wallet;

        public String _recovery;

        public BigInteger executeAfter;
    }

    public static class RecoveryFinalizedEventResponse extends BaseEventResponse {
        public String wallet;

        public String _recovery;
    }

    public static class TransactionExecutedEventResponse extends BaseEventResponse {
        public String wallet;

        public Boolean success;

        public byte[] signedHash;
    }
}
