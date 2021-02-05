package com.ondi.android_ondi.View.Menu.Register;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ondi.android_ondi.API.RetrofitClient;
import com.ondi.android_ondi.Adapter.PhotoAdapter;
import com.ondi.android_ondi.Defined.DefinedCategory;
import com.ondi.android_ondi.Model.ResponseModel;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Menu.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class RegisterFragment extends Fragment {

    Context context;
    View mainView;
    ArrayList<String> spinnerList = new ArrayList<>();
    ArrayList<Bitmap> bitmapList = null;

    RelativeLayout btn_upload_photo;
    EditText input_product_name;
    String spinnerType;
    EditText input_product_price;
    CheckBox checkbox_deal;
    EditText input_product_description;
    EditText input_product_tag;
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

        input_product_name = mainView.findViewById(R.id.input_product_name);
        input_product_price = mainView.findViewById(R.id.input_product_price);
        checkbox_deal = mainView.findViewById(R.id.checkbox_deal);
        input_product_description = mainView.findViewById(R.id.input_product_description);
        input_product_tag = mainView.findViewById(R.id.input_product_tag);
    }

    public void initSpinnerData() {
        spinnerList.add(DefinedCategory.getInstance().NOTHING);
        spinnerList.add(DefinedCategory.getInstance().DIGITAL);
        spinnerList.add(DefinedCategory.getInstance().CLOTH);
        spinnerList.add(DefinedCategory.getInstance().BEAUTY);
        spinnerList.add(DefinedCategory.getInstance().ACCESSORY);
        spinnerList.add(DefinedCategory.getInstance().FURNITURE);
        spinnerList.add(DefinedCategory.getInstance().BABY);
        spinnerList.add(DefinedCategory.getInstance().TICKET);
        spinnerList.add(DefinedCategory.getInstance().ETC);
    }

    public class SpinnerItemListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            //todo spinner커스텀
            Object selectedItem = adapterView.getSelectedItem();
            spinnerType = selectedItem.toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public void setImageView(ArrayList<Bitmap> bitmapList,int count){
        this.bitmapList = bitmapList;
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
                    try {
                        postProduct();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void postProduct() throws IOException {
        HashMap<String, RequestBody> map = new HashMap<>();

       String name =  input_product_name.getText().toString();
       String price = input_product_price.getText().toString();
       String content = input_product_description.getText().toString();
       boolean deal = checkbox_deal.isChecked();
       String tag = input_product_tag.getText().toString();

        if(bitmapList.isEmpty()){
            //사진 없음
            Toast.makeText(context,"이미지를 선택해주세요.", Toast.LENGTH_SHORT);
        }
        else
        {
            File imageFile = createFileFromBitmap(bitmapList.get(0));
            RequestBody p_image = RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);
            map.put("p_image",p_image);
            RequestBody p_category = RequestBody.create(MediaType.parse("text/plain"), spinnerType);
            map.put("p_category",p_category);
            RequestBody p_name = RequestBody.create(MediaType.parse("text/plain"), name);
            map.put("p_name",p_name);
            RequestBody p_price = RequestBody.create(MediaType.parse("text/plain"), price);
            map.put("p_price",p_price);
            RequestBody p_content = RequestBody.create(MediaType.parse("text/plain"), content);
            map.put("p_content",p_content);
            RequestBody p_tag = RequestBody.create(MediaType.parse("text/plain"), tag);
            map.put("p_tag",p_tag);
            RequestBody p_deal = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(deal));
            map.put("p_nego",p_deal);
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(8));
            map.put("p_seller",id);

            //todo AuthModel.getInstance().user.getId()
            Call<ResponseModel> call = RetrofitClient.getApiService().postProduct(map);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if(response.isSuccessful()){
                        System.out.println("result: "+response.body());
                        runOnUiThread(() -> Toast.makeText(getContext(), "물품 등록 완료", Toast.LENGTH_SHORT).show());
                    }
                    else{
                        if (response.code() != 200) {
                            try {
                                Log.v("Error code",response.errorBody().string()+ " "+response.errorBody().contentType());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        runOnUiThread(() -> Toast.makeText(getContext(), "물품 등록 실패: "+response.errorBody() , Toast.LENGTH_SHORT).show());
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    System.out.println("message: "+t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    private File createFileFromBitmap(Bitmap bitmap) throws IOException {
        File newFile = new File(getActivity().getFilesDir(),makeImageFileName());
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.close();
        return newFile;
    }

    private String makeImageFileName(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
        Date date = new Date();
        String strDate = simpleDateFormat.format(date);
        return  strDate + ".png";
    }

}
