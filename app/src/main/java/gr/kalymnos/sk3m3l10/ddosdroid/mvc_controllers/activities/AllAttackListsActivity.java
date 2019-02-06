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

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ContentType.FETCH_ONLY_USER_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ContentType.FETCH_ONLY_USER_NOT_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ContentType.INVALID_CONTENT_TYPE;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_CONTENT_TYPE;
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
                getSupportFragmentManager(), getContentType(getIntent().getExtras()));
    }

    private int getContentType(Bundle bundle) {
        if (bundleIsValidAndContainsKey(bundle, EXTRA_CONTENT_TYPE)) {
            return bundle.getInt(EXTRA_CONTENT_TYPE);
        }
        return INVALID_CONTENT_TYPE;
    }

    public static class Action {

        public static void startForJoinedAttacks(Context context) {
            context.startActivity(createIntent(context, FETCH_ONLY_USER_JOINED_ATTACKS, R.string.contributions_label));
        }

        public static void startForNonJoinedAttacks(Context context) {
            context.startActivity(createIntent(context, FETCH_ONLY_USER_NOT_JOINED_ATTACKS, R.string.join_attack_label));
        }

        @NonNull
        public static Intent createIntent(Context context, int contentType, int titleRes) {
            Intent intent = new Intent(context, AllAttackListsActivity.class);
            intent.putExtras(createBundle(contentType, context.getString(titleRes)));
            return intent;
        }

        @NonNull
        private static Bundle createBundle(int contentType, String title) {
            Bundle extras = new Bundle();
            extras.putInt(EXTRA_CONTENT_TYPE, contentType);
            extras.putString(AllAttackListsActivity.EXTRA_TITLE, title);
            return extras;
        }
    }
}
