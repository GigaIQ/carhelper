package ru.vsu.cs.polev;

import static android.content.Context.MODE_PRIVATE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class NewFuelFragment extends Fragment {

    private EditText fuelAdd;
    private EditText distanceAdd;
    private EditText litrePrice;

    private RadioGroup radioGroup;

    private Button refuelCompleteButton;

    private String fuelType = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_fuel, container, false);

        fuelAdd = view.findViewById(R.id.fuelAdd);
        distanceAdd = view.findViewById(R.id.distanceAdd);
        litrePrice = view.findViewById(R.id.litrePrice);

        radioGroup = view.findViewById(R.id.buttonGroup);

        refuelCompleteButton = view.findViewById(R.id.refuelCompleteButton);

        refuelCompleteButton.setOnClickListener(view1 -> {
            SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);

            String liters = fuelAdd.getText().toString();
            String distance = distanceAdd.getText().toString();
            String price = litrePrice.getText().toString();

            int checkId = radioGroup.getCheckedRadioButtonId();
            switch (checkId) {
                case R.id.lowOctanefuel: fuelType = "92";
                    break;
                case R.id.highOctanefuel: fuelType = "95";
                    break;
                case R.id.gas: fuelType = "Газ";
                    break;
                case R.id.disel: fuelType = "ДТ";
                    break;
            }

            if (liters.equals("") || distance.equals("") || price.equals("")) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_LONG).show();
            } else {
                if (getLastDistance() > Double.parseDouble(distance)) {
                    Toast.makeText(requireContext(), "Значение одометра не может быть меньше предыдущего", Toast.LENGTH_LONG).show();
                } else {
                    db.execSQL("INSERT OR IGNORE INTO refuels VALUES (" + liters + ", " + distance + ", " + price + ", " + "'" + fuelType + "'" + ");");

                    Cursor query = db.rawQuery("SELECT * FROM refuels;", null);
                    while (query.moveToNext()) {
                        System.out.println("----------------------");
                        System.out.println(query.getDouble(0) + " " + query.getDouble(1) + " " + query.getDouble(2) + " " + query.getString(3));
                    }
                    query.close();
                    db.close();

                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.backFromRefuel(view1);
                }
            }
        });

        distanceAdd.setHint(getLastDistance().toString());

        return view;
    }

    private Double getLastDistance() {
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
        if (distanceList.size() == 0) {
            return 0.0;
        }
        return distanceList.get(distanceList.size() - 1);
    }
}