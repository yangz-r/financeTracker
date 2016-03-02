package mad.asign.financeTracker;


import java.util.Calendar;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



/**
 * The screen is use to show the interface for the user to 
 * key in values for the search. It is used for searching
 * expense records from the database
 * 
 * @author RoboR
 *
 */
public class SearchScreen extends Activity {
	/**
	 * The calendar of the system, to get the date
	 */
	final Calendar calendar = Calendar.getInstance();
	
	/**
	 * id for from date selection
	 */
	final private int ID_FROMDATE = 1;
	
	/**
	 * id for to date selection
	 */
	final private int ID_TODATE = 2;
	
	/**
	 * id for name selection
	 */
	final private int ID_NAME = 3;
	
	/**
	 * id for category selection
	 */
	final private int ID_CATEGORY = 4;
	
	/**
	 * to store the date in integer.
	 * Store the year, month(0 - 11) and day(1 - 31)
	 * with the order of the first String to the last
	 * 
	 */
	private int [] fromDate = new int [3];
	
	/**
	 * to store the date in integer.
	 * Store the year, month(0 - 11) and day(1 - 31)
	 * with the order of the first String to the last
	 * 
	 */
	private int [] toDate = new int [3];
	
	/**
	 * Date picker dialog
	 */
	private DatePickerDialog dialog = null; 
	
	/**
	 * Width of the screen
	 */
	private int screenWidth;
	
	/**
	 * Height of the screen
	 */
	private int screenHeight;
	
