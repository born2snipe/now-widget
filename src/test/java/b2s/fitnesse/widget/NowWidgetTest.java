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
import fitnesse.wikitext.widgets.WidgetTestCase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class NowWidgetTest extends WidgetTestCase {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("MM/dd/yyyy");

    public void test_pattern_badSyntax() throws Exception {
        String errorMessage = HtmlUtil.metaText("!now pattern(MM/dd/yyyy<br/>Bad syntax!<br/>");
        assertEquals(errorMessage, widget("!now pattern(MM/dd/yyyy", "01/01/2000").render());
    }

    public void test_year_badSyntax() throws Exception {
        String errorMessage = HtmlUtil.metaText("!now year(1<br/>Bad syntax!<br/>");
        assertEquals(errorMessage, widget("!now year(1", "01/01/2000").render());
    }
    
    public void test_day_badSyntax() throws Exception {
        String errorMessage = HtmlUtil.metaText("!now day(1<br/>Bad syntax!<br/>");
        assertEquals(errorMessage, widget("!now day(1", "01/01/2000").render());
    }
    
    public void test_month_badSyntax() throws Exception {
        String errorMessage = HtmlUtil.metaText("!now month(1<br/>Bad syntax!<br/>");
        assertEquals(errorMessage, widget("!now month(1", "01/01/2000").render());
    }
    
    public void test_noPattern() throws Exception {
        assertEquals("01/01/2000", widget("!now", "01/01/2000").render());
    }

    public void test_addYear_WithPlus() throws Exception {
        assertEquals("01/01/2010", widget("!now pattern(MM/dd/yyyy) year(+10)", "01/01/2000").render());
    }
    
    public void test_addYear() throws Exception {
        assertEquals("01/01/2010", widget("!now pattern(MM/dd/yyyy) year(10)", "01/01/2000").render());
    }
    
    public void test_addDay() throws Exception {
        assertEquals("01/11/2000", widget("!now pattern(MM/dd/yyyy) day(10)", "01/01/2000").render());
    }
    
    public void test_addMonth() throws Exception {
        assertEquals("02/01/2000", widget("!now pattern(MM/dd/yyyy) month(1)", "01/01/2000").render());
    }

    public void test_subtractMonth() throws Exception {
        assertEquals("2000", widget("!now month(-6) pattern(yyyy)", "07/01/2000").render());
    }
    
    public void test_onlyPattern() throws Exception {
        assertEquals("01/01/2000", widget("!now pattern(MM/dd/yyyy)", "01/01/2000").render());
    }

    public void test_callRenderMultipleTimesWithDateManipulation() throws Exception {
        NowWidget widget = widget("!now pattern(yyyy) month(6)", "01/01/2000");
        assertEquals("2000", widget.render());
        assertEquals("2000", widget.render());
    }

    public void test_pattern() {
        assertMatch("!now");
        assertMatch("!now pattern(MM/dd/yyyy)");
        assertMatch("!now year(10)");
        assertMatch("!now month(10)");
        assertMatch("!now day(10)");
        assertMatchEquals(" !now pattern(MM/dd/yyyy)", "!now pattern(MM/dd/yyyy)");
        assertMatchEquals("!now pattern(MM/dd/yyyy)\nnext-line", "!now pattern(MM/dd/yyyy)");
        assertMatchEquals("!define year1 {!now pattern(yyyy)}", "!now pattern(yyyy)");
        assertMatchEquals("!define year1 [!now pattern(yyyy)]", "!now pattern(yyyy)");
        assertMatchEquals("!define year1 (!now pattern(yyyy))", "!now pattern(yyyy)");
    }

    protected String getRegexp() {
        return NowWidget.REGEXP;
    }

    private NowWidget widget(String widgetPattern, final String datePattern) {
        NowWidget widget = new NowWidget(null, widgetPattern);
        widget.setCalenderFactory(new NowWidget.CalendarFactory() {
            @Override
            public Calendar build() {
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(FORMATTER.parse(datePattern));
                }
                catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                return calendar;
            }
        });
        return widget;
    }
}
