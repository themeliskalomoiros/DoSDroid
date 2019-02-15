package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants;

import static android.support.constraint.Constraints.TAG;

public class AttackCreationViewMvcImpl implements AttackCreationViewMvc {
    private View root;
    private EditText websiteEditText;
    private TextView websiteHint, networkConfigHint;
    private Spinner spinner;
    private FloatingActionButton fab;
    private ProgressBar progressBar;

    private OnAttackCreationButtonClickListener onAttackCreationButtonClickListener;
    private OnNetworkConfigurationSelectedListener onNetworkConfigurationSelectedListener;
    private OnWebsiteTextChangeListener onWebsiteTextChangeListener;

    public AttackCreationViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initializeViews(inflater, container);
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_attack_creation, container, false);
        websiteHint = root.findViewById(R.id.tv_website_hint);
        networkConfigHint = root.findViewById(R.id.tv_network_config_hint);
        progressBar = root.findViewById(R.id.progressBar);
        initializeEditText();
        initializeSpinner(inflater);
        initializeFab();
    }

    private void initializeEditText() {
        websiteEditText = root.findViewById(R.id.ed_website);
        websiteEditText.addTextChangedListener(createTextChangedListenerForEditText());
    }

    private void initializeSpinner(LayoutInflater inflater) {
        spinner = root.findViewById(R.id.spinner);
        spinner.setAdapter(getArrayAdapter(inflater));
        spinner.setOnItemSelectedListener(getSpinnerItemSelectedListener(inflater));
    }

    private ArrayAdapter<CharSequence> getArrayAdapter(LayoutInflater inflater) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(),
                R.array.network_technologies_titles, R.layout.item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private AdapterView.OnItemSelectedListener getSpinnerItemSelectedListener(LayoutInflater inflater) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (onNetworkConfigurationSelectedListener != null) {
                    onNetworkConfigurationSelectedListener.onNetworkSelected(getNetworkConfigHint()
                            + " " + getNetworkConfigDescription(pos));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(AttackCreationViewMvc.class.getSimpleName(), "Nothing selected");
            }

            private String getNetworkConfigDescription(int pos) {
                return inflater.getContext().getResources().getStringArray(R.array.network_technologies_description)[pos];
            }

            @NonNull
            private String getNetworkConfigHint() {
                return inflater.getContext().getString(R.string.network_configuration_set_label);
            }
        };
    }

    private void initializeFab() {
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener((view -> {
            if (onAttackCreationButtonClickListener != null) {
                onAttackCreationButtonClickListener.onAttackCreationButtonClicked(websiteEditText.getText().toString());
            }
        }));
    }

    @Override
    public void setWebsiteHint(String hint) {
        websiteHint.setText(hint);
    }

    @Override
    public void setNetworkConfigHint(String hint) {
        networkConfigHint.setText(hint);
    }

    @Override
    public void setOnAttackCreationButtonClickListener(OnAttackCreationButtonClickListener listener) {
        onAttackCreationButtonClickListener = listener;
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
                return Constants.NetworkType.INTERNET;
            case 1:
                return Constants.NetworkType.WIFI_P2P;
            case 2:
                return Constants.NetworkType.NSD;
            case 3:
                return Constants.NetworkType.BLUETOOTH;
            default:
                throw new UnsupportedOperationException(TAG + ": No such network configuration");
        }
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
}
