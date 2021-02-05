package com.ondi.android_ondi.View.Menu.Register;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ondi.android_ondi.Adapter.PhotoAdapter;
import com.ondi.android_ondi.Defined.DefinedCategory;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Menu.MainActivity;

import java.util.ArrayList;

public class RegisterFragment extends Fragment {

    Context context;
    View mainView;
    ArrayList<String> spinnerList = new ArrayList<>();

    RelativeLayout btn_upload_photo;
    LinearLayout btn_register;

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        initSpinnerData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_register, container, false);
        initView();
        return mainView;
    }

    private void initView() {
        Spinner spinner = mainView.findViewById(R.id.spinner_register);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinnerList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new SpinnerItemListener());

        btn_upload_photo = mainView.findViewById(R.id.btn_upload_photo);
        btn_upload_photo.setOnClickListener(new ClickListener());

        btn_register = mainView.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new ClickListener());
    }

    public void initSpinnerData() {
        spinnerList.add(DefinedCategory.getInstance().NOTHING);
        spinnerList.add(DefinedCategory.getInstance().DIGITAL);
        spinnerList.add(DefinedCategory.getInstance().CLOTH);
        spinnerList.add(DefinedCategory.getInstance().BEAUTY);
        spinnerList.add(DefinedCategory.getInstance().ACCESSORY);
        spinnerList.add(DefinedCategory.getInstance().FURNITURE);
        spinnerList.add(DefinedCategory.getInstance().BABY);
        spinnerList.add(DefinedCategory.getInstance().SPORTS);
        spinnerList.add(DefinedCategory.getInstance().ETC);
    }

    public class SpinnerItemListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            //todo spinner커스텀
            Object selectedItem = adapterView.getSelectedItem();
            if (DefinedCategory.getInstance().NOTHING.equals(selectedItem)) {
                //((TextView)view).setTextColor(ContextCompat.getColor(context, R.color.blue_grey));
            }
            else if (DefinedCategory.getInstance().DIGITAL.equals(selectedItem)) {

            }
            else if (DefinedCategory.getInstance().CLOTH.equals(selectedItem)) {

            }
            else if (DefinedCategory.getInstance().BEAUTY.equals(selectedItem)) {

            }
            else if (DefinedCategory.getInstance().ACCESSORY.equals(selectedItem)) {

            }
            else if (DefinedCategory.getInstance().FURNITURE.equals(selectedItem)) {

            }
            else if (DefinedCategory.getInstance().BABY.equals(selectedItem)) {

            }
            else if (DefinedCategory.getInstance().SPORTS.equals(selectedItem)) {

            }
            else if (DefinedCategory.getInstance().ETC.equals(selectedItem)) {

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public void setImageView(ArrayList<Bitmap> bitmapList,int count){
        RecyclerView recyclerView = mainView.findViewById(R.id.recycler_photo);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        PhotoAdapter adapter = new PhotoAdapter(getContext(),bitmapList);
        recyclerView.setAdapter(adapter);

        TextView text_photo_count = mainView.findViewById(R.id.text_photo_count);
        text_photo_count.setText(String.valueOf(count));
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_upload_photo :{
                    ((MainActivity)getActivity()).openGallery();
                    break;
                }
                case R.id.btn_register :{
                    //todo 서버에 저장 요청

                }
            }
        }
    }


}
