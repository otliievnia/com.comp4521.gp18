package com.example.comp4521.view.dialog;

import android.app.Activity;
import android.app.ProgressDialog;

import androidx.fragment.app.Fragment;

public class ProgressDialogClass {
    private ProgressDialog dialogObj;
    private Activity activity;

    public ProgressDialogClass(final Activity activity) {
        this.activity = activity;
        dialogObj = new ProgressDialog(activity);
    }

    public void showDialog(final String title, final String msg) {
        dialogObj.setMessage(msg);
        dialogObj.setTitle(title);
        dialogObj.show();
    }

    public void dismissDialog() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (dialogObj.isShowing())
                    dialogObj.dismiss();
            }
        });
    }

}
