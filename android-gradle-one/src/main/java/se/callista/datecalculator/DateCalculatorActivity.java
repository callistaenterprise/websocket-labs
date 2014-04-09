package se.callista.datecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.joda.time.LocalDate;

public class DateCalculatorActivity extends Activity {

    private final String TAG = this.getClass().getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_calculator_layout);

        final EditText inputField  = (EditText) findViewById(R.id.editText);
        final EditText resultField = (EditText) findViewById(R.id.editResult);

        Button button = (Button) findViewById(R.id.buttonCalculate);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String daysString = inputField.getText().toString();
                Log.d(TAG, "Days as string: " + daysString);

                try {
                    int days = Integer.parseInt(daysString);
                    Log.d(TAG, "Days: " + days);

                    LocalDate now = LocalDate.now();
                    String newDate = now.plusDays(days).toString("yyyy-MM-dd");
                    Log.d(TAG, "New Date: " + newDate);

                    resultField.setText(newDate + "\n(" + days + " days added to today's date, " + now + ")");

                } catch (NumberFormatException nfe) {
                    Log.e(TAG, "Error parsing: " + daysString, nfe);
                    resultField.setText("\"" + daysString + "\" is not an integer, try again!");
                }
            }
        });
    }
}