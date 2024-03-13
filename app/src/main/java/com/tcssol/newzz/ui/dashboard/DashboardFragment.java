package com.tcssol.newzz.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcssol.newzz.Data.Repository;
import com.tcssol.newzz.Data.SavedViewModel;
import com.tcssol.newzz.Model.NewItemClickListner;
import com.tcssol.newzz.Model.News;
import com.tcssol.newzz.Model.NewsAdapter;
import com.tcssol.newzz.databinding.FragmentDashboardBinding;

import java.time.LocalDate;
import java.util.List;

public class DashboardFragment extends Fragment implements Repository.OnArticlesFetchedListener, NewItemClickListner {

    private FragmentDashboardBinding binding;
    private AutoCompleteTextView search;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private Repository repository=new Repository();
    private ProgressBar progressBar;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        search=binding.searchView;
        recyclerView=binding.fragmentSearchRecycleView;
        progressBar=binding.progressBar2;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Handle the activity result here
                    }
                }
        );
        Repository.OnArticlesFetchedListener listener=this;
        LocalDate date=LocalDate.now().minusMonths(1);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    hideKeyboard();
                    progressBar.setVisibility(View.VISIBLE);
                    repository.getEverything(search.getText().toString(),String.valueOf(date),"popularity",listener);
                    return true; // Consume the event
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onArticlesFetched(List<News> articles) {
        progressBar.setVisibility(View.GONE);
        adapter = new NewsAdapter(articles,getContext(),1,this);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);

    }

    @Override
    public void onError(String errorMessage) {
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
    }

    @Override
    public void deleteArticle(News news) {
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}