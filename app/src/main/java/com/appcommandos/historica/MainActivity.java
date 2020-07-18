package com.appcommandos.historica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appcommandos.historica.data.AnswerListAsyncRespons;
import com.appcommandos.historica.data.QuestionBank;
import com.appcommandos.historica.model.Question;
import com.appcommandos.historica.model.Score;
import com.appcommandos.historica.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String SCORE_ID = "highestScore";
    private TextView questionTextView;
    private TextView questionCounterTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton preButton;
    private ImageButton nextButton;
    private int currentQuestionIndex=0;
    private List<Question> questionList;
    private TextView scoreTextView;
    private TextView highestScoreTextView;


    private int scoreCounter=0;
    private Score score;
    private Prefs prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = new Score(); //score object
        prefs = new Prefs(MainActivity.this);

        nextButton = findViewById(R.id.next_button);
        preButton = findViewById(R.id.prev_button);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        questionCounterTextView = findViewById(R.id.counter_text);
        questionTextView = findViewById(R.id.question_textview);
        scoreTextView = findViewById(R.id.scoreTextView);
        highestScoreTextView = findViewById(R.id.highestScoreTextView);


        nextButton.setOnClickListener(this);
        preButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        highestScoreTextView.setText(MessageFormat.format("Highest Score: {0}", prefs.getHighScore()));
        currentQuestionIndex = prefs.getState();



        questionList =  new QuestionBank().getQuestion(new AnswerListAsyncRespons() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterTextView.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionArrayList.size()));

            }
        });

//        Log.d("main","OnCreate: "+ questionList);

    }

    @Override
    public void onClick(View v) {

      switch (v.getId()){
          case R.id.prev_button:

              if(currentQuestionIndex > 0){
                  currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                  updateQuestion();
              }

              break;
          case R.id.next_button:

              currentQuestionIndex = (currentQuestionIndex+1) % questionList.size();
              updateQuestion();
              break;
          case R.id.true_button:
              checkAnswer(true);
              updateQuestion();
              break;
          case R.id.false_button:
              checkAnswer(false);
              updateQuestion();
              break;
      }

    }

    private void checkAnswer(boolean userChiseCorrect) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isSnswerTrue();

        int toastMessageId =0;
        if(userChiseCorrect == answerIsTrue){
            fadeView();
            addPoints();
//            currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
//            updateQuestion();
            toastMessageId = R.string.correct_answer;

        }else{
            shakeAnimation();
            deductPoints();
            toastMessageId = R.string.wrong_answer;
        }

        Toast.makeText(MainActivity.this, toastMessageId,Toast.LENGTH_SHORT).show();

    }

    private void updateQuestion(){
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        questionCounterTextView.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionList.size()));

    }

    private void fadeView(){
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goNext();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private void addPoints(){
        scoreCounter +=1;
        score.setScore(scoreCounter);
        scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));

        Log.d("Score","addPoints" + score.getScore());
    }
    private void deductPoints(){
        scoreCounter -=1;
        if(scoreCounter > 0){

            score.setScore(scoreCounter);
            scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        }else {
            scoreCounter =0;
            score.setScore(scoreCounter);
            scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
            Log.d("Score","deductPoints:"+scoreCounter);
        }

    }

    @Override
    protected void onPause() {
        prefs.saveHighestSocre(score.getScore());
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }
    private void goNext(){
        currentQuestionIndex = (currentQuestionIndex+1) % questionList.size();
        updateQuestion();
    }




}
