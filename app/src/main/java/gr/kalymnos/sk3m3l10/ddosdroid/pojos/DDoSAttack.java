package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

import java.util.List;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

public class DDoSAttack {

    private static final String TAG = DDoSAttack.class.getSimpleName();

    private String pushId, targetWebsite;
    private List<DDoSBot> botsList;
    private DDoSBot owner;
    private boolean isActive;
    private long timeMilli;

    public DDoSAttack(String targetWebsite, DDoSBot owner) {
        this.targetWebsite = targetWebsite;
        this.owner = owner;
        this.timeMilli = System.currentTimeMillis();
    }

    public DDoSAttack(String targetWebsite, List<DDoSBot> botsList, DDoSBot owner, boolean isActive, long timeMilli) {
        this.targetWebsite = targetWebsite;
        this.botsList = botsList;
        this.owner = owner;
        this.isActive = isActive;
        this.timeMilli = timeMilli;
    }

    public DDoSAttack(String pushId, String targetWebsite, List<DDoSBot> botsList, DDoSBot owner, boolean isActive, long timeMilli) {
        this(targetWebsite, botsList, owner, isActive, timeMilli);
        this.pushId = pushId;
    }

    public int getBotNetCount() {
        if (listHasItems(botsList)) {
            return botsList.size();
        }
        return 0;
    }

    public void addBot(DDoSBot bot) {
        if (listHasItems(botsList)) {
            botsList.add(bot);
        }
        throw new UnsupportedOperationException(TAG + "bot list is null or empty");
    }

    public void removeBot(DDoSBot bot) {
        if (listHasItems(botsList)) {
            botsList.remove(bot);
        }
        throw new UnsupportedOperationException(TAG + "bot list is null or empty");
    }

    public void start() {
        // TODO: needs implementation
    }

    public void stop() {
        // TODO: needs implementation
    }

    public String getPushId() {
        return pushId;
    }

    public String getTargetWebsite() {
        return targetWebsite;
    }

    public DDoSBot getOwner() {
        return owner;
    }

    public long getTimeMilli() {
        return timeMilli;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
