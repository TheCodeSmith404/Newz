package com.tcssol.newzz.ui.notifications;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcssol.newzz.Data.SavedViewModel;
import com.tcssol.newzz.Model.NewItemClickListner;
import com.tcssol.newzz.Model.News;
import com.tcssol.newzz.Model.NewsAdapter;
import com.tcssol.newzz.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment implements NewItemClickListner {

    private FragmentNotificationsBinding binding;
    private SavedViewModel savedViewModel;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        recyclerView=binding.recycleView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savedViewModel = new ViewModelProvider(this).get(SavedViewModel.class);
        savedViewModel.getAllSavedNews().observe(getViewLifecycleOwner(),data->{
            Log.d("Saved","Setting Adapter");
            adapter=new NewsAdapter(data,getContext(),2,this);
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

    }

    @Override
    public void deleteArticle(News news) {
        SavedViewModel.delete(news);
    }
}