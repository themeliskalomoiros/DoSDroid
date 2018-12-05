package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackInfoViewMvc;

public class JoinAttackInfoFragment extends Fragment implements JoinAttackInfoViewMvc.OnJoinAttackClickListener {

    public static JoinAttackInfoFragment getInstance(Bundle args) {
        JoinAttackInfoFragment instance = new JoinAttackInfoFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onJoinAttackClicked() {

    }
}
