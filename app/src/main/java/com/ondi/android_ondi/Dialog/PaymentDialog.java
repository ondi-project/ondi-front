package com.ondi.android_ondi.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.ondi.android_ondi.R;

public class PaymentDialog {
    Context context;
    Dialog dialog;
    LinearLayout btn_history;

    public PaymentDialog(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
    }

    public void show(){
        initView();
        dialog.show();
    }

    private void initView() {
        dialog.setContentView(R.layout.dialog_payment);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);//모서리 둥글게할때 남은 부분 투명하게
        dialog.setCancelable(false);

        btn_history = dialog.findViewById(R.id.btn_history);
        btn_history.setOnClickListener(new ClickListener());

    }

    public void dismiss(){
        dialog.dismiss();
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_history :{
                    //todo 거래내역으로
                    dialog.dismiss();
                    break;
                }
            }
        }
    }

}
