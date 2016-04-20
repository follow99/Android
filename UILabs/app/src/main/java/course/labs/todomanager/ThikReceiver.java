package course.labs.todomanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ThikReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
			Toast.makeText(context,"what's going on?",Toast.LENGTH_SHORT).show();
		}
// Do something
		else {
			Toast.makeText(context,"一脸懵逼",Toast.LENGTH_SHORT).show();
		}
// Do something else
	}
}
