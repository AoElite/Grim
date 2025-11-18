package ac.grim.grimac.utils.common;

public class FormattingUtil {

    public static String formatTime(long ms) {
        final long totalSeconds = ms / 1000;
        final long millis = ms % 1000;
        final long seconds = totalSeconds % 60;
        final long totalMinutes = totalSeconds / 60;
        final long minutes = totalMinutes % 60;
        final long hours = totalMinutes / 60;

        StringBuilder sb = new StringBuilder();

        if (hours > 0) sb.append(hours).append("h");
        if (minutes > 0) sb.append(minutes).append("m");
        if (seconds > 0) sb.append(seconds).append("s");
        if (millis > 0 || sb.isEmpty()) sb.append(millis).append("ms");

        return sb.toString();
    }


}
