package com.tcssol.newzz.Model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    public static MutableLiveData<String> showCategory=new MutableLiveData<>("Top HeadLines");
    public static MutableLiveData<String> country=new MutableLiveData<>("IN");
    public static MutableLiveData<String> language=new MutableLiveData<>("en");
    public static MutableLiveData<String > getLanguage(){return SharedViewModel.language;}
    public static void setLanguage(String text){SharedViewModel.language.setValue(text);}
    public static String getLanguageText(){return SharedViewModel.language.getValue();}
    public static String getCountryText(){return SharedViewModel.country.getValue();}
    public static MutableLiveData<String > getCountry(){return SharedViewModel.country;}
    public static void setCountry(String text){SharedViewModel.country.setValue(text);}

    public static MutableLiveData<String> getShowCategory() {
        return SharedViewModel.showCategory;
    }

    public static void setShowCategory(String Text) {
        SharedViewModel.showCategory.setValue(Text);
    }
}
