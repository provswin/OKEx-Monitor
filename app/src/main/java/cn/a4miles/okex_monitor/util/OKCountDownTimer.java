package cn.a4miles.okex_monitor.util;

import android.os.CountDownTimer;

public abstract class OKCountDownTimer extends CountDownTimer {
    private String mSymbol;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public OKCountDownTimer(long millisInFuture, long countDownInterval, String symbol) {
        super(millisInFuture, countDownInterval);
        mSymbol = symbol;
    }

    public String getSymbol() {
        return mSymbol;
    }
}
