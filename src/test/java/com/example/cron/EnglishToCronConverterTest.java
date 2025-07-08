package com.example.cron;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.englishtocron.Cron;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class EnglishToCronConverterTest {

    private final String input;
    private final String expected;

    public EnglishToCronConverterTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{index}: \"{0}\" => \"{1}\"")
    public static Collection<Object[]> cronTestCases() {
        return Arrays.asList(new Object[][]{
                {"Run every second", "* * * * * ? *"},
                {"every 5 second", "0/5 * * * * ? *"},
                {"every 5 second on september", "0/5 * * * SEP ? *"},
                {"every 5 second on 9 month", "0/5 * * * 9 ? *"},
                {"Every 2 seconds, only on thursday", "0/2 * * ? * THU *"},
                {"Run every 2 second on the 12th day", "0/2 0 0 12 * ? *"},
                {"Run every 2 second on Monday thursday", "0/2 * * ? * MON,THU *"},
                {"Run every 10 seconds Monday through thursday between 6:00 am and 8:00 pm", "0/10 * 6-20 ? * MON-THU *"},
                {"Run every minute", "0 * * * * ? *"},
                {"Run every 15 minutes", "0 0/15 * * * ? *"},
                {"every minutes on thursday", "0 * * ? * THU *"},
                {"every 2 minutes on Thursday", "0 0/2 * ? * THU *"},
                {"Run every 10 minutes Monday through Friday every month", "0 0/10 * ? * MON-FRI *"},
                {"Run every 1 minutes Monday through Thursday between 6:00 am and 9:00 pm", "0 0/1 6-21 ? * MON-THU *"},
                {"Run every 5 minutes Monday through Thursday between 6:00 am and 9:00 am", "0 0/5 6-9 ? * MON-THU *"},
                {"Every 5 minutes, only on Friday", "0 0/5 * ? * FRI *"},
                {"Run every 3 hours", "0 0 0/3 * * ? *"},
                {"Run every 6 hours, starting at 1:00 pm on day Monday", "0 0 0/6 ? * MON *"},
                {"Run every 1 hour only on weekends", "0 0 0/1 ? * SAT,SUN *"},
                {"Run every hour only on weekends", "0 0 * ? * SAT,SUN *"},
                {"2pm on Tuesday, Wednesday and Thursday", "0 0 14 ? * TUE,WED,THU *"},
                {"Run every day", "0 0 0 */1 * ? *"},
                {"Run every 4 days", "0 0 0 */4 * ? *"},
                {"every day at 4:00 pm", "0 0 16 */1 * ? *"},
                {"every 2 day at 4:00 pm", "0 0 16 */2 * ? *"},
                {"every 5 day at 4:30 pm", "0 30 16 */5 * ? *"},
                {"every 5 day at 4:30 pm only in September", "0 30 16 */5 SEP ? *"},
                {"every 5 day at 4:30 pm Monday through Thursday", "0 30 16 ? * MON-THU *"},
                {"Run every day from January to March", "0 0 0 */1 JAN-MAR ? *"},
                {"Run every 3 days at noon", "0 0 12 */3 * ? *"},
                {"Run every 2nd day of the month", "0 0 0 2 * ? *"},
                {"Run every sec from January to March", "* * * * JAN-MAR ? *"},
                {"Run every minute from January to March", "0 * * * JAN-MAR ? *"},
                {"Run every hours from January to March", "0 0 * * JAN-MAR ? *"},
                {"every 2 day from January to August in 2020 and 2024", "0 0 0 */2 JAN-AUG ? 2020,2024"},
                {"Run at 10:00 am", "0 0 10 * * ? *"},
                {"Run at 12:15 pm", "0 15 12 * * ? *"},
                {"Run at 6:00 pm every Monday through Friday", "0 0 18 ? * MON-FRI *"},
                {"Run at noon every Sunday", "0 0 12 ? * SUN *"},
                {"Run at midnight on the 1st and 15th of the month", "0 0 0 1,15 * ? *"},
                {"midnight on Tuesdays", "0 0 0 ? * TUE *"},
                {"Run at 5:15am every Tuesday", "0 15 5 ? * TUE *"},
                {"7pm every Thursday", "0 0 19 ? * THU *"},
                {"2pm and 6pm", "0 0 14,18 * * ? *"},
                {"5am, 10am and 3pm", "0 0 5,10,15 * * ? *"},
                {"Run every hour only on Monday", "0 0 * ? * MON *"},
                {"Run every 30 seconds only on weekends", "0/30 * * ? * SAT,SUN *"},
                {"4pm, 5pm and 7pm", "0 0 16,17,19 * * ? *"},
                {"4pm, 5pm, and 7pm", "0 0 16,17,19 * * ? *"},
                {"4pm, 5pm, 7pm", "0 0 16,17,19 * * ? *"},
                {"4pm and 5pm and 7pm", "0 0 16,17,19 * * ? *"},
        });
    }

    @Test
    public void canParseString() {
        try {
            String result = Cron.fromString(input).format();
            assertEquals("Failed for input: '" + input + "'", expected, result);
        } catch (Exception e) {
            fail("Unexpected error for input: '" + input + "': " + e.getMessage());
        }
    }
}
