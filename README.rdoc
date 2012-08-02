== Why does it exist?
It is very similar to the !today widget found in FitNesse. The main difference is you can alter the date by months, days, and years, instead of days only.

== Examples Usages

    Control the pattern of output.........!now pattern(MM/dd/yyyy)
	Add a month...........................!now pattern(MM/dd/yyyy) month(1)
    Add a day.............................!now pattern(MM/dd/yyyy) day(1)
	Add a year............................!now pattern(MM/dd/yyyy) year(1)
	Subtracting month, day, and year......!now pattern(MM/dd/yyyy) month(-1) day(-1) year(-1)
	
	