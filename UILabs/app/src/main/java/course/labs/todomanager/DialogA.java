package course.labs.todomanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class DialogA extends DialogFragment implements DialogInterface.OnClickListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("biaoti")
				.setPositiveButton("ok", this)
				.setNegativeButton("cancel", this);

		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		switch(id) {
			case AlertDialog.BUTTON_NEGATIVE:
				Toast.makeText(getActivity(), "Negative", Toast.LENGTH_SHORT).show();
				break;
			case AlertDialog.BUTTON_POSITIVE:
				Toast.makeText(getActivity(), "Positive", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
		}
	}
}
