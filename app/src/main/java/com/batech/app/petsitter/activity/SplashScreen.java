package com.batech.app.petsitter.activity;

import com.batech.app.petsitter.R;
import com.batech.app.petsitter.other.JsonParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends Activity {

	String now_playing, earned;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		FirebaseDatabase.getInstance().setPersistenceEnabled(true);

		/*
		 * Showing splashscreen while making network calls to download necessary
		 * data before launching the app Will use AsyncTask to make http call
		 */
		new PrefetchData().execute();

	}

	/*
	 * Async Task to make http call
	 */
	private class PrefetchData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// before making http calls
			Log.e("JSON", "Pre execute");

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			/*
			 * Will make http call here This call will download required data
			 * before launching the app 
			 * example: 
			 * 1. Downloading and storing SQLite 
			 * 2. Downloading images 
			 * 3. Parsing the xml / json 
			 * 4. Sending device information to server 
			 * 5. etc.,
			 */
			JsonParser jParser = new JsonParser();
//			String json = jsonParser
//					.getJSONFromUrl("http://api.androidhive.info/game/game_stats.json");*/
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.makeHttpRequest("http://api.androidhive.info/game/game_stats.json", "POST", params);

			Log.e("Response: 111", "> " + json);
/*
			if (json != null) {
				try {
					JSONObject jObj = new JSONObject(json).getJSONObject("game_stat");
					now_playing = jObj.getString("now_playing");
					earned = jObj.getString("earned");

					Log.e("JSON", "> " + now_playing + earned);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
*/
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// After completing http call
			// will close this activity and lauch main activity
			FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

			if(user == null){
				Intent i = new Intent(SplashScreen.this, LoginActivity.class);
				i.putExtra("now_playing", now_playing);
				i.putExtra("earned", earned);
				startActivity(i);
			}
			else {
				Intent i = new Intent(SplashScreen.this, MainActivity.class);
				i.putExtra("now_playing", now_playing);
				i.putExtra("earned", earned);
				startActivity(i);
			}
			// close this activity
			finish();
		}

	}

}
