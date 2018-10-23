package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens;

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
import android.widget.Spinner;
import android.widget.TextView;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class AttackCreationViewMvcImpl implements AttackCreationViewMvc {

    private View root;
    private FloatingActionButton fab;
    private EditText websiteEditText;
    private TextView websiteHint, networkConfigHint;
    private Spinner spinner;

    private OnAttackCreationButtonClickListener onAttackCreationButtonClickListener;
    private OnSpinnerItemSelectedListener onSpinnerItemSelectedListener;
    private OnWebsiteTextChangeListener onWebsiteTextChangeListener;

    public AttackCreationViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initializeViews(inflater, container);
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
    public void setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener listener) {
        onSpinnerItemSelectedListener = listener;
    }

    @Override
    public void setOnWebsiteTextChangeListener(OnWebsiteTextChangeListener listener) {
        onWebsiteTextChangeListener = listener;
    }

    @Override
    public View getRootView() {
        return root;
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_part_attack_creation, container, false);
        initializeFab();
        initializeEditText();
        websiteHint = root.findViewById(R.id.tv_website_hint);
        networkConfigHint = root.findViewById(R.id.tv_network_config_hint);
        initializeSpinner(inflater);
    }

    private void initializeEditText() {
        websiteEditText = root.findViewById(R.id.ed_website);
        websiteEditText.addTextChangedListener(new TextWatcher() {
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
        });
    }

    private void initializeSpinner(LayoutInflater inflater) {
        spinner = root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(),
                R.array.network_technologies, R.layout.item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (onSpinnerItemSelectedListener != null) {
                    onSpinnerItemSelectedListener.onSpinnerItemSelected(getNetworkConfigHInt()
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
            private String getNetworkConfigHInt() {
                return inflater.getContext().getString(R.string.network_configuration_set_label);
            }
        });
    }

    private void initializeFab() {
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener((view -> {
            if (onAttackCreationButtonClickListener != null) {
                onAttackCreationButtonClickListener.onAttackCreationButtonClicked(websiteEditText.getText().toString());
            }
        }));
    }
}
