package com.example.lab_2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

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




    }
}
