package com.tcssol.newzz.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.tcssol.newzz.Data.Repository;
import com.tcssol.newzz.Data.SavedViewModel;
import com.tcssol.newzz.Model.NewItemClickListner;
import com.tcssol.newzz.Model.News;
import com.tcssol.newzz.Model.NewsAdapter;
import com.tcssol.newzz.Model.SharedViewModel;
import com.tcssol.newzz.databinding.FragmentHomeBinding;
import com.tcssol.newzz.databinding.SelectCategoryBinding;
import com.tcssol.newzz.ui.SelectCategory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements Repository.OnArticlesFetchedListener , NewItemClickListner {

    private FragmentHomeBinding binding;
    private SelectCategoryBinding bindingCategory;
    private RadioGroup group;
    private RecyclerView recyclerView;
    BottomSheetBehavior<View> bottomSheetBehavior;
    SelectCategory selectCategory=new SelectCategory();
    private NewsAdapter adapter;
    private Repository repository;
    private ProgressBar progressBar;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        progressBar=binding.progressBar;
        bindingCategory=SelectCategoryBinding.inflate(inflater,container,false);
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Handle the activity result here
                    }
                }
        );



        View constraintLayout=bindingCategory.fragmentSelectCategory;
//        bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
//        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int screenHeight = displayMetrics.heightPixels;
//        int maxHeight = (int) (screenHeight * 0.50);
//        bottomSheetBehavior.setMaxHeight(maxHeight);
        Repository.OnArticlesFetchedListener fetchedListener=this;
        RadioButton button=binding.radioButton5;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                ArrayList<String> stringData=new ArrayList<>();
                String[] categories = new String[]{"Politics", "Business", "Finance", "Health", "Science", "Environment", "Education", "LifeStyle", "Art", "Travel", "Fashion", "Food"};
                Arrays.sort(categories);
                stringData.addAll(Arrays.asList(categories));
                bundle.putStringArrayList("items",stringData);
                selectCategory.setArguments(bundle);
                showSelectCategories();
            }
        });

        group=binding.radioGroupHome;
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button=group.findViewById(checkedId);
                if(button.getText().toString().equals("Top HeadLines")){
                    repository.getTopHeadlines("in",fetchedListener);
                    progressBar.setVisibility(View.VISIBLE);
                }else if(button.getText().toString().equals("Show All")){

                }else{
                    SharedViewModel.setShowCategory(button.getText().toString());
                }
            }
        });

        recyclerView = binding.homeRecyleView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        repository = new Repository();
        Log.d("Load Data","Initialized Repository");
        LocalDate date=LocalDate.now().minusMonths(1);
        repository.getEverything("Sports", String.valueOf(date),"popularity",this);
        SharedViewModel.getShowCategory().observe(getActivity(),data->{
            repository.getEverything(data, String.valueOf(date),"popularity",this);
            progressBar.setVisibility(View.VISIBLE);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onArticlesFetched(List<News> articles) {
        // Update the UI with the fetched articles
        progressBar.setVisibility(View.GONE);
        adapter = new NewsAdapter(articles,getContext(),1,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onError(String errorMessage) {
        // Handle errors
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void OpenArticle(String link) {
        Uri webpage = Uri.parse(link);
        Log.d("Open Browser","Click Registered");
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(webpage);
        startActivity(i);
//        Intent showArticle=new Intent(getActivity(), BrowseArticle.class);
//        showArticle.putExtra("Url",link);
//        someActivityResultLauncher.launch(showArticle);

    }

    @Override
    public void shareArticle(String link) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Check Out This Article!\n\n"+link);
        startActivity(Intent.createChooser(shareIntent, "Share link via"));



    }

    @Override
    public void saveArticle(News news) {
        SavedViewModel.insert(news);
        Log.d("Saved","Clicked Save");
    }

    @Override
    public void deleteArticle(News news) {

    }

    private void showSelectCategories() {
        selectCategory.show(getActivity().getSupportFragmentManager(),selectCategory.getTag());
    }
}