package com.example.lab_2;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StateAdapter  extends RecyclerView.Adapter<StateAdapter.ViewHolder>{
    private String TAG = "MyTag";
    private final LayoutInflater inflater;
    private final List<State> states;
    int selected_position = 0;

    StateAdapter(Context context, List<State> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public StateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_alt, parent, false);
        return new ViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(StateAdapter.ViewHolder holder, int position) {
        State state = states.get(position);
        holder.imageView.setImageResource(state.getImage());
        holder.nameView.setText(state.getName());
        holder.itemView.setBackgroundColor(selected_position == position ? Color.argb(0.5f, 0,0,0) : Color.TRANSPARENT);
        holder.nameView.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutInfo("Click" + holder.getAdapterPosition(), false);
                if (holder.getAdapterPosition()  == RecyclerView.NO_POSITION) return;

                // Updating old as well as new positions
                notifyItemChanged(selected_position);
                selected_position = holder.getAdapterPosition();
                notifyItemChanged(selected_position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView;
        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.IvPicture);
            nameView = view.findViewById(R.id.tvTitle);
        }

    }
    private void OutInfo(String text, Boolean outToast){
        Log.d(TAG, text);
        if(!outToast)
            return;
        //Toast toast = Toast.makeText(getActivity().getApplicationContext(),
        //        text, Toast.LENGTH_SHORT);
        //toast.show();
    }
}