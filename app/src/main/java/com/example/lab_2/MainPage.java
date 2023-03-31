package com.example.lab_2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import java.util.ArrayList;
import java.util.Random;

public class MainPage extends Fragment {

    private String TAG = "MyTag";

    public MainPage(){
        super(R.layout.main_page_fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String str = bundle.getString("text", "name");
            Log.d(TAG, str);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.main_page_fragment, container, false);
        ListView listView = contentView.findViewById(R.id.listView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView text = (TextView) view.findViewById(R.id.outText);
        getParentFragmentManager().setFragmentResultListener("requestKey",
                this, new FragmentResultListener() {
        @Override
        public void onFragmentResult(@NonNull String requestKey,
                @NonNull Bundle bundle) {
            String result = bundle.getString("text");
            Log.d(TAG, result);
            text.setText(result);
        }
    });
        ListView listView = view.findViewById(R.id.listView);
        ArrayList<Item> items = new ArrayList<>();

        for(int i = 0; i < 200; i++){
            int pic = R.drawable.wait;
            int rand = new Random().nextInt(3);
            switch (rand){
                case 0:
                    pic = R.drawable.wait;
                    break;
                case 1:
                    pic = R.drawable.successfully;
                    break;
                case 2:
                    pic = R.drawable.fail;
                    break;
            }
            int index = i + 1;
            items.add(new Item(pic, "Test " + index));
        }

        ListViewAdapter adapter = new ListViewAdapter(view.getContext(), items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OutInfo("Click test №" + l, true);
            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i){
                    case 0:
                        OutInfo("Cписок закончил прокрутку", true);
                        break;
                    case 1:
                        OutInfo("Cписок начал прокрутку", true);
                        break;
                    case 2:
                        OutInfo("Инерция", true);
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
    }

    private void OutInfo(String text, Boolean outToast){
        Log.d(TAG, text);
        if(!outToast)
            return;
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                text, Toast.LENGTH_SHORT);
        toast.show();
    }
}

