package com.example.lab_2;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TestPage extends Fragment {

    private String TAG = "MyTag";
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
    private TextView questText;
    private ArrayList<TextView> ans = new ArrayList<>();
    private ArrayList<ImageButton> ans_button = new ArrayList<>();
    private int step;
    private ImageButton left;
    private ImageButton right;
    private ConstraintLayout quitLayout;
    private Button no_out;
    private Button yes_out;
    private ArrayList<Long> complete;
    private int indexTest;
    private TestPage testPage;
    private View _view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
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
        _view = view;
        String result = getArguments().getString("num_test");
        text.setText("Тест №" + result.toString());
        indexTest = Integer.valueOf(result)-1;
        loadQuestions(Integer.valueOf(result));
        mView = view;

        left = (ImageButton) view.findViewById(R.id.left);
        right = (ImageButton) view.findViewById(R.id.right);

        ans.add((TextView) view.findViewById(R.id.a1_text));
        ans.add((TextView) view.findViewById(R.id.a2_text));
        ans.add((TextView) view.findViewById(R.id.a3_text));
        ans.add((TextView) view.findViewById(R.id.a4_text));

        ans_button.add((ImageButton) view.findViewById(R.id.a1));
        ans_button.add((ImageButton) view.findViewById(R.id.a2));
        ans_button.add((ImageButton) view.findViewById(R.id.a3));
        ans_button.add((ImageButton) view.findViewById(R.id.a4));
        testPage = this;
        Button nextButton_list = (Button) view.findViewById(R.id.endTest);
        nextButton_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getParentFragmentManager();
                DialogFragmentQuit myDialogFragment = new DialogFragmentQuit(testPage);
                myDialogFragment.show(manager, "DialogQuit");
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

        for(int i = 0; i < 4; i++) {
            int finalI = i;
            ans_button.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswer(finalI);
                }
            });
        }

        new CountDownTimer(600000, 1000) {
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                time = millisUntilFinished;
                timerText.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            public void onFinish() {
                timerText.setText("00:00:00");
                loadMainMenu();
            }
        }.start();
    }

    public void loadMainMenu(){
        int count_correct = 0;
        View view = _view;
        for(int i = 0; i < questions.size(); i++){
            if(questions.get(i).CheckCorrect()){
                count_correct++;
            }
        }


        DocumentReference user = db.collection("users").document(auth.getCurrentUser().getUid());


        int finalCount_correct = count_correct;
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    complete = (ArrayList<Long>) document.getData().get("testes_complete");
                    Map<String, ArrayList> testes = new HashMap<>();
                    if (finalCount_correct > questions.size() / 2) {
                        complete.set(indexTest, 1L);
                    } else {
                        complete.set(indexTest, 2L);
                    }
                    testes.put("testes_complete", complete);
                    user.set(testes);
                    outInfo(complete.toString(), true);
                    Navigation.findNavController(view).popBackStack();
                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

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
                                        String text_question =(String) document.getData().get("question");
                                        long correct = (Long) document.getData().get("correct");
                                        Question question = new Question(text_question, answers, (int) correct, finalI);
                                        question.setGiven_answer(-1);
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
        setQuestionSavedAnswer(step);
        for (int i = 0; i < q.getAnswers().size(); i++){
            ans.get(i).setText(q.getAnswers().get(i));
        }
    }

    private void setAnswer(int index){
        if(questions.get(step).getGiven_answer() != -1){
            ans_button.get(questions.get(step).getGiven_answer()).setImageResource(R.drawable.circle);
        }
        questions.get(step).setGiven_answer(index);
        ans_button.get(questions.get(step).getGiven_answer()).setImageResource(R.drawable.circle_done);
    }

    private void setQuestionSavedAnswer(int step){
        for(int i = 0; i <  4; i++){
            ans_button.get(i).setImageResource(R.drawable.circle);
        }
        if(questions.get(step).getGiven_answer() != -1) {
            ans_button.get(questions.get(step).getGiven_answer()).setImageResource(R.drawable.circle_done);
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

