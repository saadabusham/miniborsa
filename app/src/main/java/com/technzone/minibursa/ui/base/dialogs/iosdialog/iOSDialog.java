package com.technzone.minibursa.ui.base.dialogs.iosdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import com.technzone.minibursa.R;

public class iOSDialog {
    private Dialog dialog;
    private TextView dialogButtonOk ,dialogButtonNo;
    private TextView title_lbl, subtitle_lbl;
    private View separator;
    private iOSDialogClickListener positiveListener;
    private iOSDialogClickListener negativeListener;
    private boolean negativeExist;
    private static final String LOG_ERROR = "iOSDialog_ERROR";

    public iOSDialog(Context context, String title, String subtitle, boolean bold, Typeface typeFace,boolean cancelable) {
        negativeExist=false;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_ios);
        if(dialog.getWindow()!=null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initViews();

        dialog.setCancelable(cancelable);
        setTitle(context.getResources().getString(R.string.alert_dialog_title));
        setSubtitle(subtitle);
        setBoldPositiveLabel(bold);
        setTypefaces(typeFace);

        initEvents();
    }

    public void setPositive(String okLabel, iOSDialogClickListener listener) {
        this.positiveListener = listener;
        this.dismiss();
        setPositiveLabel(okLabel);
    }
    public void setNegative(String koLabel, iOSDialogClickListener listener) {
        if (listener != null){
            this.negativeListener = listener;
            this.dismiss();
            negativeExist = true;
            setNegativeLabel(koLabel);
        }
    }
    public void show(){
        if(!negativeExist){
            dialogButtonNo.setVisibility(View.GONE);
            separator.setVisibility(View.GONE);
        }
        dialog.show();
    }
    public void dismiss(){
        dialog.dismiss();
    }
    public void setTitle(String title){
        title_lbl.setText(title);
    }
    public void setSubtitle(String subtitle){
        subtitle_lbl.setText(subtitle);
    }
    private void setPositiveLabel(String positive){
        dialogButtonOk.setText(positive);
    }
    private void setNegativeLabel(String negative){
        dialogButtonNo.setText(negative);
    }
    private void setBoldPositiveLabel(boolean bold){
        if(bold)
            dialogButtonOk.setTypeface(null, Typeface.BOLD);
        else
            dialogButtonOk.setTypeface(null, Typeface.NORMAL);
    }
    private void setTypefaces(Typeface appleFont){
        if(appleFont!=null) {
            title_lbl.setTypeface(appleFont);
            subtitle_lbl.setTypeface(appleFont);
            dialogButtonOk.setTypeface(appleFont);
            dialogButtonNo.setTypeface(appleFont);
        }
    }


    private void initViews() {
        title_lbl = dialog.findViewById(R.id.title);
        subtitle_lbl =  dialog.findViewById(R.id.subtitle);
        dialogButtonOk =  dialog.findViewById(R.id.dialogButtonOK);
        dialogButtonNo =  dialog.findViewById(R.id.dialogButtonNO);
        separator =  dialog.findViewById(R.id.separator);

        subtitle_lbl.setMaxLines(28);
        subtitle_lbl.setScroller(new Scroller(dialog.getContext()));
        subtitle_lbl.setVerticalScrollBarEnabled(true);
        subtitle_lbl.setMovementMethod(new ScrollingMovementMethod());
    }

    private void initEvents(){
        dialogButtonOk.setOnClickListener(view -> {
            if (positiveListener != null) {
                positiveListener.onClick(iOSDialog.this);
            }
        });
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (negativeListener != null) {
                    negativeListener.onClick(iOSDialog.this);
                }
            }
        });
    }


}

/*
 *  Made with love (and coffee and pizza) in Naples, Italy!
 *
 *  __
 * // ""--.._
 * ||  (_)  _ "-._
 * ||    _ (_)    '-.
 * ||   (_)   __..-'
 * \\__..--""
 *
 */