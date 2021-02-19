package com.ayeshapp.spends.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ayeshapp.spends.Database.DatabaseHelper;
import com.ayeshapp.spends.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddExpenseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView addExpenseReceiptImageView, expenseRecieptImageView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String date;
    DatabaseHelper databaseHelper;
    OnCloseListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try
        {
            listener = (OnCloseListener) context;

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    context.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }

    public AddExpenseFragment() {
        // Required empty public constructor
    }

    public static AddExpenseFragment newInstance() {
        AddExpenseFragment fragment = new AddExpenseFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            date = getArguments().getString("date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        addExpenseReceiptImageView = view.findViewById(R.id.imageViewAddExpenseReceipt);
        expenseRecieptImageView = view.findViewById(R.id.imageViewExpenseReceipt);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner categotySpinner = (Spinner) view.findViewById(R.id.catagory_spinner);

        databaseHelper = new DatabaseHelper(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_list_item, getResources().getStringArray(R.array.Catagory) );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        categotySpinner.setAdapter(adapter);

        Button close = view.findViewById(R.id.item_cancel);
        Button saveItem = view.findViewById(R.id.save_item_button);
        EditText itemNameEditText = view.findViewById(R.id.item_name);
        EditText itemCostEditText = view.findViewById(R.id.item_price);
        String category = categotySpinner.getSelectedItem().toString();

        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = String.valueOf(itemNameEditText.getText());
                String itemCost = String.valueOf(itemCostEditText.getText());

                if (itemName.equals("") || itemCost.equals("") || category.equals("")) {
                    Toast.makeText(getContext(), "Please fill all the fields.", Toast.LENGTH_LONG).show();
                } else {
                    databaseHelper.insertExpenseData(date, itemName, category, itemCost);
                    listener.onCloseClicked();
                }

            }
        });

        close.setOnClickListener(v -> {
            listener.onCloseClicked();
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        addExpenseReceiptImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });

    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[] {Manifest.permission.CAMERA}, 101);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Camera permissiom required", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 102);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 102) {
            Bitmap expenseReceipt = (Bitmap) data.getExtras().get("data");
            expenseRecieptImageView.setImageBitmap(expenseReceipt);
        }
    }

    public interface OnCloseListener{
        void onCloseClicked();
    }
}