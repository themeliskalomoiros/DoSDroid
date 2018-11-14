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
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen.AttackListViewMvc.OnSwitchCheckedStateListener;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen.AttackListViewMvc.OnAttackItemClickListener;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

class AttacksAdapter extends RecyclerView.Adapter<AttacksAdapter.AttackHolder> {

    private static final String TAG = AttacksAdapter.class.getSimpleName();
    private static final int ITEM_VIEW_TYPE_SIMPLE_ATTACK = 0;
    private static final int ITEM_VIEW_TYPE_JOINED_ATTACK = 1;
    private static final int ITEM_VIEW_TYPE_OWNER_ATTACK = 2;

    private Context context;
    private List<DDoSAttack> attackList;
    private OnAttackItemClickListener itemClickListener;
    private OnSwitchCheckedStateListener switchCheckedStateListener;

    AttacksAdapter(Context context) {
        this.context = context;
    }

    public void addAttacks(List<DDoSAttack> attackList) {
        this.attackList = attackList;
    }

    @NonNull
    @Override
    public AttackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_VIEW_TYPE_JOINED_ATTACK:
                return new JoinedAttackHolder(createViewFrom(R.layout.list_item_attack_joined, parent));
            case ITEM_VIEW_TYPE_OWNER_ATTACK:
                return new OwnerAttackHolder(createViewFrom(R.layout.list_item_attack_owner, parent));
            case ITEM_VIEW_TYPE_SIMPLE_ATTACK:
                return new SimpleAttackHolder(createViewFrom(R.layout.list_item_attack, parent));
            default:
                throw new UnsupportedOperationException(TAG + ": Unknown ITEM_VIEW_TYPE");
        }
    }

    private View createViewFrom(int layoutRes, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull AttackHolder attackHolder, int position) {
        if (listHasItems(attackList)) {
            DDoSAttack attack = attackList.get(position);
            String website = attack.getTargetWebsite();
            String usersJoinedText = context.getString(R.string.users_joined) + " " + attack.getBotNetCount();

            if (attackHolder instanceof OwnerAttackHolder) {
                OwnerAttackHolder ownerAttackHolder = (OwnerAttackHolder) attackHolder;
                ownerAttackHolder.bindViews(website, usersJoinedText, attack.isActive());
            } else {
                attackHolder.bindViews(website, usersJoinedText);
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
                return ITEM_VIEW_TYPE_JOINED_ATTACK;

            if (attack.getOwner().getId().equals("bot3")) {
                return ITEM_VIEW_TYPE_OWNER_ATTACK;
            }

            return ITEM_VIEW_TYPE_SIMPLE_ATTACK;
        }
        throw new UnsupportedOperationException(TAG + ": attackList is null or has no items");
    }

    public void setOnItemClickListener(OnAttackItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setOnSwitchCheckedStateListener(OnSwitchCheckedStateListener switchCheckedStateListener) {
        this.switchCheckedStateListener = switchCheckedStateListener;
    }

    abstract class AttackHolder extends RecyclerView.ViewHolder {

        private TextView websiteTextView, numberJoinedTextView;

        AttackHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews(itemView);
        }

        protected void initializeViews(@NonNull View itemView) {
            itemView.setOnClickListener((view) -> {
                if (itemClickListener != null) {
                    itemClickListener.onAttackItemClick(getAdapterPosition());
                }
            });
            websiteTextView = itemView.findViewById(R.id.website_textview);
            numberJoinedTextView = itemView.findViewById(R.id.number_joined_textview);
        }

        void bindViews(String website, String joinedText) {
            websiteTextView.setText(website);
            numberJoinedTextView.setText(joinedText);
        }
    }

    class SimpleAttackHolder extends AttackHolder {
        SimpleAttackHolder(@NonNull View itemView) {
            super(itemView);
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

        void bindViews(String website, String usersJoinedText, boolean attackIsActive) {
            super.bindViews(website, usersJoinedText);
            activationSwitch.setChecked(attackIsActive);
        }
    }
}
