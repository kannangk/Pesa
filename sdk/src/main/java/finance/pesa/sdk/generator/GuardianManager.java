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
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
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
public class GuardianManager extends Contract {
   // public static final String BINARY = "";

    public static final String FUNC_ADDGUARDIAN = "addGuardian";

    public static final String FUNC_ADDMODULE = "addModule";

    public static final String FUNC_CANCELGUARDIANADDITION = "cancelGuardianAddition";

    public static final String FUNC_CANCELGUARDIANREVOKATION = "cancelGuardianRevokation";

    public static final String FUNC_CONFIRMGUARDIANADDITION = "confirmGuardianAddition";

    public static final String FUNC_CONFIRMGUARDIANREVOKATION = "confirmGuardianRevokation";

    public static final String FUNC_EXECUTE = "execute";

    public static final String FUNC_GETNONCE = "getNonce";

    public static final String FUNC_GUARDIANCOUNT = "guardianCount";

    public static final String FUNC_INIT = "init";

    public static final String FUNC_ISGUARDIAN = "isGuardian";

    public static final String FUNC_RECOVERTOKEN = "recoverToken";

    public static final String FUNC_RELAYER = "relayer";

    public static final String FUNC_REVOKEGUARDIAN = "revokeGuardian";

    public static final String FUNC_SECURITYPERIOD = "securityPeriod";

    public static final String FUNC_SECURITYWINDOW = "securityWindow";

