package e.aakriti.work.podcast_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;

import e.aakriti.work.common.Utility;
import e.aakriti.work.jsonParser.JSONParser;

public class User_details extends AppCompatActivity {

    //***************Globle variables***************
    //**********************************************

    EditText usernameEditText,emailEditText;
    Button submit;
   // TextView t;
    final String emailPattren = "[a-z0-9._]+@[a-z]+\\.[a-z]+";
    private ImageButton ib;
    private Calendar cal;
    private int dayOfMonthinInteger;
    private int monthInInteger;
    private int yearInIntteger;
    private EditText birthdayEditText;
    private int user_age;
    int year,month,day;
    int explicitValue;
    String yearInString, monthInString, daysOfMonthInString;
    String mobileValue, emailValue, tokenValue, usernameValue, dobvalue, explicitvlaue;
    String url,phoneNumber;
    String firstName, lastName, emailConnectWithLinkedin, profilePicUrl;

    String  emailConnectWithFacebook,id;
    String  emailConnectWithGooglePlus;

    //***************String variables to receive value Button pressed***************
    //******************************************************************************

    private static String whichButtonPressed;

    private static  final String deviceToken = "123123";
    Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);

        utility = new Utility(User_details.this);

        //***************Object creation of layout childs***************
        //**************************************************************

        usernameEditText = (EditText) findViewById(R.id.username_edittext);
        emailEditText = (EditText) findViewById(R.id.email_edittext);
        // t = (TextView)findViewById(R.id.txt);
        submit = (Button) findViewById(R.id.submit_userinfo);
        ib = (ImageButton) findViewById(R.id.imageButton1);

        //*********Getting phone number from Login activity***************
        //****************************************************************

        Intent getInfoFromLoginActivityIntent = getIntent();

        //***************Phone Number Of user from connect with phone**************
        //*************************************************************************

        phoneNumber = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa");
        whichButtonPressed = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa.buttonPressed.connectWithPhone");

        //***************info Of user from connect with Linkedin*******************
        //*************************************************************************

        firstName = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName");
        lastName = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa.lastName");
        emailConnectWithLinkedin = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa.email.ConnectWithLinkedIn");
        profilePicUrl = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl");
        id = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa.id");
        //firstNameConectWithfacebook = getInfoFromLoginActivityIntent.getStringExtra("firstname");
        //lastNameConnectWithFacebook = getInfoFromLoginActivityIntent.getStringExtra("lastname");
        emailConnectWithFacebook = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa.email.connectWithFacebook");
        // profilePicUrlOfFacebook = getInfoFromLoginActivityIntent.getStringExtra("profileImage");

        //firstNameConectWithGooglePlus = getInfoFromLoginActivityIntent.getStringExtra("firstname");
        // lastNameConnectWithGooglePlus = getInfoFromLoginActivityIntent.getStringExtra("lastname");
        emailConnectWithGooglePlus = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa.email.connectWithGooglePlus");
        // profilePicUrlOfGooglePlus = getInfoFromLoginActivityIntent.getStringExtra("profileImage");

        whichButtonPressed = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa.buttonPressed.ConnectWithLinkedIn");
        whichButtonPressed = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa.buttonPressed.connectWithFacebook");
        whichButtonPressed = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa.buttonPressed.connectWithGooglePlus");
        whichButtonPressed = getInfoFromLoginActivityIntent.getStringExtra("e.aakriti.work.podcast_app.Whooshkaa.buttonPressed.connectWithPhone");


        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        birthdayEditText = (EditText) findViewById(R.id.bitrhday_edittext);

        if (whichButtonPressed != null && whichButtonPressed.equals("connectWithLinkedinButtonPressed")) {
            usernameEditText.setText(firstName + " " + lastName);
            emailEditText.setText(emailConnectWithLinkedin);
        } else if (whichButtonPressed != null && whichButtonPressed.equals("connectWithFacebookButtonPressed")) {
            usernameEditText.setText(firstName + " " + lastName);
            emailEditText.setText(emailConnectWithFacebook);

        } else if (whichButtonPressed != null && whichButtonPressed.equals("connectWithGooglePlusButtonPressed")) {
            usernameEditText.setText(firstName + " " + lastName);
            emailEditText.setText(emailConnectWithGooglePlus);

        }

        //*************Image button of calendar***************
        //****************************************************

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);

            }

        });
        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
        //**********Text change event handler of birthday.***************
        //***************************************************************

        birthdayEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String age = birthdayEditText.getText().toString();
                int length = age.length();

                if (length == 10) {
                    yearInString = age.substring(0, 4);
                    monthInString = age.substring(5, 7);
                    daysOfMonthInString = age.substring(8, 10);

                    yearInIntteger = Integer.parseInt(yearInString);
                    monthInInteger = Integer.parseInt(monthInString);
                    dayOfMonthinInteger = Integer.parseInt(daysOfMonthInString);

                    if (getAge(yearInIntteger, monthInInteger, dayOfMonthinInteger) >= 18) {
                        alertBox();
                    } else {
                        explicitValue = 0;
                    }

                } else if (length == 9) {
                    char hyphenAtSix = age.charAt(6);
                    char hyphenAtSeven = age.charAt(7);
                    int asciiValueOfHyphenAtSix = (int) hyphenAtSix;
                    int asciiValueOfHyphenAtSeven = (int) hyphenAtSeven;

                    if (asciiValueOfHyphenAtSix == 45) {
                        yearInString = age.substring(0, 4);
                        monthInString = age.substring(5, 6);
                        daysOfMonthInString = age.substring(7, 9);

                        yearInIntteger = Integer.parseInt(yearInString);
                        monthInInteger = Integer.parseInt(monthInString);
                        dayOfMonthinInteger = Integer.parseInt(daysOfMonthInString);

                        if (getAge(yearInIntteger, monthInInteger, dayOfMonthinInteger) >= 18) {
                            alertBox();
                        } else {
                            explicitValue = 0;
                        }


                    } else if (asciiValueOfHyphenAtSeven == 45) {
                        yearInString = age.substring(0, 4);
                        monthInString = age.substring(5, 7);
                        daysOfMonthInString = age.substring(8, 9);

                        yearInIntteger = Integer.parseInt(yearInString);
                        monthInInteger = Integer.parseInt(monthInString);
                        dayOfMonthinInteger = Integer.parseInt(daysOfMonthInString);

                        if (getAge(yearInIntteger, monthInInteger, dayOfMonthinInteger) >= 18) {
                            alertBox();
                        } else {
                            explicitValue = 0;
                        }

                    }

                } else if (length == 8) {
                    yearInString = age.substring(0, 4);
                    monthInString = age.substring(5, 6);
                    daysOfMonthInString = age.substring(7, 8);

                    yearInIntteger = Integer.parseInt(yearInString);
                    monthInInteger = Integer.parseInt(monthInString);
                    dayOfMonthinInteger = Integer.parseInt(daysOfMonthInString);

                    if (getAge(yearInIntteger, monthInInteger, dayOfMonthinInteger) >= 18) {
                        alertBox();
                    } else {
                        explicitValue = 0;
                    }

                }

            }
        });


        //***************Event handling of submit button***************
        //*************************************************************


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (utility.isNetworkAvailable()) {
                        try {
                            // URLEncode user define data
                            if ( whichButtonPressed.equals("connectWithPhoneButtonpressed")) {


                                mobileValue = URLEncoder.encode(phoneNumber);
                                emailValue = URLEncoder.encode(emailEditText.getText().toString());
                                tokenValue = URLEncoder.encode(deviceToken);
                                usernameValue = URLEncoder.encode(usernameEditText.getText().toString());
                                dobvalue = URLEncoder.encode(birthdayEditText.getText().toString());
                                explicitvlaue = URLEncoder.encode(String.valueOf(explicitValue));

                                url = "http://www.whooshkaa.com/index.php/api/UserAfterVerification/mob_no/" + phoneNumber +
                                        "/email/" + emailEditText.getText().toString() + "/token/" + deviceToken + "/username/" + usernameEditText.getText().toString() + "/dob/" + birthdayEditText.getText().toString() + "/explicit/" + String.valueOf(explicitValue);
                            } else if ( whichButtonPressed.equals("connectWithLinkedinButtonPressed")) {
                                url = "http://www.whooshkaa.com/index.php/api/GetExplicits/user/" + id + "/explicit/" + String.valueOf(explicitValue) + "/dob/" + birthdayEditText.getText().toString();
                            } else if ( whichButtonPressed.equals("connectWithFacebookButtonPressed")) {
                                url = "http://www.whooshkaa.com/index.php/api/GetExplicits/user/" + id + "/explicit/" + String.valueOf(explicitValue) + "/dob/" + birthdayEditText.getText().toString();
                            } else if ( whichButtonPressed.equals("connectWithGooglePlusButtonPressed")) {
                                url = "http://www.whooshkaa.com/index.php/api/GetExplicits/user/" + id + "/explicit/" + String.valueOf(explicitValue) + "/dob/" + birthdayEditText.getText().toString();
                            }

                        } catch (Exception e) {

                        }

                        if (usernameEditText.getText().toString().trim().equals("")) {
                            usernameEditText.setError("Username is mandatory");
                        } else if (usernameEditText.getText().toString().length() <= 5) {
                            usernameEditText.setError("Username length must be atleast 6 characters");
                        } else if (emailEditText.getText().toString().trim().equals("")) {
                            emailEditText.setError("Email address is mandatory");
                        } else if (emailEditText.getText().toString().trim().matches(emailPattren) == false) {
                            emailEditText.setError("Please Enter a valid email address");
                        } else if (birthdayEditText.getText().toString().trim().equals("")) {
                            birthdayEditText.setError("Please enter your date of birth");
                        } else {
                            if(whichButtonPressed.equals("connectWithPhoneButtonpressed")) {
                             new JSONParse().execute();
                            //t.setText(phoneNumber+ " "+emailEditText.getText().toString()+" "+deviceToken+" " +usernameEditText.getText().toString()+
                            //  " "+   birthdayEditText.getText().toString()+" "+String.valueOf(explicitValue));
                            Intent userDetailIntent = new Intent(User_details.this, QuestionListingActivity.class);
                            userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName", firstName);
                            //userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.lastName", lastName);
                            //userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl",profilePicUrl);
                              userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.id", id);
                                startActivity(userDetailIntent);
                            finish();
                    }
                    if( whichButtonPressed.equals("connectWithLinkedinButtonPressed")){
                        new JSONParseForSocialLogin().execute();
                        Intent userDetailIntent = new Intent(User_details.this, QuestionListingActivity.class);
                        userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName",firstName);
                        userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.lastName", lastName);
                        userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl", profilePicUrl);
                        userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.id",id);
                        startActivity(userDetailIntent);
                        finish();

                    }
                    else if ( whichButtonPressed.equals("connectWithFacebookButtonPressed")){
                        new JSONParseForSocialLogin().execute();
                        Intent userDetailIntent = new Intent(User_details.this, QuestionListingActivity.class);
                        userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName",firstName);
                        userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.lastName", lastName);
                        userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl", profilePicUrl);
                        userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.id", id);
                        startActivity(userDetailIntent);
                        finish();
                    }
                    else if(whichButtonPressed.equals("connectWithGooglePlusButtonPressed")){
                        new JSONParseForSocialLogin().execute();
                        Intent userDetailIntent = new Intent(User_details.this, QuestionListingActivity.class);
                        userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName",firstName);
                        userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.lastName", lastName);
                        userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl", profilePicUrl);
                        userDetailIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.id",id);
                        startActivity(userDetailIntent);
                        finish();
                    }
                        }


                    }else{
                        Toast.makeText(User_details.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(User_details.this, "Please click on submit", Toast.LENGTH_LONG).show();
    }
    //***************To display explicit alert dialog***************
    //***************************************************************

    public void alertBox(){

        if (getAge(yearInIntteger,monthInInteger,dayOfMonthinInteger)>= 18) {
            final AlertDialog.Builder explicitAlert = new AlertDialog.Builder(User_details.this);
            explicitAlert.setMessage("View explicit content" + "\n" + "Do you want to be served +18 content?");


            explicitAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    explicitValue = 1;
                    //t.setText(String.valueOf(explicitValue));

                }
            });

            explicitAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    explicitValue = 0;
                    // t.setText(String.valueOf(explicitValue));
                    dialog.cancel();
                }
            });
            AlertDialog alert = explicitAlert.create();
            alert.show();
            TextView message = (TextView)alert.findViewById(android.R.id.message);
            message.setTextSize(15);
            message.setGravity(Gravity.CENTER);
        }
    }

    //***************to disdplay calendar dialog***************
    //*********************************************************

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        DatePickerDialog dialog = new DatePickerDialog(this, datePickerListener, year, month, day);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        return dialog;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay)

        {
            birthdayEditText.setText(selectedYear + "-" + (selectedMonth + 1) + "-"
                    + selectedDay);

        }
    };

    //***************Calculate user's age*****************
    //****************************************************

    public int getAge (int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        //if(a < 0)
          //  throw new IllegalArgumentException("Age < 0");
        return a;
    }

    //************************AsyncTask For Connect With Phone*********************
    //*****************************************************************************

    private class JSONParse extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(User_details.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            String res = jParser.getJSONFromUrl(url);
            try {

                JSONObject jsonObject = new JSONObject(res);
                String responce = jsonObject.getString("response");
                String userId = jsonObject.getString("user_id");
                String userName = jsonObject.getString("username");
                String pictureUrl = jsonObject.getString("picImage");
                String eMail = jsonObject.getString("email");
                String message = jsonObject.getString("msg");

                //displaytxt.setText("Responce = " + responce + "\n UserId = " + userId + "\n UserName = " + userName + "\n Email = " + eMail +
                //     "\n Message = " + message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            pDialog.dismiss();


        }
    }


    //***************AsyncTask for api of socialLogin *******************
    //*******************************************************************

    private class JSONParseForSocialLogin extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(User_details.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            String res = jParser.getJSONFromUrl(url);
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            pDialog.dismiss();
            try {

                JSONObject jsonObject = new JSONObject(res);
                String responce = jsonObject.getString("response");
                String explicit = jsonObject.getString("user_id");
                String msg = jsonObject.getString("username");

                //displaytxt.setText("Responce = " + responce + "\n UserId = " + userId + "\n UserName = " + userName + "\n Email = " + eMail +
                //     "\n Message = " + message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
