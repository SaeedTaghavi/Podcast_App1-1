
package e.aakriti.work.podcast_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import e.aakriti.work.common.SharedData;
import e.aakriti.work.jsonParser.JSONParser;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
		GoogleApiClient.ConnectionCallbacks {
	

	//***************Globle variables***************
	//**********************************************
    
    //User data collected from login with social

	static String userPicturUrlLinkedIn;
	static String connectWithPhoneNumber=null;
	HttpResponse httpResponse;
	String responseFromJSON,url,msg,pic,firstNameFromSocialLogin,lastNameFromScialLogin,fly;
	public static String id;
	int responseInInt;
	String connectWithLinkedinUrl,responseFromJSONOfConnectWithLinkedin;

	//***************Used inplace of device token generated at the time of GCM***************
	//***************************************************************************************

	private static final String deviceToken = "123123";

	String mobileValue, deviceValue;
	TextView t,t2,t3;

	//***************String constance to check which Button pressed***************
	//****************************************************************************

	public static final String isConnectWithPhoneButtonPressed = "connectWithPhoneButtonpressed";
	public static final String isConnectWithLinkedinButtonPressed = "connectWithLinkedinButtonPressed";
	public static  final String isConnectWithFacebookButtonPressed = "connectWithFacebookButtonPressed";
	public static final String isConnectWithGooglePlusButtonPressed = "connectWithGooglePlusButtonPressed";

	//User data collected from login with social

	public static String firstNameFromGoogle = null;
	public static String lastNameFromGoogle = null;
	public static String email = null;
	public static Bitmap userPicture = null;
	public static String userPictureUrlGoogle = null;
	public static String fullName = null;
	public static AccessToken accessToken = null;
	static String accessUrl = null;
	public static String uid = null;
	HashMap<String, String> all;
	public static String emailFromGoogle;
	public static String firstNameFromLinkedin,lastNameFromLinkedin;
	public static String idFromLinkedin;
	public static String googleAccessToken = null;

	//From Database
	public static String imageUrlFromFacebook = null;
	public static String imageUrlFromGoogle = null;
	public static String firstNameFromDatabase = null;
	public static String lastNameFromDatabase = null;
	public static String response = null;

	public static String errorState = null;



	String androidId;

	//For google+
	private static final int RC_SIGN_IN = 9001;
	private GoogleApiClient mGoogleApiClient;
	SignInButton googlesignInButton;
	private static String mAccountName = " ";

	//for facebook
	private LoginButton facebookLogInButton;
	public static String facebookUid = null;
	public static String facebookFirstName = null;
	public static String facebookLastName = null;
	public static String facebookEmail = null;
	public static String facebookPictureUrl = null;
	private CallbackManager callbackManager;
	public static String facebookAccessUrl = null;
	static LoginResult facebookloginResult = null;

	SharedData shared;


	Button connectWithPhoneButton,connectWithFacebookButton,connectWithGooglePlusButton,
			connectWithLinkedInButton;
	DigitsAuthButton digitsButton;
	int flag_Fp=0;
	SharedData shareData;
	private static final String TWITTER_KEY = "xavtqj9txo3TvlKJxz18hR6zY";
	private static final String TWITTER_SECRET = "5yBzBWr7C0yM3KPypNu6V3zSrfTi7L7E1bNsWvrKLcYZZLlfUF";

	private static final String host = "api.linkedin.com";
	private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
			"(email-address,first-Name,last-Name,picture-url,id)";
	Boolean useStoredImage = true;
	public static String emailFromPhone,usernameFromPhone,userPicturUrlPhone,userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();

		setContentView(R.layout.login);

		shared = new SharedData(LoginActivity.this);

		if(shared.getBoolean("loggedin",false)){
			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			intent.putExtra("e.aakriti.work.podcast_app.LoginActivity.useStoredImage",useStoredImage);
			startActivity(intent);
			finish();
		}


		//***************Connect With Phone ***************
		//*************************************************

		connectWithPhoneButton =(Button)findViewById(R.id.connectWithPhone);
		t = (TextView)findViewById(R.id.txt);

		TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
		Fabric.with(this, new TwitterCore(authConfig), new Digits());
		digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);

		//***************Fabric button onClick action***************
		//**********************************************************

		digitsButton.setCallback(new AuthCallback() {
			@Override
			public void success(DigitsSession session, String phoneNumber) {
				// TODO: associate the session userID with your user model

				url = "http://www.whooshkaa.com/index.php/api/UserVerification/mob_no/" + phoneNumber + "/token/" + deviceToken;
				connectWithPhoneNumber = phoneNumber;
				new JSONParse().execute();

				Toast.makeText(LoginActivity.this, " " + phoneNumber, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void failure(DigitsException exception) {
				Log.d("Digits", "Sign in with Digits failure", exception);
			}
		});

		//***************button for connect with phone******************
		//**************************************************************

		connectWithPhoneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shared.putBoolean("loggedin", true);
				digitsButton.performClick();//Click action is performed on button given by Fabric
			}
		});


		connectWithFacebookButton = (Button) findViewById(R.id.fbConnectButton);
		connectWithGooglePlusButton = (Button)findViewById(R.id.googlePlusConnectButton);
		connectWithLinkedInButton = (Button)findViewById(R.id.inConnectButton);



		//****************Configure facebook Login Button***************
		//**************************************************************
		facebookLogInButton = (LoginButton) findViewById(R.id.facebookLogInButton);
		facebookLogInButton.setReadPermissions(Arrays.asList("email, public_profile, user_photos, user_status, user_about_me"));



		//***************EventHandler for Connect With Facebook***************
		//********************************************************************
         connectWithFacebookButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				shared.putBoolean("loggedin", true);
				//Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				//startActivity(intent);
				//finish();
				LoginManager.getInstance().logOut();
				facebookLogInButton.performClick();

			}
		});
		//****************Registering of facebook call back button***************
		//***********************************************************************

		facebookLogInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				accessToken = loginResult.getAccessToken();
				GraphRequest request = GraphRequest.newMeRequest(
						loginResult.getAccessToken(),
						new GraphRequest.GraphJSONObjectCallback() {
							@Override
							public void onCompleted(
									JSONObject object,
									GraphResponse response) {
								try {
									facebookUid = object.getString("id");
									fullName = object.getString("name");
									facebookFirstName = object.getString("first_name");
									facebookLastName = object.getString("last_name");
									facebookEmail = object.getString("email");
									facebookPictureUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
									if (facebookFirstName != null & facebookLastName != null) {

										facebookAccessUrl = "http://www.whooshkaa.com/index.php/api/SocialLogIn/email/" + facebookEmail + "/" +
												"fn/" + facebookFirstName + "/ln/" + facebookLastName + "/profile_pic/" + facebookUid + "/token/" + deviceToken;
										new DoTask().execute(facebookAccessUrl);
									}

								} catch (JSONException e) {
									Log.e("error on parsing", e.toString());

								}
							}
						});
				Bundle parameters = new Bundle();
				parameters.putString("fields", "picture,email,first_name,last_name,name");
				request.setParameters(parameters);
				request.executeAsync();

			}

			@Override
			public void onCancel() {
				//Toast.makeText(LoginActivity.this, "Login canceled", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onError(FacebookException e) {
				Toast.makeText(LoginActivity.this, "Login error",
						Toast.LENGTH_LONG).show();
			}

		});


		//***************Google code to handle aip of google pluse**************
		//**********************************************************************

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
				.enableAutoManage(this, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.addApi(Plus.API)
				.build();
		googlesignInButton = (SignInButton) findViewById(R.id.googleLogInButon);
		googlesignInButton.setSize(SignInButton.SIZE_STANDARD);
		googlesignInButton.setScopes(gso.getScopeArray());


		//***************EventHandler for Connect with Google Plus***************
		//***********************************************************************
		
		connectWithGooglePlusButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shared.putBoolean("loggedin", true);
				if(mGoogleApiClient.isConnected()){
					Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
					mGoogleApiClient.disconnect();
					mGoogleApiClient.connect();
				}
				signIn();
				
			}
		});
		
        //****************EventHandler for Connect With LinkedIn***************
		//*********************************************************************
		
		connectWithLinkedInButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shared.putBoolean("loggedin", true);
				signinLinkedIn();

										}
		});

	}



	/*@Override
	protected void onStart() {
		super.onStart();
		Intent i = new Intent("e.aakriti.work.podcast_app.SENDBROADCAST");
		i.putExtra("CONNECTIONCHECKED", "yes");
		sendBroadcast(i);

		//check for internet connection
		if(!NetworkUtil.checkNetworkAvailable(this)){
			Intent intent = new Intent(this, OffLineModeActivity.class);
			startActivity(intent);
			finish();
		}

	}*/

	//*************[REQUIRE FOR GOOGLE+ ACCOUNT CONNECTION-START]**************
	//*************************************************************************

	private void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	public void onConnected(Bundle bundle) {

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}


	//************Handle Sign In for Google Account**************
	//***********************************************************

	private void handleGoogleLogInt(GoogleSignInResult result) {
		if (result.isSuccess()) {
			// Signed in successfully, show authenticated UI.
			GoogleSignInAccount acct = result.getSignInAccount();
			Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			emailFromGoogle = acct.getEmail();
			Person.Image personPhoto = currentPerson.getImage();
			Person.Name name = currentPerson.getName();
			fullName = currentPerson.getDisplayName();

			uid = currentPerson.getId();

			if (name.hasFamilyName()) {
				lastNameFromGoogle = name.getFamilyName();

			}
			if (name.hasGivenName()) {
				firstNameFromGoogle = name.getGivenName();
			}

			if (currentPerson.hasImage()) {
				userPictureUrlGoogle = personPhoto.getUrl();
			}
		}


		if( firstNameFromGoogle != null & lastNameFromGoogle != null) {
			accessUrl = "http://www.whooshkaa.com/index.php/api/SocialLogIn/email/" + emailFromGoogle + "/" +
					"fn/" + firstNameFromGoogle + "/ln/" + lastNameFromGoogle + "/profile_pic/" + uid + "/token/" + deviceToken;
			new DoTaskGoogle().execute(accessUrl);
		}


	}
	//***************End of google sign In***************
	//***************************************************



	//***************Method to check authentication of sign in with linkedin***************
	//*************************************************************************************

	public void signinLinkedIn(){

		LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
			public void onAuthSuccess() {
				getUserData();

			}

			public void onAuthError(LIAuthError error) {
				Toast.makeText(getApplicationContext(), "Failed " + error.toString(), Toast.LENGTH_SHORT).show();
			}
		}, true);
	}

	//***************ActivityResult for Facebook, Google+, LinkedIn***************
	//****************************************************************************

	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			if (resultCode == RESULT_OK) {

				GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
				handleGoogleLogInt(result);
			} else {
			}
		}else {
			callbackManager.onActivityResult(requestCode, resultCode, data);

		}

	}


	private static Scope buildScope(){
		return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
	}

	//***************Getting linkedIn JsonObject***************
	//*********************************************************

	public void getUserData(){
		APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
		apiHelper.getRequest(LoginActivity.this, topCardUrl, new ApiListener() {
			@Override
			public void onApiSuccess(ApiResponse result) {
				try {

					setUserProfile(result.getResponseDataAsJson());


				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			//*****************Parcing linkedIn JSONResponce Object***************
			//********************************************************************

			public void setUserProfile(JSONObject response) {

				try {

					email = response.get("emailAddress").toString();
					firstNameFromLinkedin = response.get("firstName").toString();
					lastNameFromLinkedin = response.get("lastName").toString();
					userPicturUrlLinkedIn = response.get("pictureUrl").toString();
					idFromLinkedin = response.get("id").toString();


					if(firstNameFromLinkedin != null && lastNameFromLinkedin !=null) {
						connectWithLinkedinUrl = "http://www.whooshkaa.com/index.php/api/SocialLogIn/email/" + email + "/fn/" + firstNameFromLinkedin + "/ln/" + lastNameFromLinkedin + "/profile_pic/" + idFromLinkedin + "/token/" + deviceToken;
						new JSONForLinkedin().execute();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onApiError(LIApiError error) {


			}
		});
	}


	//***************AsyncTask for connect with phone****************
	//***************************************************************

	private class JSONParse extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			JSONParser jsonParser = new JSONParser();

			// *****************Getting JSON from URL of Connect With Phone****************
			//*****************************************************************************

			String res = jsonParser.getJSONFromUrl(url);
			try {

				JSONObject jsonObject = new JSONObject(res);
				responseFromJSON = jsonObject.getString("response");
				emailFromPhone = jsonObject.getString("email");
				id = jsonObject.getString("user_id");
				userPicturUrlPhone = jsonObject.getString("picImage");
				usernameFromPhone = jsonObject.getString("username");




			} catch (JSONException e) {
				e.printStackTrace();
			}
			return responseFromJSON;
		}

		@Override
		protected void onPostExecute(String res) {
			pDialog.dismiss();
			t.setText(usernameFromPhone);
			if(responseFromJSON.equals("0")){
				Intent connectWithPhoneIntent = new Intent(LoginActivity.this, User_details.class);
				connectWithPhoneIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa",connectWithPhoneNumber);
				connectWithPhoneIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.buttonPressed.connectWithPhone",isConnectWithPhoneButtonPressed);
				connectWithPhoneIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName",usernameFromPhone);
				//connectWithPhoneIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.id", id);
				//connectWithPhoneIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl",userPicturUrlPhone);
						startActivity(connectWithPhoneIntent);
				finish();
			}
			else if(responseFromJSON.trim().equals("1")) {
				Intent connectWithPhoneIntent = new Intent(LoginActivity.this, MainActivity.class);
				connectWithPhoneIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName",usernameFromPhone);
				//connectWithPhoneIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl",userPicturUrlPhone);
				//connectWithPhoneIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.id", id);
				startActivity(connectWithPhoneIntent);
				finish();
			}
		}
	}

	//***************AsnynTask for connet with Linkedin**************
	//***************************************************************

	private class JSONForLinkedin extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			JSONParser jsonParser = new JSONParser();

			//****************Getting JSON from URL of Connect with Linkedin*****************
			//*******************************************************************************

			String res = jsonParser.getJSONFromUrl(connectWithLinkedinUrl);

			try {

				JSONObject jsonObject = new JSONObject(res);

				responseFromJSONOfConnectWithLinkedin = jsonObject.getString("response");
				id = jsonObject.getString("id");
				pic = jsonObject.getString("pic");
				firstNameFromSocialLogin = jsonObject.getString("firstname");
				lastNameFromScialLogin = jsonObject.getString("lastname");
				fly = jsonObject.getString("fli");
				msg = jsonObject.getString("msg");

			} catch (JSONException e) {
				e.printStackTrace();
			}


			return fly;
		}

		@Override
		protected void onPostExecute(String res) {
			pDialog.dismiss();
			//t.setText(res);
			if(fly.equals("1")){
				Intent connectWithLinkedInIntent = new Intent(LoginActivity.this, User_details.class);
				connectWithLinkedInIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName",firstNameFromSocialLogin);
				connectWithLinkedInIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.lastName",lastNameFromScialLogin);
				connectWithLinkedInIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.email.ConnectWithLinkedIn",email);
				connectWithLinkedInIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl",userPicturUrlLinkedIn);
				connectWithLinkedInIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicFromWhooshkaa.ConnectWithLinkedIn",pic);
				//connectWithLinkedInIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.id", id);
				connectWithLinkedInIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.buttonPressed.ConnectWithLinkedIn",isConnectWithLinkedinButtonPressed);
				startActivity(connectWithLinkedInIntent);
				finish();
			}
			else if(fly.trim().equals("0")) {

				Intent connectWithLinkedInIntent = new Intent(LoginActivity.this, MainActivity.class);
				connectWithLinkedInIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl",userPicturUrlLinkedIn);
				connectWithLinkedInIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName",firstNameFromSocialLogin);
				connectWithLinkedInIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.lastName",lastNameFromScialLogin);
				//connectWithLinkedInIntent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.id", id);
				startActivity(connectWithLinkedInIntent);
				finish();
			}

		}
	}

	//***************AsyncTask for Facebook***************
	//****************************************************

	private class DoTask extends AsyncTask<String, Void, String>{
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();


		}

		@Override
		protected String  doInBackground(String... params) {
			JSONParser jSonparser = new JSONParser();
			//all = jSonparser.getUserInfo(params[0]);

			errorState = params[0];

			try{
				JSONObject obj =  new JSONObject(jSonparser.getJSONFromUrl(params[0]));
				response = obj.getString("fli");
				imageUrlFromFacebook = obj.getString("pic");
				firstNameFromDatabase = obj.getString("firstname");
				lastNameFromDatabase = obj.getString("lastname");
				id = obj.getString("id");

			} catch (JSONException e){
				Log.e("Json", errorState);
			}

			return response;
		}

		@Override
		protected void onPostExecute(String s) {
			pDialog.dismiss();
			if(response.equals("0")){

				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl",facebookPictureUrl);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName",facebookFirstName);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.lastName",facebookLastName);
				//intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.id", id);
				startActivity(intent);
				finish();
			}
			else if(response.equals("1")){
				Intent intent = new Intent(LoginActivity.this,User_details.class);
				intent.putExtra("accessToken", accessToken);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl", imageUrlFromFacebook);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName", firstNameFromDatabase);
				intent.putExtra("fullname", fullName);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.lastName", lastNameFromDatabase);
				//intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.id", id);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.email.connectWithFacebook",facebookEmail);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.buttonPressed.connectWithFacebook",isConnectWithFacebookButtonPressed);
				startActivity(intent);
				finish();
			}

		}
	}

	//***************AsyncTask For GooglePluse***************
	//*******************************************************

	private class DoTaskGoogle extends AsyncTask<String, Void, String>{
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();


		}

		@Override
		protected String  doInBackground(String... params) {
			JSONParser jSonparser = new JSONParser();
			//all = jSonparser.getUserInfo(params[0]);

			errorState = params[0];

			try{
				JSONObject obj =  new JSONObject(jSonparser.getJSONFromUrl(params[0]));
				response = obj.getString("fli");
				imageUrlFromGoogle = obj.getString("pic");
				firstNameFromDatabase = obj.getString("firstname");
				lastNameFromDatabase = obj.getString("lastname");
				id = obj.getString("id");

			} catch (JSONException e){
				Log.e("Json", errorState);
			}

			return response;
		}

		@Override
		protected void onPostExecute(String s) {
			pDialog.dismiss();
			if(response.equals("0")){

				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl",userPictureUrlGoogle);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName", firstNameFromDatabase);

				//intent.putExtra("fullname", fullName);

				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.lastName", lastNameFromDatabase);
				//intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.id", id);
				startActivity(intent);
				finish();
			}
			else if(response.equals("1")){
				Intent intent = new Intent(LoginActivity.this,User_details.class);
				intent.putExtra("accessToken", accessToken);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.profilePicUrl", imageUrlFromGoogle);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.firstName", firstNameFromDatabase);
				intent.putExtra("fullname", fullName);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.lastName", lastNameFromDatabase);
				//intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.id", id);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.email.connectWithGooglePlus",emailFromGoogle);
				intent.putExtra("e.aakriti.work.podcast_app.Whooshkaa.buttonPressed.connectWithGooglePlus",isConnectWithGooglePlusButtonPressed);
				startActivity(intent);
				finish();
			}

		}
	}
}
