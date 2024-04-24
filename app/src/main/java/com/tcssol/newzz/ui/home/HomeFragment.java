package com.tcssol.newzz.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tcssol.newzz.ui.BrowseArticle;
import com.tcssol.newzz.ui.SelectCategory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements Repository.OnArticlesFetchedListener , NewItemClickListner{

    private FragmentHomeBinding binding;
    private SelectCategoryBinding bindingCategory;
    private RadioGroup group;
    private RecyclerView recyclerView;
    BottomSheetBehavior<View> bottomSheetBehavior;
    SelectCategory selectCategory=new SelectCategory();
    private NewsAdapter adapter;
    private Repository repository;
    private ProgressBar progressBar;
    private TextView textView;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    public String langauage="en";
    public String country="IN";
    public String category="Top Headlines";
    public Button retry;
    public boolean[] flipFlops=new boolean[]{false,false,false};

//    public SharedViewModel sharedViewModel=new SharedViewModel();

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
        Repository.OnArticlesFetchedListener fetchedListener=this;
        retry=binding.retryButton;
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchArticles(category);
            }
        });
        RadioButton button=binding.radioButton5;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                ArrayList<String> stringData=new ArrayList<>();
                String[] categories = new String[]{"Tech","World","Finance","Business","Economics","Beauty","Travel","Music","Food","Science","Gaming","Energy"};
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
                    SharedViewModel.setShowCategory("Top HeadLines");

                }else if(button.getText().toString().equals("Show All")){


                }else{
                    SharedViewModel.setShowCategory(button.getText().toString().toLowerCase());
                }
            }
        });
        textView=binding.textViewError;

        recyclerView = binding.homeRecyleView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        repository = new Repository();
        Log.d("Load Data","Initialized Repository");
        LocalDate date=LocalDate.now().minusMonths(1);
        repository.getTopHeadlines(new String[]{langauage},new String[]{country},this);
        SharedViewModel.getShowCategory().observe(getActivity(),data->{
            category = data;
            if(flipFlops[0]) {
                if (data.equals("Top HeadLines")) {
                    repository.getTopHeadlines(new String[]{langauage}, new String[]{country}, this);
                } else {
                    repository.getEverything("*", new String[]{langauage}, new String[]{country}, data, this);
                }
            }else{
                flipFlops[0]=true;
            }
        });
        SharedViewModel.getCountry().observe(getActivity(),data->{
            country = data;
            if(flipFlops[1]) {
                country = data;
                fetchArticles(category);
            }else{
                flipFlops[1]=true;
            }
        });
        SharedViewModel.getLanguage().observe(getActivity(),data->{
            langauage=data;
            if(flipFlops[2]) {
                fetchArticles(category);
            }else {
                flipFlops[2]=true;
            }
        });


        return root;
    }
    public void fetchArticles(String data){
        textView.setVisibility(View.GONE);
        retry.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        if(data.equals("Top HeadLines")){
            repository.getTopHeadlines(new String[]{langauage},new String[]{country},this);
        }else {
            repository.getEverything("*", new String[]{langauage}, new String[]{country}, data, this);
        }
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
        textView.setVisibility(View.GONE);
        retry.setVisibility(View.GONE);
        adapter = new NewsAdapter(articles,getContext(),1,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String errorMessage) {
        // Handle errors
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(errorMessage);
        retry.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        Log.d("Error_Getting Data",errorMessage);

    }

    @Override
    public void OpenArticle(News news) {
        Intent showArticle=new Intent(getActivity(), BrowseArticle.class);
        showArticle.putExtra("News",news);
        someActivityResultLauncher.launch(showArticle);

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
        Toast.makeText(getContext(),"Saved",Toast.LENGTH_SHORT).show();
        Log.d("Saved","Clicked Save");
    }

    @Override
    public void deleteArticle(News news) {

    }


    private void showSelectCategories() {
        selectCategory.show(getActivity().getSupportFragmentManager(),selectCategory.getTag());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}