package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.main_screen;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class MainScreenViewMvcImpl implements MainScreenViewMvc {

    private View root;
    private Toolbar toolbar;
    private ImageView createAttackImageView, joinAttackImageView, followingAttacksImageView;
    private TextView createAttackTextView, joinAttackTextView, followingAttacksTextView;
    private ViewGroup createAttackParentView, joinAttackParentView, followingAttacksParentView;

    private OnOptionClickListener optionClickListener;

    public MainScreenViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_main, container, false);
        initializeViews();
    }

    @Override
    public void setOnOptionClickListener(OnOptionClickListener listener) {
        optionClickListener = listener;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public View getRootView() {
        return root;
    }

    private void initializeViews() {
        toolbar = root.findViewById(R.id.toolBar);
        initializeCreateAttackViews();
        initializeJoinAttackViews();
        initializeFollowingAttacksViews();
    }

    private void initializeCreateAttackViews() {
        createAttackParentView = root.findViewById(R.id.create_attack_menu_item);
        createAttackParentView.setOnClickListener((view) -> {
            if (optionClickListener != null) {
                optionClickListener.onCreateAttackClick();
            }
        });
        createAttackImageView = createAttackParentView.findViewById(R.id.image);
        createAttackImageView.setImageResource(R.drawable.ic_swords);
        createAttackTextView = createAttackParentView.findViewById(R.id.title);
        createAttackTextView.setText(R.string.create_attack_label);
    }

    private void initializeJoinAttackViews() {
        joinAttackParentView = root.findViewById(R.id.join_attack_menu_item);
        joinAttackParentView.setOnClickListener((view) -> {
            if (optionClickListener != null) {
                optionClickListener.onJoinAttackClick();
            }
        });
        joinAttackImageView = joinAttackParentView.findViewById(R.id.image);
        joinAttackImageView.setImageResource(R.drawable.ic_red_army_flag_over_reichstag);
        joinAttackTextView = joinAttackParentView.findViewById(R.id.title);
        joinAttackTextView.setText(R.string.join_attack_label);
    }

    private void initializeFollowingAttacksViews() {
        followingAttacksParentView = root.findViewById(R.id.follow_attack_menu_item);
        followingAttacksParentView.setOnClickListener((view) -> {
            if (optionClickListener != null) {
                optionClickListener.onFollowingAttacksClick();
            }
        });
        followingAttacksImageView = followingAttacksParentView.findViewById(R.id.image);
        followingAttacksImageView.setImageResource(R.drawable.ic_soldier_sailor);
        followingAttacksTextView = followingAttacksParentView.findViewById(R.id.title);
        followingAttacksTextView.setText(R.string.following_attacks_label);
    }
}
