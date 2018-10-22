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
    private ImageView createAttackImg, joinAttackImg, followingAttacksImg;
    private TextView createAttackTitle, joinAttackTitle, followingAttacksTitle,
            createAttackSubtitle, joinAttackSubtitle, followingAttacksSubtitle;
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
        createAttackImg = createAttackParentView.findViewById(R.id.image);
        createAttackImg.setImageResource(R.drawable.ic_www_icon);
        createAttackTitle = createAttackParentView.findViewById(R.id.title);
        createAttackTitle.setText(R.string.create_attack_label);
        createAttackSubtitle = createAttackParentView.findViewById(R.id.subtitle);
        createAttackSubtitle.setText(R.string.create_attack_label_subtitle);
    }

    private void initializeJoinAttackViews() {
        joinAttackParentView = root.findViewById(R.id.join_attack_menu_item);
        joinAttackParentView.setOnClickListener((view) -> {
            if (optionClickListener != null) {
                optionClickListener.onJoinAttackClick();
            }
        });
        joinAttackImg = joinAttackParentView.findViewById(R.id.image);
        joinAttackImg.setImageResource(R.drawable.ic_ufo_invasion);
        joinAttackTitle = joinAttackParentView.findViewById(R.id.title);
        joinAttackTitle.setText(R.string.join_attack_label);
        joinAttackSubtitle = joinAttackParentView.findViewById(R.id.subtitle);
        joinAttackSubtitle.setText(R.string.join_attack_label_subtitle);
    }

    private void initializeFollowingAttacksViews() {
        followingAttacksParentView = root.findViewById(R.id.follow_attack_menu_item);
        followingAttacksParentView.setOnClickListener((view) -> {
            if (optionClickListener != null) {
                optionClickListener.onFollowingAttacksClick();
            }
        });
        followingAttacksImg = followingAttacksParentView.findViewById(R.id.image);
        followingAttacksImg.setImageResource(R.drawable.ic_fist);
        followingAttacksTitle = followingAttacksParentView.findViewById(R.id.title);
        followingAttacksTitle.setText(R.string.following_attacks_label);
        followingAttacksSubtitle = followingAttacksParentView.findViewById(R.id.subtitle);
        followingAttacksSubtitle.setText(R.string.following_attacks_label_subtitle);
    }
}
