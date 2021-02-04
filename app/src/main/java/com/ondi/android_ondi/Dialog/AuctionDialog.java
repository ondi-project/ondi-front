package com.ondi.android_ondi.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Menu.Home.ProductAdapter;

public class AuctionDialog {
    Context context;
    Dialog dialog;
    EditText input_start;
    EditText input_date;
    EditText input_time;

    public AuctionDialog(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
    }

    public void show(){
        initView();
        dialog.show();
    }

    private void initView() {
        dialog.setContentView(R.layout.dialog_auction);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);//모서리 둥글게할때 남은 부분 투명하게

        input_start = dialog.findViewById(R.id.input_start_price);
        input_date = dialog.findViewById(R.id.input_date);
        input_time = dialog.findViewById(R.id.input_time);

        LinearLayout btn_register = dialog.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new ClickListener());

    }

    public void dismiss(){
        dialog.dismiss();
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_register :{

                }
            }
        }
    }

}
