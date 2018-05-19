package cn.a4miles.okex_monitor.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import cn.a4miles.okex_monitor.R;

/*
* @Project: RentPayAndroid
* @Package com.showsoft.zubao.dialog
* @Description: 创建确认/取消的对话框
* @author showsoft
* @date 23/12/2016 11:39 AM
* $fdc
*/
public class OKConfirmDialogFragment extends DialogFragment {
    private String mTitle;

    private String mMessages;

    private String mPositiveString;

    private String mNegativeString;

    private boolean mNeedNegative = true;

    private boolean isCanClose = true;//默认可以点击外部关闭

    private ConfirmDialogListener mListener;

    /**
     * @param messages
     * @param title
     * @return
     */
    public static OKConfirmDialogFragment newInstance(String messages, String title, ConfirmDialogListener listener) {

        OKConfirmDialogFragment dialog = new OKConfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("messages", messages);
        bundle.putString("title", title);
        dialog.setArguments(bundle);
        dialog.mListener = listener;
        return dialog;
    }

    /**
     * @param messages
     * @param title
     * @return
     */
    public static OKConfirmDialogFragment newInstance(String messages, String title, String positiveString, String negativeString, ConfirmDialogListener listener) {

        OKConfirmDialogFragment dialog = new OKConfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("messages", messages);
        bundle.putString("title", title);
        bundle.putString("positiveString", positiveString);
        bundle.putString("negativeString", negativeString);
        dialog.setArguments(bundle);
        dialog.mListener = listener;
        return dialog;
    }

    /**
     * @param messages
     * @param title
     * @return
     */
    public static OKConfirmDialogFragment newInstance(String messages, String title, String
            positiveString, String negativeString, boolean needNegative, ConfirmDialogListener
                                                              listener) {

        OKConfirmDialogFragment dialog = new OKConfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("messages", messages);
        bundle.putString("title", title);
        bundle.putString("positiveString", positiveString);
        bundle.putString("negativeString", negativeString);
        bundle.putBoolean("needNegative", needNegative);
        dialog.setArguments(bundle);
        dialog.mListener = listener;
        return dialog;
    }

    /**
     * @param messages
     * @param title
     * @return
     */
    public static OKConfirmDialogFragment newInstance(String messages, String title, String
            positiveString, String negativeString, boolean needNegative, boolean isCanClose, ConfirmDialogListener
                                                              listener) {

        OKConfirmDialogFragment dialog = new OKConfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("messages", messages);
        bundle.putString("title", title);
        bundle.putString("positiveString", positiveString);
        bundle.putString("negativeString", negativeString);
        bundle.putBoolean("needNegative", needNegative);
        bundle.putBoolean("isCanClose", isCanClose);
        dialog.setArguments(bundle);
        dialog.mListener = listener;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (!TextUtils.isEmpty(mTitle)) {
            builder.setTitle(mTitle);
        }
        builder.setMessage(mMessages);
        builder.setPositiveButton(mPositiveString == null ? getResources().getString(R.string.confirm) : mPositiveString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onPositiveClick(dialog, which);
                }
            }
        });
        if (mNeedNegative) {
            builder.setNegativeButton(mNegativeString == null ? getResources().getString(R.string.cancel) : mNegativeString, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mListener != null) {
                        mListener.onNegativeClick(dialog, which);
                    }
                }
            });
        }
        Dialog dialog = builder.create();
        if (!isCanClose) {
            dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            });
        }

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mMessages = bundle.getString("messages");
        mTitle = bundle.getString("title");
        mPositiveString = bundle.getString("positiveString");
        mNegativeString = bundle.getString("negativeString");
        mNeedNegative = bundle.getBoolean("needNegative", true);
        isCanClose = bundle.getBoolean("isCanClose", true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            if (mListener == null) {
                mListener = (ConfirmDialogListener) context;
            }
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement ConfirmDialogListener");
        }
    }

    public interface ConfirmDialogListener {
        void onPositiveClick(DialogInterface dialog, int which);

        void onNegativeClick(DialogInterface dialog, int which);
    }
}

