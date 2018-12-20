package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AllAttackListsViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AllAttackListsViewMvcImpl;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_NOT_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_OWNER;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_TYPE;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_NONE;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.bundleIsValidAndContainsKey;

public class AllAttackListsActivity extends AppCompatActivity {
    private static final String TAG = "AllAttackListsActivity";
    public static final String EXTRA_TITLE = TAG + "title key";

    private AllAttackListsViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
    }

    private void setupUi() {
        initializeViewMvc();
        viewMvc.bindToolbarTitle(getIntent().getStringExtra(EXTRA_TITLE));
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc() {
        viewMvc = new AllAttackListsViewMvcImpl(LayoutInflater.from(this), null,
                getSupportFragmentManager(), getAttacksType(getIntent().getExtras()));
    }

    private int getAttacksType(Bundle bundle) {
        if (bundleIsValidAndContainsKey(bundle, EXTRA_TYPE)) {
            return bundle.getInt(EXTRA_TYPE);
        }
        return TYPE_NONE;
    }

    public static class Action{

        public static void startForJoinedAttacks(Context context){
            context.startActivity(createIntent(context,TYPE_FETCH_JOINED,R.string.contributions_label));
        }

        public static void startForNonJoinedAttacks(Context context){
            context.startActivity(createIntent(context,TYPE_FETCH_NOT_JOINED,R.string.join_attack_label));
        }

        public static void startForUserAttacks(Context context){
            context.startActivity(createIntent(context,TYPE_FETCH_OWNER,R.string.your_attacks_label));
        }

        @NonNull
        private static Intent createIntent(Context context, int attackType, int titleRes) {
            Intent intent = new Intent(context, AllAttackListsActivity.class);
            intent.putExtras(createBundle(attackType, context.getString(titleRes)));
            return intent;
        }

        @NonNull
        private static Bundle createBundle(int attackType, String title) {
            Bundle extras = new Bundle();
            extras.putInt(EXTRA_TYPE, attackType);
            extras.putString(AllAttackListsActivity.EXTRA_TITLE, title);
            return extras;
        }
    }
}