    public static final Event GUARDIANADDED_EVENT = new Event("GuardianAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event GUARDIANADDITIONCANCELLED_EVENT = new Event("GuardianAdditionCancelled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event GUARDIANADDITIONREQUESTED_EVENT = new Event("GuardianAdditionRequested", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event GUARDIANREVOKATIONCANCELLED_EVENT = new Event("GuardianRevokationCancelled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event GUARDIANREVOKATIONREQUESTED_EVENT = new Event("GuardianRevokationRequested", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event GUARDIANREVOKED_EVENT = new Event("GuardianRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event MODULECREATED_EVENT = new Event("ModuleCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
    ;

    public static final Event MODULEINITIALISED_EVENT = new Event("ModuleInitialised", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event TRANSACTIONEXECUTED_EVENT = new Event("TransactionExecuted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Bool>(true) {}, new TypeReference<Bytes32>() {}));
    ;

    @Deprecated
    protected GuardianManager(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Context context) {
        super(context.getString(R.string.guardian_abi), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected GuardianManager(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, Context context) {
        super(context.getString(R.string.guardian_abi), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected GuardianManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Context context) {
        super(context.getString(R.string.guardian_abi), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected GuardianManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, Context context) {
        super(context.getString(R.string.guardian_abi), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<GuardianAddedEventResponse> getGuardianAddedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(GUARDIANADDED_EVENT, transactionReceipt);
        ArrayList<GuardianAddedEventResponse> responses = new ArrayList<GuardianAddedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            GuardianAddedEventResponse typedResponse = new GuardianAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.guardian = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<GuardianAddedEventResponse> guardianAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, GuardianAddedEventResponse>() {
            @Override
            public GuardianAddedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(GUARDIANADDED_EVENT, log);
                GuardianAddedEventResponse typedResponse = new GuardianAddedEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.guardian = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<GuardianAddedEventResponse> guardianAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GUARDIANADDED_EVENT));
        return guardianAddedEventFlowable(filter);
    }

    public List<GuardianAdditionCancelledEventResponse> getGuardianAdditionCancelledEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(GUARDIANADDITIONCANCELLED_EVENT, transactionReceipt);
        ArrayList<GuardianAdditionCancelledEventResponse> responses = new ArrayList<GuardianAdditionCancelledEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            GuardianAdditionCancelledEventResponse typedResponse = new GuardianAdditionCancelledEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.guardian = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<GuardianAdditionCancelledEventResponse> guardianAdditionCancelledEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, GuardianAdditionCancelledEventResponse>() {
            @Override
            public GuardianAdditionCancelledEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(GUARDIANADDITIONCANCELLED_EVENT, log);
                GuardianAdditionCancelledEventResponse typedResponse = new GuardianAdditionCancelledEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.guardian = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<GuardianAdditionCancelledEventResponse> guardianAdditionCancelledEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GUARDIANADDITIONCANCELLED_EVENT));
        return guardianAdditionCancelledEventFlowable(filter);
    }

    public List<GuardianAdditionRequestedEventResponse> getGuardianAdditionRequestedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(GUARDIANADDITIONREQUESTED_EVENT, transactionReceipt);
        ArrayList<GuardianAdditionRequestedEventResponse> responses = new ArrayList<GuardianAdditionRequestedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            GuardianAdditionRequestedEventResponse typedResponse = new GuardianAdditionRequestedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.guardian = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.executeAfter = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<GuardianAdditionRequestedEventResponse> guardianAdditionRequestedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, GuardianAdditionRequestedEventResponse>() {
            @Override
            public GuardianAdditionRequestedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(GUARDIANADDITIONREQUESTED_EVENT, log);
                GuardianAdditionRequestedEventResponse typedResponse = new GuardianAdditionRequestedEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.guardian = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.executeAfter = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<GuardianAdditionRequestedEventResponse> guardianAdditionRequestedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GUARDIANADDITIONREQUESTED_EVENT));
        return guardianAdditionRequestedEventFlowable(filter);
    }

    public List<GuardianRevokationCancelledEventResponse> getGuardianRevokationCancelledEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(GUARDIANREVOKATIONCANCELLED_EVENT, transactionReceipt);
        ArrayList<GuardianRevokationCancelledEventResponse> responses = new ArrayList<GuardianRevokationCancelledEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            GuardianRevokationCancelledEventResponse typedResponse = new GuardianRevokationCancelledEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.guardian = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<GuardianRevokationCancelledEventResponse> guardianRevokationCancelledEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, GuardianRevokationCancelledEventResponse>() {
            @Override
            public GuardianRevokationCancelledEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(GUARDIANREVOKATIONCANCELLED_EVENT, log);
                GuardianRevokationCancelledEventResponse typedResponse = new GuardianRevokationCancelledEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.guardian = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<GuardianRevokationCancelledEventResponse> guardianRevokationCancelledEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GUARDIANREVOKATIONCANCELLED_EVENT));
        return guardianRevokationCancelledEventFlowable(filter);
    }

    public List<GuardianRevokationRequestedEventResponse> getGuardianRevokationRequestedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(GUARDIANREVOKATIONREQUESTED_EVENT, transactionReceipt);
        ArrayList<GuardianRevokationRequestedEventResponse> responses = new ArrayList<GuardianRevokationRequestedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            GuardianRevokationRequestedEventResponse typedResponse = new GuardianRevokationRequestedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.guardian = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.executeAfter = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<GuardianRevokationRequestedEventResponse> guardianRevokationRequestedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, GuardianRevokationRequestedEventResponse>() {
            @Override
            public GuardianRevokationRequestedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(GUARDIANREVOKATIONREQUESTED_EVENT, log);
                GuardianRevokationRequestedEventResponse typedResponse = new GuardianRevokationRequestedEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.guardian = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.executeAfter = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<GuardianRevokationRequestedEventResponse> guardianRevokationRequestedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GUARDIANREVOKATIONREQUESTED_EVENT));
        return guardianRevokationRequestedEventFlowable(filter);
    }

    public List<GuardianRevokedEventResponse> getGuardianRevokedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(GUARDIANREVOKED_EVENT, transactionReceipt);
        ArrayList<GuardianRevokedEventResponse> responses = new ArrayList<GuardianRevokedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            GuardianRevokedEventResponse typedResponse = new GuardianRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.guardian = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<GuardianRevokedEventResponse> guardianRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, GuardianRevokedEventResponse>() {
            @Override
            public GuardianRevokedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(GUARDIANREVOKED_EVENT, log);
                GuardianRevokedEventResponse typedResponse = new GuardianRevokedEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.guardian = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<GuardianRevokedEventResponse> guardianRevokedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GUARDIANREVOKED_EVENT));
        return guardianRevokedEventFlowable(filter);
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

    public RemoteFunctionCall<TransactionReceipt> addGuardian(String _wallet, String _guardian) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDGUARDIAN, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new Address(160, _guardian)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addModule(String _wallet, String _module) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDMODULE, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new Address(160, _module)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> cancelGuardianAddition(String _wallet, String _guardian) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CANCELGUARDIANADDITION, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new Address(160, _guardian)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> cancelGuardianRevokation(String _wallet, String _guardian) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CANCELGUARDIANREVOKATION, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new Address(160, _guardian)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> confirmGuardianAddition(String _wallet, String _guardian) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CONFIRMGUARDIANADDITION, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new Address(160, _guardian)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> confirmGuardianRevokation(String _wallet, String _guardian) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CONFIRMGUARDIANREVOKATION, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new Address(160, _guardian)),
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

