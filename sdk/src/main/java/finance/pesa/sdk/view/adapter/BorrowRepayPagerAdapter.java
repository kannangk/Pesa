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

import finance.pesa.sdk.utils.UserInterface;
import finance.pesa.sdk.Model.MarketData;
import finance.pesa.sdk.R;
import finance.pesa.sdk.utils.BorrowListener;
import finance.pesa.sdk.utils.Constants;
import finance.pesa.sdk.utils.RepayListener;
import finance.pesa.sdk.utils.UserInterface;
import finance.pesa.sdk.view.Interface.EnableSupplyListener;

public class BorrowRepayPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int resId = 0;
    private EnableSupplyListener enableAllowance;
    private BorrowListener borrowListener;
    private Boolean isAllowanceEnable;
    private RepayListener repayListener;
    private String address;
    private String marketAddress;
    private String walletBalance;
    private MarketData borrowData;
    private Double maxBorrowLimit = 0.0;
    private Double totalBorrowLimit = 0.0;
    private Double totalBorrowedBalance = 0.0;
    private Double totalBorrowLimitValue = 0.0;
    private Double totalSuppliedBalance = 0.0;

    public BorrowRepayPagerAdapter(Context context, EnableSupplyListener enableAllowance, BorrowListener borrowListener, RepayListener repayListener, Boolean isAllowanceEnable, String walletBalance, String address, String marketAddress, Double maxBorrowLimit, Double totalBorrowLimit, MarketData borrowData, Double totalBorrowedBalance, Double totalBorrowLimitValue, Double totalSuppliedBalance) {
        this.mContext = context;
        this.enableAllowance = enableAllowance;
        this.isAllowanceEnable = isAllowanceEnable;
        this.address = address;
        this.marketAddress = marketAddress;
        this.borrowListener = borrowListener;
        this.repayListener = repayListener;
        this.maxBorrowLimit = maxBorrowLimit;
        this.totalBorrowLimit = totalBorrowLimit;
        this.borrowData = borrowData;
        this.walletBalance = walletBalance;
        this.totalBorrowedBalance = totalBorrowedBalance;
        this.totalBorrowLimitValue = totalBorrowLimitValue;
        this.totalSuppliedBalance = totalSuppliedBalance;
    }


    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = null;
        switch (position) {
            case 0:
                resId = R.layout.borrow_part;
                layout = (ViewGroup) inflater.inflate(resId, collection, false);
                RelativeLayout enabledViewBorrow = layout.findViewById(R.id.enabled_view);
                LinearLayout activeViewBorrow = layout.findViewById(R.id.active_view);
                TextView btnEnableBorrow = layout.findViewById(R.id.btn_enable);
                TextView btnBorrow = layout.findViewById(R.id.btn_borrow);
                TextView btnMax = layout.findViewById(R.id.btn_max);
                EditText borrowToken = layout.findViewById(R.id.edit_borrow);
                TextView availability = layout.findViewById(R.id.availability);
                TextView token_available = layout.findViewById(R.id.token_available);
                TextView supply_per = layout.findViewById(R.id.supply_per);
                TextView distribution_per = layout.findViewById(R.id.distribution_per);
                TextView type = layout.findViewById(R.id.type);
                ImageView symbolIcon = layout.findViewById(R.id.symbol_icon);
                ImageView symbolIconApy = layout.findViewById(R.id.symbol_icon_apy);
                ProgressBar borrowLimitPbBorrow = layout.findViewById(R.id.borrow_limit_pb);
                TextView maxBorrowLimitBorrow = layout.findViewById(R.id.max_borrow_limit);
                TextView changeMaxBorrowLimitBorrow = layout.findViewById(R.id.change_max_borrow_limit);
                TextView maxBorrowLimitPerBorrow = layout.findViewById(R.id.max_borrow_limit_per);
                TextView changeMaxBorrowLimitPerBorrow = layout.findViewById(R.id.change_max_borrow_limit_per);
                TableRow borrowLimitTableBorrow = layout.findViewById(R.id.borrow_limit);
                TableRow borrowLimitPerTableBorrow = layout.findViewById(R.id.borrow_limit_per);
                try {
                    symbolIcon.setImageDrawable(mContext.getDrawable(UserInterface.Companion.getCoinIcon(borrowData.getMarkets().getUnderlyingSymbol())));
                    symbolIconApy.setImageDrawable(mContext.getDrawable(UserInterface.Companion.getCoinIcon(borrowData.getMarkets().getUnderlyingSymbol())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    borrowLimitTableBorrow.setVisibility(View.GONE);
                    borrowLimitPerTableBorrow.setVisibility(View.GONE);
                    maxBorrowLimitBorrow.setText("$" + UserInterface.Companion.round(totalBorrowedBalance, 2));
                    Double usedBorrowLimitValue = 0.0;
                    if (totalBorrowLimitValue != 0.0)
                        usedBorrowLimitValue = (totalBorrowedBalance / totalBorrowLimitValue) * 100;
                    maxBorrowLimitPerBorrow.setText(UserInterface.Companion.round(usedBorrowLimitValue, 2) + "%");
                    borrowLimitPbBorrow.setProgress((int) Math.round(usedBorrowLimitValue), true);
                    type.setText(borrowData.getMarkets().getUnderlyingSymbol());
                    availability.setText(borrowData.getMarkets().getUnderlyingSymbol() + " available");
                    supply_per.setText(UserInterface.Companion.round((borrowData.getMarkets().getBorrowRate() * 100), 2) + "%");
                    distribution_per.setText(UserInterface.Companion.round((borrowData.getMarkets().getDistributionBorrowApy()), 2) + "%");
                    Double tokenValues = Double.parseDouble(walletBalance) * borrowData.getMarkets().getUnderlyingPriceUSD();
                    token_available.setText(walletBalance + "=$" + UserInterface.Companion.round(tokenValues));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*if (isAllowanceEnable) {
                    enabledViewBorrow.setVisibility(View.VISIBLE);
                    btnBorrow.setVisibility(View.VISIBLE);
                    activeViewBorrow.setVisibility(View.VISIBLE);
                    btnEnableBorrow.setVisibility(View.GONE);
                } else {
                    enabledViewBorrow.setVisibility(View.GONE);
                    btnBorrow.setVisibility(View.GONE);
                    activeViewBorrow.setVisibility(View.GONE);
                    btnEnableBorrow.setVisibility(View.VISIBLE);
                }*/
                btnMax.setOnClickListener(v -> {
                    if((totalBorrowedBalance/totalBorrowLimitValue)>=0.80){
                        borrowToken.setText("" + UserInterface.Companion.roundWallet(0.0));
                        borrowToken.setSelection(borrowToken.getText().length());
                    }else {
                        Double needSafeBorrow=(totalBorrowLimitValue*0.80)-totalBorrowedBalance;
                        Double tokenValueOfMaxBorrowLimit = needSafeBorrow / borrowData.getMarkets().getUnderlyingPriceUSD();
                        Double tokenValueOfTotalBorrowLimit = totalBorrowLimit / borrowData.getMarkets().getUnderlyingPriceUSD();
                        if (tokenValueOfTotalBorrowLimit > tokenValueOfMaxBorrowLimit) {
                            if (borrowData.getMarkets().getCash() >= tokenValueOfMaxBorrowLimit) {
                                borrowToken.setText("" + UserInterface.Companion.roundWallet(tokenValueOfMaxBorrowLimit));
                            } else {
                                borrowToken.setText("" + UserInterface.Companion.roundWallet(borrowData.getMarkets().getCash()));
                            }
                        } else
                            borrowToken.setText("" + UserInterface.Companion.roundWallet(0.0));
                        borrowToken.setSelection(borrowToken.getText().length());
                    }
                });


                borrowToken.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        btnBorrow.setEnabled(false);
                        btnBorrow.setClickable(false);
                        borrowLimitTableBorrow.setVisibility(View.GONE);
                        borrowLimitPerTableBorrow.setVisibility(View.GONE);
                        btnBorrow.setText("Borrow");
                        try {
                            Double borrowToken = 0.0;
                            try {
                                borrowToken = Double.parseDouble(s.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Double tokenVaues = 0.0;
                            Double tokenValueOfTotalBorrowLimit = 0.0;
                            tokenVaues = borrowToken * borrowData.getMarkets().getUnderlyingPriceUSD();
                            tokenValueOfTotalBorrowLimit = totalBorrowLimit / borrowData.getMarkets().getUnderlyingPriceUSD();

                            if (totalSuppliedBalance == 0) {
                                btnBorrow.setText("Borrowing Limit Reached");
                            } else if (borrowToken == 0) {
                                btnBorrow.setEnabled(false);
                                btnBorrow.setClickable(false);
                            } else if (tokenValueOfTotalBorrowLimit >= borrowToken) {
                                if (borrowData.getMarkets().getCash() >= borrowToken) {
                                    btnBorrow.setEnabled(true);
                                    btnBorrow.setClickable(true);
                                    borrowLimitTableBorrow.setVisibility(View.VISIBLE);
                                    borrowLimitPerTableBorrow.setVisibility(View.VISIBLE);
                                } else {
                                    btnBorrow.setText("Insufficient Liquidity");
                                }
                            } else {
                                btnBorrow.setText("Insufficient collateral");
                            }
                            Double maxBorrow = totalBorrowedBalance + (tokenVaues);
                            changeMaxBorrowLimitBorrow.setText("$" + UserInterface.Companion.round(maxBorrow, 2));
                            Double usedBorrowLimitValue = 0.0;
                            if (maxBorrow != 0.0)
                                usedBorrowLimitValue = (maxBorrow / totalBorrowLimitValue) * 100;
                            changeMaxBorrowLimitPerBorrow.setText(UserInterface.Companion.round(usedBorrowLimitValue, 2) + "%");
                            borrowLimitPbBorrow.setProgress((int) Math.round(usedBorrowLimitValue), true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                btnBorrow.setOnClickListener(v -> {
                    borrowListener.borrowToken(borrowData, borrowToken.getText().toString());
                });
                break;
            case 1:
                resId = R.layout.repay_part;
                layout = (ViewGroup) inflater.inflate(resId, collection, false);
                RelativeLayout enabledView = layout.findViewById(R.id.enabled_view);
                LinearLayout activeView = layout.findViewById(R.id.active_view);
                TextView btnRepay = layout.findViewById(R.id.btn_repay);
                TextView btnEnable = layout.findViewById(R.id.btn_enable);
                TextView btnSafeMax = layout.findViewById(R.id.btn_max);
                EditText repayToken = layout.findViewById(R.id.edit_repay);
                TextView withdrawAvailability = layout.findViewById(R.id.availability);
                TextView tokenAvailable = layout.findViewById(R.id.token_available);
                TextView supplyPer = layout.findViewById(R.id.supply_per);
                TextView distributionPer = layout.findViewById(R.id.distribution_per);
                TextView typeCoin = layout.findViewById(R.id.type);
                ImageView symbolIconRepay = layout.findViewById(R.id.symbol_icon);
                ImageView symbolIconApyRepay = layout.findViewById(R.id.symbol_icon_apy);
                ProgressBar borrowLimitPbRepay = layout.findViewById(R.id.borrow_limit_pb);
                TextView maxBorrowLimitRepay = layout.findViewById(R.id.max_borrow_limit);
                TextView changeMaxBorrowLimitRepay = layout.findViewById(R.id.change_max_borrow_limit);
                TextView maxBorrowLimitPerRepay = layout.findViewById(R.id.max_borrow_limit_per);
                TextView changeMaxBorrowLimitPerRepay = layout.findViewById(R.id.change_max_borrow_limit_per);
                TableRow borrowLimitTableRepay = layout.findViewById(R.id.borrow_limit);
                TableRow borrowLimitPerTableRepay = layout.findViewById(R.id.borrow_limit_per);
                try {
                    symbolIconRepay.setImageDrawable(mContext.getDrawable(UserInterface.Companion.getCoinIcon(borrowData.getMarkets().getUnderlyingSymbol())));
                    symbolIconApyRepay.setImageDrawable(mContext.getDrawable(UserInterface.Companion.getCoinIcon(borrowData.getMarkets().getUnderlyingSymbol())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    borrowLimitTableRepay.setVisibility(View.GONE);
                    borrowLimitPerTableRepay.setVisibility(View.GONE);
                    maxBorrowLimitRepay.setText("$" + UserInterface.Companion.round(totalBorrowedBalance, 2));
                    Double usedBorrowLimitValue = 0.0;
                    if (totalBorrowLimitValue != 0.0)
                        usedBorrowLimitValue = (totalBorrowedBalance / totalBorrowLimitValue) * 100;
                    maxBorrowLimitPerRepay.setText(UserInterface.Companion.round(usedBorrowLimitValue, 2) + "%");
                    borrowLimitPbRepay.setProgress((int) Math.round(usedBorrowLimitValue), true);
                    typeCoin.setText(borrowData.getMarkets().getUnderlyingSymbol());
                    withdrawAvailability.setText(borrowData.getMarkets().getUnderlyingSymbol() + " available");
                    supplyPer.setText(UserInterface.Companion.round((borrowData.getMarkets().getBorrowRate() * 100), 2) + "%");
                    distributionPer.setText(UserInterface.Companion.round((borrowData.getMarkets().getDistributionBorrowApy()), 2) + "%");
                    Double tokenVaues = Double.parseDouble(walletBalance) * borrowData.getMarkets().getUnderlyingPriceUSD();
                    tokenAvailable.setText(walletBalance + "=$" + UserInterface.Companion.round(tokenVaues));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isAllowanceEnable) {
                    enabledView.setVisibility(View.VISIBLE);
                    btnRepay.setVisibility(View.VISIBLE);
                    activeView.setVisibility(View.VISIBLE);
                    btnEnable.setVisibility(View.GONE);
                } else {
                    enabledView.setVisibility(View.GONE);
                    btnRepay.setVisibility(View.GONE);
                    activeView.setVisibility(View.GONE);
                    btnEnable.setVisibility(View.VISIBLE);
                }
                if ( borrowData.getMarkets().getStoredBorrowBalance() == 0) {
                    btnRepay.setText("No balance to repay");
                }
                btnSafeMax.setOnClickListener(v -> {
                    Double walletBal = Double.parseDouble(walletBalance);
                    if (borrowData.getMarkets().getStoredBorrowBalance() <= walletBal)
                        repayToken.setText("" + UserInterface.Companion.roundWallet(borrowData.getMarkets().getStoredBorrowBalance()));
                    else if (borrowData.getMarkets().getStoredBorrowBalance() > walletBal)
                        repayToken.setText("" + UserInterface.Companion.roundWallet(walletBal));
                    else
                        repayToken.setText("" + UserInterface.Companion.roundWallet(0.0));
                    repayToken.setSelection(repayToken.getText().length());
                });
                repayToken.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        btnRepay.setEnabled(false);
                        btnRepay.setClickable(false);
                        borrowLimitTableRepay.setVisibility(View.GONE);
                        borrowLimitPerTableRepay.setVisibility(View.GONE);
                        btnRepay.setText("Repay");
                        try {
                            Double repayToken = 0.0;
                            try {
                                repayToken = Double.parseDouble(s.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Double tokenVaues = repayToken * borrowData.getMarkets().getUnderlyingPriceUSD();
                            if ( borrowData.getMarkets().getStoredBorrowBalance() == 0) {
                                btnRepay.setText("No balance to repay");
                            } else if (repayToken == 0) {
                                btnRepay.setEnabled(false);
                                btnRepay.setClickable(false);
                            } else if (borrowData.getMarkets().getStoredBorrowBalance() >= repayToken) {
                                btnRepay.setEnabled(true);
                                btnRepay.setClickable(true);
                                borrowLimitTableRepay.setVisibility(View.VISIBLE);
                                borrowLimitPerTableRepay.setVisibility(View.VISIBLE);
                            } else {
                                btnRepay.setText("No funds available");
                            }
                            changeMaxBorrowLimitRepay.setText("$" + UserInterface.Companion.round((totalBorrowedBalance - (tokenVaues)), 2));
                            Double usedBorrowLimitValue = 0.0;
                            if (totalBorrowLimitValue != 0.0)
                                usedBorrowLimitValue = ((totalBorrowedBalance - (tokenVaues)) / totalBorrowLimitValue) * 100;
                            changeMaxBorrowLimitPerRepay.setText(UserInterface.Companion.round(usedBorrowLimitValue, 2) + "%");
                            borrowLimitPbRepay.setProgress((int) Math.round(usedBorrowLimitValue), true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                btnRepay.setOnClickListener(v -> {
                    repayListener.repayTokens(borrowData, repayToken.getText().toString());
                });
                btnEnable.setOnClickListener(v -> enableAllowance.onClickedEnable(address, marketAddress,borrowData));
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
