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
    
    public void test_onlyPattern() throws Exception {
        assertEquals("01/01/2000", widget("!now pattern(MM/dd/yyyy)", "01/01/2000").render());
    }

    public void test_pattern() {
        assertMatch("!now");
        assertNoMatch(" !now pattern(MM/dd/yyyy)");
        assertMatch("!now pattern(MM/dd/yyyy)");
        assertMatchEquals("!now pattern(MM/dd/yyyy)\nnext-line", "!now pattern(MM/dd/yyyy)");
    }

    protected String getRegexp() {
        return NowWidget.REGEXP;
    }

    private NowWidget widget(String widgetPattern, String datePattern) {
        Calendar calendar = Calendar.getInstance();
        NowWidget widget = new NowWidget(null, widgetPattern);
        widget.setCalender(calendar);
        try {
            calendar.setTime(FORMATTER.parse(datePattern));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return widget;
    }
}
