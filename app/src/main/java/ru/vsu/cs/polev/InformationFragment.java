package ru.vsu.cs.polev;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.DecimalFormat;
import java.util.ArrayList;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.vsu.cs.polev.databinding.FragmentInformationBinding;

public class InformationFragment extends Fragment {

    FragmentInformationBinding binding;

    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private String[] items = {"Да", "Нет"};

    String gotResponse = "";
    String finishPrice = "...";


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_information, container, false);

        Thread gfgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://gas-price.p.rapidapi.com/europeanCountries")
                        .get()
                        .addHeader("content-type", "application/octet-stream")
                        .addHeader("X-RapidAPI-Key", "4d0e98e091msh8354653cad81506p15b59bjsn58db2dcbd0f1")
                        .addHeader("X-RapidAPI-Host", "gas-price.p.rapidapi.com")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    gotResponse = response.body().string();
                    System.out.println(gotResponse);
                    finishPrice = getPrice(gotResponse);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        gfgThread.start();



        try {
            binding.fuelCount.setText(grtAvengereFuelLos());
            binding.distanceCount.setText(String.valueOf(getAllDistance()));
            binding.fuelAllCost.setText(String.valueOf(getAllFuelCost()));
            binding.oneKmPrice.setText(String.valueOf(getOneKmPrice()));
            binding.realFuelPrice.setText(finishPrice + " euro");
        } catch (Exception ex) {

        }

        binding.cleanInfoButton.setOnClickListener(view1 -> {

            builder = new AlertDialog.Builder(this.getActivity());

            builder.setTitle("Вы уверены?");

            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                    db.execSQL("delete from refuels");

                    db.close();
                    dialogInterface.dismiss();
                }
            });

            builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            dialog = builder.create();
            dialog.show();

//            SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
//            db.execSQL("delete from refuels");
//
//            db.close();
        });


        return binding.getRoot();
    }


    private String getPrice(String str) {
        String input = "currency:\"euro\" lpg:\"0,264\" diesel:\"0,578\" gasoline:\"0,572\" country:\"Russia\"";
        String[] parts = input.split(" ");
        String gasolinePrice = "";
        String countryName = "Russia";

        for (String part : parts) {
            if (part.startsWith("gasoline:")) {
                gasolinePrice = part.substring(9).replace("\"", "");
            } else if (part.startsWith("country:")) {
                countryName = part.substring(8).replace("\"", "");
            }
        }

        String[] countryParts = input.split("country:\"" + countryName + "\"");
        if (countryParts.length > 1) {
            String[] gasolineParts = countryParts[1].split("gasoline:\"");
            if (gasolineParts.length > 1) {
                gasolinePrice = gasolineParts[1].split("\"")[0].replace(",", ".");
            }
        }

        System.out.println(gasolinePrice);
        System.out.println(countryName);
        return gasolinePrice;
    }

    private double getOneKmPrice() {
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM refuels;", null);
        ArrayList<Double> litersList = new ArrayList<>();
        ArrayList<Double> distanceList = new ArrayList<>();
        ArrayList<Double> priceList = new ArrayList<>();
        try {
            while(query.moveToNext()){
                litersList.add(query.getDouble(0));
                distanceList.add(query.getDouble(1));
                priceList.add(query.getDouble(2));
            }
        } catch (Exception ex) {

        }
        query.close();
        db.close();

        if (litersList.size() < 2) {
            return 0.0;
        }

        double allDistance = distanceList.get(distanceList.size() - 1) - distanceList.get(0);
        double allCoast = 0.0;
        for (int i = 0; i < distanceList.size(); i++) {
            allCoast += priceList.get(i) * litersList.get(i);
        }
        return allCoast / allDistance;
    }

    private double getAllFuelCost() {
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM refuels;", null);
        ArrayList<Double> fuelList = new ArrayList<>();
        ArrayList<Double> priceList = new ArrayList<>();
        try {
            while(query.moveToNext()){
                fuelList.add(query.getDouble(0));
                priceList.add(query.getDouble(2));
            }
        } catch (Exception ex) {

        }
        query.close();
        db.close();

        double allCoast = 0.0;
        for (int i = 0; i < fuelList.size(); i++) {
            allCoast += fuelList.get(i) * priceList.get(i);
        }
        if (fuelList.size() == 0) {
            return 0.0;
        }
        return Math.round(allCoast);
    }

    private double getAllDistance() {
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM refuels;", null);
        ArrayList<Double> distanceList = new ArrayList<>();
        try {
            while(query.moveToNext()){
                distanceList.add(query.getDouble(1));
            }
        } catch (Exception ex) {

        }
        query.close();
        db.close();

        if (distanceList.size() < 2) {
            return 0.0;
        }

        return distanceList.get(distanceList.size() - 1) - distanceList.get(0);
    }
    private String grtAvengereFuelLos() {
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);

        ArrayList<Double> fuelList = new ArrayList<>();
        ArrayList<Double> distanceList = new ArrayList<>();

        Cursor query = db.rawQuery("SELECT * FROM refuels;", null);
        try {
            while(query.moveToNext()){
                fuelList.add(query.getDouble(0));
                distanceList.add(query.getDouble(1));
            }
        } catch (Exception ex) {

        }
        query.close();
        db.close();

        double tempLoss = 0;
        double ans = 0;
        int counter = 0;

            if (fuelList.size() >= 2 && distanceList.size() >= 2) {
                for (int i = 1; i < fuelList.size(); i++) {
                    counter++;
                    double lastRefuel = fuelList.get(i - 1);
                    double distance = distanceList.get(i) - distanceList.get(i - 1);

                    tempLoss = lastRefuel / distance * 100;

                    ans += tempLoss;
                }
                ans = ans / counter;

                return new DecimalFormat("0.00").format(ans);
            }else {
                return "No inf";
            }
        }

    }




