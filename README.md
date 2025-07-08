# CronEncoder for Java
### Converts English Phrases to Cron Expressions

---

🕒 Time Units Supported

These indicate what time unit the job should recur on:

second(s) → "Run every second", "every 5 second", etc.

minute(s) → "Run every 10 minutes", "every 2 minutes", etc.

hour(s) → "Run every 3 hours", "Run every hour", etc.

day(s) → "Run every day", "Run every 4 days", etc.

month(s) → Implied by "on september", "on 9 month", etc.

specific times → "Run at 10:00 am", "5am, 10am and 3pm", "4pm, 5pm, and 7pm"



---

📅 Day and Week Support

Can specify:

Specific days: "on Monday", "on thursday", "on Tuesday, Wednesday and Thursday"

Day ranges: "Monday through Friday", "Monday through Thursday"

Weekends: "only on weekends" → implies "SAT,SUN"

Weekdays by name: "only on Friday", "every Tuesday"

Ordinal days: "every 2nd day of the month"



---

📆 Month Support

Supports:

Month names: "on september", "from January to March"

Month numbers: "on 9 month"

Month ranges: "from January to August"

Specific month + year: "in 2020 and 2024"



---

⏰ Specific Times

Simple: "at 10:00 am", "at 12:15 pm", "at noon", "at midnight"

Multiple times: "2pm and 6pm", "4pm, 5pm, and 7pm"



---

⌛ Time Ranges

Supports time range constraints:

"between 6:00 am and 9:00 am"

"between 6:00 am and 8:00 pm" → converted to 6-20 in hours



---

📈 Combinations

Supports complex combinations like:

"Run every 5 day at 4:30 pm Monday through Thursday"

"Run every 10 seconds Monday through thursday between 6:00 am and 8:00 pm"


This means the parser can handle multiple overlapping conditions, such as time of day, recurrence rate, day of week, and month filters.


---

🛑 Special Values

"noon" → 12:00 pm

"midnight" → 12:00 am

"every second", "every minute", etc. → flexible intervals

"*" usage inferred for seconds when not specified (e.g., "Run every second" → * * * * * ? *)



---

🧠 Natural Language Parsing Features

It supports:

Singular/plural normalization: "second" and "seconds" both work.

Conjunctions and lists: "and", commas (e.g., "Monday, Wednesday and Friday")

Case-insensitive matching: "Run every Minute", "run Every day" should work.

Partial sentences: "every minute on thursday"



---

✅ Summary of Supported Patterns

Feature	Example Input

Seconds	"Run every 5 second"

Minutes	"every 2 minutes on Thursday"

Hours	"Run every 3 hours"

Days	"Run every 4 days"

Specific Time	"Run at 10:00 am"

Time Ranges	"between 6:00 am and 9:00 am"

Days of Week	"Monday through Thursday"

Multiple Times	"4pm, 5pm, and 7pm"

Specific Dates	"on the 12th day"

Months	"on September", "from January to March"

Years	"in 2020 and 2024"



---

❓Limitations (Inferred)

The parser may not support fuzzy language like "every now and then" or "around noon".

Unclear if relative expressions like "next Monday" or "last day of month" are supported.

The parser doesn't show support for time zones or "cron @every" shorthand.

