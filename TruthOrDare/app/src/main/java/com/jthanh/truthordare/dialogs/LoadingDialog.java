package com.jthanh.truthordare.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jthanh.truthordare.R;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog alertDialog;
    private TextView dialogText;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_loading_dialog, null);
        dialogText = view.findViewById(R.id.dialogText);
        dialogText.setText(msg);
        builder.setView(view);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissDialog() {
        alertDialog.dismiss();
    }
}
