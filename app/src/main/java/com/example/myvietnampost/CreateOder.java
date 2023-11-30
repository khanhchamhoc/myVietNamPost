package com.example.myvietnampost;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.myvietnampost.API.ApiService;
import com.example.myvietnampost.API.RetrofitInstance;
import com.example.myvietnampost.databinding.ActivityCreateOderBinding;
import com.example.myvietnampost.model.Order;
import com.example.myvietnampost.model.RandomIdGenerator;
import com.example.myvietnampost.model.apiModel.District;
import com.example.myvietnampost.model.apiModel.Division;
import com.example.myvietnampost.model.apiModel.Ward;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateOder extends AppCompatActivity {
    private ActivityCreateOderBinding binding;
    private CreateOderViewModel viewModel;
    List<Division> divisions;
    List<District> districts;
    List<Ward> wards;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();
    private static final int PICK_IMAGE_REQUEST = 1;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_oder);
        viewModel = new ViewModelProvider(this).get(CreateOderViewModel.class);
        viewModel.fetchDataFromFirebase();
        viewModel.getUserLiveData().observe(this, user -> {
            if(user != null){
                binding.setUser(user);
            }
        });

        getData();
        order = new Order();

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
        binding.buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
        binding.footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPaymentOptionId = binding.radioGroup1.getCheckedRadioButtonId();
                RadioButton selectedPaymentOption = findViewById(selectedPaymentOptionId);
                int selectedServiceOptionId = binding.radioGroup.getCheckedRadioButtonId();
                RadioButton selectedServiceOption = findViewById(selectedServiceOptionId);
                String paymentMethod = selectedPaymentOption.getText().toString();
                String serviceMethod = selectedServiceOption.getText().toString();
                if (order == null) {
                    order = new Order();
                }

                if (TextUtils.isEmpty(order.getImageUrl())) {
                    Toast.makeText(CreateOder.this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }
                String id = RandomIdGenerator.generateRandomId();
                String imgUrl = order.getImageUrl();
                Order order = new Order(id,imgUrl,binding.addressIP.getText().toString().trim(), binding.switchOptions.isChecked(), binding.ThanhPho.getSelectedItem().toString().trim(), binding.nameIP1.getText().toString().trim(),paymentMethod, binding.phoneNumberIP.getText().toString().trim(), binding.Tinh.getSelectedItem().toString(),serviceMethod, binding.QuanHuyen.getSelectedItem().toString(), Integer.parseInt(binding.weightIP.getText().toString()));
                if(viewModel.check(order)){
                    uploadImageToFirebaseStorage(Uri.parse(order.getImageUrl()), id);
                    addOrder(id,imgUrl, paymentMethod, serviceMethod);
                }else
                    wrongInfoDialog();         }
        });
    }
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateOder.this, android.R.layout.simple_spinner_item, divisionNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.Tinh.setAdapter(adapter);
                } else {
                    Toast.makeText(CreateOder.this, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Division>> call, Throwable t) {
                Toast.makeText(CreateOder.this, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadDataThanhPho(int selectedItem) {
        districts = divisions.get(selectedItem).getDistricts();
        ArrayList<String> districtsName = new ArrayList<>();
        for (District district : districts){
            districtsName.add(district.getName());
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(CreateOder.this, android.R.layout.simple_spinner_item, districtsName);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.ThanhPho.setAdapter(adapter1);
    }
    private void loadDataQuanHuyen(int selectedItem) {
        wards = districts.get(selectedItem).getWards();
        ArrayList<String> wardName = new ArrayList<>();
        for (Ward ward : wards){
            wardName.add(ward.getName());
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(CreateOder.this, android.R.layout.simple_spinner_item, wardName);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.QuanHuyen.setAdapter(adapter2);
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private void uploadImageToFirebaseStorage(Uri imageUrl, String id) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        StorageReference imageRef = storageRef.child("images/" + id + ".jpg");

        UploadTask uploadTask = imageRef.putFile(imageUrl);

        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String uploadedImageUrl = uri.toString();
                    order.setImageUrl(uploadedImageUrl);
                });
            } else {
                Toast.makeText(CreateOder.this, "Lỗi khi tải ảnh lên: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addOrder(String id,String imgUrl,String paymentMethod, String serviceMethod) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận đơn hàng");
        builder.setMessage("Bạn có chắc chắn muốn thêm đơn hàng ?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Order order = new Order(id,imgUrl,binding.addressIP.getText().toString().trim(), binding.switchOptions.isChecked(), binding.ThanhPho.getSelectedItem().toString().trim(), binding.nameIP1.getText().toString().trim(),paymentMethod, binding.phoneNumberIP.getText().toString().trim(), binding.Tinh.getSelectedItem().toString(),serviceMethod, binding.QuanHuyen.getSelectedItem().toString(), Integer.parseInt(binding.weightIP.getText().toString()));
                viewModel.addOrder(order);
                viewModel.getUpdateStatus().observe(CreateOder.this, isUpdateSuccessful -> {
                    if (isUpdateSuccessful != null) {
                        if (isUpdateSuccessful) {
                            Toast.makeText(CreateOder.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CreateOder.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void wrongInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Vui lòng nhập đúng thông tin");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            binding.imageView.setImageURI(selectedImageUri);
            if (order == null) {
                order = new Order();
            }
            order.setImageUrl(selectedImageUri.toString());
        }
    }

}