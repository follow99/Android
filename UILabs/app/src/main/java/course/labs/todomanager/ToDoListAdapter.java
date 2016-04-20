package course.labs.todomanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import course.labs.todomanager.ToDoItem.Status;




public class ToDoListAdapter extends BaseAdapter {

	// List of ToDoItems
	private final List<ToDoItem> mItems = new ArrayList<ToDoItem>();
	
	private final Context mContext;

	private static final String TAG = "Lab-UserInterface";

	public ToDoListAdapter(Context context) {

		mContext = context;


	}






	// Add a ToDoItem to the adapter
	// Notify observers that the data set has changed

	public void add(ToDoItem item) {

		mItems.add(item);
		notifyDataSetChanged();

	}
	public void remove(int pos){

		mItems.remove(pos);
		notifyDataSetChanged();
	}
	
	// Clears the list adapter of all items.
	
	public void clear(){

		mItems.clear();
		notifyDataSetChanged();
	
	}

	// Returns the number of ToDoItems

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Retrieve the number of ToDoItems

	@Override
	public Object getItem(int pos) {

		return mItems.get(pos);

	}

	// Get the ID for the ToDoItem
	// In this case it's just the position

	@Override
	public long getItemId(int pos) {

		return pos;

	}

	//Create a View to display the ToDoItem 
	// at specified position in mItems


		@Override
	    public View getView(final int position, final View convertView, ViewGroup parent) {


		//TODO - Get the current ToDoItem
		final ToDoItem toDoItem = (ToDoItem) getItem(position);

		//TODO - Inflate the View for this ToDoItem
		// from todo_item.xml.
		final RelativeLayout itemLayout =(RelativeLayout) ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.todo_item,parent, false);
		//LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View itemLayout = inflater.inflate(R.layout.todo_item, parent, false);
		//TODO - Fill in specific ToDoItem data
		// Remember that the data that goes in this View
		// corresponds to the user interface elements defined 
		// in the layout file
		//TODO progress bar
		final ProgressBar progressBar= (ProgressBar) itemLayout.findViewById(R.id.progressBar);
		progressBar.setMax((int) ((toDoItem.getDate().getTime()-toDoItem.SettedDate)/(1000 * 60 * 60 * 24)));
		progressBar.setProgress((int) ((new Date().getTime()-toDoItem.SettedDate)/(1000 * 60 * 60 * 24)));
		//progressDialog.setProgressBarVisibility(true);
		//setProgress(4500);

		//TODO WarningView
		final TextView warningView=(TextView) itemLayout.findViewById(R.id.WarningView);


		//TODO - Display Title in TextView

		final TextView titleView = (TextView) itemLayout.findViewById(R.id.titleView);
		titleView.setText(toDoItem.getTitle());

		//titleView.setBackgroundColor(Color.argb(127,255,0,0));
		// TODO - Set up Status CheckBox

		final CheckBox statusView = (CheckBox) itemLayout.findViewById(R.id.statusCheckBox);
		statusView.setChecked(toDoItem.getStatus() == Status.DONE);

		//TODO - set backgroundColor based on status
		if ((toDoItem.getStatus() == Status.DONE)){
			itemLayout.setBackgroundColor(Color.argb(127,0,255,255));//CYAN

			warningView.setVisibility(View.INVISIBLE);
			Toast.makeText(itemLayout.getContext(),String.valueOf((int) ((toDoItem.getDate().getTime()-toDoItem.SettedDate)/(1000 * 60 * 60 * 24))),Toast.LENGTH_SHORT).show();
			Toast.makeText(itemLayout.getContext(),String.valueOf((int) ((toDoItem.getDate().getTime()-new Date().getTime())/(1000 * 60 * 60 * 24))),Toast.LENGTH_SHORT).show();
			//warningView.setBackgroundColor(Color.argb(0,0,255,255));
		}
		else {
			itemLayout.setBackgroundColor(Color.argb(127,255,0,255));//MAGENTA);//

			warningView.setVisibility(View.VISIBLE);
			//warningView.setBackgroundColor(Color.GREEN);
		}

		statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				log("Entered onCheckedChanged()");
				
				// TODO - Set up and implement an OnCheckedChangeListener, which
				//TODO -set color change when checkbox been checked or not
				// is called when the user toggles the status checkbox
				if (isChecked){
					toDoItem.setStatus(Status.DONE);
					itemLayout.setBackgroundColor(Color.argb(127,0,255,255));//CYAN

					warningView.setVisibility(View.INVISIBLE);

					warningView.setBackgroundColor(Color.RED);
					notifyDataSetChanged();
                    //Toast.makeText(mContext,String.valueOf(toDoItem.getDate().getTime()),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(mContext,String.valueOf(toDoItem.getDate().getTime()-new Date().getTime()),Toast.LENGTH_SHORT).show();

				}
				else{
					toDoItem.setStatus(Status.NOTDONE);
					itemLayout.setBackgroundColor(Color.argb(127,255,0,255));}//MAGENTA);//

					warningView.setVisibility(View.VISIBLE);

					warningView.setBackgroundColor(Color.GREEN);
					notifyDataSetChanged();
			}

			

		});




		//TODO - Display Priority in a TextView

		//final TextView priorityView = (TextView) itemLayout.findViewById(R.id.spinner);
            // priorityView.setText(toDoItem.getPriority().toString());

            //TODO -Display Priority in a Spinner
            final Spinner spinner= (Spinner) itemLayout.findViewById(R.id.spinner);

            //TODO- need short out 两个一样的选项出现在下拉菜单里
            String[] strings = {toDoItem.getPriority().toString(),"LOW", "MED", "HIGH"};

            ArrayAdapter<String> adapter;

            {
                adapter = new ArrayAdapter<String>(itemLayout.getContext(), R.layout.dropdown_item,strings);
            }
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    toDoItem.setPriority(ToDoItem.Priority.valueOf(adapterView.getItemAtPosition(i).toString()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

		
		// TODO - Display Time and Date. 
		// Hint - use ToDoItem.FORMAT.format(toDoItem.getDate()) to get date and time String
        //TODO set data color to red when less than 24hrs
		final TextView dateView = (TextView) itemLayout.findViewById(R.id.dateView);
            if (toDoItem.getDate().getTime()-new Date().getTime()<=86400000){
				warningView.setBackgroundColor(Color.RED);
                dateView.setTextColor(Color.RED);
				notifyDataSetChanged();
			}

		    dateView.setText(toDoItem.getDate().toString());



		// Return the View you just created
		return itemLayout;

	}

	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}

}
