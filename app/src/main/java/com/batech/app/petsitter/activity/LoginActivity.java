package com.batech.app.petsitter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.batech.app.petsitter.R;
import com.batech.app.petsitter.model.Profiles;
import com.batech.app.petsitter.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,
		View.OnClickListener {
	private static final String TAG = LoginActivity.class.getSimpleName();
	private Button btnLinkToRegisterScreen;
	private Button btnSignIn;
	private EditText inputEmail;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private DatabaseReference mDatabase;
	private GoogleSignInAccount acct;

	private ProgressDialog mAuthProgressDialog;

	private static final int RC_SIGN_IN = 9001;
	private SignInButton mSignInButton;

	private GoogleApiClient mGoogleApiClient;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
		mSignInButton.setSize(mSignInButton.SIZE_WIDE);

		mSignInButton.setOnClickListener(this);

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		mAuth = FirebaseAuth.getInstance();

		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					// User is signed in
//					Toast.makeText(getApplicationContext(),
//							"onAuthStateChanged:signed_in:" + user.getUid(), Toast.LENGTH_LONG)
//							.show();

				} else {

				}
				// ...
			}
		};

		inputEmail = (EditText) findViewById(R.id.field_email);
		inputPassword = (EditText) findViewById(R.id.field_password);
		btnSignIn = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegisterScreen = (Button) findViewById(R.id.btnLinkToRegisterScreen);

		btnLinkToRegisterScreen.setOnClickListener(this);

		btnSignIn.setOnClickListener(this);
	}

	private void signIn(final String email, final String password) {
		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						if (!task.isSuccessful()) {
							Log.w(TAG, "signInWithEmail:failed", task.getException());
							Toast.makeText(LoginActivity.this, R.string.auth_failed,
									Toast.LENGTH_SHORT).show();
						} else {
							// Staring MainActivity

							FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
							if (user.isEmailVerified()) {
								Intent i = new Intent(getApplicationContext(), com.batech.app.petsitter.activity.MainActivity.class);
								startActivity(i);
								finish();
							} else {
								Toast.makeText(LoginActivity.this, R.string.auth_activate,
										Toast.LENGTH_SHORT).show();

							}

						}
						mAuthProgressDialog.hide();
					}
				});
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	@Override
	public void onStart() {
		super.onStart();
		mAuth.addAuthStateListener(mAuthListener);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mAuthListener != null) {
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.sign_in_button:
				Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
				startActivityForResult(signInIntent, RC_SIGN_IN);
				break;
			case R.id.btnLinkToRegisterScreen:
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
				finish();
			case R.id.btnLogin:
				if(validateForm()){
					String email = inputEmail.getText().toString().trim();
					String password = inputPassword.getText().toString();
					signIn(email, password);
				}
			default:
				return;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				// Google Sign In was successful, authenticate with Firebase
				acct = result.getSignInAccount();
				firebaseAuthWithGoogle();
			} else {
				// Google Sign In failed
				Log.e(TAG, "Google Sign In failed.");
			}
		}
	}

	private void firebaseAuthWithGoogle() {
		Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						if (!task.isSuccessful()) {
							Log.w(TAG, "signInWithCredential", task.getException());
							Toast.makeText(LoginActivity.this, "Authentication failed.",
									Toast.LENGTH_SHORT).show();
						} else {
							mDatabase = FirebaseDatabase.getInstance().getReference();
							createUserForFirebase();
							startActivity(new Intent(LoginActivity.this, MainActivity.class));
							finish();
						}
					}
				});
	}


	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.d(TAG, "onConnectionFailed:" + connectionResult);
		Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
	}

	private boolean validateForm() {
		boolean valid = true;

		String email = inputEmail.getText().toString().trim();

		if (TextUtils.isEmpty(email)) {
			inputEmail.setError("Required.");
			valid = false;
		} else {
			inputEmail.setError(null);
		}

		String password = inputPassword.getText().toString();
		if (TextUtils.isEmpty(password)) {
			inputPassword.setError("Required.");
			valid = false;
		} else {
			inputPassword.setError(null);
		}

		return valid;
	}

	public void createUserForFirebase(){

		String userid = getUid();
		String email = acct.getEmail();
		String displayName = acct.getDisplayName();
		String familyName = acct.getFamilyName();
		String givenName = acct.getGivenName();
		Uri photoUrl = acct.getPhotoUrl();

		User user = new User(email, email, givenName, familyName, displayName, photoUrl);
		//		String username, String email, String name, String surname, String fullname, Uri uri

		Map<String, Object> userValues = user.toMap();

		Map<String, Object> childUpdates = new HashMap<>();
		//            childUpdates.put("/posts/" + key, postValues);
		childUpdates.put("/users/" + userid, userValues);

		mDatabase.updateChildren(childUpdates);


	}
}