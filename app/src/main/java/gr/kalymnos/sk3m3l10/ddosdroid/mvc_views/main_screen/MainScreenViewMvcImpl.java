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
    public void bindCreateAttackTitle(int titleRes) {
        createAttackTextView.setText(titleRes);
    }

    @Override
    public void bindJoinAttackTitle(int titleRes) {
        joinAttackTextView.setText(titleRes);
    }

    @Override
    public void bindFollowingAttacksTitle(int titleRes) {
        followingAttacksTextView.setText(titleRes);
    }

    @Override
    public void bindCreateAttackImage(int imgRes) {
        createAttackImageView.setBackgroundResource(imgRes);
    }

    @Override
    public void bindJoinAttackImage(int imgRes) {
        joinAttackImageView.setBackgroundResource(imgRes);
    }

    @Override
    public void bindFollowingAttacksImage(int imgRes) {
        followingAttacksImageView.setBackgroundResource(imgRes);
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
        createAttackTextView = createAttackParentView.findViewById(R.id.title);
    }

    private void initializeJoinAttackViews() {
        joinAttackParentView = root.findViewById(R.id.join_attack_menu_item);
        joinAttackParentView.setOnClickListener((view) -> {
            if (optionClickListener != null) {
                optionClickListener.onJoinAttackClick();
            }
        });
        joinAttackImageView = joinAttackParentView.findViewById(R.id.image);
        joinAttackTextView = joinAttackParentView.findViewById(R.id.title);
    }

    private void initializeFollowingAttacksViews() {
        followingAttacksParentView = root.findViewById(R.id.follow_attack_menu_item);
        followingAttacksParentView.setOnClickListener((view) -> {
            if (optionClickListener != null) {
                optionClickListener.onFollowingAttacksClick();
            }
        });
        followingAttacksImageView = followingAttacksParentView.findViewById(R.id.image);
        followingAttacksTextView = followingAttacksParentView.findViewById(R.id.title);
    }
}
