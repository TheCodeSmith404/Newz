package com.tcssol.newzz.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tcssol.newzz.Model.SharedViewModel;
import com.tcssol.newzz.databinding.SelectCategoryBinding;

import java.util.List;

public class SelectCategory extends BottomSheetDialogFragment {
    private View view;
    private Button cancel;
    private RadioGroup radioGroup;
    private ScrollView scrollView;
    private Bundle data=new Bundle();
    SharedViewModel viewModel=new SharedViewModel();
    private Button addNew;
    private SelectCategoryBinding binding;



    public SelectCategory() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SelectCategoryBinding.inflate(inflater, container, false);
        view=binding.getRoot();
        cancel=binding.cancelButtonSelectCategory;
        radioGroup=binding.selectCategoryRadioGroup;
        addNew=binding.addButtonSelectCategory;
        Log.d("Sub Categories Error","Creating Select Spinner");
        return  view;

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        data=getArguments();
        List<String> items=data.getStringArrayList("items");
        for(int i=0;i<items.size();i++){
            Log.d("Sub Categories Error","Created Radio");
            RadioButton radioButton = new RadioButton(getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                radioButton.setButtonIcon(null);
            }
            String text=items.get(i);
            radioButton.setText(text);
            radioButton.setId(View.generateViewId()); // Generate unique IDs for each RadioButton
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT, // Set width to match parent
                    RadioGroup.LayoutParams.WRAP_CONTENT // Set height to wrap content
            );
            layoutParams.setMargins(0, 2, 0, 2); // Set top and end margin

            radioButton.setLayoutParams(layoutParams);
            radioGroup.addView(radioButton);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("Sub Categories Error","Radio Clicked");
                RadioButton radio=view.findViewById(checkedId);
                SharedViewModel.setShowCategory(radio.getText().toString());
                dismiss();
                onDestroy();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onDestroy();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        radioGroup=null;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(SharedExpenseViewModel.getChange()) {
//            data = getArguments();
//            String set = data.getString("isEdit");
//            if (set.isEmpty())
//                set = "General";
//            List<String> items = new ArrayList<>(data.getStringArrayList("items"));
//            for (int i = 0; i < items.size(); i++) {
//                RadioButton radioButton = new RadioButton(getContext());
//                String text = items.get(i);
//                radioButton.setText(text);
//                radioButton.setId(View.generateViewId()); // Generate unique IDs for each RadioButton
//                radioGroup.addView(radioButton);
//                if (text.equals(set)) {
//                    radioButton.setChecked(true);
//                }
//            }
//        }
//        SharedExpenseViewModel.setChange(true);
    }
}

