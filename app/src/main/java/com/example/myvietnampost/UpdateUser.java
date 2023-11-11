package com.example.myvietnampost;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myvietnampost.API.ApiService;
import com.example.myvietnampost.API.RetrofitInstance;
import com.example.myvietnampost.databinding.ActivityUpdateUserBinding;
import com.example.myvietnampost.model.apiModel.District;
import com.example.myvietnampost.model.apiModel.Division;
import com.example.myvietnampost.model.apiModel.Ward;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUser extends AppCompatActivity {
    ActivityUpdateUserBinding binding;
    List<Division> divisions;
    List<District> districts;
    List<Ward> wards;
    private UpdateUserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_user);
        binding.dateTimeIP.setRawInputType(InputType.TYPE_NULL);
        binding.dateTimeIP.setTextIsSelectable(true);
        binding.dateTimeIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dateTimeIP.setRawInputType(InputType.TYPE_NULL);
                showDatePickerDialog();
            }
        });
//        Mac dinh
//
        viewModel = new ViewModelProvider(this).get(UpdateUserViewModel.class);
//        Tinh Thay doi
        viewModel.getDivisionsLiveData().observe(this, new Observer<List<Division>>() {
            @Override
            public void onChanged(List<Division> divisionList) {
                if(divisionList != null){

                }else {
                    getData();
                }
            }
        });

        viewModel.getDistrictsLiveData().observe(this, new Observer<List<District>>() {
            @Override
            public void onChanged(List<District> districtList) {
                if(districtList != null){

                }else {

                }
            }
        });
        getData();
        binding.Tinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedItem = position;
                viewModel.updateDistricts(divisions.get(selectedItem).getDistricts());
                loadDataThanhPho(selectedItem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.ThanhPho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedItem = position;
                viewModel.updateWards(districts.get(selectedItem).getWards());
                loadDataQuanHuyen(selectedItem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //    Ngày sinh
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String selectedDate = day + "/" + (month + 1) + "/" + year;
                binding.dateTimeIP.setText(selectedDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
//    Lấy dữ  liệu từ api
    private void getData() {
        ApiService service = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
        Call<List<Division>> call = service.getDivisions();
        call.enqueue(new Callback<List<Division>>() {
            @Override
            public void onResponse(Call<List<Division>> call, Response<List<Division>> response) {
                if (response.isSuccessful()) {
                    divisions = response.body();
                    ArrayList<String> divisionNames = new ArrayList<>();
                    for (Division division : divisions) {
                        divisionNames.add(division.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(UpdateUser.this, android.R.layout.simple_spinner_item, divisionNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.Tinh.setAdapter(adapter);
                } else {
                    Toast.makeText(UpdateUser.this, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Division>> call, Throwable t) {
                Toast.makeText(UpdateUser.this, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadDataThanhPho(int selectedItem) {
        districts = divisions.get(selectedItem).getDistricts();
        ArrayList<String> districtsName = new ArrayList<>();
        for (District district : districts){
            districtsName.add(district.getName());
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(UpdateUser.this, android.R.layout.simple_spinner_item, districtsName);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.ThanhPho.setAdapter(adapter1);
    }
    private void loadDataQuanHuyen(int selectedItem) {
        wards = districts.get(selectedItem).getWards();
        ArrayList<String> wardName = new ArrayList<>();
        for (Ward ward : wards){
            wardName.add(ward.getName());
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(UpdateUser.this, android.R.layout.simple_spinner_item, wardName);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.QuanHuyen.setAdapter(adapter2);
    }

}