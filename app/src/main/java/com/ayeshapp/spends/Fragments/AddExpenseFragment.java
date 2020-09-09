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
 * Use the {@link AddExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddExpenseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner catagotySpinner = (Spinner) view.findViewById(R.id.catagory_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Catagory) );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catagotySpinner.setAdapter(adapter);

        Button close = view.findViewById(R.id.item_cancel);
        close.setOnClickListener(v -> {
            listener.onCloseClicked();
        });

    }

    public interface OnCloseListener{
        public void onCloseClicked();
    }
}