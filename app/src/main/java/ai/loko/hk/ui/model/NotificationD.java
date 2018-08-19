package ai.loko.hk.ui.model;

public class NotificationD {
    private String title;
    private String message;
    private String iconUrl;
    private String action;
    private String actionDestination;

    public String getTitle() {
        return title;
    }

    public NotificationD setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public NotificationD setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public NotificationD setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        return this;
    }

    public String getAction() {
        return action;
    }

    public NotificationD setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionDestination() {
        return actionDestination;
    }

    public NotificationD setActionDestination(String actionDestination) {
        this.actionDestination = actionDestination;
        return this;
    }
}
