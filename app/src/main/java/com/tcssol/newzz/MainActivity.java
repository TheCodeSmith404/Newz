package com.tcssol.newzz;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tcssol.newzz.Data.SavedViewModel;
import com.tcssol.newzz.Model.SharedViewModel;
import com.tcssol.newzz.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public SavedViewModel savedViewModel;
    View toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        toolbar=binding.divider;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        savedViewModel=new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(SavedViewModel.class);





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu); // Inflate the menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id == R.id.menu_language) {
            PopupMenu popup = new PopupMenu(this,toolbar, Gravity.RIGHT);
            popup.getMenuInflater().inflate(R.menu.language_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(subitem -> {
                String languageIso=subitem.getTitle().toString();
                String retText="en";
                if(languageIso.equals("English"))
                    retText="en";
                else if(languageIso.equals("Hindi"))
                    retText="hi";
                else if(languageIso.equals("Spanish"))
                    retText="ja";
                else if(languageIso.equals("Japanese"))
                    retText="es";
                SharedViewModel.setLanguage(retText);



                return true;
            });
            popup.show();

            return true;
        }else if(id==R.id.menu_country){
            PopupMenu popup = new PopupMenu(this,toolbar, Gravity.RIGHT);
            popup.getMenuInflater().inflate(R.menu.country_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(subitem -> {
                String countryIso=subitem.getTitle().toString();
                String retText="IN";
                if(countryIso.equals("India"))
                    retText="IN";
                else if(countryIso.equals("USA"))
                    retText="US";
                else if(countryIso.equals("Japan"))
                    retText="JP";
                else if(countryIso.equals("Spain"))
                    retText="ES";
                SharedViewModel.setCountry(retText);

                return true;
            });
            popup.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}