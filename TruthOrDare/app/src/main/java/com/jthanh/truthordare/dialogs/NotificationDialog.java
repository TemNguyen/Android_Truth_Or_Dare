package com.jthanh.truthordare.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jthanh.truthordare.R;

public class NotificationDialog {
    private Activity activity;
    private AlertDialog alertDialog;
    private TextView messageText;
    private ImageView messageImage;

    public NotificationDialog(Activity activity) {
        this.activity = activity;
    }

    public void startDialog(String msg, String icon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_message_dialog, null);

        messageText = view.findViewById(R.id.messageText);
        messageText.setText(msg);

        messageImage = (ImageView) view.findViewById(R.id.messageImage);

        switch (icon) {
            case "yes":
                messageImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_baseline_check_24));
                break;
            case "no":
                messageImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_baseline_clear_24));
                break;
            case "sad":
                messageImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_sad));
        }



        builder.setView(view);
        builder.setCancelable(false);


        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissDialog() {
        alertDialog.dismiss();
    }
}
