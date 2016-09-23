package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private TextView ghostText;
    private TextView gameStatus;
    private Button bChallenge;
    private  Button bRestart;
    private TextView userScore,computerScore;
    private int scoreUser=0,scoreComputer=0;

    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        gameStatus =(TextView) findViewById(R.id.gameStatus);
        ghostText =(TextView) findViewById(R.id.ghostText);
        bChallenge = (Button) findViewById(R.id.bChallenge);
        bRestart =(Button) findViewById(R.id.bRestart);
        userScore=(TextView) findViewById(R.id.scoreUser);
        computerScore =(TextView) findViewById(R.id.scoreComputer);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
        bChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkChallenge();
            }
        });
        bRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart(null);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("TEXT", ghostText.getText().toString());
        outState.putString("LABEL", gameStatus.getText().toString());
        outState.putInt("SCOREUSER", scoreUser);
        outState.putInt("SCORECOMPUTER", scoreComputer);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            ghostText.setText(savedInstanceState.getString("TEXT"));
            gameStatus.setText(savedInstanceState.getString("LABEL"));
            userTurn = true;  // a saved state is always user turn
            scoreUser = savedInstanceState.getInt("SCOREUSER");
            scoreComputer = savedInstanceState.getInt("SCORECOMPUTER");
            computerScore.setText("Computer's Score: "+ scoreComputer);
            userScore.setText("User's Score: "+ scoreUser);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        String text= ghostText.getText().toString();
        String newWord;
        if(text.length()>3  && dictionary.isWord(text)){
            gameStatus.setText("Computer Wins: "+ text + " is a valid word");
            scoreComputer+=1;
            computerScore.setText("Computer's Score: "+ scoreComputer);
            return;
        }
        else {
            newWord=dictionary.getAnyWordStartingWith(text);
           // Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
            if(newWord==null){
                gameStatus.setText("Computer Wins: "+ text + " is not a valid prefix");
                scoreComputer+=1;
                computerScore.setText("Computer's Score: "+ scoreComputer);
                return;
            }
            else{
                text+=newWord.charAt(text.length());
                ghostText.setText(text);
            }
        }
        userTurn = true;
        label.setText(USER_TURN);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char in =(char) event.getUnicodeChar();
        String ghostString = ((String) (ghostText.getText())).trim();
        if((in>='a' && in<='z') || ( in >='A' && in<='Z')){
            ghostString+=in;
            gameStatus.setText(COMPUTER_TURN);
            ghostText.setText(ghostString);
            computerTurn();
        }
        else
        Toast.makeText(this,"Please enter character between A-Z or a-z only",Toast.LENGTH_SHORT).show();
        return super.onKeyUp(keyCode, event);
    }
    private void checkChallenge(){
        String text = ghostText.getText().toString();
        String newWord;
        if(text.length()>3 && dictionary.isWord(text)){
            gameStatus.setText("User Wins: "+ text + " is a valid word");
            scoreUser+=1;
            userScore.setText("User's Score: "+ scoreUser);
            return;
        }
        else{
            newWord =dictionary.getAnyWordStartingWith(text);
            if(newWord==null){
                gameStatus.setText("User Wins: "+ text + " is not a valid prefix");
                scoreUser+=1;
                userScore.setText("User's Score: "+ scoreUser);
                return;
            }
            else{
                gameStatus.setText("Computer Wins: "+ text + " is  a valid prefix eg.: "+newWord);
                scoreComputer+=1;
                computerScore.setText("Computer's Score: "+ scoreComputer);
                return;
            }
        }
    }
}
