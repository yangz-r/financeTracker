package mad.asign.financeTracker;

import java.util.ArrayList;
import java.util.Calendar;
import android.text.format.Time;
import android.util.Log;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



/**
 * This is used to show the interface of the report,
 * It will display a pie chart that shows the expense statistic  
 * It will display according to three different parameters
 * 
 *  Daily, Weekly, or Monthly
 * 
 * @author RoboR
 *
 */
public class ReportView extends Activity {
	/**
	 * Current time
	 */
	final private Time today = new Time(Time.getCurrentTimezone());
	
	/**
	 * The identifier for day
	 */
	final private int DAILY = 1;
	
	/**
	 * Identifier for week
	 */
	final private int WEEKLY = 2;
	
	/**
	 * Identifier for month
	 */
	final private int MONTHLY = 3;
	
	/**
	 * Relative layout to place the pie chart and buttons
	 */
	private RelativeLayout rl;
	
	/**
	 * Table layout to place the categories 
	 */
	private TableLayout tl;
	
	/**
	 * Scroll for the whole activity
	 */
	private ScrollView sv; 
	
	/**
	 * The date range 
	 */
	private String titleDate;
	
	/**
	 * the current selected time to display
	 */
	private int selTime = MONTHLY;
	
	/**
	 * To change the button text
	 * to show the current display time
	 */
	private String timeString;
	
	/**
	 * the starting date of the record
	 */
	private String fromDate;
	
	/**
	 * the ending date of the record
	 */
	private String toDate;
	
	/**
	 * Database to read the records
	 */
	private DatabaseHandler db;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		today.setToNow();
		
		db = new DatabaseHandler(this);
		
		sv = new ScrollView(this);
		rl = new RelativeLayout(this);
		tl = new TableLayout(this);
		tl.setStretchAllColumns(true);
		tl.setShrinkAllColumns(true);
		
		placeLayout();
		
		calculateChart();			
		
		sv.addView(rl);
		setContentView(sv);	
		
	}
	
	
	
	/**
	 * This is to calculate and display the pie chart 
	 */
	private void calculateChart(){
		String[] columns = new String[] { "sum(" + DatabaseHandler.EXPENSE_AMOUNT + ")", 
				DatabaseHandler.EXPENSE_MAIN_CATEGORY, DatabaseHandler.EXPENSE_DATE };
		
		String sel = DatabaseHandler.EXPENSE_DATE + " BETWEEN ? AND ? ";

		String [] selArg = {fromDate.toString(), toDate.toString()};

		Cursor c = db.query(columns, sel, selArg, DatabaseHandler.EXPENSE_MAIN_CATEGORY, null, null);
		
		ArrayList<String> expenses = new ArrayList<String>();
		ArrayList<String> mCate = new ArrayList<String>();
		
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			expenses.add(c.getString(0));
			mCate.add(c.getString(1));
		}
		
		//Chart & Computation
		double value = 0;
		double total = 0;
		int range = 0;
		int stDegree = 270;
		
		for(int i = 0; i < expenses.size(); i++) {
			value = Double.parseDouble(expenses.get(i)); 
			total += value;
		}
		
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); // The desired size of the child
		params1.setMargins(80, 180, 80, 310); // location in screen
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		params2.setMargins(40, 550, 40, 0);
		int[] colors = {0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffff00, 0xff00ffff, 0xffff00ff, 0xff888888, 
						0xffcccccc, 0xff444444, 0xff000000, 0x88888888, 0x66666666, 0x44444444, 0x22222222,
						0x11111111, 0x33333333, 0x55555555, 0x99999999, 0x12121212, 0x35353553, 0x87426545,
						0xf1f1f1f1, 0xaaaaaaaa, 0x1a1a1a1a, 0xbbbbbbbb, 0xeeeeeeee, 0xe5e5a5a5, 0x21212121};

		for(int j = 0; j < expenses.size(); j++) {
			//pie chart
			range = (int)(Double.valueOf(expenses.get(j))/total*360);
			ShapeDrawable piechart = new ShapeDrawable(new ArcShape(stDegree, range));
			piechart.setIntrinsicHeight(320);
			piechart.setIntrinsicWidth(320);
			piechart.getPaint().setColor(colors[j]);
			stDegree += range;
			
			ImageView iv = new ImageView(this);
			iv.setImageDrawable(piechart);
			rl.addView(iv, params1);
			
			//legend squarebox
			ShapeDrawable roundRect = new ShapeDrawable(new RoundRectShape(new float[] {5,5,5,5,5,5,5,5}, null,null));
			roundRect.setIntrinsicHeight(30);
			roundRect.setIntrinsicWidth(30);
			roundRect.getPaint().setColor(colors[j]);
			ImageView iv2 = new ImageView(this);
			iv2.setImageDrawable(roundRect);
			TableRow tr = new TableRow(this);
			tr.setPadding(2, 2, 2, 2);
			tr.addView(iv2);
			
			//MainCategory for legend
			TextView txt1 = new TextView(this);
			TextView txt2 = new TextView(this);
			TextView txt3 = new TextView(this);
			txt3.setText("RM " + expenses.get(j));
			txt2.setText("" + (String.format("%.2f", Double.valueOf(expenses.get(j))/total*100)) + "%");
			txt1.setText(mCate.get(j));
			tr.addView(txt1);
			tr.addView(txt2);
			tr.addView(txt3);
			tl.addView(tr);
		}
		rl.addView(tl, params2);
	}
	
	
	
	/**
	 * This is to place the buttons and title
	 */
	private void placeLayout(){	
		getDates();

		titleDate = fromDate.substring(0, 4) + "-" + fromDate.substring(4, 6) + "-" + fromDate.substring(6, 8)
				+ "  to  " + 
				toDate.substring(0, 4) + "-" + toDate.substring(4, 6) + "-" + toDate.substring(6, 8);

		TextView titleText = new TextView(this);
		titleText.setText(titleDate);
		
		// The desired size of the child		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(260,30); 
		params.setMargins(110, 110, 110, 660);
		rl.addView(titleText, params);
		
		
		Button timeBut = new Button(this);
		timeBut.setText(timeString);
		params = new RelativeLayout.LayoutParams(100, 60);
		params.setMargins(75, 10, 290, 730);
		rl.addView(timeBut, params);
		timeBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				selTime = (selTime % 3) + 1;
				
				tl.removeAllViews();
				rl.removeAllViews();
				sv.removeAllViews();
				
				placeLayout();
				calculateChart();
				sv.addView(rl);
			}			
		});
	}
	
		
	
	
	/**
	 * To obtain the date range of the current selection
	 */
	public void getDates(){
		Calendar calendar = Calendar.getInstance();
		int lastDay = calendar.getActualMaximum(Calendar.DATE);
		
		if(selTime == DAILY){
			timeString = "Day";
			
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
			
			fromDate = todayDate; 
			toDate = todayDate;
			
		}else if(selTime == WEEKLY){
			timeString = "Week";

			int weekStartDay = 0;
			int weekEndDay = 0;
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
			
			fromDate = wkStartDate;
			toDate = wkEndDate;
			
		}else if(selTime == MONTHLY){
			timeString = "Month";

			String currentYearMonth = String.valueOf(today.year);
			
			if( (today.month + 1) < 10){
				currentYearMonth += "0";
			}
			currentYearMonth += String.valueOf( (today.month + 1) );
			
			fromDate = currentYearMonth + "01";
			toDate= currentYearMonth + lastDay;			
		}
		
	}	
		
}