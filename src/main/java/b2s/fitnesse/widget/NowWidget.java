/**
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package b2s.fitnesse.widget;

import fitnesse.html.HtmlUtil;
import fitnesse.wikitext.WikiWidget;
import fitnesse.wikitext.widgets.ParentWidget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NowWidget extends WikiWidget {
    public static final String REGEXP = "!now( (pattern|day|month|year)\\(.*?\\))*";

    private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
    private static final Pattern PATTERN = Pattern.compile("pattern\\((.+?)\\)");
    private static final Pattern MONTH = Pattern.compile("month\\((.+?)\\)");
    private static final Pattern DAY = Pattern.compile("day\\((.+?)\\)");
    private static final Pattern YEAR = Pattern.compile("year\\((.+?)\\)");

    private final String input;
    private CalendarFactory calenderFactory = new CalendarFactory();

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

        Calendar calendar = calenderFactory.build();
        calendar.add(Calendar.MONTH, months);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        calendar.add(Calendar.YEAR, years);

        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        return formatter.format(calendar.getTime());
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

    protected void setCalenderFactory(CalendarFactory calendarFactory) {
        this.calenderFactory = calendarFactory;
    }

    public static class CalendarFactory {
        public Calendar build() {
            return Calendar.getInstance();
        }
    }
}
