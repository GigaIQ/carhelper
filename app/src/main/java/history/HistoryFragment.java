package history;

import static android.content.Context.MODE_PRIVATE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


import ru.vsu.cs.polev.R;
import ru.vsu.cs.polev.databinding.FragmentHistoryBinding;

public class HistoryFragment extends Fragment {

    FragmentHistoryBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);

        List<Item> items = new ArrayList<>();

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
            ex.printStackTrace();
        }
        query.close();
        db.close();

        try {
            double allCoast = priceList.get(priceList.size() - 1);
            double diff = 0;
            double liter = litersList.get(litersList.size() - 1);
            double price = priceList.get(priceList.size() - 1) ;

            for (int i = litersList.size() - 1; i >= 0; i--) {
                try {
                    diff = distanceList.get(i + 1) - distanceList.get(i );
                    allCoast = priceList.get(i) * litersList.get(i);
                    liter = litersList.get(i);
                    price = priceList.get(i);



                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (i != litersList.size() - 1) {
                    items.add(new Item(String.valueOf(liter), Double.toString(diff), Double.toString(price), Double.toString(allCoast)));
                }
                if (i == litersList.size() - 1) {
                    items.add(new Item(String.valueOf(liter), Double.toString(diff), Double.toString(price),
                            String.format(Double.toString(priceList.get(priceList.size() - 1) * litersList.get(priceList.size() - 1)), "0.0")));
                }
            }
        }catch (Exception ex) {

            ex.printStackTrace();
        }

        if (litersList.size() == 0) {
            items.add(new Item(String.valueOf(0), Double.toString(0), Double.toString(0), Double.toString(0)));
        }

        binding.recycleView.setLayoutManager(new LinearLayoutManager(requireContext().getApplicationContext()));
        binding.recycleView.setAdapter(new MyAdapter(getActivity().getApplicationContext(), items));

        return binding.getRoot();
    }

}