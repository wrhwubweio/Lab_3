package com.example.lab_2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
public class MainPage extends Fragment {

    private String TAG = "MyTag";
    private final String CHANNEL_ID="main page";
    private FirebaseFirestore db;
    private long count_testes;
    ArrayList<Long> complete = new ArrayList<Long>();
    private FirebaseAuth auth;

    public MainPage(){
        super(R.layout.main_page_fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView text = (TextView) view.findViewById(R.id.outText);
        String result = getArguments().getString("name");

        if(result != null) {
            Log.d(TAG, result);
            text.setText(result);
        }

        Button quit = (Button) view.findViewById(R.id.quit);

        ListView listView = view.findViewById(R.id.listView);
        ArrayList<Item> items = new ArrayList<>();

        db.collection("testes").document("info_testes")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                count_testes = (long) document.getData().get("count_testes");
                                CheckTestes(view, listView, items, count_testes);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle result = new Bundle();
                result.putString("num_test", String.valueOf(i+1));
                Navigation.findNavController(view).navigate(R.id.action_main_to_test, result);
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Navigation.findNavController(view).navigate(R.id.action_MainFragment_to_LoginFragment);
            }
        });
    }


    public void CheckTestes(View view, ListView listView, ArrayList<Item> items, long count){
        auth = FirebaseAuth.getInstance();
        String id = auth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        complete = (ArrayList<Long>)document.getData().get("testes_complete");
                        OutInfo(complete.toString(), true);
                        SetListView(view, listView, items, count_testes);
                        //OutInfo(document.getData().get("testes_complete").toString(), true);
                    } else {
                        Map<String, ArrayList> testes = new HashMap<>();
                        for(int i = 0; i < count; i++){
                            complete.add(0L);
                        }
                        testes.put("testes_complete", complete);
                        docRef.set(testes);
                        SetListView(view, listView, items, count_testes);
                    }

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void SetListView(View view, ListView listView, ArrayList<Item> items, long count){
        for(int i = 0; i < complete.size(); i++){
            int pic = R.drawable.wait;
            int rand = complete.get(i).intValue();
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
            ListViewAdapter adapter = new ListViewAdapter(view.getContext(), items);
            listView.setAdapter(adapter);
        }
    }

    private void OutInfo(String text, Boolean outToast){
        Log.d(TAG, text);
        if(!outToast)
            return;
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                text, Toast.LENGTH_SHORT);
        toast.show();
    }




    private void showNotification(String title, String text) {

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(
                5, builder.build()
        );
    }
}

