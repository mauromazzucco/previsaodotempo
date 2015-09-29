package com.stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.stormy.R;

/**
 * Created by mauro on 21/09/15.
 */
public class AlertDialogFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.error_title)
        .setMessage(R.string.error_message)
        .setPositiveButton(R.string.error_ok_button, null);

        AlertDialog dialog = builder.create();
        return dialog;

    }
}
