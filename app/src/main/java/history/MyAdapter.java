package history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import history.Item;
import history.MyViewHolder;
import ru.vsu.cs.polev.R;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<Item> itemsList;

    public MyAdapter(Context context, List<Item> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.literView.setText(itemsList.get(position).getLiter());
        holder.distanceView.setText(itemsList.get(position).getDiff());
        holder.priceView.setText(itemsList.get(position).getPrice());
        holder.allCoastView.setText(itemsList.get(position).getAllCost());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
