package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes;

import static android.support.constraint.Constraints.TAG;

public class AttackCreationViewMvcImpl implements AttackCreationViewMvc {
    private View root;
    private EditText websiteEditText;
    private TextView websiteCreationTime, networkConfigHint;
    private Spinner spinner;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private ImageView datePicker, timePicker;

    private OnAttackCreationClickListener onAttackCreationClickListener;
    private OnNetworkConfigurationSelectedListener onNetworkConfigurationSelectedListener;
    private OnWebsiteTextChangeListener onWebsiteTextChangeListener;
    private OnPickerClickListener onPickerClickListener;

    public AttackCreationViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initViews(inflater, container);
    }

    private void initViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_attack_creation, container, false);
        websiteCreationTime = root.findViewById(R.id.tv_website_creation_time);
        networkConfigHint = root.findViewById(R.id.tv_network_config_hint);
        progressBar = root.findViewById(R.id.progressBar);
        initPickers();
        initEditText();
        initSpinner(inflater);
        initFab();
    }

    private void initPickers() {
        initDatePicker();
        initTimePicker();
    }

    private void initDatePicker() {
        datePicker = root.findViewById(R.id.date_picker);
        datePicker.setOnClickListener(view -> {
            if (onPickerClickListener != null)
                onPickerClickListener.onDatePickerClick();
        });
    }

    private void initTimePicker() {
        timePicker = root.findViewById(R.id.time_picker);
        timePicker.setOnClickListener(view -> {
            if (onPickerClickListener != null)
                onPickerClickListener.onTimePickerClick();
        });
    }

    private void initEditText() {
        websiteEditText = root.findViewById(R.id.ed_website);
        websiteEditText.addTextChangedListener(createTextChangedListenerForEditText());
    }

    private TextWatcher createTextChangedListenerForEditText() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (onWebsiteTextChangeListener != null) {
                    onWebsiteTextChangeListener.websiteTextChanged(editable.toString());
                }
            }
        };
    }

    private void initSpinner(LayoutInflater inflater) {
        spinner = root.findViewById(R.id.spinner);
        spinner.setAdapter(getArrayAdapter(inflater));
        spinner.setOnItemSelectedListener(getItemSelectedListener(inflater));
    }

    private ArrayAdapter<CharSequence> getArrayAdapter(LayoutInflater inflater) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(),
                R.array.network_technologies_titles, R.layout.item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private AdapterView.OnItemSelectedListener getItemSelectedListener(LayoutInflater inflater) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (onNetworkConfigurationSelectedListener != null) {
                    setNetworkHintAccordingTo(pos);
                }
            }

            private void setNetworkHintAccordingTo(int pos) {
                String hint = getNetworkPrefix() + " " + getNetworkDescription(pos);
                onNetworkConfigurationSelectedListener.onNetworkSelected(hint);
            }

            @NonNull
            private String getNetworkPrefix() {
                return inflater.getContext().getString(R.string.network_configuration_prefix);
            }

            private String getNetworkDescription(int pos) {
                return inflater.getContext().getResources().getStringArray(R.array.network_technologies_description)[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
    }

    private void initFab() {
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener((view -> {
            if (onAttackCreationClickListener != null) {
                onAttackCreationClickListener.onAttackCreationClicked(websiteEditText.getText().toString());
            }
        }));
    }

    @Override
    public void bindWebsiteCreationTime(String hint) {
        websiteCreationTime.setText(hint);
    }

    @Override
    public void bindNetworkConfig(String hint) {
        networkConfigHint.setText(hint);
    }

    @Override
    public void setOnAttackCreationClickListener(OnAttackCreationClickListener listener) {
        onAttackCreationClickListener = listener;
    }

    @Override
    public void setOnNetworkConfigurationSelectedListener(OnNetworkConfigurationSelectedListener listener) {
        onNetworkConfigurationSelectedListener = listener;
    }

    @Override
    public void setOnWebsiteTextChangeListener(OnWebsiteTextChangeListener listener) {
        onWebsiteTextChangeListener = listener;
    }

    @Override
    public void setOnPickerClickListener(OnPickerClickListener listener) {
        onPickerClickListener = listener;
    }

    @Override
    public View getRootView() {
        return root;
    }

    @Override
    public void showLoadingIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getNetworkConf() {
        switch (spinner.getSelectedItemPosition()) {
            case 0:
                return NetworkTypes.INTERNET;
            case 1:
                return NetworkTypes.WIFI_P2P;
            case 2:
                return NetworkTypes.NSD;
            case 3:
                return NetworkTypes.BLUETOOTH;
            default:
                throw new UnsupportedOperationException(TAG + ": No such network configuration");
        }
    }
}
