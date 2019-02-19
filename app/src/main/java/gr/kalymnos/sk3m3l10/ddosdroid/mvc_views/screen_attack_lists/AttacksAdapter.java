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
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.status.repository.SharedPrefPersistance;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.status.repository.ServerStatusPersistance;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc.OnActivateSwitchCheckedStateListener;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc.OnAttackClickListener;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc.OnJoinSwitchCheckedStateListener;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.FETCH_ONLY_USER_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.FETCH_ONLY_USER_NOT_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.FETCH_ONLY_USER_OWN_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.CollectionUtil.itemFromLinkedHashSet;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.CollectionUtil.hasItems;

class AttacksAdapter extends RecyclerView.Adapter<AttacksAdapter.AttackHolder> {
    private static final String TAG = "AttacksAdapter";

    private Context context;
    private LinkedHashSet<Attack> attacks;
    private OnAttackClickListener itemClickListener;
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
            Attack attack = itemFromLinkedHashSet(attacks, position);
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
            Attack attack = itemFromLinkedHashSet(attacks, position);
            return getItemViewTypeFrom(attack);
        }
        throw new UnsupportedOperationException(TAG + ": attacks is null or has no items");
    }

    private int getItemViewTypeFrom(Attack attack) {
        if (Attacks.includesBot(attack, Bots.local()))
            return FETCH_ONLY_USER_JOINED_ATTACKS;
        if (Attacks.getHostUUIDText(attack).equals(Bots.local().getId())) {
            return FETCH_ONLY_USER_OWN_ATTACKS;
        }
        return FETCH_ONLY_USER_NOT_JOINED_ATTACKS;
    }

    public void setOnItemClickListener(OnAttackClickListener listener) {
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
            initViews(itemView);
        }

        protected void initViews(@NonNull View itemView) {
            itemView.setOnClickListener((view) -> {
                if (itemClickListener != null) {
                    itemClickListener.onAttackClick(getAdapterPosition());
                }
            });
            websiteTitle = itemView.findViewById(R.id.website_textview);
            websiteSubtitle = itemView.findViewById(R.id.number_joined_textview);
            websiteIcon = itemView.findViewById(R.id.website_imageview);
        }

        void bind(Attack attack) {
            websiteTitle.setText(attack.getWebsite());
            websiteSubtitle.setText(createTextFrom(attack.getBotIds().size()));
            //  TODO: if a website has a favicon.ico then display it in websiteIcon
        }

        private String createTextFrom(int usersJoined) {
            return context.getString(R.string.users_joined) + " " + usersJoined;
        }
    }

    private class NotJoinedAttackHolder extends AttackHolder {
        NotJoinedAttackHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class JoinedAttackHolder extends AttackHolder {
        private Switch joinSwitch;

        JoinedAttackHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void initViews(@NonNull View itemView) {
            super.initViews(itemView);
            initSwitch(itemView);
        }

        private void initSwitch(@NonNull View itemView) {
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
            initActivationSwitch(itemView);
        }

        private void initActivationSwitch(@NonNull View itemView) {
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

        @Override
        void bind(Attack attack) {
            super.bind(attack);
            setCheckedState();
        }

        private void setCheckedState() {
            Attack attack = itemFromLinkedHashSet(attacks, getLayoutPosition());
            String serverWebsite = attack.getWebsite();
            ServerStatusPersistance persistance = new SharedPrefPersistance(context);
            boolean serverActive = persistance.isStarted(serverWebsite);
            activateSwitch.setChecked(serverActive);
        }
    }

    private interface AttackHolderBuilder {
        AttackHolder build(int viewType, ViewGroup parent);
    }

    class AttackHolderBuilderImpl implements AttackHolderBuilder {

        @Override
        public AttackHolder build(int viewType, ViewGroup parent) {
            switch (viewType) {
                case FETCH_ONLY_USER_JOINED_ATTACKS:
                    return new JoinedAttackHolder(createViewFrom(R.layout.list_item_attack_joined, parent));
                case FETCH_ONLY_USER_OWN_ATTACKS:
                    return new OwnerAttackHolder(createViewFrom(R.layout.list_item_attack_owner, parent));
                case FETCH_ONLY_USER_NOT_JOINED_ATTACKS:
                    return new NotJoinedAttackHolder(createViewFrom(R.layout.list_item_attack, parent));
                default:
                    throw new UnsupportedOperationException(TAG + ": Unknown ITEM_VIEW_TYPE");
            }
        }

        private View createViewFrom(int layoutRes, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(layoutRes, parent, false);
        }
    }
}
