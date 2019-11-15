package com.oshop.hm.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.oshop.hm.R;
import com.oshop.hm.constant.ConstantValues;
import com.oshop.hm.customs.DialogLoader;
import com.oshop.hm.models.phone_model.PhoneData;
import com.oshop.hm.network.APIClient;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;


public class PhoneVerify extends Fragment implements View.OnClickListener {
// #################################################################################################


    private static final String TAG = "PhoneAuthFragment";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    //private ViewGroup mPhoneNumberViews;
    //private ViewGroup mSignedInViews;

    //private TextView mStatusText;
    private TextView mDetailText;

    private Spinner mPhoneCode;
    private EditText mPhoneNumberField;
    private EditText mVerificationField;

    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    //private Button mSignOutButton;

    DialogLoader dialogLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.phone_verify, container, false);

        // #############################################################
        // Restore instance state
        if (savedInstanceState != null) {
            //onRestoreInstanceState(savedInstanceState);
            //onActivityCreated(savedInstanceState);
            mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
        }

        // Set the Title of Toolbar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.verify_phone_number));

        dialogLoader = new DialogLoader(getContext());

        // Assign views
        //mPhoneNumberViews = rootView.findViewById(R.id.phone_auth_fields);
        //mSignedInViews = rootView.findViewById(R.id.signed_in_buttons);

        //mStatusText = rootView.findViewById(R.id.status);
        mDetailText = rootView.findViewById(R.id.mDetailText);

        mPhoneNumberField = rootView.findViewById(R.id.field_phone_number);
        mVerificationField = rootView.findViewById(R.id.field_verification_code);
        mPhoneCode = rootView.findViewById(R.id.phone_code);

        mStartButton = rootView.findViewById(R.id.button_start_verification);
        mVerifyButton = rootView.findViewById(R.id.button_verify_phone);
        mResendButton = rootView.findViewById(R.id.button_resend);
        //mSignOutButton = rootView.findViewById(R.id.sign_out_button);

        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
        //mSignOutButton.setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                //updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                //Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(PhoneVerify.this.getView(), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
        // ##############################################################

        return rootView;
    }



    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        // [START_EXCLUDE]
        if (mVerificationInProgress && validateTextView(mPhoneNumberField)) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]





    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    /*@Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }*/


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        String phoneCode = mPhoneCode.getSelectedItem().toString();
        if(!validateString(phoneCode))
            return;
        phoneNumber =  phoneCode + phoneNumber;
        Toast.makeText(getActivity(), ""+phoneNumber, Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            String token = user.getIdToken(false).getResult().getToken();
                            //Log.w("the-phone-token" , token);
                            toNextFragment(token);
                            // [START_EXCLUDE]
                            //updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void toNextFragment(String token) {
        signOut();
        dialogLoader.showProgressDialog();
        Call<PhoneData> call = APIClient.getInstance()
                .verifyPhoneNumber(
                        ConstantValues.TOKEN , // O Shop User Auth Token
                        token // PhoneNumber Auth Token
                );
        call.enqueue(new Callback<PhoneData>() {
            @Override
            public void onResponse(Call<PhoneData> call, Response<PhoneData> response) {

                dialogLoader.hideProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {

                        //String message = response.body().getMessage();
                        //String phoneRetrived = response.body().getData();

                        // Save necessary details in SharedPrefs
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("UserInfo", MODE_PRIVATE).edit();
                        editor.putInt("is_number_verify" , 1);
                        editor.apply();
                        ConstantValues.IS_PHONE_VERIFIED = 1;

                        goToShippingAddress();

                        Snackbar.make(PhoneVerify.this.getView(), R.string.sign_in_with_phone_succes, Snackbar.LENGTH_SHORT).show();
                        //Log.w("the-message ", "success" + response.body());

                    } else if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        Snackbar.make(PhoneVerify.this.getView(),response.body().getMessage(), Snackbar.LENGTH_SHORT).show();
                        //R.string.status_sign_in_failed
                        //Log.w("the-message ", "failed" + response.body().getMessage());

                    } else {
                       // Log.w("the-message ", "failed" + response.message());
                    }
                }
                // Response Failed
                else {
                    // Show the Error Message

                }
            }

            @Override
            public void onFailure(Call<PhoneData> call, Throwable t) {
                dialogLoader.showProgressDialog();
                //Log.w("the-message" ,"failed"+ t.getMessage());
            }
        });


    }

    private void goToShippingAddress() {

        Fragment fragment = new Shipping_Address();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment)
                .addToBackStack(getString(R.string.actionCart)).commit();
    }


    private void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                //enableViews(mStartButton, mPhoneNumberField,mPhoneCode);
                //disableViews(mVerifyButton, mResendButton, mVerificationField);

                mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                //enableViews(mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                //disableViews(mStartButton);
                mPhoneCode.setVisibility(View.GONE);
                mPhoneNumberField.setVisibility(View.GONE);
                mStartButton.setVisibility(View.GONE);
                mVerificationField.setVisibility(View.VISIBLE);
                mVerifyButton.setVisibility(View.VISIBLE);
                mResendButton.setVisibility(View.VISIBLE);
                mDetailText.setText(R.string.status_code_sent);
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                //enableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                mDetailText.setText(R.string.status_verification_failed);
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                //disableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                //mDetailText.setText(R.string.status_verification_succeeded);
                // Set the verification text based on the credential
               /* if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText(R.string.instant_validation);
                    }
                }*/

                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                mDetailText.setText(R.string.status_sign_in_failed);
                break;
            case STATE_SIGNIN_SUCCESS:
                //mDetailText.setText(R.string.sign_in_with_phone_succes);
                // Np-op, handled by sign-in check
                break;
        }

        if (user == null) {
            // Signed out
            //mPhoneNumberViews.setVisibility(View.VISIBLE);
            //mSignedInViews.setVisibility(View.GONE);

            //mStatusText.setText(R.string.signed_out);

        } else {
            // Signed in
            // TODO goto next Fragment
            /*mPhoneNumberViews.setVisibility(View.GONE);
            mSignedInViews.setVisibility(View.VISIBLE);

            enableViews(mPhoneNumberField, mVerificationField);
            mPhoneNumberField.setText(null);
            mVerificationField.setText(null);

            mStatusText.setText(R.string.common_signin_button_text);
            mDetailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            */
        }
    }


    private boolean validateTextView(TextView t) {
        String theText = t.getText().toString();
        if (TextUtils.isEmpty(theText)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    private boolean validateString(String s) {
        if (TextUtils.isEmpty(s)) {
            return false;
        }
        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_verification:
                if (!validateTextView(mPhoneNumberField)) {
                    return;
                }

                startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                break;

            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;

            case R.id.button_resend:
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                break;

        }
    }



//###############################################################################################
}
