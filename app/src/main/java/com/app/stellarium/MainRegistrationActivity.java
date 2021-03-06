package com.app.stellarium;

import static com.app.stellarium.utils.PasswordEncoder.encodePasswordMD5;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.percentlayout.widget.PercentLayoutHelper;
import androidx.percentlayout.widget.PercentRelativeLayout;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.dialog.DialogPasswordReset;
import com.app.stellarium.dialog.EmailConfirmationDialog;
import com.app.stellarium.filters.EmailFilter;
import com.app.stellarium.filters.PasswordFilter;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class MainRegistrationActivity extends AppCompatActivity {

    private boolean isSigninScreen = true;
    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private TextView tvSigninInvoker;
    private LinearLayout llSignin;
    private Button forgotPasswordBtn;
    private Button btnSignup;
    private Button btnSignin;
    private RelativeLayout mainRegistration;
    private Animation scaleUp;
    TextInputEditText signInEmailEditText, signInPasswordEditText, signUpEmailEditText, signUpPasswordEditText;
    Button facebookButton, googleButton, skipButton;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private PasswordFilter passwordFilter = new PasswordFilter();
    private EmailFilter emailFilter = new EmailFilter();
    private TextInputLayout signInInputLayout, signUpInputLayout, signInInputLayoutPassword, signUpInputLayoutPassword;
    private ImageView signUpEye, signInEye;
    private boolean isShowSignup = false, isShowSignin = false;
    private DatabaseHelper databaseHelper;
    private EmailConfirmationDialog emailConfirmationDialog;
    private boolean isReadyToResume = false;
    private boolean isConfirmationRunning = true;
    private int serverID;
    private Intent myIntent;
    private boolean showSkipButton = true;
    private boolean isBackButtonEnabled = false;

    private float letterSpacing = 0.212f;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registration);

        signInEmailEditText = findViewById(R.id.signInEmailEditText);
        signInPasswordEditText = findViewById(R.id.signInPasswordEditText);
        signUpEmailEditText = findViewById(R.id.signUpEmailEditText);
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditText);
        mainRegistration = findViewById(R.id.activity_main_registration);

        signUpEye = findViewById(R.id.signup_eye_password);
        signUpEye.setVisibility(View.GONE);
        signUpEye.setOnClickListener(eyeOnClickListener);

        signInEye = findViewById(R.id.signin_eye_password);
        signInEye.setVisibility(View.GONE);
        signInEye.setOnClickListener(eyeOnClickListener);

        signInInputLayout = findViewById(R.id.signInTextInputLayout);
        signUpInputLayout = findViewById(R.id.signUpTextInputLayout);
        signInInputLayoutPassword = findViewById(R.id.signInTextInputPasswordLayout);
        signUpInputLayoutPassword = findViewById(R.id.signUpTextInputPasswordLayout);

        signInPasswordEditText.setFilters(new InputFilter[]{passwordFilter});
        signUpPasswordEditText.setFilters(new InputFilter[]{passwordFilter});
        signInEmailEditText.setFilters(new InputFilter[]{emailFilter});
        signUpEmailEditText.setFilters(new InputFilter[]{emailFilter});

        tvSignupInvoker = findViewById(R.id.tvSignupInvoker);
        tvSigninInvoker = findViewById(R.id.tvSigninInvoker);

        btnSignup = findViewById(R.id.btnSignup);
        btnSignin = findViewById(R.id.btnSignin);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordButton);

        googleButton = findViewById(R.id.buttonGoggle);
        facebookButton = findViewById(R.id.buttonFacebook);
        skipButton = findViewById(R.id.registrationSkipButton);

        llSignup = findViewById(R.id.llSignup);
        llSignin = findViewById(R.id.llSignin);
        scaleUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float widthPixels = displayMetrics.widthPixels;
        float heightPixels = displayMetrics.heightPixels;
        if (heightPixels / widthPixels < 1.55) {
            mainRegistration.setBackgroundResource(R.drawable.star_sky_reg2);
        }
        signUpPasswordEditText.setLetterSpacing(letterSpacing);
        signInPasswordEditText.setLetterSpacing(letterSpacing);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        showSkipButton = getIntent().getBooleanExtra("showSkipButton", true);
        isBackButtonEnabled = getIntent().getBooleanExtra("isBackButtonEnabled", false);

        if (showSkipButton) {
            skipButton.setVisibility(View.VISIBLE);
        } else {
            skipButton.setVisibility(View.INVISIBLE);
        }

        emailConfirmationDialog = new EmailConfirmationDialog(getLayoutInflater().getContext());
        emailConfirmationDialog.setOnClick(new UnaryOperator<Void>() {
            @Override
            public Void apply(Void unused) {
                waitForEmailConfirmation(serverID, myIntent);
                return null;
            }
        });
        emailConfirmationDialog.setOnCancel(new UnaryOperator<Void>() {
            @Override
            public Void apply(Void unused) {
                isConfirmationRunning = false;
                return null;
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("632189590717-1n3jc5bdchq75l1r32hcpp5roegq3utf.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FacebookSdk.setApplicationId("1292672574576885");
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TEST", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("TEST", "facebook:onCancel");
            }

            @Override
            public void onError(@NonNull FacebookException error) {
                Log.d("TEST", "facebook:onError", error);
            }
        });

        signInEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validate_email(signInEmailEditText, signInInputLayout);
            }
        });

        signUpEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validate_email(signUpEmailEditText, signUpInputLayout);
            }
        });
        signUpPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEyeVisibility(signUpEye, signUpPasswordEditText);
                checkValidityPassword(signUpPasswordEditText, signUpInputLayoutPassword);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEyeVisibility(signUpEye, signUpPasswordEditText);
                checkValidityPassword(signUpPasswordEditText, signUpInputLayoutPassword);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEyeVisibility(signUpEye, signUpPasswordEditText);
                checkValidityPassword(signUpPasswordEditText, signUpInputLayoutPassword);
            }
        });
        signInPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEyeVisibility(signInEye, signInPasswordEditText);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEyeVisibility(signInEye, signInPasswordEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEyeVisibility(signInEye, signInPasswordEditText);
            }
        });
        class ButtonClickListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case (R.id.buttonFacebook):
                        view.startAnimation(scaleUp);
                        mAuth.signOut();
                        LoginManager.getInstance().logInWithReadPermissions(MainRegistrationActivity.this, Arrays.asList("public_profile", "email"));
                        break;
                    case (R.id.buttonGoggle):
                        view.startAnimation(scaleUp);
                        mGoogleSignInClient.signOut();
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, 100);
                        break;
                    case (R.id.registrationSkipButton):
                        view.startAnimation(scaleUp);
                        Intent intent = new Intent(MainRegistrationActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                        break;
                    case (R.id.btnSignup):
                        view.startAnimation(scaleUp);
                        if (signUpEmailEditText.getText().toString().isEmpty()
                                || signUpPasswordEditText.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "??????????????????, ????????????????????, ?????? ????????.", Toast.LENGTH_LONG).show();

                        } else if (signUpInputLayout.getError() != null) {
                            Toast.makeText(getApplicationContext(), "???????????????????????? ??????????.", Toast.LENGTH_LONG).show();
                        } else if (signUpInputLayoutPassword.getError() != null) {
                            Toast.makeText(getApplicationContext(), "???????????????????????? ????????????.", Toast.LENGTH_LONG).show();
                        } else if (checkIfUserExists(signUpEmailEditText.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "???????????????????????? ?? ?????????? ?????????????? ?????????????????????? ?????????? ?????? ????????????????????.", Toast.LENGTH_LONG).show();
                        } else {
                            serverID = registerUserByEmailPassword(signUpEmailEditText.getText().toString(), signUpPasswordEditText.getText().toString());
                            if (serverID == 0) {
                                Toast.makeText(getApplicationContext(), "???? ?????????????? ???????????????????? ???????????????????? ?? ????????????????.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            myIntent = new Intent(MainRegistrationActivity.this, RegistrationActivity.class);
                            isReadyToResume = true;
                            waitForEmailConfirmation(serverID, myIntent);
                        }
                        break;
                    case (R.id.btnSignin):
                        view.startAnimation(scaleUp);
                        if (signInEmailEditText.getText().toString().isEmpty()
                                || signInPasswordEditText.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "??????????????????, ????????????????????, ?????? ????????.", Toast.LENGTH_LONG).show();
                        } else if (signInInputLayout.getError() != null) {
                            Toast.makeText(getApplicationContext(), "???????????????????????? ??????????.", Toast.LENGTH_LONG).show();
                        } else {
                            User user = getUserByEmailAndPassword(signInEmailEditText.getText().toString(), signInPasswordEditText.getText().toString());
                            if (user != null) {
                                if (user.date == null || user.name == null || user.sex == null || user.sign == null) {
                                    myIntent = new Intent(MainRegistrationActivity.this, RegistrationActivity.class);
                                    myIntent.putExtra("userServerID", user.id);
                                    myIntent.putExtra("userEmail", signInEmailEditText.getText().toString());
                                    myIntent.putExtra("userPassword", signInPasswordEditText.getText().toString());
                                } else {
                                    databaseHelper = new DatabaseHelper(getApplicationContext());
                                    SQLiteDatabase database = databaseHelper.getWritableDatabase();
                                    databaseHelper.dropUserTable(database);
                                    databaseHelper.dropAffirmationTable(database);
                                    databaseHelper.dropFavoriteAffirmationsTable(database);
                                    databaseHelper.insertUser(database, user);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(UserTable.COLUMN_EMAIL, signInEmailEditText.getText().toString());
                                    contentValues.put(UserTable.COLUMN_PASSWORD, signInPasswordEditText.getText().toString());
                                    database.update(UserTable.TABLE_NAME, contentValues, UserTable.COLUMN_SERVER_ID + "=" + user.id, null);
                                    database.close();
                                    databaseHelper.close();
                                    myIntent = new Intent(MainRegistrationActivity.this, MainActivity.class);
                                }
                                MainRegistrationActivity.this.startActivity(myIntent);
                            }
                        }
                        break;
                    case (R.id.forgotPasswordButton):
                        view.startAnimation(scaleUp);
                        Dialog dialog = new DialogPasswordReset(MainRegistrationActivity.this);
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        break;

                }
            }
        }

        facebookButton.setOnClickListener(new ButtonClickListener());
        googleButton.setOnClickListener(new ButtonClickListener());
        skipButton.setOnClickListener(new ButtonClickListener());
        btnSignin.setOnClickListener(new ButtonClickListener());
        btnSignup.setOnClickListener(new ButtonClickListener());
        forgotPasswordBtn.setOnClickListener(new ButtonClickListener());


        tvSignupInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                isSigninScreen = false;
                showSignupForm();
            }
        });

        tvSigninInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                isSigninScreen = true;
                showSigninForm();
            }
        });
        showSigninForm();

    }

    View.OnClickListener eyeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.signup_eye_password:
                    eyeChange(isShowSignup, signUpPasswordEditText, signUpEye);
                    isShowSignup = !isShowSignup;
                    break;
                case R.id.signin_eye_password:
                    eyeChange(isShowSignin, signInPasswordEditText, signInEye);
                    isShowSignin = !isShowSignin;
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (isBackButtonEnabled) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReadyToResume) {
            waitForEmailConfirmation(serverID, myIntent);
        }
    }

    private int registerUserByEmailPassword(String email, String password) {
        ServerConnection serverConnection = new ServerConnection();
        String response = serverConnection.getStringResponseByParameters("register/?mail=" + email + "&password=" + encodePasswordMD5(password));
        if (response != null) {
            try {
                return Integer.parseInt(response);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    private void waitForEmailConfirmation(int userServerID, Intent myIntent) {
        emailConfirmationDialog.show();
        emailConfirmationDialog.startGifAnimation();
        isConfirmationRunning = true;
        ServerConnection serverConnection = new ServerConnection();
        Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isConfirmed = false;
                while (!isConfirmed && isConfirmationRunning) {
                    try {
                        String response = serverConnection.getStringResponseByParameters("check_confirm/?user_id=" + userServerID);
                        if (Integer.parseInt(response) == 1) {
                            isConfirmed = true;
                        }
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                emailConfirmationDialog.stopGifAnimation();
                                isConfirmationRunning = false;
                            }
                        });
                    }
                }
                if (!isConfirmationRunning) {
                    return;
                }
                isConfirmationRunning = false;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        emailConfirmationDialog.dismiss();
                        myIntent.putExtra("userServerID", userServerID);
                        myIntent.putExtra("userEmail", signUpEmailEditText.getText().toString());
                        myIntent.putExtra("userPassword", signUpPasswordEditText.getText().toString());
                        MainRegistrationActivity.this.startActivity(myIntent);
                    }
                });
            }
        }).start();
    }

    private void validate_email(TextInputEditText email, TextInputLayout layout) {
        String regex = "(^|\\(|:)[a-z]+([-|\\.]?[a-z0-9])*@[a-z0-9]+([-|\\.]?[a-z0-9])*\\.[a-z]+(\\s|\\b|$|\\,|\\?)";
        if (!email.getText().toString().isEmpty() && !Pattern.matches(regex, email.getText().toString())) {
            layout.setError("???????????????????????? ??????????.");
            layout.setErrorIconDrawable(null);
        } else {
            layout.setError(null);
        }
    }

    private void eyeChange(boolean isShow, TextInputEditText textInputEditText, ImageView eye) {
        if (isShow) {
            eye.setImageResource(R.drawable.ic_eye_hide_white);
            textInputEditText.setLetterSpacing(letterSpacing);
            textInputEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            textInputEditText.setSelection(textInputEditText.length());
        } else {
            eye.setImageResource(R.drawable.ic_eye_show_white);
            textInputEditText.setLetterSpacing(0f);
            textInputEditText.setTransformationMethod(null);
            textInputEditText.setSelection(textInputEditText.length());
        }
    }

    private void setEyeVisibility(ImageView eye, TextInputEditText text) {
        if (text.getText().toString().isEmpty()) {
            eye.setVisibility(View.GONE);
        } else {
            eye.setVisibility(View.VISIBLE);
        }
    }

    private void checkValidityPassword(TextInputEditText text, TextInputLayout layout) {
        if (!text.toString().isEmpty() && text.length() < 8) {
            layout.setError("?????????????? ???? ?????????? 8 ????????????????");
            layout.setErrorIconDrawable(null);
        } else {
            layout.setError(null);
        }
    }

    private boolean checkIfUserExists(String email) {
        ServerConnection serverConnection = new ServerConnection();
        String response = serverConnection.getStringResponseByParameters("user_exist/?mail=" + email);
        return response != null && response.contains("True");
    }

    private User getUserByEmailAndPassword(String email, String password) {
        ServerConnection serverConnection = new ServerConnection();
        String response = null;
        User user = null;
        try {
            response = serverConnection.getStringResponseByParameters("auth/?mail=" + email + "&password=" + encodePasswordMD5(password));
            if (response.equals("False")) {
                throw new InvalidParameterException("???????????????? ?????????? ?????? ????????????.");
            }
            user = new Gson().fromJson(response, User.class);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "???? ?????????????? ???????????????????? ???????????????????? ?? ????????????????.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "???????????????? ?????????? ?????? ????????????.", Toast.LENGTH_LONG).show();
            return null;
        }

        return user;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TEST", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TEST", "Google sign in failed", e);
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (checkUserByUID(user.getUid())) {
                                User userToInsert = getUserByGoogleID(user.getUid());
                                if (userToInsert == null) {
                                    Intent myIntent = new Intent(MainRegistrationActivity.this, RegistrationActivity.class);
                                    myIntent.putExtra("userUID", user.getUid());
                                    myIntent.putExtra("userEmail", user.getEmail());
                                    myIntent.putExtra("userName", user.getDisplayName());
                                    myIntent.putExtra("isFacebook", false);
                                    MainRegistrationActivity.this.startActivity(myIntent);
                                } else {
                                    SQLiteDatabase database = databaseHelper.getWritableDatabase();
                                    databaseHelper.insertUser(database, userToInsert);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(UserTable.COLUMN_GOOGLE_ID, user.getUid());
                                    database.update(UserTable.TABLE_NAME, contentValues, UserTable.COLUMN_SERVER_ID + "=" + userToInsert.id, null);
                                    database.close();
                                    Intent myIntent = new Intent(MainRegistrationActivity.this, MainActivity.class);
                                    MainRegistrationActivity.this.startActivity(myIntent);
                                }
                            } else {
                                int serverID = registerUserByUID(user.getUid(), false);
                                Intent myIntent = new Intent(MainRegistrationActivity.this, RegistrationActivity.class);
                                myIntent.putExtra("userUID", user.getUid());
                                myIntent.putExtra("userServerID", serverID);
                                myIntent.putExtra("userEmail", user.getEmail());
                                myIntent.putExtra("userName", user.getDisplayName());
                                myIntent.putExtra("isFacebook", false);
                                MainRegistrationActivity.this.startActivity(myIntent);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TEST", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TEST", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (checkUserByUID(user.getUid())) {
                                User userToInsert = getUserByFacebookID(user.getUid());
                                if (userToInsert == null) {
                                    Intent myIntent = new Intent(MainRegistrationActivity.this, RegistrationActivity.class);
                                    myIntent.putExtra("userUID", user.getUid());
                                    myIntent.putExtra("userEmail", user.getEmail());
                                    myIntent.putExtra("userName", user.getDisplayName());
                                    myIntent.putExtra("isFacebook", true);
                                    MainRegistrationActivity.this.startActivity(myIntent);
                                } else {
                                    SQLiteDatabase database = databaseHelper.getWritableDatabase();
                                    databaseHelper.insertUser(database, userToInsert);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(UserTable.COLUMN_GOOGLE_ID, user.getUid());
                                    database.update(UserTable.TABLE_NAME, contentValues, UserTable.COLUMN_FACEBOOK_ID + "=" + user.getUid(), null);
                                    database.close();
                                    Intent myIntent = new Intent(MainRegistrationActivity.this, MainActivity.class);
                                    MainRegistrationActivity.this.startActivity(myIntent);
                                }
                            } else {
                                int serverID = registerUserByUID(user.getUid(), true);
                                Intent myIntent = new Intent(MainRegistrationActivity.this, RegistrationActivity.class);
                                myIntent.putExtra("userUID", user.getUid());
                                myIntent.putExtra("userServerID", serverID);
                                myIntent.putExtra("userEmail", user.getEmail());
                                myIntent.putExtra("userName", user.getDisplayName());
                                myIntent.putExtra("isFacebook", true);
                                MainRegistrationActivity.this.startActivity(myIntent);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TEST", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private int registerUserByUID(String uid, boolean isFacebook) {
        ServerConnection serverConnection = new ServerConnection();
        String params = "register/?";
        if (isFacebook) {
            params += "facebook=" + uid;
        } else {
            params += "google=" + uid;
        }
        String response = serverConnection.getStringResponseByParameters(params);
        if (response != null) {
            try {
                return Integer.parseInt(response);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    private boolean checkUserByUID(String uid) {
        ServerConnection serverConnection = new ServerConnection();
        String response = serverConnection.getStringResponseByParameters("check_uid/?uid=" + uid);
        Log.d("CHECK_UID", response);
        return !response.equals("Error");
    }

    private User getUserByGoogleID(String uid) {
        try {
            ServerConnection serverConnection = new ServerConnection();
            String response = serverConnection.getStringResponseByParameters("/auth/?google=" + uid);
            return new Gson().fromJson(response, User.class);
        } catch (Exception e) {
            return null;
        }
    }

    private User getUserByFacebookID(String uid) {
        try {
            ServerConnection serverConnection = new ServerConnection();
            String response = serverConnection.getStringResponseByParameters("/auth/?facebook=" + uid);
            return new Gson().fromJson(response, User.class);
        } catch (Exception e) {
            return null;
        }
    }

    private void showSignupForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.15f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.85f;
        llSignup.requestLayout();

        tvSignupInvoker.setVisibility(View.GONE);
        tvSigninInvoker.setVisibility(View.VISIBLE);
        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right_to_left);
        llSignup.startAnimation(translate);

        Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_right_to_left);
        btnSignup.startAnimation(clockwise);

    }

    private void showSigninForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.85f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.15f;
        llSignup.requestLayout();

        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left_to_right);
        llSignin.startAnimation(translate);

        tvSignupInvoker.setVisibility(View.VISIBLE);
        tvSigninInvoker.setVisibility(View.GONE);
        Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_left_to_right);
        btnSignin.startAnimation(clockwise);
        forgotPasswordBtn.startAnimation(clockwise);
    }
    private void hideKeyboard(){
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}