	/**
	 * main layout of this activity
	 */
	private LinearLayout mainLayout;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);		
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		
		mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		//Initialization		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		setSelectedDate(ID_FROMDATE, year, month, day);
		setSelectedDate(ID_TODATE, year, month, day);
		
		addInputTable();
		addButtonRow();
				
		ScrollView scroll = new ScrollView(this);
		scroll.setLayoutParams( new LinearLayout.LayoutParams(screenWidth, screenHeight) );
		scroll.addView(mainLayout);
		
		setContentView(scroll);		
	}
	
	
	
	/**
	 * Add an table that consits of the labels and the input field
	 */
	private void addInputTable(){
		String selDate = fromDate[2] + "-"  + (fromDate[1] + 1) + "-" + fromDate[0]; 
		InputFilter [] filters = new InputFilter[1];

		TableLayout inputTable = new TableLayout(this);		
		TableRow.LayoutParams paramLabel = new TableRow.LayoutParams( (int)(screenWidth * 0.3), TableRow.LayoutParams.MATCH_PARENT);		
		TableRow.LayoutParams paramInput = new TableRow.LayoutParams( (int)(screenWidth * 0.7) - 20, 
										TableRow.LayoutParams.MATCH_PARENT);
		
		int textSize = 13;
		int textWidth = (int)(screenWidth * 0.25);
		
		//Date Row
		TableRow dateRow = new TableRow(this);
		dateRow.setGravity(Gravity.CENTER);
		dateRow.setPadding(0, 25, 0, 25);
		
		TextView dateText = new TextView(this);		
		dateText.setTextSize(textSize);
		dateText.setText("Date");
		dateText.setBackgroundColor(Color.rgb(180, 230, 30) );
		dateText.setWidth(textWidth);
		
		LinearLayout dateTextLayout = new LinearLayout(this);
		dateTextLayout.setGravity(Gravity.CENTER);
		dateTextLayout.setLayoutParams(paramLabel);
		dateTextLayout.addView(dateText);
		dateText.setPadding(20, 20, 20, 20);
		dateRow.addView(dateTextLayout);
		
		LinearLayout dateRangeLayout = new LinearLayout(this);		
		dateRangeLayout.setOrientation(LinearLayout.HORIZONTAL);
		dateRangeLayout.setLayoutParams(paramInput);
		dateRangeLayout.setGravity(Gravity.CENTER);
		
		//Date selection
		Button fromDateBut = new Button(this);
		fromDateBut.setId(ID_FROMDATE);
		fromDateBut.setText(selDate);
		fromDateBut.setTextSize(textSize);
		fromDateBut.setWidth(145);
		fromDateBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
                dialog = new DatePickerDialog(v.getContext(),
                                 new PickDate(ID_FROMDATE), fromDate[0], fromDate[1], fromDate[2]);
                dialog.updateDate(fromDate[0], fromDate[1], fromDate[2]);                                
                dialog.show();    	
			}				
		});
		dateRangeLayout.addView(fromDateBut);

		TextView toText = new TextView(this);
		toText.setTextSize(textSize);
		toText.setPadding(8, 0, 8, 0);
		toText.setText("to");
		dateRangeLayout.addView(toText);
		
		Button toDateBut = new Button(this);
		toDateBut.setId(ID_TODATE);
		toDateBut.setText(selDate);
		toDateBut.setTextSize(textSize);
		toDateBut.setWidth(145);
		toDateBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
                dialog = new DatePickerDialog(v.getContext(),
                                 new PickDate(ID_TODATE), toDate[0], toDate[1], toDate[2]);
                dialog.updateDate(toDate[0], toDate[1], toDate[2]);                                
                dialog.show();    				
			}				
		});
		dateRangeLayout.addView(toDateBut);							
		dateRow.addView(dateRangeLayout);
		
		inputTable.addView(dateRow);
		

		//Name Row
		TableRow nameRow = new TableRow(this);
		nameRow.setGravity(Gravity.CENTER);
		nameRow.setPadding(0, 25, 0, 25);

		TextView nameText = new TextView(this);
		nameText.setTextSize(textSize);
		nameText.setText("Name");
		nameText.setWidth(textWidth);
		nameText.setPadding(20, 20, 20, 20);
		nameText.setBackgroundColor(Color.rgb(180, 230, 30) );
		
		LinearLayout nameTextLayout = new LinearLayout(this);
		nameTextLayout.setGravity(Gravity.CENTER);
		nameTextLayout.setLayoutParams(paramLabel);
		nameTextLayout.addView(nameText);
		
		nameRow.addView(nameTextLayout);
		
		filters[0] = new InputFilter.LengthFilter(40);

		final EditText nameInput = new EditText(this);
		nameInput.setId(ID_NAME);		
		nameInput.setFilters(filters);
		nameInput.setMaxLines(1);
		nameInput.setLayoutParams(paramInput);		
		nameRow.addView(nameInput);
	
		inputTable.addView(nameRow);
	
		
		//Main Category Row
		TableRow mainCategoryRow = new TableRow(this);
		mainCategoryRow.setPadding(0, 25, 0, 25);
		mainCategoryRow.setGravity(Gravity.CENTER);
		
		TextView categoryText = new TextView(this);
		categoryText.setTextSize(textSize);
		categoryText.setText("Category");
		categoryText.setWidth(textWidth);
		categoryText.setPadding(20, 20, 20, 20);
		categoryText.setBackgroundColor(Color.rgb(180, 230, 30) );

		LinearLayout categoryTextLayout = new LinearLayout(this);
		categoryTextLayout.setGravity(Gravity.CENTER);				
		categoryTextLayout.setLayoutParams(paramLabel);
		categoryTextLayout.addView(categoryText);
		
		mainCategoryRow.addView(categoryTextLayout);

		String [] categoryList = getCategoryList();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner = new Spinner(this);
		spinner.setId(ID_CATEGORY);		
		spinner.setLayoutParams(paramInput);
		spinner.setPadding(0, 0, 0, 0);
		spinner.setAdapter(adapter);		
		mainCategoryRow.addView(spinner);

		inputTable.addView(mainCategoryRow);
		

		mainLayout.addView(inputTable);
	}
	
	
	
	/**
	 * Add the buttons: search and clear 
	 */
	private void addButtonRow(){
		LinearLayout buttonRowLayout = new LinearLayout(this);
		buttonRowLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonRowLayout.setGravity(Gravity.CENTER);
		
		int textSize = 15;
		
		//Search Button
		Button searchBut = new Button(this);
		searchBut.setText("Search");
		searchBut.setTextSize(textSize);
		searchBut.setWidth(180);		
		searchBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				String ymdDateFrom = String.valueOf(fromDate[0]);
				if( (fromDate[1] + 1) < 10){
					ymdDateFrom += "0";
				}
				ymdDateFrom += String.valueOf(fromDate[1] + 1);
				if(fromDate[2] < 10){
					ymdDateFrom += "0";
				}
				ymdDateFrom += String.valueOf(fromDate[2]);

				String ymdDateTo = String.valueOf(toDate[0]);
				if( (toDate[1] + 1) < 10){
					ymdDateTo += "0";
				}
				ymdDateTo += String.valueOf(toDate[1] + 1);
				if(toDate[2] < 10){
					ymdDateTo += "0";		
				}
				ymdDateTo += toDate[2];
				
				Spinner spinner = (Spinner) findViewById(ID_CATEGORY);
				String category = spinner.getSelectedItem().toString();
				
				EditText nameInput = (EditText) findViewById(ID_NAME); 
				String name = nameInput.getText().toString();
				
				SharedPreferences preference = getSharedPreferences(FinanceTracker.PREF_FILE_NAME, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();

				editor.putString(FinanceTracker.SEARCH_DATE_FROM, ymdDateFrom);
				editor.putString(FinanceTracker.SEARCH_DATE_TO, ymdDateTo);
				editor.putString(FinanceTracker.SEARCH_CATEGORY, category);
				
				if(name.contentEquals("")){
					editor.putString(FinanceTracker.SEARCH_NAME, null);
				}else{
					editor.putString(FinanceTracker.SEARCH_NAME, name);
				}
				
				editor.commit();
				
				Intent toResultView = new Intent(SearchScreen.this, ResultView.class);
				startActivity(toResultView);
			}
		});		
		
		LinearLayout searchButLayout = new LinearLayout(this);
		searchButLayout.setPadding(0, 0, 20, 0);
		searchButLayout.addView(searchBut);
		
		buttonRowLayout.addView(searchButLayout);
		
		Button clearBut = new Button(this);		
		clearBut.setText("Clear");
		clearBut.setTextSize(textSize);
		clearBut.setWidth(180);
		clearBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				EditText nameInput = (EditText) findViewById(ID_NAME);
				nameInput.setText("");
			}
		});
		
		LinearLayout clearButLayout = new LinearLayout(this);
		clearButLayout.setPadding(20, 0, 0, 0);
		clearButLayout.addView(clearBut);
		
		buttonRowLayout.addView(clearButLayout);
		
		
		mainLayout.addView(buttonRowLayout);
	}
	
	
	
	/**
	 * The category List consists of the field "All Categories"
	 * to search for records of all categories
	 * 
	 * @return string array of the category list
	 */
	private String [] getCategoryList(){
		SharedPreferences preference = getSharedPreferences(FinanceTracker.PREF_FILE_NAME, Context.MODE_PRIVATE);

		String str = preference.getString(FinanceTracker.PREF_MAIN_CATEGORY, null);
		String [] categories = str.split(",");
		
		String [] extraCategories = new String [categories.length + 1];
		extraCategories[0] = "All Categories";
		
		for(int i = 1; i < extraCategories.length; i++){
			extraCategories[i] = categories[i-1];
		}
				
		return extraCategories;		
	}
	

	
	/**
	 * Change the selected date of to date and form date
	 * 
	 * @param id identify for the type of date, ID_FROMDATE or ID_TODATE
	 * @param year value of year, integer
	 * @param month value of the month, integer
	 * @param day value of the day, integer
	 */
	private void setSelectedDate(int id, int year, int month, int day){
		if(id == ID_FROMDATE){
			fromDate[0] = year;
			fromDate[1] = month;
			fromDate[2] = day;
			
		}else if(id == ID_TODATE){
			toDate[0] = year;
			toDate[1] = month;
			toDate[2] = day;
		}	
	}
	
	
	
	/**
	 * Handler for the dialog picker, changes the selected to and from
	 * date. and also validate the range of it
	 * 
	 * @author RoboR
	 *
	 */
	private class PickDate implements DatePickerDialog.OnDateSetListener {
		private int id;
		
		PickDate(int id){
			this.id = id;
		}

	    @Override
	    public void onDateSet(DatePicker view, int year, int month, int day) {
	        view.updateDate(year, month, day);

	        if(this.id == ID_FROMDATE){
		        setSelectedDate(ID_FROMDATE, year, month, day);

	        	TextView tv = (TextView) findViewById(ID_FROMDATE);		
		        tv.setText( day + "-"  + (month + 1) + "-" + year );
		        
	        	//Check the year
		        if(fromDate[0] > toDate[0]){
			        setSelectedDate(ID_TODATE, year, month, day);

			        tv = (TextView) findViewById(ID_TODATE);
			        tv.setText( day + "-"  + (month + 1) + "-" + year );
			        
		        } else if(fromDate[1] > toDate[1]){
		        	//Check the month
			        setSelectedDate(ID_TODATE, year, month, day);

			        tv = (TextView) findViewById(ID_TODATE);
			        tv.setText( day + "-"  + (month + 1) + "-" + year );	
			        
		        }else if(fromDate[2] > toDate[2]){
		        	//Check the Date
			        setSelectedDate(ID_TODATE, year, month, day);

			        tv = (TextView) findViewById(ID_TODATE);
			        tv.setText( day + "-"  + (month + 1) + "-" + year );		        	
		        }
		        
	        }else if (this.id == ID_TODATE){
		        setSelectedDate(ID_TODATE, year, month, day);

		        TextView tv = (TextView) findViewById(ID_TODATE);
		        tv.setText( day + "-"  + (month + 1) + "-" + year );
		        
		      //Check the year
		        if(fromDate[0] > toDate[0]){
			        setSelectedDate(ID_FROMDATE, year, month, day);

			        tv = (TextView) findViewById(ID_FROMDATE);
			        tv.setText( day + "-"  + (month + 1) + "-" + year );
			        
		        } else if(fromDate[1] > toDate[1]){
		        	//Check the month
			        setSelectedDate(ID_FROMDATE, year, month, day);

			        tv = (TextView) findViewById(ID_FROMDATE);
			        tv.setText( day + "-"  + (month + 1) + "-" + year );	
			        
		        }else if(fromDate[2] > toDate[2]){
		        	//Check the Date
			        setSelectedDate(ID_FROMDATE, year, month, day);

			        tv = (TextView) findViewById(ID_FROMDATE);
			        tv.setText( day + "-"  + (month + 1) + "-" + year );		        	
		        }
	        }
	        
	        dialog.hide();
	    }	    
	}
}
