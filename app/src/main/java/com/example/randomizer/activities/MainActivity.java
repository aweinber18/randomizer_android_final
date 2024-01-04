package com.example.randomizer.activities;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.randomizer.lib.Utils.showInfoDialog;
import static com.example.randomizer.model.RandomNumber.getJSONFromGame;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.randomizer.R;
import com.example.randomizer.databinding.ActivityMainBinding;
import com.example.randomizer.lib.Utils;
import com.example.randomizer.model.RandomNumber;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String mKeyRandomNumber = "MRN";
    private final String mKeyHistory = "HISTORY";
    private RandomNumber mRandomNumber;
    EditText fromInput, toInput;
    private ArrayList<Integer> mNumberHistory;
    private TextView generatedNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeHistoryList(savedInstanceState, mKeyHistory);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        fromInput = findViewById(R.id.from_input);
        toInput = findViewById(R.id.to_input);
        generatedNum = findViewById(R.id.generated_num);

        mRandomNumber = new RandomNumber();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (isValidInputs(fromInput, toInput)){
                    mRandomNumber.setFromTo(
                            Integer.parseInt(fromInput.getText().toString()),
                            Integer.parseInt(toInput.getText().toString())
                    );
                    mRandomNumber.setCurrentRandomNumberToNewlyGenerated();
                    generatedNum.setText(Integer.toString(mRandomNumber.getCurrentRandomNumber()));
                    mNumberHistory.add(Integer.valueOf(mRandomNumber.getCurrentRandomNumber()));

                    Snackbar.make(view, "Look Up! It's your lucky number!", Snackbar.LENGTH_SHORT)
                            .setAnchorView(R.id.fab)
                            .setAction("Action", null).show();
                }
                else {
                    Snackbar.make(view, "Next time, try valid inputs!", Snackbar.LENGTH_SHORT)
                            .setAnchorView(R.id.fab)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private boolean isValidInputs(EditText fromInput, EditText toInput) {
        // ensure there is input, from-to has a positive range, keep inputs < Integer.MAX_VALUE
        return fromInput.length() > 0 && toInput.length() > 0
                && Integer.parseInt(fromInput.getText().toString())
                    < Integer.parseInt(toInput.getText().toString())
                && fromInput.length() < 10 && toInput.length() < 10;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();

        if (itemId == R.id.action_view_history) {
            showHistory();
            return true;
        } else if (itemId == R.id.action_clear_history) {
            clearHistory();
            return true;
        } else if (itemId == R.id.action_about) {
            showAbout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAbout() {
        showInfoDialog(MainActivity.this, "Random Number Generation",
                "This is quite a simple application. \n" +
                        "Just press the button to generate \n" +
                        "a random number between 0 and 100.\n\n" +
                        "By: Avraham Weinberg, MCOO 255");
    }

    private void clearHistory() {
        mNumberHistory.clear();
    }

    private void showHistory() {
        int len = mNumberHistory.toString().length() - 1;

        showInfoDialog(MainActivity.this, "Random Number History",
                mNumberHistory.toString().substring(1, len)); // need to drop the last comma!
    }
    private void initializeHistoryList (Bundle savedInstanceState, String key)
    {
        if (savedInstanceState != null) {
            mNumberHistory = savedInstanceState.getIntegerArrayList (key);
        }
        else {
            String history = getDefaultSharedPreferences (this).getString (key, null);
            mNumberHistory = history == null ?
                    new ArrayList<> () : Utils.getNumberListFromJSONString (history);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        restoreFromPreferences();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveOrDeleteGameInSharedPrefs();
    }

    private void saveOrDeleteGameInSharedPrefs() {
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
        String historyToSave = Utils.getJSONStringFromNumberList(mNumberHistory);
        editor.putString(mKeyHistory, historyToSave);
        editor.apply();
    }
    private void restoreFromPreferences() {
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        String historyToRestore = defaultSharedPreferences.getString(mKeyHistory, null);
        if (historyToRestore != null)
            mNumberHistory = Utils.getNumberListFromJSONString(historyToRestore);
        else
            mNumberHistory = new ArrayList<>();
        updateUI();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(mKeyHistory, mNumberHistory);
    }


    @SuppressLint("SetTextI18n")
    private void updateUI() {
        fromInput.setText(Integer.toString(mRandomNumber.getFrom()));
        toInput.setText(Integer.toString(mRandomNumber.getTo()));
        generatedNum.setText(Integer.toString(mRandomNumber.getCurrentRandomNumber()));
    }

}