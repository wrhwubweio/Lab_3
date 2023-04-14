package com.example.lab_2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class Login extends Fragment {

    private String TAG = "MyTag";
    private View view;

    public Login(){
        super(R.layout.login_fragment);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button nextButton_list = (Button) view.findViewById(R.id.ok);
        EditText editText = (EditText) view.findViewById(R.id.editText_name);

        nextButton_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result = new Bundle();
                result.putString("name", String.valueOf(editText.getText()));
                Navigation.findNavController(view).navigate(R.id.action_login_to_main, result);
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

