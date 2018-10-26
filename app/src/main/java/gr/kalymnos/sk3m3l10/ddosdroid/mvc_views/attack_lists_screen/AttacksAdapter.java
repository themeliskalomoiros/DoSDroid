package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

class AttacksAdapter extends RecyclerView.Adapter<AttacksAdapter.AttackHolder> {

    private static final int ITEM_VIEW_TYPE_ATTACK = 0;
    private static final int ITEM_VIEW_TYPE_ATTACK_FOLLOWER = 1;
    private static final int ITEM_VIEW_TYPE_ATTACK_OWNER = 2;
    private static final String TAG = AttacksAdapter.class.getSimpleName();

    private Context context;
    private List<DDoSAttack> attackList;

    private AllAttackListsViewMvc.OnAttackItemClickListener itemClickListener;

    public AttacksAdapter(Context context) {
        this.context = context;
    }

    public void addAttacks(List<DDoSAttack> attackList) {
        this.attackList = attackList;
    }

    @NonNull
    @Override
    public AttackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == ITEM_VIEW_TYPE_ATTACK_FOLLOWER) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item_attack_follower, parent, false);
            return new AttackHolderFollower(itemView);
        } else if (viewType == ITEM_VIEW_TYPE_ATTACK_OWNER) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item_attack_owner, parent, false);
            return new AttackOwnerHolder(itemView);
        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item_attack, parent, false);
            return new AttackHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AttackHolder attackHolder, int position) {
        if (listHasItems(attackList)) {
            DDoSAttack attack = attackList.get(position);
            String website = attack.getTargetWebsite();
            String followingText = context.getString(R.string.people_who_follow_attack_label) + " " + attack.getBotNetCount();

            if (attackHolder instanceof AttackOwnerHolder) {
                AttackOwnerHolder attackOwnerHolder = (AttackOwnerHolder) attackHolder;
                attackOwnerHolder.bindViews(website, followingText, attack.isActive());
            } else {
                attackHolder.bindViews(website, followingText);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (listHasItems(attackList)) {
            return attackList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (ValidationUtils.listHasItems(attackList)) {
            //  TODO: after FakeAttackRepo replace with real user id instead of "bot3"
//            String userId = DDoSBot.getLocalUserDDoSBot().getId();
            DDoSAttack attack = attackList.get(position);

            if (attack.botBelongsToBotnet("bot3"))
                return ITEM_VIEW_TYPE_ATTACK_FOLLOWER;

            if (attack.getOwner().getId().equals("bot3")) {
                return ITEM_VIEW_TYPE_ATTACK_OWNER;
            }

            return ITEM_VIEW_TYPE_ATTACK;
        }
        throw new UnsupportedOperationException(TAG + ": attackList is null or has no items");
    }

    public void setOnItemClickListener(AllAttackListsViewMvc.OnAttackItemClickListener listener) {
        itemClickListener = listener;
    }

    class AttackHolder extends RecyclerView.ViewHolder {

        private TextView tvWebsite, tvFollowers;

        public AttackHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews(itemView);
        }

        private void initializeViews(@NonNull View itemView) {
            itemView.setOnClickListener((view) -> {
                if (itemClickListener != null) {
                    itemClickListener.onAttackItemClick(getAdapterPosition());
                }
            });
            tvWebsite = itemView.findViewById(R.id.tv_website);
            tvFollowers = itemView.findViewById(R.id.tv_followers);
        }

        void bindViews(String website, String followersText) {
            tvWebsite.setText(website);
            tvFollowers.setText(followersText);
        }
    }

    class AttackHolderFollower extends AttackHolder {
        public AttackHolderFollower(@NonNull View itemView) {
            super(itemView);
        }
    }

    class AttackOwnerHolder extends AttackHolder {

        private Switch activationSwitch;

        public AttackOwnerHolder(@NonNull View itemView) {
            super(itemView);
            activationSwitch = itemView.findViewById(R.id.attack_activation_switch);
        }

        void bindViews(String website, String followersText, boolean attackIsActive) {
            super.bindViews(website, followersText);
            activationSwitch.setChecked(attackIsActive);
        }
    }
}
