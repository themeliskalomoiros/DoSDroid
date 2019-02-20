package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedHashSet;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.FETCH_ONLY_USER_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.FETCH_ONLY_USER_NOT_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.FETCH_ONLY_USER_OWN_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc.OnAttackClickListener;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc.OnJoinedAttackDeleteClickListener;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc.OnOwnerAttackDeleteClickListener;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.CollectionUtil.hasItems;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.CollectionUtil.itemFromLinkedHashSet;

class AttacksAdapter extends RecyclerView.Adapter<AttacksAdapter.AttackHolder> {
    private static final String TAG = "AttacksAdapter";

    private Context context;
    private LinkedHashSet<Attack> attacks;
    private OnAttackClickListener itemClickListener;
    private OnJoinedAttackDeleteClickListener joinedDeleteClickListener;
    private OnOwnerAttackDeleteClickListener ownerDeleteClickListener;

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
        if (isOwner(attack)) {
            return FETCH_ONLY_USER_OWN_ATTACKS;
        }
        return FETCH_ONLY_USER_NOT_JOINED_ATTACKS;
    }

    private boolean isOwner(Attack attack) {
        return Attacks.getHostUUIDText(attack).equals(Bots.local().getId());
    }

    public void setOnItemClickListener(OnAttackClickListener listener) {
        itemClickListener = listener;
    }

    public void setOnJoinedAttackDeleteClickListener(OnJoinedAttackDeleteClickListener listener) {
        this.joinedDeleteClickListener = listener;
    }

    public void setOnOwnerAttackDeleteClickListener(OnOwnerAttackDeleteClickListener listener) {
        this.ownerDeleteClickListener = listener;
    }

    abstract class AttackHolder extends RecyclerView.ViewHolder {
        private TextView websiteTitle, websiteSubtitle;

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
        }

        void bind(Attack attack) {
            websiteTitle.setText(attack.getWebsite());
            websiteSubtitle.setText(createTextFrom(attack.getBotIds().size()));
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
        private ImageView deleteAttack;

        JoinedAttackHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void initViews(@NonNull View itemView) {
            super.initViews(itemView);
            initSwitch(itemView);
        }

        private void initSwitch(@NonNull View itemView) {
            deleteAttack = itemView.findViewById(R.id.delete_attack);
            deleteAttack.setOnClickListener(view -> {
                if (joinedDeleteClickListener != null)
                    joinedDeleteClickListener.onJoinedAttackDeleteClick(getAdapterPosition());
            });
        }
    }

    private class OwnerAttackHolder extends AttackHolder {
        private ImageView deleteAttack;

        OwnerAttackHolder(@NonNull View itemView) {
            super(itemView);
            initActivationSwitch(itemView);
        }

        private void initActivationSwitch(@NonNull View itemView) {
            deleteAttack = itemView.findViewById(R.id.delete_attack);
            deleteAttack.setOnClickListener(view -> {
                if (ownerDeleteClickListener != null)
                    ownerDeleteClickListener.onOwnerAttackDeleteClick(getAdapterPosition());
            });
        }

        @Override
        void bind(Attack attack) {
            super.bind(attack);
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
