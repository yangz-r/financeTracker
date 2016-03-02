package mad.asign.financeTracker;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;



/**
 * This activity is use to show the results obtain
 * from the search
 * 
 * @author RoboR
 *
 */
public class ResultView extends Activity{
	/**
	 * to store the from date of the search
	 */
	private String  fromDate;
	
	/**
	 * to store the to date of the search
	 */
	private String  toDate;
	
	/**
	 * stores the name of the search
	 */
	private String searchName;
	
	/**
	 * stores the category of the search
	 */
	private String searchCategory;
	
	/**
	 * store the query results
	 */
	private Expense [] exp;

	/**
	 * height of the screen
	 */
	private int screenWidth;
	
	/**
	 * main layout of this activity
	 */
	private LinearLayout mainLayout;
	
	/**
	 * to display the results, without the summary header row  
	 */
	private LinearLayout resultLayout;
	
	
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);		
		screenWidth = metrics.widthPixels;
		
		mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);

		getInitializeValues();
		getSearchItems();
		displaySearchResults();				
		
		setContentView(mainLayout);
	}
	
	
	
	/**
	 * Initialize the values for the parameters of the search
	 */
	private void getInitializeValues(){
		SharedPreferences preference = getSharedPreferences(FinanceTracker.PREF_FILE_NAME, Context.MODE_PRIVATE);
		
		fromDate = preference.getString(FinanceTracker.SEARCH_DATE_FROM, null);		
		toDate = preference.getString(FinanceTracker.SEARCH_DATE_TO, null);		
		searchName = preference.getString(FinanceTracker.SEARCH_NAME, null);		
		searchCategory = preference.getString(FinanceTracker.SEARCH_CATEGORY, null);	
		
		SharedPreferences.Editor editor = preference.edit();

		//Clear the search preference
		editor.putString(FinanceTracker.SEARCH_DATE_FROM, null);
		editor.putString(FinanceTracker.SEARCH_DATE_TO, null);
		editor.putString(FinanceTracker.SEARCH_CATEGORY, null);
		editor.putString(FinanceTracker.SEARCH_NAME, null);
		editor.commit();
	}
	
	
	
	/**
	 * To obtain the search results, result is store in the Expense array.
	 * Will handle different scenario of search combinations
	 */
	private void getSearchItems(){
		DatabaseHandler db = new DatabaseHandler(this);		
		Cursor c = null;
		String [] columns = {DatabaseHandler.EXPENSE_KEY_ROWID, DatabaseHandler.EXPENSE_AMOUNT, 
				DatabaseHandler.EXPENSE_DATE, DatabaseHandler.EXPENSE_NAME,
				DatabaseHandler.EXPENSE_MAIN_CATEGORY};
		String selection;
		
		//No Search Name 
		if(searchName == null){			
			
			if(searchCategory == "All Categories"){
				//No Search name + All Categories
				selection = DatabaseHandler.EXPENSE_DATE + " BETWEEN ? AND ? ";
				
				String [] selectionArgs = {fromDate, toDate};
				
				c = db.query(columns, selection, selectionArgs, null, null, null);
				
			}else{		
				//No Search name + selected Category
				selection = DatabaseHandler.EXPENSE_DATE + " BETWEEN ? AND ? AND "
							+ DatabaseHandler.EXPENSE_MAIN_CATEGORY + " = ?";			
					
				String [] selectionArgs = {fromDate, toDate, searchCategory};	

				c = db.query(columns, selection, selectionArgs, null, null, null);
			}			
		}else{
			//Search Name included
			if(searchCategory == "All Categories"){
				//Search name + All Categories
				selection = DatabaseHandler.EXPENSE_DATE + " BETWEEN ? AND ? AND "
							+ DatabaseHandler.EXPENSE_NAME + " = ?";
				
				String [] selectionArgs = {fromDate, toDate, searchName};
				
				c = db.query(columns, selection, selectionArgs, null, null, null);				
			}else{		
				//Search name + selected Category
				selection = DatabaseHandler.EXPENSE_DATE + " BETWEEN ? AND ? AND "
							+ DatabaseHandler.EXPENSE_NAME + " = ? AND "
							+ DatabaseHandler.EXPENSE_MAIN_CATEGORY + " = ?";			
					
				String [] selectionArgs = {fromDate, toDate, searchName, searchCategory};	

				c = db.query(columns, selection, selectionArgs, null, null, null);
			}					
		}
		
		//Insert all the query results into exp
		c.moveToFirst();

		exp = new Expense [c.getCount()];
		
		while(c.isAfterLast() == false){
			exp[c.getPosition()] = new Expense( Integer.parseInt(c.getString(0)), c.getString(1),
					Integer.parseInt(c.getString(2).substring(0, 4)), 
					Integer.parseInt(c.getString(2).substring(4, 6)),
					Integer.parseInt(c.getString(2).substring(6, 8)), 
					c.getString(3), 
					c.getString(4), null );

			c.moveToNext();
		}
	}
	
	
	
	/**
	 * To show the results, will place the summary header layout
	 * and also the query results
	 */
	private void displaySearchResults(){
		LinearLayout.LayoutParams paramCol_1= new LinearLayout.LayoutParams( (int)(screenWidth * 0.7), 
				LinearLayout.LayoutParams.MATCH_PARENT);		
		LinearLayout.LayoutParams paramCol_2 = new LinearLayout.LayoutParams( (int)(screenWidth * 0.3), 
				LinearLayout.LayoutParams.MATCH_PARENT);
		
		//Header
		LinearLayout headerRow = new LinearLayout(this);
		headerRow.setBackgroundColor( Color.rgb(239, 228, 176) );
		headerRow.setPadding(10, 25, 10, 25);
		headerRow.setOrientation(LinearLayout.HORIZONTAL);
		
		String fDate = fromDate.substring(6, 8) + "-" + fromDate.substring(4, 6) + "-" + fromDate.substring(0, 4);
		String tDate = toDate.substring(6, 8) + "-" + toDate.substring(4, 6) + "-" + toDate.substring(0, 4);

		//Date range
		TextView dateText = new TextView(this);
		dateText.setText(fDate + "  to  " + tDate);
		dateText.setLayoutParams(paramCol_1);
		headerRow.addView(dateText);
		
		//Total Records
		TextView totalRecordText = new TextView(this);
		totalRecordText.setText(exp.length + " Records");
		totalRecordText.setLayoutParams(paramCol_2);
		headerRow.addView(totalRecordText);
		
		mainLayout.addView(headerRow);	
		
		//Add a line for division
		TextView lineView = new TextView(this) {
			
            protected void onDraw(Canvas canvas) {
            	super.onDraw(canvas);
 	            Paint paint = new Paint();
 	            paint.setStyle(Paint.Style.STROKE);
 	            paint.setColor(Color.BLACK);
 	            paint.setStrokeWidth(7);
 	            
 	            canvas.drawLine(0, 0, screenWidth, 0, paint);
			};					
		};
		lineView.setHeight(5);
		mainLayout.addView(lineView);
		
		resultLayout = new LinearLayout(this);
		resultLayout.setOrientation(LinearLayout.VERTICAL);
		
		for(int i = 0; i < exp.length; i++){
			displaySearchRow(exp[i]);
		}
		
		ScrollView scroll = new ScrollView(this);
		scroll.addView(resultLayout);		
		
		mainLayout.addView(scroll);
	}
	
	
	
	/**
	 * To show a row of the result expense
	 * 
	 * @param exp the expense to be shown
	 */
	private void displaySearchRow(Expense exp){
	
		//First row is to store the date and amount in a same line
		LinearLayout row_1 = new LinearLayout(this);
		row_1.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams paramCol_1 = new LinearLayout.LayoutParams( (int)(screenWidth * 0.5) - 20, 
												LinearLayout.LayoutParams.MATCH_PARENT);
		
		String date = exp.getDate().substring(0, 4)  + 
					  "-" + exp.getDate().substring(4, 6) + 
					  "-" + exp.getDate().substring(6, 8);
		
		TextView dateText = new TextView(this);
		dateText.setText("Date : " + date);
		dateText.setPadding(20, 20, 0, 0);
		dateText.setLayoutParams(paramCol_1);
		row_1.addView(dateText);
		
		TextView amountText = new TextView(this);
		amountText.setGravity(Gravity.RIGHT);		
		amountText.setPadding(0, 20, 0, 0);
		amountText.setLayoutParams(paramCol_1);
		DecimalFormat df = new DecimalFormat("#,##0.00");		
		amountText.setText("$ " + df.format(Double.parseDouble(exp.getAmount()) ) );
		row_1.addView(amountText);
		
		//Category 
		TextView categoryText = new TextView(this);
		categoryText.setPadding(20, 0, 0, 0);
		categoryText.setText("Category : " + exp.getMainCategory() );
		
		//Name 
		TextView nameText = new TextView(this);
		nameText.setPadding(20, 0, 0, 20);
		nameText.setText("Name : " + exp.getName() );
		
		//Add a line for division
		TextView lineView = new TextView(this) {
			
            protected void onDraw(Canvas canvas) {
            	super.onDraw(canvas);
 	            Paint paint = new Paint();
 	            paint.setStyle(Paint.Style.STROKE);
 	            paint.setColor(Color.BLACK);
 	            paint.setStrokeWidth(5);
 	            
 	            canvas.drawLine(0, 0, screenWidth, 0, paint);
			};					
		};
		lineView.setHeight(5);
		
		//Pack all up
		LinearLayout resultRow = new LinearLayout(this);
		resultRow.setOrientation(LinearLayout.VERTICAL);
		
		resultRow.addView(row_1);
		resultRow.addView(categoryText);
		resultRow.addView(nameText);
		resultRow.addView(lineView);
		
		resultLayout.addView(resultRow);			
	}

}
