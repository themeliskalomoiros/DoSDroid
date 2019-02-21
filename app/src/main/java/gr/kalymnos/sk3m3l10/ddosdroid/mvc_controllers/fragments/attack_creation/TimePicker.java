package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_creation;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

public class TimePicker extends DialogFragment {
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);
        return new TimePickerDialog(getContext(), timeSetListener, currentHour, currentMinute,
                DateFormat.is24HourFormat(getContext()));
    }

    public void setTimeSetListener(TimePickerDialog.OnTimeSetListener timeSetListener) {
        this.timeSetListener = timeSetListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timeSetListener = null;
    }
}
