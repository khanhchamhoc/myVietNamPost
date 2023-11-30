package com.example.myvietnampost.ui.home;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvietnampost.OrderListAdapter;
import com.example.myvietnampost.databinding.FragmentHomeBinding;
import com.example.myvietnampost.model.Order;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private List<Order> orderList;
    private OrderListAdapter orderListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerViewOrderList = binding.recyclerViewOrderList;
        orderList = new ArrayList<>();
        orderListAdapter = new OrderListAdapter(orderList);
        recyclerViewOrderList.setAdapter(orderListAdapter);
        recyclerViewOrderList.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Load danh sách đơn hàng từ Firestore
        loadOrdersFromFirestore();

        return root;
    }

    private void loadOrdersFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ordersRef = db.collection("order");

        ordersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                orderList.clear(); // Xóa danh sách hiện tại (nếu có)
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Order order = document.toObject(Order.class);
                    orderList.add(order);
                }
                orderListAdapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "Error getting orders", task.getException());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
