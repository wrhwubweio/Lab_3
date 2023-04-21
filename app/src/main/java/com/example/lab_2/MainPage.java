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

import java.util.ArrayList;
import java.util.Random;
public class MainPage extends Fragment {

    private String TAG = "MyTag";
    private final String CHANNEL_ID="main page";

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
        showNotification("Main", "opened main menu");
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

        String result = getArguments().getString("name");
        if(result != null) {
            Log.d(TAG, result);
            text.setText(result);
        }
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
                Bundle result = new Bundle();
                result.putString("num_test", String.valueOf(i+1));
                Navigation.findNavController(view).navigate(R.id.action_main_to_test, result);
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

