package com.example.stiregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText editTextStudentNumber, editTextLastName, editTextFirstName, editTextMiddleName, editTextBirthday, editTextAge;
    private TextView asteriskStudentNumber, asteriskLastName, asteriskFirstName, asteriskBirthday, asteriskAge;
    private Button buttonRegister;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        //initialize input fields
        editTextStudentNumber = findViewById(R.id.editTextStudentNumber);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextMiddleName = findViewById(R.id.editTextMiddleName);
        editTextBirthday = findViewById(R.id.editTextBirthday);
        editTextAge = findViewById(R.id.editTextAge);
        buttonRegister = findViewById(R.id.buttonRegister);

        //Asterisk TextViews
        asteriskStudentNumber = findViewById(R.id.AsteriskStudentNumber);
        asteriskLastName = findViewById(R.id.AsteriskLastName);
        asteriskFirstName = findViewById(R.id.AsteriskFirstName);
        asteriskBirthday = findViewById(R.id.AsteriskBirthday);
        asteriskAge = findViewById(R.id.AsteriskAge);

        calendar = Calendar.getInstance();

        //Setup date picker for Birthday
        editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showDatePickerDialog();}

        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { registerStudent();}
        });
    }

    private void showDatePickerDialog() {
        int year =calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                calendar.set(Calendar.YEAR, selectedYear);
                calendar.set(Calendar.MONTH, selectedMonth);
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                updateBirthdayField();
                updateAgeField();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateAgeField() {
        int age = calculateAge(calendar);
        editTextAge.setText(String.valueOf(age));
    }

    private int calculateAge(Calendar birthDate) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        return age;
    }

    private void updateBirthdayField() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        editTextBirthday.setText(dateFormat.format(calendar.getTime()));
    }

    private void registerStudent() {
        String studentNumber = editTextStudentNumber.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String middleName = editTextMiddleName.getText().toString().trim();
        String birthday = editTextBirthday.getText().toString().trim();
        String ageStr =editTextAge.getText().toString().trim();

        //reset Visibility of Asterisks
        asteriskStudentNumber.setVisibility(View.GONE);
        asteriskLastName.setVisibility(View.GONE);
        asteriskFirstName.setVisibility(View.GONE);
        asteriskBirthday.setVisibility(View.GONE);
        asteriskAge.setVisibility(View.GONE);

        boolean isValid = true;
        if (TextUtils.isEmpty(studentNumber)){
            asteriskStudentNumber.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (TextUtils.isEmpty(lastName)){
            asteriskLastName.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (TextUtils.isEmpty(firstName)){
            asteriskFirstName.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (TextUtils.isEmpty(birthday)){
            asteriskBirthday.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (TextUtils.isEmpty(ageStr)){
            asteriskAge.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (!isValid){
            Toast.makeText(this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age =Integer.parseInt(ageStr);

        boolean isInserted = db.insertData(studentNumber, lastName, firstName, middleName, birthday, age);
        if (isInserted){
            Toast.makeText(this, "Student registered successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        }else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        editTextStudentNumber.setText("");
        editTextLastName.setText("");
        editTextFirstName.setText("");
        editTextMiddleName.setText("");
        editTextBirthday.setText("");
        editTextAge.setText("");

        //Hide Asterisks
        asteriskStudentNumber.setVisibility(View.GONE);
        asteriskLastName.setVisibility(View.GONE);
        asteriskFirstName.setVisibility(View.GONE);
        asteriskBirthday.setVisibility(View.GONE);
        asteriskAge.setVisibility(View.GONE);
    }
}