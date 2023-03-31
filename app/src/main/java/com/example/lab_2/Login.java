package com.example.lab_2;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Login extends Fragment {

    private String TAG = "MyTag";

    public Login(){
        super(R.layout.login_fragment);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button nextButton_list = (Button) view.findViewById(R.id.ok);
        Button nextButton_rec = (Button) view.findViewById(R.id.ok2);
        EditText editText = (EditText) view.findViewById(R.id.editText_name);
        nextButton_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result = new Bundle();
                result.putString("text", String.valueOf(editText.getText()));
                getParentFragmentManager().setFragmentResult(
                        "requestKey", result);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_login,
                        new MainPage())
                        .commit();
            }
        });

        nextButton_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result = new Bundle();
                result.putString("text", String.valueOf(editText.getText()));
                getParentFragmentManager().setFragmentResult(
                        "requestKey", result);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_login,
                                new MainPageAlt())
                        .commit();
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

