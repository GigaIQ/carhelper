package ru.vsu.cs.polev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.navigation.NavigationBarView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    NavController navController;

    String gotResponse = "";
    String finishPrice = "0.0";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.informationFragment);
        NavigationUI.setupWithNavController((NavigationBarView) findViewById(R.id.navView), navController);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS refuels (fuel REAL, distance REAL, fuel_price REAL, fuel_type TEXT)");



//        HttpResponse<String> response = Unirest.get("https://api.collectapi.com/gasPrice/stateUsaPrice?state=WA")
//                .header("content-type", "application/json")
//                .header("authorization", "apikey your_token")
//                .asString();


    }



    public void backFromRefuel(View view) {
        navController.popBackStack();
    }

    public String getPrice() {
//        return String.valueOf(Double.parseDouble(finishPrice) * 90);
        return  finishPrice;
    }

}