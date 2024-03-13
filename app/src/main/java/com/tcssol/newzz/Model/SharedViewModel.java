package com.tcssol.newzz.Model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    public static MutableLiveData<String> showCategory=new MutableLiveData<>("Top HeadLines");

    public static MutableLiveData<String> getShowCategory() {
        return SharedViewModel.showCategory;
    }

    public static void setShowCategory(String Text) {
        SharedViewModel.showCategory.setValue(Text);
    }
}
