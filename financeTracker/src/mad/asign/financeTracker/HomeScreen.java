package mad.asign.financeTracker;

import java.text.DecimalFormat;
import java.util.Calendar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ImageButton;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.database.Cursor;



/**
 * The home screen of the application, runs after the splash screen
 * is shown. Displays the Daily, Weekly and Monthly Expense
 * 
 * Navigation to add Expense, Manage Category, Report and Search
 * are also shown here
 * 
 * @author RoboR
 *
 */
public class HomeScreen extends Activity{
	/**
	 * The current time of the system.
	 * Update every time the Activity is run
	 * for accurate results 
	 */
	final private Time today = new Time(Time.getCurrentTimezone());
	
	/**
	 * Constant identifier for daily
	 */
	final private int DAILY = 1;
	
	/**
	 * Constant identifier for weekly
	 */
	final private int WEEKLY = 2;
	
	/**
	 * Constant identifier for monthly
	 */
	final private int MONTHLY = 3;
	
	/**
	 * The main layout of this activity that 
	 * inherent other layout 
	 */
	private LinearLayout mainLayout;
	
	/**
	 * The width of the screen
	 */
	private int screenWidth;
	
	
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//update the current time to the application
		today.setToNow();
			
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;

		mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);

		addExpenseSummaryRow(DAILY);
		addExpenseSummaryRow(WEEKLY);
		addExpenseSummaryRow(MONTHLY);
		
		addButtonTable();
		
		ScrollView scroll = new ScrollView(this);
		scroll.addView(mainLayout);
		
		setContentView(scroll);	
	}
	
	
	
	/**
	 * Use to add the row to display the summary expense
	 * for daily, weekly, and monthly
	 * 
	 * @param time identifier: DAILY, WEEKLY, or MONTHLY 
	 */
	private void addExpenseSummaryRow(int time){
		float textSize = 15f;
		String timeString = "";
		String expenseString = "";
		int colorValue = 0;
		
		//Initialize data for different time identifier
		if(time == DAILY){
			timeString = "Daily"; 
			expenseString = getDailyExpense();
			colorValue = Color.argb(255, 255, 255, 128);
		}else if(time == WEEKLY){
			timeString = "Weekly";			
			expenseString = getWeeklyExpense();
			colorValue = Color.argb(255, 153, 217, 234);
		}else if(time == MONTHLY){
			timeString = "Monthly";
			expenseString = getMonthlyExpense();
			colorValue = Color.argb(255, 255, 174, 201);
		}else{
			Log.v("ERROR", "Wrong time in addExpenseSummaryRow()");
		}
		
	
		if(time == DAILY || time == WEEKLY || time == MONTHLY){
			//To add The expense summary data
			TableRow expenseRow = new TableRow(this);
			expenseRow.setPadding(15, 10, 15, 10);
			expenseRow.setBackgroundColor(colorValue);
			
			TableRow.LayoutParams paramCol_1 = new TableRow.LayoutParams( (int)(screenWidth * 0.2), 
										TableRow.LayoutParams.MATCH_PARENT);
			TableRow.LayoutParams paramCol_2 = new TableRow.LayoutParams( (int)(screenWidth * 0.5), 
										TableRow.LayoutParams.MATCH_PARENT);
			TableRow.LayoutParams paramCol_3 = new TableRow.LayoutParams( (int)(screenWidth * 0.3) - 30, 
										TableRow.LayoutParams.MATCH_PARENT);
			
			TextView timeText = new TextView(this);
			timeText.setText(timeString);
			timeText.setTextSize(textSize);
			timeText.setLayoutParams(paramCol_1);			

			TextView dollarText = new TextView(this);
			dollarText.setText("$");
			dollarText.setGravity(Gravity.RIGHT);
			dollarText.setLayoutParams(paramCol_2);

			TextView expenseText = new TextView(this);
			expenseText.setText(expenseString);
			expenseText.setGravity(Gravity.RIGHT);
			expenseText.setTextSize(textSize);
			expenseText.setLayoutParams(paramCol_3);
			
			expenseRow.addView(timeText);
			expenseRow.addView(dollarText);
			expenseRow.addView(expenseText);
									
			mainLayout.addView(expenseRow);
		}
	}
	
	
	
	/**
	 * To add the navigation buttons 
	 */
	private void addButtonTable(){	
		TableLayout buttonTable = new TableLayout(this);
		
		/*First Row of Buttons*/
		TableRow butRow1 = new TableRow(this);	
		butRow1.setPadding(0, 80, 0, 0);
		butRow1.setGravity(Gravity.CENTER);

		
		//Add Expense Button
		ImageButton expenseBut = new ImageButton(this);
		expenseBut.setPadding(0, 0, 0, 0);		
		Bitmap expenseIcon = BitmapFactory.decodeResource(getResources(), R.drawable.add_expense_icon);		
		expenseBut.setImageBitmap(expenseIcon);
		expenseBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				Intent toAddExpenseScreen = new Intent(HomeScreen.this, AddExpenseScreen.class);
				startActivity(toAddExpenseScreen);		
				finish();
			}			
		});		

		LinearLayout expenseLayout = new LinearLayout(this);
		expenseLayout.setPadding(0, 0, 70, 0);
		expenseLayout.addView(expenseBut);
		butRow1.addView(expenseLayout);
		
		//Manage Category Button
		ImageButton manageBut = new ImageButton(this);	
		manageBut.setPadding(0, 0, 0, 0);
		Bitmap manageIcon = BitmapFactory.decodeResource(getResources(), R.drawable.manage_category_icon);		
		manageBut.setImageBitmap(manageIcon);
		manageBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				Intent toManageScreen = new Intent(HomeScreen.this, ManageScreen.class);
				startActivity(toManageScreen);
			}
		});
	
		LinearLayout manageLayout = new LinearLayout(this);
		manageLayout.setPadding(70, 0, 0, 0);
		manageLayout.addView(manageBut);
		butRow1.addView(manageLayout);		
		
		/*Second Row of Button*/
		TableRow butRow2 = new TableRow(this);
		butRow2.setPadding(0, 80, 0, 0);
		butRow2.setGravity(Gravity.CENTER);
		
		//Report Button
		ImageButton reportBut = new ImageButton(this);
		reportBut.setPadding(0, 0, 0, 0);
		Bitmap reportIcon = BitmapFactory.decodeResource(getResources(), R.drawable.report_icon);
		reportBut.setImageBitmap(reportIcon);
		reportBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				Intent toReportScreen = new Intent(HomeScreen.this, ReportView.class);
				startActivity(toReportScreen);			
			}			
		});
		
		LinearLayout reportLayout = new LinearLayout(this);
		reportLayout.setPadding(0, 0, 70, 0);
		reportLayout.addView(reportBut);		
		butRow2.addView(reportLayout);

		//Search Button
		ImageButton searchBut = new ImageButton(this);
		searchBut.setPadding(0, 0, 0, 0);
		Bitmap searchIcon = BitmapFactory.decodeResource(getResources(), R.drawable.search_icon);
		searchBut.setImageBitmap(searchIcon);
		searchBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				Intent toSearchScreen = new Intent(HomeScreen.this, SearchScreen.class);
				startActivity(toSearchScreen);			
			}			
		});	

		LinearLayout searchLayout = new LinearLayout(this);
		searchLayout.setPadding(70, 0, 0, 0);
		searchLayout.addView(searchBut);		
		butRow2.addView(searchLayout);	
						
		/*Add The title captions*/
		
		//Row 1
		TableRow titleRow_1 = new TableRow(this);
		titleRow_1.setGravity(Gravity.CENTER);
				
		TextView addExpenseTitle = new TextView(this);
		addExpenseTitle.setText("Add Expense");
		LinearLayout expenseTitleLayout = new LinearLayout(this);
		expenseTitleLayout.setPadding(0, 0, 20, 0);
		expenseTitleLayout.addView(addExpenseTitle);
		
		TextView manageTitle = new TextView(this);;		
		manageTitle.setText("Manage Category");
		LinearLayout manageTitleLayout = new LinearLayout(this);
		manageTitleLayout.setPadding(35, 0, 0, 0);
		manageTitleLayout.addView(manageTitle);
		
		titleRow_1.addView(expenseTitleLayout);
		titleRow_1.addView(manageTitleLayout);
		
		//Row 2
		TableRow titleRow_2 = new TableRow(this);
		titleRow_2.setGravity(Gravity.CENTER);
				
		TextView addReportTitle = new TextView(this);
		addReportTitle.setText("View Report");
		LinearLayout addReportTitleLayout = new LinearLayout(this);
		addReportTitleLayout.setPadding(0, 0, 25, 80);
		addReportTitleLayout.addView(addReportTitle);
		
		TextView searchTitle = new TextView(this);;		
		searchTitle.setText("Search");
		LinearLayout searchTitleLayout = new LinearLayout(this);
		searchTitleLayout.setPadding(80, 0, 0, 80);
		searchTitleLayout.addView(searchTitle);
		
		titleRow_2.addView(addReportTitleLayout);
		titleRow_2.addView(searchTitleLayout);

	
		//Add the main Views
		buttonTable.addView(butRow1);
		buttonTable.addView(titleRow_1);
		buttonTable.addView(butRow2);
		buttonTable.addView(titleRow_2);
		
		mainLayout.addView(buttonTable);
	}
	
	
	
	/**
	 * To get the total of daily expense, query the 
	 * database for the results
	 *  
	 * @return total for daily expense
	 */
	private String getDailyExpense(){		
		DatabaseHandler db = new DatabaseHandler(this);
		
		double totalExp = 0;
		
		String sel = DatabaseHandler.EXPENSE_DATE + " =? ";
		
		//Format the date
		String todayDate = String.valueOf(today.year);
		if( (today.month + 1) < 10){
			todayDate += "0";
		}
		todayDate += String.valueOf(today.month + 1);
		
		if( (today.monthDay) < 10){
			todayDate += "0";
		}
		todayDate += String.valueOf(today.monthDay);
		
		String [] selArg = {todayDate};
		
		Cursor c = db.query(new String[] {DatabaseHandler.EXPENSE_AMOUNT}, sel, selArg, null, null, null);
		
		c.moveToFirst();
		
		while(c.isAfterLast() == false){			
			totalExp += Double.parseDouble( c.getString(0) );
			
			c.moveToNext();
		}
		
		db.close();
		
	   DecimalFormat df = new DecimalFormat("#,##0.00");
	   		
	   return df.format(totalExp);
	}
	
	
	
	/**
	 * To get the weekly expenses, starts from sunday to saturday
	 * 
	 * @return the total weekly expense
	 */
	private String getWeeklyExpense(){		
		DatabaseHandler db = new DatabaseHandler(this);		
		int weekStartDay = 0;
		int weekEndDay = 0;
		double totalExp = 0;
		
		String sel = DatabaseHandler.EXPENSE_DATE + " BETWEEN ? AND ? ";
		
		Calendar calendar = Calendar.getInstance();
		int lastDay = calendar.getActualMaximum(Calendar.DATE);
		String yearMonth;
		
		//Get the start date of the week
		weekStartDay = today.monthDay - today.weekDay;		
		if(weekStartDay <= 0){
			weekStartDay = 1;
		}		
		
		//Get the end date of the week
		weekEndDay = today.monthDay + (7 - (today.weekDay + 1) );
		if(weekEndDay > lastDay){
			weekEndDay = lastDay;
		}
		
		yearMonth = String.valueOf(today.year);
		if( (today.month + 1) < 10){
			yearMonth += "0";
		}
		yearMonth += String.valueOf( (today.month + 1) );
		
		//The date of the start of the week
		String wkStartDate = yearMonth;
		if(weekStartDay < 10){
			wkStartDate += "0";
		}		
		wkStartDate += String.valueOf(weekStartDay);

		//Date of the end of the week		
		String wkEndDate = yearMonth;
		if(weekEndDay < 10){
			wkEndDate += "0";
		}
		wkEndDate += String.valueOf(weekEndDay);

		String [] selArg = {wkStartDate, wkEndDate};
		
		Cursor c = db.query(new String[] {DatabaseHandler.EXPENSE_AMOUNT}, sel, selArg, null, null, null);

		c.moveToFirst();

		while(c.isAfterLast() == false){			
			totalExp += Double.parseDouble( c.getString(0) );

			c.moveToNext();
		}
		
		db.close();
		
	   DecimalFormat df = new DecimalFormat("#,##0.00");

	   return df.format(totalExp);
	}
	
		
	
	/**
	 * To get the monthly expense
	 * 
	 * @return the total of monthly expense
	 */
	private String getMonthlyExpense(){		
		DatabaseHandler db = new DatabaseHandler(this);
		Calendar calendar = Calendar.getInstance();
		int lastDay = calendar.getActualMaximum(Calendar.DATE);
		
		String currentYearMonth = String.valueOf(today.year);
		if( (today.month + 1) < 10){
			currentYearMonth += "0";
		}
		currentYearMonth += String.valueOf( (today.month + 1) );
		
		String startDate = currentYearMonth + "01";
		String endDate = currentYearMonth + lastDay;
		
		double totalExp = 0;
		
		String sel = DatabaseHandler.EXPENSE_DATE + " BETWEEN ? AND ? ";		
		
		String [] selArg = {startDate, endDate};		
		
		Cursor c = db.query(new String[] {DatabaseHandler.EXPENSE_AMOUNT}, sel, selArg, null, null, null);
		
		c.moveToFirst();
		
		while(c.isAfterLast() == false){			
			totalExp += Double.parseDouble( c.getString(0) );
			
			c.moveToNext();
		}
		
		db.close();
		
		DecimalFormat df = new DecimalFormat("#,##0.00");

	   return df.format(totalExp);
	}

}
