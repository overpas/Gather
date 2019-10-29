package by.overpass.gather.ui.create;

public enum MeetingType {

    BUSINESS(1), ENTERTAINMENT(2), PROTEST(3);

    private final int type;

    MeetingType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static boolean isBusiness(int type) {
        return type == BUSINESS.type;
    }

    public static boolean isEntertainment(int type) {
        return type == ENTERTAINMENT.type;
    }

    public static boolean isProtest(int type) {
        return type == PROTEST.type;
    }
}
