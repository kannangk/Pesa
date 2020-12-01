package finance.pesa.sdk.view.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import finance.pesa.sdk.Model.MarketData;
import finance.pesa.sdk.utils.SupplyListener;
import finance.pesa.sdk.utils.UserInterface;
import finance.pesa.sdk.utils.WithdrawListener;
import finance.pesa.sdk.view.Interface.EnableSupplyListener;
import finance.pesa.sdk.Model.MarketData;
import finance.pesa.sdk.R;
import finance.pesa.sdk.utils.Constants;
import finance.pesa.sdk.utils.SupplyListener;
import finance.pesa.sdk.utils.UserInterface;
import finance.pesa.sdk.utils.WithdrawListener;
import finance.pesa.sdk.view.Interface.EnableSupplyListener;

public class MyPageAdapter extends PagerAdapter {

    private Context mContext;
    private int resId = 0;
    private EnableSupplyListener enableAllowance;
    private SupplyListener supplyListener;
    private WithdrawListener withdrawListener;
    private Boolean isSupplyEnable;
    private String address;
    private String marketAddress;
    private String coinBalance;
    private MarketData supplyData;
    private Double totalBorrowedBalance = 0.0;
    private Double totalBorrowLimitValue = 0.0;
    private Double collateralSuppliedBalance = 0.0;

