package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.LinkedHashSet;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.repository.SharedPrefsStatusRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.repository.StatusRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc.OnActivateSwitchCheckedStateListener;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc.OnAttackItemClickListener;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc.OnJoinSwitchCheckedStateListener;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.CollectionUtil.getItemFromLinkedHashSet;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.CollectionUtil.hasItems;

class AttacksAdapter extends RecyclerView.Adapter<AttacksAdapter.AttackHolder> {
    private static final String TAG = "AttacksAdapter";
    private static final int ITEM_VIEW_TYPE_SIMPLE_ATTACK = 0;
    private static final int ITEM_VIEW_TYPE_JOINED_ATTACK = 1;
    private static final int ITEM_VIEW_TYPE_OWNER_ATTACK = 2;

    private Context context;
    private LinkedHashSet<Attack> attacks;
    private OnAttackItemClickListener itemClickListener;
    private OnJoinSwitchCheckedStateListener switchCheckedStateListener;
    private OnActivateSwitchCheckedStateListener activateSwitchCheckedStateListener;

    AttacksAdapter(Context context) {
        this.context = context;
    }

    public void addAttacks(LinkedHashSet<Attack> attackList) {
        this.attacks = attackList;
    }

    @NonNull
    @Override
    public AttackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttackHolderBuilderImpl().build(viewType, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull AttackHolder attackHolder, int position) {
        if (hasItems(attacks)) {
            Attack attack = getItemFromLinkedHashSet(attacks, position);
            attackHolder.bind(attack);
            attackHolder.itemView.setTag(String.valueOf(position));
        }
    }

    @Override
    public int getItemCount() {
        if (hasItems(attacks)) {
            return attacks.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasItems(attacks)) {
            Attack attack = getItemFromLinkedHashSet(attacks, position);
            return getItemViewTypeFrom(attack);
        }
        throw new UnsupportedOperationException(TAG + ": attacks is null or has no items");
    }

    private int getItemViewTypeFrom(Attack attack) {
        if (Attacks.includesBot(attack, Bots.local()))
            return ITEM_VIEW_TYPE_JOINED_ATTACK;
        if (Attacks.getHostUUIDText(attack).equals(Bots.local().getId())) {
            return ITEM_VIEW_TYPE_OWNER_ATTACK;
        }
        return ITEM_VIEW_TYPE_SIMPLE_ATTACK;
    }

    public void setOnItemClickListener(OnAttackItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setOnSwitchCheckedStateListener(OnJoinSwitchCheckedStateListener listener) {
        this.switchCheckedStateListener = listener;
    }

    public void setOnActivateSwitchCheckedStateListener(OnActivateSwitchCheckedStateListener listener) {
        this.activateSwitchCheckedStateListener = listener;
    }

    abstract class AttackHolder extends RecyclerView.ViewHolder {

        private TextView websiteTitle, websiteSubtitle;
        private ImageView websiteIcon;

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
            websiteTitle = itemView.findViewById(R.id.website_textview);
            websiteSubtitle = itemView.findViewById(R.id.number_joined_textview);
            websiteIcon = itemView.findViewById(R.id.website_imageview);
        }

        void bind(Attack attack) {
            websiteTitle.setText(attack.getWebsite());
            websiteSubtitle.setText(createUsersJoinedTextFrom(attack.getBotIds().size()));
            //  TODO: if a website has a favicon.ico then display it in websiteIcon
        }

        private String createUsersJoinedTextFrom(int usersJoined) {
            return context.getString(R.string.users_joined) + " " + usersJoined;
        }
    }

    private class SimpleAttackHolder extends AttackHolder {
        SimpleAttackHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class JoinedAttackHolder extends AttackHolder {

        private Switch joinSwitch;

        JoinedAttackHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void initializeViews(@NonNull View itemView) {
            super.initializeViews(itemView);
            joinSwitch = itemView.findViewById(R.id.join_switch);
            joinSwitch.setOnCheckedChangeListener((view, isChecked) -> {
                if (switchCheckedStateListener != null) {
                    switchCheckedStateListener.onJoinSwitchCheckedState(getAdapterPosition(), isChecked);
                }
            });
        }
    }

    private class OwnerAttackHolder extends AttackHolder {

        private Switch activateSwitch;

        OwnerAttackHolder(@NonNull View itemView) {
            super(itemView);
            initializeActivationSwitch(itemView);
        }

        @Override
        void bind(Attack attack) {
            super.bind(attack);
            setCheckedState();
        }

        private void setCheckedState() {
            Attack attack = getItemFromLinkedHashSet(attacks, getLayoutPosition());
            String serverWebsite = attack.getWebsite();
            StatusRepository repo = new SharedPrefsStatusRepository(context);
            boolean serverActive = repo.isStarted(serverWebsite);
            activateSwitch.setChecked(serverActive);
        }

        private void initializeActivationSwitch(@NonNull View itemView) {
            activateSwitch = itemView.findViewById(R.id.activation_switch);
            setCheckedListener();
        }

        private void setCheckedListener() {
            activateSwitch.setOnCheckedChangeListener((view, isChecked) -> {
                if (activateSwitchCheckedStateListener != null) {
                    activateSwitchCheckedStateListener.onActivateSwitchCheckedState(getAdapterPosition(), isChecked);
                }
            });
        }
    }

    private interface AttackHolderBuilder {
        AttackHolder build(int viewType, ViewGroup parent);
    }

    class AttackHolderBuilderImpl implements AttackHolderBuilder {

        @Override
        public AttackHolder build(int viewType, ViewGroup parent) {
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
    }
}
