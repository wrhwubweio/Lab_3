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
        OutInfo("Constructor",false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OutInfo("onCreate",false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button nextButton = (Button) view.findViewById(R.id.ok);
        EditText editText = (EditText) view.findViewById(R.id.editText_name);
        OutInfo("onViewCreated",false);
        nextButton.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OutInfo("onAttach",false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        OutInfo("onCreateView",false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        OutInfo("onViewStateRestored",false);
    }

    @Override
    public void onStart() {
        super.onStart();
        OutInfo("onStart",true);
    }

    @Override
    public void onResume() {
        super.onResume();
        OutInfo("onResume",true);
    }

    @Override
    public void onPause() {
        super.onPause();
        OutInfo("onPause",true);
    }

    @Override
    public void onStop() {
        super.onStop();
        OutInfo("onStop",true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OutInfo("onDestroyView",true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OutInfo("onDestroy",true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        OutInfo("onDetach",true);
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

