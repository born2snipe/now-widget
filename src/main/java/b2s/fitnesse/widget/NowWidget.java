package b2s.fitnesse.widget;

import fitnesse.html.HtmlUtil;
import fitnesse.wikitext.WikiWidget;
import fitnesse.wikitext.widgets.ParentWidget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NowWidget extends WikiWidget {
    public static final String REGEXP = "^!now.*?$";

    private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
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

    public String render() throws Exception {
        String datePattern = DEFAULT_DATE_FORMAT;

        if (hasPattern(input)) {
            datePattern = string(PATTERN, input);
            if (datePattern == null) {
                return badSyntax();
            }
        }

        int months = toInt(MONTH, input);
        if (months == 0 && hasMonth(input)) {
            return badSyntax();
        }
        int days = toInt(DAY, input);
        if (days == 0 && hasDay(input)) {
            return badSyntax();
        }
        int years = toInt(YEAR, input);
        if (years == 0 && hasYear(input)) {
            return badSyntax();
        }


        calender.add(Calendar.MONTH, months);
        calender.add(Calendar.DAY_OF_YEAR, days);
        calender.add(Calendar.YEAR, years);

        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        return formatter.format(calender.getTime());
    }

    private boolean hasYear(String input) {
        return input.contains("year");
    }
    
    private boolean hasDay(String input) {
        return input.contains("day");
    }

    private boolean hasMonth(String input) {
        return input.contains("month");
    }

    private String badSyntax() {
        StringBuilder error = new StringBuilder();
        error.append(input).append(HtmlUtil.BRtag).append("Bad syntax!").append(HtmlUtil.BRtag);
        return HtmlUtil.metaText(error.toString());
    }

    private boolean hasPattern(String input) {
        return input.contains("pattern");
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
