package com.ayeshapp.spends.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ayeshapp.spends.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddIncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddIncomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        Spinner catagotyIncomeSpinner = view.findViewById(R.id.catagory_income_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.CatagoryIncome) );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catagotyIncomeSpinner.setAdapter(adapter);

        Button close = view.findViewById(R.id.item_cancel);
        close.setOnClickListener(v -> {
            listener.onIncomeCloseClicked();
        });
    }

    public interface OnIncomeCloseListener{
        public void onIncomeCloseClicked();
    }
}