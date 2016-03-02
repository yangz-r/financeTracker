package mad.asign.financeTracker;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


/**
 * This is the Intent activity of the application.
 * It will display the Splash Screen and will later move to HomeScreen.
 * The preference name are also placed here
 * 
 * 
 * @author RoboR
 *
 */
public class FinanceTracker extends Activity {
	/**
	 * Name of the shared preference file
	 */
	public static final String PREF_FILE_NAME = "Preference_file";
	
	/**
	 * Preference name for main category
	 */
	public static final String PREF_MAIN_CATEGORY = "Main Category";
	
	/**
	 * Preference name for search date
	 */
	public static final String SEARCH_DATE_FROM = "Query From Date";
	
	/**
	 * Preference name for search date
	 */
	public static final String SEARCH_DATE_TO = "Query To Date";
	
	/**
	 * Preference name for search category
	 */
	public static final String SEARCH_CATEGORY = "Query Category";
	
	/**
	 * Preference name for search name
	 */
	public static final String SEARCH_NAME = "Query Name";
	
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final SplashScreen launch = new SplashScreen(this);
		setContentView(launch);
		
		//Load the preference for Category
		SharedPreferences preference = getSharedPreferences(FinanceTracker.PREF_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();

		if(preference.getString(PREF_MAIN_CATEGORY, null) == null){
			editor.putString(PREF_MAIN_CATEGORY, "Food and Drinks,Household,Entertainment,Grocery,Others");
			editor.commit();
		}

		new CountDownTimer(4500,100){
            @Override
            public void onTick(long miliseconds){ 
        		
            }

            @Override
            public void onFinish(){
               //after 4.5 seconds            	
            	finish();

        		Intent toHomeScreen = new Intent(FinanceTracker.this, HomeScreen.class);
            	startActivity(toHomeScreen);      
            }
        }.start(); 
	}
}