    public MyPageAdapter(Context context, MarketData supplyData, EnableSupplyListener enableAllowance, SupplyListener supplyListener, WithdrawListener withdrawListener, Boolean isSupplyEnable, String address, String marketAddress, String coinBalance, Double totalBorrowedBalance, Double totalBorrowLimitValue, Double collateralSuppliedBalance) {
        this.mContext = context;
        this.enableAllowance = enableAllowance;
        this.supplyListener = supplyListener;
        this.withdrawListener = withdrawListener;
        this.isSupplyEnable = isSupplyEnable;
        this.address = address;
        this.marketAddress = marketAddress;
        this.coinBalance = coinBalance;
        this.supplyData = supplyData;
        this.totalBorrowedBalance = totalBorrowedBalance;
        this.totalBorrowLimitValue = totalBorrowLimitValue;
        this.collateralSuppliedBalance = collateralSuppliedBalance;
    }


    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = null;
        switch (position) {
            case 0:
                resId = R.layout.supply_part;
                layout = (ViewGroup) inflater.inflate(resId, collection, false);
                RelativeLayout enabledView = layout.findViewById(R.id.enabled_view);
                LinearLayout activeView = layout.findViewById(R.id.active_view);
                TextView btnSupply = layout.findViewById(R.id.btn_supply);
                TextView btnEnable = layout.findViewById(R.id.btn_enable);
                TextView btnMax = layout.findViewById(R.id.btn_max);
                EditText supplyToken = layout.findViewById(R.id.supply_token);
                TextView availability = layout.findViewById(R.id.availability);
                TextView token_available = layout.findViewById(R.id.token_available);
                TextView supply_per = layout.findViewById(R.id.supply_per);
                TextView distribution_per = layout.findViewById(R.id.distribution_per);
                TextView type = layout.findViewById(R.id.type);
                ImageView symbolIcon = layout.findViewById(R.id.symbol_icon);
                ImageView symbolIconApy = layout.findViewById(R.id.symbol_icon_apy);
                ProgressBar borrowLimitPbSupply = layout.findViewById(R.id.borrow_limit_pb);
                TextView maxBorrowLimitSupply = layout.findViewById(R.id.max_borrow_limit);
                TextView changeMaxBorrowLimitSupply = layout.findViewById(R.id.change_max_borrow_limit);
                TextView maxBorrowLimitPerSupply = layout.findViewById(R.id.max_borrow_limit_per);
                TextView changeMaxBorrowLimitPerSupply = layout.findViewById(R.id.change_max_borrow_limit_per);
                TableRow borrowLimitTableSupply = layout.findViewById(R.id.borrow_limit);
                TableRow borrowLimitPerTableSupply = layout.findViewById(R.id.borrow_limit_per);
                try {
                    symbolIcon.setImageDrawable(mContext.getDrawable(UserInterface.Companion.getCoinIcon(supplyData.getMarkets().getUnderlyingSymbol())));
                    symbolIconApy.setImageDrawable(mContext.getDrawable(UserInterface.Companion.getCoinIcon(supplyData.getMarkets().getUnderlyingSymbol())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    borrowLimitTableSupply.setVisibility(View.GONE);
                    borrowLimitPerTableSupply.setVisibility(View.GONE);
                    maxBorrowLimitSupply.setText("$" + UserInterface.Companion.round(totalBorrowLimitValue, 2));
                    changeMaxBorrowLimitSupply.setText("$" + UserInterface.Companion.round(totalBorrowLimitValue, 2));
                    Double usedBorrowLimitValue = 0.0;
                    if (totalBorrowLimitValue != 0.0)
                        usedBorrowLimitValue = (totalBorrowedBalance / totalBorrowLimitValue) * 100;
                    maxBorrowLimitPerSupply.setText(UserInterface.Companion.round(usedBorrowLimitValue, 2) + "%");
                    changeMaxBorrowLimitPerSupply.setText(UserInterface.Companion.round(usedBorrowLimitValue, 2) + "%");
                    borrowLimitPbSupply.setProgress((int) Math.round(usedBorrowLimitValue), true);
                    type.setText(supplyData.getMarkets().getUnderlyingSymbol());
                    availability.setText(supplyData.getMarkets().getUnderlyingSymbol() + " available");
                    supply_per.setText(UserInterface.Companion.round((supplyData.getMarkets().getSupplyRate() * 100), 2) + "%");
                    distribution_per.setText(UserInterface.Companion.round((supplyData.getMarkets().getDistributionSupplyApy()), 2) + "%");
                    Double tokenVaues = Double.parseDouble(coinBalance) * supplyData.getMarkets().getUnderlyingPriceUSD();
                    token_available.setText(coinBalance + "=$" + UserInterface.Companion.round(tokenVaues));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                btnMax.setOnClickListener(v -> {
                    supplyToken.setText("" + UserInterface.Companion.roundWallet(Double.parseDouble(coinBalance)));
                    supplyToken.setSelection(supplyToken.getText().length());
                });
                supplyToken.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        btnSupply.setEnabled(false);
                        btnSupply.setClickable(false);
                        borrowLimitTableSupply.setVisibility(View.GONE);
                        borrowLimitPerTableSupply.setVisibility(View.GONE);
                        btnSupply.setText("Supply");
                        try {
                            Double supplyToken = 0.0;
                            try {
                                supplyToken = Double.parseDouble(s.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Double totalBalance = Double.parseDouble(coinBalance);
                            if (supplyToken == 0) {
                                btnSupply.setEnabled(false);
                                btnSupply.setClickable(false);
                            } else if (totalBalance >= supplyToken) {
                                btnSupply.setEnabled(true);
                                btnSupply.setClickable(true);
                                borrowLimitTableSupply.setVisibility(View.VISIBLE);
                                borrowLimitPerTableSupply.setVisibility(View.VISIBLE);
                            } else {
                                btnSupply.setText("No funds available");
                            }
                            if (supplyData.getMarkets().getEnteredMarket()) {
                                Double tokenValues = supplyToken * supplyData.getMarkets().getUnderlyingPriceUSD();
                                Double maxBorrow = totalBorrowLimitValue + (tokenValues * supplyData.getMarkets().getCollateralFactor());
                                changeMaxBorrowLimitSupply.setText("$" + UserInterface.Companion.round(maxBorrow, 2));
                                Double usedBorrowLimitValue = 0.0;
                                if (maxBorrow != 0.0)
                                    usedBorrowLimitValue = (totalBorrowedBalance / maxBorrow) * 100;
                                changeMaxBorrowLimitPerSupply.setText(UserInterface.Companion.round(usedBorrowLimitValue, 2) + "%");
                                borrowLimitPbSupply.setProgress((int) Math.round(usedBorrowLimitValue), true);
                            } else {
                                changeMaxBorrowLimitPerSupply.setText(maxBorrowLimitPerSupply.getText().toString());
                                changeMaxBorrowLimitSupply.setText(maxBorrowLimitSupply.getText().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                if (isSupplyEnable) {
                    enabledView.setVisibility(View.VISIBLE);
                    btnSupply.setVisibility(View.VISIBLE);
                    activeView.setVisibility(View.VISIBLE);
                    btnEnable.setVisibility(View.GONE);
                } else {
                    enabledView.setVisibility(View.GONE);
                    btnSupply.setVisibility(View.GONE);
                    activeView.setVisibility(View.GONE);
                    btnEnable.setVisibility(View.VISIBLE);
                }
                btnSupply.setOnClickListener(v -> {
                    supplyListener.supplyTokens(supplyData, supplyToken.getText().toString());
                });
                btnEnable.setOnClickListener(v -> enableAllowance.onClickedEnable(address, marketAddress,supplyData));
                break;
            case 1:
                resId = R.layout.withdraw_part;
                layout = (ViewGroup) inflater.inflate(resId, collection, false);
                RelativeLayout enabledViewWithdraw = layout.findViewById(R.id.enabled_view);
                LinearLayout activeViewWithdraw = layout.findViewById(R.id.active_view);
                TextView btnEnableWithdraw = layout.findViewById(R.id.btn_enable);
                TextView btnWithdraw = layout.findViewById(R.id.btn_withdraw);
                TextView btnSafeMax = layout.findViewById(R.id.btn_max);
                EditText withdrawToken = layout.findViewById(R.id.withdraw_token);
                // TextView withdrawAvailability = layout.findViewById(R.id.availability);
                TextView tokenAvailable = layout.findViewById(R.id.token_available);
                TextView supplyPer = layout.findViewById(R.id.supply_per);
                TextView distributionPer = layout.findViewById(R.id.distribution_per);
                TextView typeCoin = layout.findViewById(R.id.type);
                ImageView symbolIconWithdraw = layout.findViewById(R.id.symbol_icon);
                ImageView symbolIconApyWithdraw = layout.findViewById(R.id.symbol_icon_apy);
                ProgressBar borrowLimitPbWithdraw = layout.findViewById(R.id.borrow_limit_pb);
                TextView maxBorrowLimitWithdraw = layout.findViewById(R.id.max_borrow_limit);
                TextView changeMaxBorrowLimitWithdraw = layout.findViewById(R.id.change_max_borrow_limit);
                TextView maxBorrowLimitPerWithdraw = layout.findViewById(R.id.max_borrow_limit_per);
                TextView changeMaxBorrowLimitPerWithdraw = layout.findViewById(R.id.change_max_borrow_limit_per);
                TableRow borrowLimitTableWithdraw = layout.findViewById(R.id.borrow_limit);
                TableRow borrowLimitPerTableWithdraw = layout.findViewById(R.id.borrow_limit_per);
                try {
                    symbolIconWithdraw.setImageDrawable(mContext.getDrawable(UserInterface.Companion.getCoinIcon(supplyData.getMarkets().getUnderlyingSymbol())));
                    symbolIconApyWithdraw.setImageDrawable(mContext.getDrawable(UserInterface.Companion.getCoinIcon(supplyData.getMarkets().getUnderlyingSymbol())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    borrowLimitTableWithdraw.setVisibility(View.GONE);
                    borrowLimitPerTableWithdraw.setVisibility(View.GONE);
                    maxBorrowLimitWithdraw.setText("$" + UserInterface.Companion.round(totalBorrowLimitValue, 2));
                    Double usedBorrowLimitValue = 0.0;
                    if (totalBorrowLimitValue != 0.0)
                        usedBorrowLimitValue = (totalBorrowedBalance / totalBorrowLimitValue) * 100;
                    maxBorrowLimitPerWithdraw.setText(UserInterface.Companion.round(usedBorrowLimitValue, 2) + "%");
                    borrowLimitPbWithdraw.setProgress((int) Math.round(usedBorrowLimitValue), true);

                    typeCoin.setText(supplyData.getMarkets().getUnderlyingSymbol());
                    //withdrawAvailability.setText(supplyData.getMarkets().getUnderlyingSymbol() + " available");
                    supplyPer.setText(UserInterface.Companion.round((supplyData.getMarkets().getSupplyRate() * 100), 2) + "%");
                    distributionPer.setText(UserInterface.Companion.round((supplyData.getMarkets().getDistributionSupplyApy()), 2) + "%");
                    tokenAvailable.setText(UserInterface.Companion.round(supplyData.getMarkets().getTotalUnderlyingSupplied()) + supplyData.getMarkets().getUnderlyingSymbol() + "=$" + UserInterface.Companion.round(supplyData.getMarkets().getTotalUnderlyingSuppliedinUSD()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Double maxLimitVal = (((totalBorrowLimitValue * .80) - (totalBorrowedBalance)) / (0.80 * supplyData.getMarkets().getCollateralFactor())) / supplyData.getMarkets().getUnderlyingPriceUSD();
                Double maxVal = maxLimitVal < 0 ? 0 : maxLimitVal;
                if (supplyData.getMarkets().getTotalUnderlyingSupplied() > maxVal && supplyData.getMarkets().getEnteredMarket())
                    btnSafeMax.setText(mContext.getString(R.string.safe_max));
                else
                    btnSafeMax.setText(mContext.getString(R.string.max));
                if (isSupplyEnable) {
                    enabledViewWithdraw.setVisibility(View.VISIBLE);
                    btnWithdraw.setVisibility(View.VISIBLE);
                    activeViewWithdraw.setVisibility(View.VISIBLE);
                    btnEnableWithdraw.setVisibility(View.GONE);
                } else {
                    enabledViewWithdraw.setVisibility(View.GONE);
                    btnWithdraw.setVisibility(View.GONE);
                    activeViewWithdraw.setVisibility(View.GONE);
                    btnEnableWithdraw.setVisibility(View.VISIBLE);
                }
                btnSafeMax.setOnClickListener(v -> {
                    Double withdrawTok = supplyData.getMarkets().getTotalUnderlyingSupplied();
                    if (totalBorrowedBalance == 0 || !supplyData.getMarkets().getEnteredMarket()) {
                        if (supplyData.getMarkets().getCash() > withdrawTok) {
                            withdrawToken.setText("" + UserInterface.Companion.roundWallet(withdrawTok));
                        } else {
                            withdrawToken.setText("" + UserInterface.Companion.roundWallet(supplyData.getMarkets().getCash()));
                        }
                        withdrawToken.setSelection(withdrawToken.getText().length());
                    } else {
                        try {
                            //Double safeMaxWithdraw = Double.parseDouble(UserInterface.Companion.roundWallet((totalBorrowLimitValue - totalBorrowedBalance) / supplyData.getMarkets().getCollateralFactor()));
                            if (withdrawTok < maxVal && supplyData.getMarkets().getCash() > withdrawTok) {
                                withdrawToken.setText("" + UserInterface.Companion.roundWallet((withdrawTok)));
                            } else if (withdrawTok > maxVal && supplyData.getMarkets().getCash() > maxVal) {
                                withdrawToken.setText("" + UserInterface.Companion.roundWallet(maxVal));
                            } else {
                                withdrawToken.setText("" + UserInterface.Companion.roundWallet(supplyData.getMarkets().getCash()));
                            }
                            withdrawToken.setSelection(withdrawToken.getText().length());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                withdrawToken.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        btnWithdraw.setEnabled(false);
                        btnWithdraw.setClickable(false);
                        borrowLimitTableWithdraw.setVisibility(View.GONE);
                        borrowLimitPerTableWithdraw.setVisibility(View.GONE);
                        btnWithdraw.setText("Withdraw");
                        try {
                            Double withdrawToken = 0.0;
                            try {
                                withdrawToken = Double.parseDouble(s.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Double tokenVaues = withdrawToken * supplyData.getMarkets().getUnderlyingPriceUSD();
                            Double tokenSuppliedVaues = Double.parseDouble(UserInterface.Companion.roundWallet(supplyData.getMarkets().getTotalUnderlyingSuppliedinUSD()));


                            if (tokenSuppliedVaues == 0) {
                                btnWithdraw.setText("No balance to withdraw");
                            } else if (withdrawToken == 0) {
                                btnWithdraw.setEnabled(false);
                                btnWithdraw.setClickable(false);
                            } else if (tokenSuppliedVaues >= tokenVaues) {
                                if (totalBorrowedBalance >= 0 && supplyData.getMarkets().getCash() >= withdrawToken) {
                                    Double maxWithdraw = Double.parseDouble(UserInterface.Companion.roundWallet((totalBorrowLimitValue - totalBorrowedBalance) / supplyData.getMarkets().getCollateralFactor()));
                                    if (maxWithdraw == 0)
                                        maxWithdraw = Double.parseDouble(UserInterface.Companion.roundWallet(supplyData.getMarkets().getTotalUnderlyingSuppliedinUSD()));
                                    if (maxWithdraw >= tokenVaues) {
                                        if (tokenVaues <= tokenSuppliedVaues) {
                                            borrowLimitTableWithdraw.setVisibility(View.VISIBLE);
                                            borrowLimitPerTableWithdraw.setVisibility(View.VISIBLE);
                                            btnWithdraw.setEnabled(true);
                                            btnWithdraw.setClickable(true);
                                        } else {
                                            btnWithdraw.setText("Insufficient Liquidity");
                                        }
                                    }else if(!supplyData.getMarkets().getEnteredMarket()){
                                        if(tokenSuppliedVaues >= tokenVaues && withdrawToken<=supplyData.getMarkets().getCash()){
                                            borrowLimitTableWithdraw.setVisibility(View.VISIBLE);
                                            borrowLimitPerTableWithdraw.setVisibility(View.VISIBLE);
                                            btnWithdraw.setEnabled(true);
                                            btnWithdraw.setClickable(true);
                                        }else {
                                            btnWithdraw.setText("Insufficient Liquidity");
                                        }
                                    } else {
                                        btnWithdraw.setText("Insufficient Liquidity");
                                    }
                                } else {
                                    btnWithdraw.setText("Insufficient Liquidity");
                                }
                            } else {
                                btnWithdraw.setText("Insufficient Liquidity");
                            }
                            Double maxBorrowLimit = 0.0;
                            if (supplyData.getMarkets().getEnteredMarket()) {
                                maxBorrowLimit = (totalBorrowLimitValue - (tokenVaues * supplyData.getMarkets().getCollateralFactor()));
                                if (maxBorrowLimit < 0)
                                    maxBorrowLimit = 0.0;
                                changeMaxBorrowLimitWithdraw.setText("$" + UserInterface.Companion.round(maxBorrowLimit, 2));
                                Double usedBorrowLimitValue = 0.0;
                                if (maxBorrowLimit != 0.0)
                                    usedBorrowLimitValue = (totalBorrowedBalance / maxBorrowLimit) * 100;
                                changeMaxBorrowLimitPerWithdraw.setText(UserInterface.Companion.round(usedBorrowLimitValue, 2) + "%");
                                borrowLimitPbWithdraw.setProgress((int) Math.round(usedBorrowLimitValue), true);
                            } else {
                                changeMaxBorrowLimitWithdraw.setText(maxBorrowLimitWithdraw.getText().toString());
                                changeMaxBorrowLimitPerWithdraw.setText(maxBorrowLimitPerWithdraw.getText().toString());
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                btnWithdraw.setOnClickListener(v -> {
                    withdrawListener.withdrawTokens(supplyData, withdrawToken.getText().toString());
                });
                btnEnableWithdraw.setOnClickListener(v -> enableAllowance.onClickedEnable(address, marketAddress,supplyData));
                break;
        }
        collection.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
