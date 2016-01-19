using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TimeDifference
{
    public class TimeDiff
    {
        public struct TimeOfDay
        {
            public int hour;
            public int minute;
            public bool isPM;

            public TimeOfDay(int h, int m, bool p) { hour = h; minute = m; isPM = p; }
        }

        public struct TimeInterval
        {
            public int hours;
            public int minutes;

            public TimeInterval(int h, int m) { hours = h; minutes = m; }
        }

        private static readonly TimeInterval errorInterval;

        static TimeDiff()
        {
            errorInterval = new TimeInterval(-1, -1);
        }

        public static TimeInterval computeTimeDiff(TimeOfDay early, TimeOfDay late)
        {
            TimeInterval result;

            if (early.hour < 1 || early.hour > 12)
            {
                Console.WriteLine("Invalid early hour");
                return errorInterval;
            }
            if (late.hour < 1 || late.hour > 12)
            {
                Console.WriteLine("Invalid late hour");
                return errorInterval;
            }
            if (early.minute < 0 || early.minute > 60)
            {
                Console.WriteLine("Invalid early minute");
                return errorInterval;
            }
            if (late.minute < 0 || late.minute > 60)
            {
                Console.WriteLine("Invalid early minute");
                return errorInterval;
            }

            if (early.isPM != late.isPM)
            {
                result.hours = late.hour + 12 - early.hour;
            }
            else if (early.hour > late.hour)
            {
                result.hours = late.hour + 24 - early.hour;
            }
            else
            {
                result.hours = late.hour - early.hour;
            }

            if (early.minute == late.minute)
            {
                result.minutes = 0;
            }
            else if (early.minute > late.minute)
            {
                result.minutes = late.minute + 60 - early.minute;
                result.hours -= 1;
            }
            else
            {
                result.minutes = late.minute - early.minute;
            }

            return result;
        }

        private static TimeOfDay? getTimeOfDayFromInput(string prompt)
        {
            try
            {
                Console.Write(prompt);
                string userStr = Console.ReadLine();
                var parts = userStr.Split(':');
                var rest = parts[1].Split(' ');
                int h = Convert.ToInt32(parts[0]);
                int m = Convert.ToInt32(rest[0]);
                bool p = false;
                if (rest[1] == "PM" || rest[1] == "pm")
                {
                    p = true;
                }
                else if (rest[1] == "AM" || rest[1] == "am")
                {
                    p = false;
                }
                else
                {
                    Console.WriteLine("Invalid input");
                    return null;
                }
                return new TimeOfDay(h, m, p);
            }
            catch (Exception e)
            {
                Console.WriteLine("Invalid input");
                return null;
            }
        }

        static void Main(string[] args)
        {
            TimeOfDay? early = null;
            while (early.Equals(null))
            {
                early = getTimeOfDayFromInput("Enter early time: ");
            }
            TimeOfDay earlyTime = (TimeOfDay)early;

            TimeOfDay? late = null;
            while (late.Equals(null))
            {
                late = getTimeOfDayFromInput("Enter late time: ");
            }
            TimeOfDay lateTime = (TimeOfDay)late;

            TimeInterval result = computeTimeDiff(earlyTime, lateTime);

            if (result.Equals(errorInterval))
            {
                Console.WriteLine("computeTimeDiff reported an error");
            }
            else
            {
                Console.WriteLine("The difference is {0} hours and {1} minutes", result.hours, result.minutes);
            }

            // Require the user to press a key to exit window
            Console.ReadKey();
        }
    }
}
