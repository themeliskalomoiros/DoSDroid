package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_main;

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
    private ImageView createAttackImg, joinAttackImg, contributionImg;
    private TextView createAttackTitle, joinAttackTitle, contributionTitle,
            createAttackSubtitle, joinAttackSubtitle, contributionSubtitle;
    private ViewGroup createAttackParentView, joinAttackParentView, contributionParentView;

    private OnMainActionClickListener optionClickListener;

    public MainScreenViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_main, container, false);
        initializeViews();
    }

    @Override
    public void setOnMainActionClickListener(OnMainActionClickListener listener) {
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
        initializeContributionViews();
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
        createAttackTitle.setText(R.string.attack_a_website_label);
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

    private void initializeContributionViews() {
        contributionParentView = root.findViewById(R.id.contribution_menu_item);
        contributionParentView.setOnClickListener((view) -> {
            if (optionClickListener != null) {
                optionClickListener.onContributionClick();
            }
        });
        contributionImg = contributionParentView.findViewById(R.id.image);
        contributionImg.setImageResource(R.drawable.ic_fist);
        contributionTitle = contributionParentView.findViewById(R.id.title);
        contributionTitle.setText(R.string.contributions_label);
        contributionSubtitle = contributionParentView.findViewById(R.id.subtitle);
        contributionSubtitle.setText(R.string.joined_attacks_label_subtitle);
    }
}
