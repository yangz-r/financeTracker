package mad.asign.financeTracker;

import mad.asign.financeTracker.R.drawable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;



/**
 * This activity is use to show the interface to manage
 * the categories
 * 
 * 
 * @author RoboR
 *
 */
public class ManageScreen extends Activity{
	/**
	 * Width of the screen
	 */
	private int screenWidth;
	
	/**
	 * height of the screen
	 */
	private int screenHeight;	
	
	/**
	 * The shared preference to store the categories
	 */
	private SharedPreferences preference;
	
	/**
	 * Incremental id value
	 */
	private int idCount = 0;
	
	/**
	 * main layout for this activity
	 */
	private LinearLayout mainLayout;
	
	/**
	 * layout to display the categories
	 */
	private TableLayout categoryTableLayout;
	
	/**
	 * to store the name of the category
	 */
	private String [] categories;

	
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		
		mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);

		//Get the categories available
		preference = getSharedPreferences(FinanceTracker.PREF_FILE_NAME, Context.MODE_PRIVATE);	
		String categoryStr = preference.getString(FinanceTracker.PREF_MAIN_CATEGORY, null);	
		categories = categoryStr.split(",");
		
		categoryTableLayout = new TableLayout(this);
		
		//Add the category row by row into the table
		for(int i = 0; i < categories.length; i++){
			addCategoryRow(categories[i]);
		}
		
		ScrollView scroll = new ScrollView(this);
		scroll.setLayoutParams(new ScrollView.LayoutParams(screenWidth, screenHeight - 320) );
		scroll.addView(categoryTableLayout);
		mainLayout.addView(scroll);
		
		addCategoryAddRow();
		
		setContentView(mainLayout);
	}
	
	
	
	/**
	 * To add an row that shows the category information
	 * Contains the category name, a remove button and a edit
	 * name button
	 * 
	 * @param category name of the category to be added
	 */
	private void addCategoryRow(String category){
	  Bitmap removeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.minus_icon);
	  Bitmap editIcon = BitmapFactory.decodeResource(getResources(), drawable.edit_name_icon);

		TableRow categoryRow = new TableRow(this);
		categoryRow.setPadding(20, 30, 20, 10);
		categoryRow.setId(idCount++);
		
		TableRow.LayoutParams paramCol_1 = new TableRow.LayoutParams( (int) ( (screenWidth - 40) * 0.45), TableRow.LayoutParams.MATCH_PARENT);
		TableRow.LayoutParams paramCol_2 = new TableRow.LayoutParams( (int) ( (screenWidth - 40) * 0.275), TableRow.LayoutParams.MATCH_PARENT);
		TableRow.LayoutParams paramCol_3 = new TableRow.LayoutParams( (int) ( (screenWidth - 40) * 0.275), TableRow.LayoutParams.MATCH_PARENT);
		
		//Category
		TextView categoryText = new TextView(this);		
		categoryText.setId(idCount++);
		categoryText.setText(category);
		categoryText.setTextSize(18f);
		categoryText.setBackgroundColor(Color.rgb(239, 228, 176));
		categoryText.setPadding(8, 8, 8, 8);
		categoryText.setLayoutParams(paramCol_1);
		categoryText.setGravity(Gravity.CENTER_VERTICAL);
		categoryRow.addView(categoryText);
		
		//Remove Button
		ImageButton removeBut = new ImageButton(this);
		removeBut.setPadding(0, 0, 0, 0);
		removeBut.setImageBitmap(removeIcon);
		removeBut.setId(idCount++);
		removeBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(final View v){
				
				AlertDialog.Builder addAlert = new AlertDialog.Builder(v.getContext());
	        	addAlert.setTitle("Remove Category");
	        	addAlert.setMessage("Confirm to Delete");		        	
	        			        	
	        	addAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {							
						String categoryString = preference.getString(FinanceTracker.PREF_MAIN_CATEGORY, null);
						int position = v.getId() / 4;

						TextView categoryTxt = (TextView) findViewById(position * 4 + 1);
						String removeCategory = categoryTxt.getText().toString(); 

						
						categoryString = categoryString.replace("," + removeCategory, "");
						categoryString = categoryString.replace(removeCategory + ",", "");

						SharedPreferences.Editor editor = preference.edit();

						editor.putString(FinanceTracker.PREF_MAIN_CATEGORY, categoryString);
						editor.commit();

						LinearLayout removeLayout = (LinearLayout) findViewById(position * 4);					
						categoryTableLayout.removeView(removeLayout);
						
						Toast.makeText(ManageScreen.this, 
								"Category " + removeCategory + " has been removed", 
								Toast.LENGTH_SHORT).show();
					}	
				});
	        	
	        	addAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();							
					}
				});
	        	
	        	addAlert.show();		
			}				
		});		
		
		LinearLayout removeButLayout = new LinearLayout(this);
		removeButLayout.addView(removeBut);
		removeButLayout.setLayoutParams(paramCol_2);
		removeButLayout.setGravity(Gravity.CENTER);
		categoryRow.addView(removeButLayout);
		
		//Edit Name Button
		ImageButton editBut = new ImageButton(this);
		editBut.setPadding(0, 0, 0, 0);
		editBut.setId(idCount++);
		editBut.setImageBitmap(editIcon);		
		editBut.setOnClickListener(new OnClickListener(){

			public void onClick(final View v){
	        	final EditText newNameText  = new EditText(v.getContext());

				AlertDialog.Builder changeAlert = new AlertDialog.Builder(v.getContext());
	        	changeAlert.setTitle("Change Name");
	        	changeAlert.setMessage("Input");			        	
	        	changeAlert.setView(newNameText);		        	
	        	changeAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int position = v.getId() / 4;

						String categoryString = preference.getString(FinanceTracker.PREF_MAIN_CATEGORY, null);
						
						String [] categories = categoryString.split(",");
						boolean exist = false;
						String newName = newNameText.getText().toString();
						
						for(int i = 0; i < categories.length; i++){
							
							if( categories[i].equalsIgnoreCase(newName) ){
								exist = true;
								Toast.makeText(ManageScreen.this, 
										"Category " + newName + " has already exits", 
										Toast.LENGTH_SHORT).show();									
								break;
							}
						}
						
						if(!exist){
							TextView chgView = (TextView) findViewById(position * 4 + 1);	
							String previousCategory = chgView.getText().toString();
							
							categoryString = categoryString.replace(previousCategory,
									newName);
							
							SharedPreferences.Editor editor = preference.edit();
							editor.putString(FinanceTracker.PREF_MAIN_CATEGORY, categoryString);
							editor.commit();

							chgView.setText(newName);
							
							Toast.makeText(ManageScreen.this, 
									previousCategory + " has changed to " + newName,
									Toast.LENGTH_SHORT).show();
						}							
					}
						
				});
	        	
	        	changeAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();							
					}
				});
	        	
	        	changeAlert.show();
	        }		   
		});		
		
		LinearLayout editButLayout = new LinearLayout(this);
		editButLayout.addView(editBut);
		editButLayout.setLayoutParams(paramCol_3);
		editButLayout.setGravity(Gravity.CENTER);		
		categoryRow.addView(editButLayout);		
		
		categoryTableLayout.addView(categoryRow);
	}
	
	

	/**
	 * To add the button for adding a new category
	 * at the bottom of the screen.
	 * 
	 * The buttons is used to add a new category. Validation
	 * will be done.
	 */
	private void addCategoryAddRow(){	  		  
		Button addBut = new Button(this);
		addBut.setText("Add Category");
		addBut.setOnClickListener(new OnClickListener() {
			
			public void onClick(final View v){
				
				final EditText categoryInput = new EditText(v.getContext());
	
				//Alert dialog to prompt for the new category's name
	        	AlertDialog.Builder addAlert = new AlertDialog.Builder(v.getContext());
	        	addAlert.setTitle("Add New Category");
	        	addAlert.setMessage("Input");		        	
	        	addAlert.setView(categoryInput);		        	
	        	addAlert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						SharedPreferences preference = getSharedPreferences(FinanceTracker.PREF_FILE_NAME, Context.MODE_PRIVATE);													
						SharedPreferences.Editor editor = preference.edit();

						String categories = preference.getString(FinanceTracker.PREF_MAIN_CATEGORY, null);	
						
						String [] c = categories.split(",");
						String inputCategory = categoryInput.getText().toString();
						boolean exist = false;
						
						for(int i = 0; i < c.length; i++){
							if( c[i].equalsIgnoreCase(inputCategory) ){
								exist = true;
								Toast.makeText(ManageScreen.this, "Category Aready Exist", Toast.LENGTH_SHORT).show();									
								break;
							}
						}
						
						if(!exist){
							categories += "," + inputCategory;
							
							editor.putString(FinanceTracker.PREF_MAIN_CATEGORY, categories);
							editor.commit();
							
							addCategoryRow(inputCategory);
							
							Toast.makeText(ManageScreen.this, inputCategory + " has added into Category", 
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	        	
	        	addAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();							
					}
				});
	        	
	        	addAlert.show();
			}
		});
		
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
		mainLayout.addView(lineView);
				
		//Place the add Button
		LinearLayout addButLayout = new LinearLayout(this);
		addButLayout.setPadding(0, 10, 0, 0);
		addButLayout.setLayoutParams( new LinearLayout.LayoutParams(screenWidth, 300) );
		addButLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		addButLayout.addView(addBut);

		mainLayout.addView(addButLayout);
	}
}
