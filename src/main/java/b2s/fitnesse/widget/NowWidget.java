package b2s.fitnesse.widget;

import fitnesse.html.HtmlUtil;
import fitnesse.wikitext.WikiWidget;
import fitnesse.wikitext.widgets.ParentWidget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NowWidget extends WikiWidget {
    public static final String REGEXP = "^!now .+?$";

    private static final Pattern PATTERN = Pattern.compile("pattern\\((.+?)\\)");
    private static final Pattern MONTH = Pattern.compile("month\\((.+?)\\)");
    private static final Pattern DAY = Pattern.compile("day\\((.+?)\\)");
    private static final Pattern YEAR = Pattern.compile("year\\((.+?)\\)");

    private final String input;
    private Calendar calender = Calendar.getInstance();

    public NowWidget(ParentWidget parent, String input) {
        super(parent);
        this.input = input;
    }

    @Override
    public String render() throws Exception {
        String datePattern = string(PATTERN, input);
        if (datePattern == null) {
            StringBuilder error = new StringBuilder();
            error.append(input).append(HtmlUtil.BRtag).append("Bad syntax!").append(HtmlUtil.BRtag);
            error.append("Options:").append(HtmlUtil.BRtag).append("<ul>");
            error.append("</ul>");
            return HtmlUtil.metaText(error.toString());
        }
        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        calender.add(Calendar.MONTH, toInt(MONTH, input));
        calender.add(Calendar.DAY_OF_YEAR, toInt(DAY, input));
        calender.add(Calendar.YEAR, toInt(YEAR, input));
        return formatter.format(calender.getTime());
    }

    private int toInt(Pattern pattern, String input) {
        String value = string(pattern, input);
        if (value == null) return 0;
        return Integer.parseInt(value.replace("+", ""));
    }

    private String string(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.find())
            return matcher.group(1);
        return null;
    }

    protected void setCalender(Calendar calender) {
        this.calender = calender;
    }
}
