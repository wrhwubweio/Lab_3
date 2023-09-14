package com.example.lab_2;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private ArrayList<Question> questions = new ArrayList<Question>();
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private long count_questions;
    private int num_test = 0;
    private TextView questText;
    private ArrayList<Button> ans = new ArrayList<>();
    private int step;
    private Button left;
    private Button right;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
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
        questText = (TextView) view.findViewById(R.id.question);
        timerText = (TextView) view.findViewById(R.id.timer);

        String result = getArguments().getString("num_test");
        text.setText("test №" + result.toString());
        loadQuestions(Integer.valueOf(result));
        mView = view;

        left = (Button) view.findViewById(R.id.left);
        right = (Button) view.findViewById(R.id.right);

        ans.add( (Button) view.findViewById(R.id.a1));
        ans.add( (Button) view.findViewById(R.id.a2));
        ans.add( (Button) view.findViewById(R.id.a3));
        ans.add( (Button) view.findViewById(R.id.a4));
        Button nextButton_list = (Button) view.findViewById(R.id.endTest);
        nextButton_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).popBackStack();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStep(-1);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStep(1);
            }
        });

        new CountDownTimer(600000, 1000) {
            public void onTick(long millisUntilFinished) {
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

    private void loadQuestions(int indexTest){
        count_questions = 0;
        db.collection("testes")
                .document("test_"+indexTest)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                count_questions = (long) document.getData().get("count_questions");
                                findQuestions(indexTest);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }


    private void findQuestions(int index_test){
        for(int i = 0; i < count_questions; i++){
            int finalI = i;
            if(questions.size() <= i) {
                db.collection("testes")
                        .document("test_" + index_test).collection("questions").document("question_" + (i + 1))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        ArrayList<String> answers = (ArrayList<String>) document.getData().get("answers");
                                        String text_question = finalI + ": " + (String) document.getData().get("question");
                                        Question question = new Question(text_question, answers);
                                        question.setIndex(finalI);
                                        outInfo(questions.size() + " : " + finalI, true);
                                        questions.add(question);
                                        if(questions.size() == count_questions){
                                            Collections.reverse(questions);
                                            setQuestion(0);
                                        }
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
            }
        }
    }

    private void AddStep(int dir){
        step += dir;
        if(step == count_questions){
            step = 0;
        }
        if(step < 0){
            step = (int) (count_questions - 1);
        }
        setQuestion(step);
    }

    private void setQuestion(int index){
        Question q = questions.get(index);
        questText.setText(q.getQuestion());

        for (int i = 0; i < q.getAnswers().size(); i++){
            ans.get(i).setText(q.getAnswers().get(i));
        }
    }


    private void outInfo(String text, Boolean outToast){
        Log.d(TAG, text);
        if(!outToast)
            return;
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                text, Toast.LENGTH_LONG);
        toast.show();
    }
}

