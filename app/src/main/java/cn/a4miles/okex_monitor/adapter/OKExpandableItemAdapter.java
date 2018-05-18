package cn.a4miles.okex_monitor.adapter;

import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Observable;

import cn.a4miles.okex_monitor.R;
import cn.a4miles.okex_monitor.network.OKDataMonitorManager;
import cn.a4miles.okex_monitor.network.OKDataObserver;
import cn.a4miles.okex_monitor.util.DensityUtils;
import cn.a4miles.okex_monitor.util.OKCountDownTimer;
import cn.a4miles.okex_monitor.util.OKTimeUtil;
import cn.a4miles.okex_monitor.entity.OKSymbolLevelItem;
import cn.a4miles.okex_monitor.entity.OKSymbolTickersLevelItem;

/**
 * Created by huhuaxiang on 2018/5/15.
 */

public class OKExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, OKExpandableItemAdapter.ViewHolder> {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    private long mInterval;
    private OKDataMonitorManager manager;

    public  OKExpandableItemAdapter(List<MultiItemEntity> data, long interval, List<String>
            symbols) {
        super(data);

        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);

        mInterval = interval;

        manager = OKDataMonitorManager.getSingleton(symbols, mInterval);
    }

    @Override
    protected void convert(final OKExpandableItemAdapter.ViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case TYPE_LEVEL_0:
                final OKSymbolLevelItem symbolLevelItem = (OKSymbolLevelItem) item;
                helper.setText(R.id.tvSymbol, ((OKSymbolLevelItem) item).getSymbol())
                        .setImageResource(R.id.iv, symbolLevelItem.isExpanded() ? R.mipmap.arrow_b : R.mipmap
                                .arrow_r);

                final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) helper.getView(R.id
                        .progressView).getLayoutParams();

                final int width = DensityUtils.getScreenWidth(mContext);

                // 倒计时10秒，1秒刷新一次数字
                // 配合网络请求时间
                if (helper.countDownTimer != null) {
                    helper.countDownTimer.cancel();
                    helper.countDownTimer = null;
                }

                helper.countDownTimer = new OKCountDownTimer(mInterval, 1000,
                        symbolLevelItem
                                .getSymbol()) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        double percent = (millisUntilFinished) / 1000 * 0.1;
                        params.width = (int) (width * percent);
                        params.height = DensityUtils.dp2px(mContext, 80);
                        helper.getView(R.id.progressView).setLayoutParams(params);
                    }

                    @Override
                    public void onFinish() {
                        // 结束后重新计时
                        double percent = (mInterval) / 1000 * 0.1;
                        params.width = (int) (width * percent);
                        params.height = DensityUtils.dp2px(mContext, 80);
                        helper.getView(R.id.progressView).setLayoutParams(params);
                        start();
                    }
                };

                if (helper.observer != null) {
                    manager.deleteObserver(helper.observer);
                    helper.observer = null;
                }

                helper.observer = new OKDataObserver() {
                    @Override
                    public void update(Observable o, OKSymbolTickersLevelItem item, String symbol) {

                    }
                };

                if (symbolLevelItem.isExpanded()) {
                    helper.setVisible(R.id.progressView, true);
                    helper.countDownTimer.start();
                } else {
                    helper.setVisible(R.id.progressView, false);
                    helper.countDownTimer.cancel();
                }

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (symbolLevelItem.isExpanded()) {
                            collapse(pos);
                            manager.stopMonitor(symbolLevelItem.getSymbol());
                            manager.deleteObserverByKey(symbolLevelItem.getSymbol());
                            helper.observer = null;
                        } else {
                            expand(pos);
                            manager.addObserver(symbolLevelItem.getSymbol() + TYPE_LEVEL_0, helper
                                    .observer);
                            manager.startMonitor(symbolLevelItem.getSymbol());
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1: {
                final OKSymbolTickersLevelItem symbolTickersLevelItem =
                        (OKSymbolTickersLevelItem) item;

                if (helper.observer != null) {
                    manager.deleteObserver(helper.observer);
                    helper.observer = null;
                }

                helper.observer = new OKDataObserver() {
                    @Override
                    public void update(Observable o, OKSymbolTickersLevelItem symbolTickersLevelItem, String symbol) {
                        if (symbol.equals(symbolTickersLevelItem.getSymbol()) && !TextUtils.isEmpty(symbolTickersLevelItem
                                .getDate())) {
                            helper.setVisible(R.id.progressBar, false);
                            helper.setVisible(R.id.llContent, true);
                            setViewWithTickers(symbolTickersLevelItem, helper);
                        }
                    }
                };

                manager.addObserver(((OKSymbolTickersLevelItem) symbolTickersLevelItem).getSymbol() + TYPE_LEVEL_1, helper.observer);

                OKSymbolTickersLevelItem data = manager.getData(((OKSymbolTickersLevelItem)
                        symbolTickersLevelItem).getSymbol());
                if (data != null && !TextUtils.isEmpty(data.getDate())) {
                    // 如果已经有数据，则显示当前数据
                    setViewWithTickers(data, helper);
                } else {
                    helper.setVisible(R.id.progressBar, true);
                    helper.setVisible(R.id.llContent, false);
                }
            }
            break;
        }
    }

    private void setViewWithTickers(OKSymbolTickersLevelItem item, BaseViewHolder helper) {
        // This Week
        helper.setText(R.id.tvTradeTime, "trade signal " + OKTimeUtil.getStrDateFromLong
                (Long.valueOf(item.getDate()) * 1000, OKTimeUtil.PATTERN_YMDHMS_1));

        helper.setText(R.id.tvThisWeekInfo, "bid1: " + item.getThisWeekBuy() + "    spot ask1: " +
                item.getSell());

        helper.setText(R.id.tvThisWeekPercent, item.getThisWeekSpread());

        // Next Week
        helper.setText(R.id.tvNextWeekInfo, "bid1: " + item.getNextWeekBuy() + "    spot ask1: " +
                item.getSell());

        helper.setText(R.id.tvNextWeekPercent, item.getNextWeekSpread());

        // Quarter
        helper.setText(R.id.tvQuarterInfo, "bid1: " + item.getQuarterBuy() + "    spot ask1: " +
                item.getSell());

        helper.setText(R.id.tvQuarterPercent, item.getQuarterSpread());

        setSpreadColor(new BigDecimal(item.getThisWeekSpread()), helper,
                R.id.tvThisWeekPercent);

        setSpreadColor(new BigDecimal(item.getNextWeekSpread()), helper,
                R.id.tvNextWeekPercent);

        setSpreadColor(new BigDecimal(item.getQuarterSpread()), helper, R
                .id.tvQuarterPercent);
    }

    private void setSpreadColor(BigDecimal spread, BaseViewHolder helper, @IdRes int viewId) {
        if (spread.compareTo(new BigDecimal("1")) == 0) {
            helper.setTextColor(viewId, mContext.getResources
                    ().getColor(android.R.color.black));
        } else if (spread.compareTo(new BigDecimal("1")) > 0) {
            helper.setTextColor(viewId, mContext.getResources
                    ().getColor(R.color.premiumColor));
        } else {
            helper.setTextColor(viewId, mContext.getResources
                    ().getColor(R.color.agioColor));
        }
    }

    public void resumeAllMonitors() {
        manager.resumeAllMonitors();
    }

    public void stopAllMonitors() {
        manager.stopAllMonitors();
    }

    public class ViewHolder extends BaseViewHolder {

        OKCountDownTimer countDownTimer;

        OKDataObserver observer;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