    public RemoteFunctionCall<BigInteger> getNonce(String _wallet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETNONCE, 
                Arrays.<Type>asList(new Address(160, _wallet)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> guardianCount(String _wallet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GUARDIANCOUNT, 
                Arrays.<Type>asList(new Address(160, _wallet)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> init(String _wallet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INIT, 
                Arrays.<Type>asList(new Address(160, _wallet)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> isGuardian(String _wallet, String _guardian) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISGUARDIAN, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new Address(160, _guardian)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> recoverToken(String _token) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RECOVERTOKEN, 
                Arrays.<Type>asList(new Address(160, _token)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> relayer(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_RELAYER, 
                Arrays.<Type>asList(new Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeGuardian(String _wallet, String _guardian) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REVOKEGUARDIAN, 
                Arrays.<Type>asList(new Address(160, _wallet),
                new Address(160, _guardian)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    @Deprecated
    public static GuardianManager load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Context context) {
        return new GuardianManager(contractAddress, web3j, credentials, gasPrice, gasLimit, context);
    }

    @Deprecated
    public static GuardianManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Context context) {
        return new GuardianManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit, context);
    }

    public static GuardianManager load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, Context context) {
        return new GuardianManager(contractAddress, web3j, credentials, contractGasProvider, context);
    }

    public static GuardianManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, Context context) {
        return new GuardianManager(contractAddress, web3j, transactionManager, contractGasProvider, context);
    }

    public static RemoteCall<GuardianManager> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _registry, String _guardianStorage, BigInteger _securityPeriod, BigInteger _securityWindow, Context context) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, _registry),
                new Address(160, _guardianStorage),
                new Uint256(_securityPeriod),
                new Uint256(_securityWindow)));
        return deployRemoteCall(GuardianManager.class, web3j, credentials, contractGasProvider, context.getString(R.string.guardian_abi), encodedConstructor);
    }

    public static RemoteCall<GuardianManager> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _registry, String _guardianStorage, BigInteger _securityPeriod, BigInteger _securityWindow, Context context) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, _registry),
                new Address(160, _guardianStorage),
                new Uint256(_securityPeriod),
                new Uint256(_securityWindow)));
        return deployRemoteCall(GuardianManager.class, web3j, transactionManager, contractGasProvider, context.getString(R.string.guardian_abi), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<GuardianManager> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _registry, String _guardianStorage, BigInteger _securityPeriod, BigInteger _securityWindow, Context context) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, _registry),
                new Address(160, _guardianStorage),
                new Uint256(_securityPeriod),
                new Uint256(_securityWindow)));
        return deployRemoteCall(GuardianManager.class, web3j, credentials, gasPrice, gasLimit, context.getString(R.string.guardian_abi), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<GuardianManager> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _registry, String _guardianStorage, BigInteger _securityPeriod, BigInteger _securityWindow, Context context) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, _registry),
                new Address(160, _guardianStorage),
                new Uint256(_securityPeriod),
                new Uint256(_securityWindow)));
        return deployRemoteCall(GuardianManager.class, web3j, transactionManager, gasPrice, gasLimit, context.getString(R.string.guardian_abi), encodedConstructor);
    }

    public static class GuardianAddedEventResponse extends BaseEventResponse {
        public String wallet;

        public String guardian;
    }

    public static class GuardianAdditionCancelledEventResponse extends BaseEventResponse {
        public String wallet;

        public String guardian;
    }

    public static class GuardianAdditionRequestedEventResponse extends BaseEventResponse {
        public String wallet;

        public String guardian;

        public BigInteger executeAfter;
    }

    public static class GuardianRevokationCancelledEventResponse extends BaseEventResponse {
        public String wallet;

        public String guardian;
    }

    public static class GuardianRevokationRequestedEventResponse extends BaseEventResponse {
        public String wallet;

        public String guardian;

        public BigInteger executeAfter;
    }

    public static class GuardianRevokedEventResponse extends BaseEventResponse {
        public String wallet;

        public String guardian;
    }

    public static class ModuleCreatedEventResponse extends BaseEventResponse {
        public byte[] name;
    }

    public static class ModuleInitialisedEventResponse extends BaseEventResponse {
        public String wallet;
    }

    public static class TransactionExecutedEventResponse extends BaseEventResponse {
        public String wallet;

        public Boolean success;

        public byte[] signedHash;
    }
}
