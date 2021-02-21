package com.ayeshapp.spends.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
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

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddIncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddIncomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;

    private String currentPhotoPath;
    private String mParam1;
    private String mParam2;
    private String date;
    private String incomeCategory;

    private Spinner categoryIncomeSpinner;
    private EditText incomeAmountEditText;
    private ImageView addIncomeReceiptImageView, incomeReceiptImageView;
    private Button saveIncomeButton, closeIncomeButton;

    DatabaseHelper databaseHelper;
    OnIncomeCloseListener listener;

    public AddIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try
        {
            listener = (OnIncomeCloseListener) context;

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    context.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }

    public static AddIncomeFragment newInstance() {
        AddIncomeFragment fragment = new AddIncomeFragment();
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
        return inflater.inflate(R.layout.fragment_add_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());
        categoryIncomeSpinner = view.findViewById(R.id.catagory_income_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_list_item, getResources().getStringArray(R.array.CatagoryIncome) );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        categoryIncomeSpinner.setAdapter(adapter);

        closeIncomeButton = view.findViewById(R.id.buttonCancelIncome);
        saveIncomeButton = view.findViewById(R.id.buttonSaveIncome);
        incomeAmountEditText = view.findViewById(R.id.editTextIncomeAmount);
        addIncomeReceiptImageView = view.findViewById(R.id.imageViewAddIncomeReceipt);
        incomeReceiptImageView = view.findViewById(R.id.imageViewIncomeReceipt);


    }

    @Override
    public void onStart() {
        super.onStart();
        addIncomeReceiptImageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });

        saveIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeCategory = categoryIncomeSpinner.getSelectedItem().toString();
                String incomeAmount = String.valueOf(incomeAmountEditText.getText());

                if (incomeCategory.equals("") || incomeAmount.equals("")) {
                    Toast.makeText(getContext(), "Please fill all the fields.", Toast.LENGTH_LONG).show();
                } else {
                    listener.onIncomeCloseClicked();
                    databaseHelper.insertIncomeData(date, incomeCategory, incomeAmount, currentPhotoPath);
                    Toast.makeText(getContext(), "Income added.", Toast.LENGTH_LONG).show();
                }
            }
        });
        closeIncomeButton.setOnClickListener(v -> {
            listener.onIncomeCloseClicked();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getContext(), "Camera permissiom required", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File imageFile = new File(currentPhotoPath);
                incomeReceiptImageView.setImageURI(Uri.fromFile(imageFile));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.ayeshapp.spends.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    public interface OnIncomeCloseListener{
        public void onIncomeCloseClicked();
    }
}