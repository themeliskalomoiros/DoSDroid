package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_list_screen;

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

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

class AttacksAdapter extends RecyclerView.Adapter<AttacksAdapter.AttackHolder> {

    private Context context;
    private List<DDoSAttack> attackList;

    public AttacksAdapter(Context context) {
        this.context = context;
    }

    public void addAttacks(List<DDoSAttack> attackList) {
        this.attackList = attackList;
    }

    @NonNull
    @Override
    public AttackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_attack_owner, parent, false);
        return new AttackHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttackHolder attackHolder, int position) {
        if (listHasItems(attackList)) {
            DDoSAttack attack = attackList.get(position);
            String website = attack.getTargetWebsite();
            String followingText = context.getString(R.string.people_who_follow_attack_label) + " " + attack.getBotNetCount();
            attackHolder.bindViews(website, followingText, attack.isActive());
        }
    }

    @Override
    public int getItemCount() {
        if (listHasItems(attackList)) {
            return attackList.size();
        }
        return 0;
    }

    class AttackHolder extends RecyclerView.ViewHolder {

        private Switch activationSwitch;
        private TextView tvWebsite, tvFollowers;

        public AttackHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews(itemView);
        }

        private void initializeViews(@NonNull View itemView) {
            activationSwitch = itemView.findViewById(R.id.attack_activation_switch);
            tvWebsite = itemView.findViewById(R.id.tv_website);
            tvFollowers = itemView.findViewById(R.id.tv_followers);
        }

        void bindViews(String website, String followersText, boolean attackIsActive) {
            tvWebsite.setText(website);
            tvFollowers.setText(followersText);
            activationSwitch.setChecked(attackIsActive);
        }
    }
}
