package com.example.twistedpurpose.lab1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The main activity for the guessing game.
 * By Soren Ludwig
 */
public class MainActivity extends AppCompatActivity {
    private final static String HIGHER = "Higher";
    private final static String LOWER = "Lower";
    private final static String WIN = "You win! ";
    private final static String ERROR = "Error!  Not a number!";

    private int numGuesses = 0;

    private void processGuess() {
        int mysteryNum = 5;
        int guess = 0;
        boolean error = false;
        String message = "";

        // Fetch value in text field to perform a guess on
        EditText editText = (EditText) findViewById(R.id.guess_entry);
        String guessStr = editText.getText().toString();

        // Try to parse it into an int, send a toast if it failed to format
        try {
            guess = Integer.parseInt(guessStr);
        } catch (NumberFormatException e){
            error = true;
        }

        if (!error) {
            // If the guessed number is lower than the actual number, print a higher
            if (guess < mysteryNum) {
                message = HIGHER;
                // if it is lower, print lower
            } else if (guess > mysteryNum) {
                message = LOWER;
            } else {
                // If you guessed it, print a winner message and how many tries it took
                String guessMsg = getString(R.string.winning_guess_message, numGuesses);
                message = WIN + guessMsg;
            }
        } else {
            // Otherwise spit out an error
            message = ERROR;
        }

        numGuesses += 1;

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // Clear text on screen
        editText.setText("");
    }
    private final View.OnClickListener btnGuess = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            processGuess();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add a listener for the guess button.
        Button newPicButton = (Button)findViewById(R.id.guess_button);
        newPicButton.setOnClickListener(btnGuess);
    }


}
