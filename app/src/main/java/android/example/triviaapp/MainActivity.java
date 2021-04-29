package android.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.example.triviaapp.databinding.ActivityMainBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String MESSAGE_ID = "savedData";
    private ActivityMainBinding binding;
    private ArrayList<Question> questionArrayList;
    private int currentIndex = 0;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SharedPreferences getData = getSharedPreferences(MESSAGE_ID, 0);
        currentIndex = getData.getInt("currentIndex", 0);
        score = getData.getInt("score", 0);


        questionArrayList = new Repository().getQuestionBank(questionBank -> {
            Question question = questionBank.get(currentIndex);
            binding.qStatement.setText(question.getStatement());
            binding.qTotal.setText("" + questionBank.size());
            binding.qNum.setText("" + (currentIndex + 1));
            binding.textScore.setText("Score : " + score);
        });

        binding.buttonFalse.setOnClickListener(view -> {
            checkAnswer(false);
            updateQuestion();
        });

        binding.buttonTrue.setOnClickListener(view -> {
            checkAnswer(true);
            updateQuestion();
        });
    }

    private void checkAnswer(boolean answer) {
        boolean verdict = questionArrayList.get(currentIndex).getVerdict();
        String message;
        if(answer == verdict) {
            message = "Correct";
            score++;
            shakeAnimation(true);
        } else {
            message = "Incorrect";
            shakeAnimation(false);
        }

//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion() {
        currentIndex = (currentIndex + 1) % questionArrayList.size();
        Question question = questionArrayList.get(currentIndex);
        binding.qStatement.setText(question.getStatement());
        binding.qNum.setText("" + (currentIndex + 1));
        binding.textScore.setText("Score : " + score);
        SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("currentIndex", currentIndex);
        editor.putInt("score", score);

        editor.apply();
    }

    private void shakeAnimation(boolean answer) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(answer) {
                    binding.qStatement.setTextColor(Color.GREEN);
                } else {
                    binding.qStatement.setTextColor(Color.RED);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                binding.qStatement.setTextColor(getColor(R.color.gray));
            }
        });
    }
}