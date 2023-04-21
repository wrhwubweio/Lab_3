package com.example.lab_2;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

public class TestPage extends Fragment {

    private String TAG = "MyTag";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public TestPage(){
        super(R.layout.test_page);
    }
    private  Intent intent;
    private View mView;
    private TextView timerText;
    private long time;

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
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView text = (TextView) view.findViewById(R.id.nameTest);
        timerText = (TextView) view.findViewById(R.id.timer);

        String result = getArguments().getString("num_test");
        text.setText("test №" + result.toString());
        mView = view;

        Button nextButton_list = (Button) view.findViewById(R.id.endTest);
        nextButton_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).popBackStack();
            }
        });

        new CountDownTimer(600000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                time = millisUntilFinished;
                timerText.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                timerText.setText("00:00:00");
            }
        }.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        setBanner();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(intent != null) {
            requireActivity().stopService(intent);
        }
    }

    private void setBanner(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(getContext()) == true){
                Navigation.findNavController(mView).popBackStack();
                intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, 16);
                return;
            }
        }
        intent = new Intent(getContext(),  TestService.class);
        intent.putExtra("time", Long.valueOf(time));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(intent);
        }
        else{
            requireActivity().startService(intent);
        }
    }



    private void outInfo(String text, Boolean outToast){
        Log.d(TAG, text);
        if(!outToast)
            return;
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                text, Toast.LENGTH_SHORT);
        toast.show();
    }
}

