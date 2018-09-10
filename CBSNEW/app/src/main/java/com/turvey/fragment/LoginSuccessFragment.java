package com.turvey.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.turvey.activity.KeepMain;
import com.turvey.cbs.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoginSuccessFragment extends DialogFragment {

    private View root_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.dialog_login_success, container, false);

        TextView datetext = (TextView) root_view.findViewById(R.id.datetext);
        TextView timetext = (TextView) root_view.findViewById(R.id.timetext);
        String date_n = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault()).format(new Date());
        String time_n = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        datetext.setText(date_n);
        timetext.setText(time_n);

        ((FloatingActionButton) root_view.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), KeepMain.class));
                dismiss();
            }
        });

        return root_view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}