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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends Fragment {

    private String TAG = "MyTag";
    private View view;
    private DBHelper DB;
    private FirebaseAuth auth;
    public Login(){
        super(R.layout.login_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DB = new DBHelper(getContext());
        super.onViewCreated(view, savedInstanceState);
        Button nextButton_list = (Button) view.findViewById(R.id.ok);
        EditText editTextLogin = (EditText) view.findViewById(R.id.editText_login);
        EditText editTextPassword = (EditText) view.findViewById(R.id.editText_password);

        auth = FirebaseAuth.getInstance();
        this.view = view;

        if (auth.getCurrentUser() != null) {
            //OutInfo("автоматический вход", true);
            LoadMainMenu(auth.getCurrentUser().getEmail());
            return;
        }
        nextButton_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onJoin(editTextLogin.getText().toString(), editTextPassword.getText().toString());
            }
        });
    }


    private void onJoin(String login, String password) {
        if (login.equals("") || password.equals("")) {
            OutInfo("Введите все поля!", true);
            return;
        }
        auth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            LoadMainMenu(login);
                            return;
                        } else {
                            OutInfo("Ошибка", true);
                        }
                    }
                });

    }

    private void LoadMainMenu(String name){
        Bundle result = new Bundle();
        result.putString("name", name);
        Navigation.findNavController(view).navigate(R.id.action_login_to_main, result);
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

