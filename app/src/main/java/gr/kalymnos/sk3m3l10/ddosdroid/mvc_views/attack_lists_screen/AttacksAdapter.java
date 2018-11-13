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

    private static final String TAG = AttacksAdapter.class.getSimpleName();
    private static final int ITEM_VIEW_TYPE_ATTACK = 0;
    private static final int ITEM_VIEW_TYPE_ATTACK_JOINED = 1;
    private static final int ITEM_VIEW_TYPE_ATTACK_OWNER = 2;

    private Context context;
    private List<DDoSAttack> attackList;

    private AttackListViewMvc.OnAttackItemClickListener itemClickListener;

    AttacksAdapter(Context context) {
        this.context = context;
    }

    public void addAttacks(List<DDoSAttack> attackList) {
        this.attackList = attackList;
    }

    @NonNull
    @Override
    public AttackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == ITEM_VIEW_TYPE_ATTACK_JOINED) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item_attack_joined, parent, false);
            return new JoinedAttackHolder(itemView);
        } else if (viewType == ITEM_VIEW_TYPE_ATTACK_OWNER) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item_attack_owner, parent, false);
            return new OwnerAttackHolder(itemView);
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
            String peopleJoinedText = context.getString(R.string.people_who_joined_attack_label) + " " + attack.getBotNetCount();

            if (attackHolder instanceof OwnerAttackHolder) {
                OwnerAttackHolder ownerAttackHolder = (OwnerAttackHolder) attackHolder;
                ownerAttackHolder.bindViews(website, peopleJoinedText, attack.isActive());
            } else {
                attackHolder.bindViews(website, peopleJoinedText);
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
                return ITEM_VIEW_TYPE_ATTACK_JOINED;

            if (attack.getOwner().getId().equals("bot3")) {
                return ITEM_VIEW_TYPE_ATTACK_OWNER;
            }

            return ITEM_VIEW_TYPE_ATTACK;
        }
        throw new UnsupportedOperationException(TAG + ": attackList is null or has no items");
    }

    public void setOnItemClickListener(AttackListViewMvc.OnAttackItemClickListener listener) {
        itemClickListener = listener;
    }

    class AttackHolder extends RecyclerView.ViewHolder {

        private TextView tvWebsite, tvPeopleJoined;

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
            tvPeopleJoined = itemView.findViewById(R.id.tv_people_who_joined);
        }

        void bindViews(String website, String peopleJoinedText) {
            tvWebsite.setText(website);
            tvPeopleJoined.setText(peopleJoinedText);
        }
    }

    class JoinedAttackHolder extends AttackHolder {
        public JoinedAttackHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class OwnerAttackHolder extends AttackHolder {

        private Switch activationSwitch;

        public OwnerAttackHolder(@NonNull View itemView) {
            super(itemView);
            activationSwitch = itemView.findViewById(R.id.attack_activation_switch);
        }

        void bindViews(String website, String peopleJoinedText, boolean attackIsActive) {
            super.bindViews(website, peopleJoinedText);
            activationSwitch.setChecked(attackIsActive);
        }
    }
}
