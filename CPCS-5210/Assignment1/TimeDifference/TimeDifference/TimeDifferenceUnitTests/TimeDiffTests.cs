using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using TimeDifference;

namespace TimeDifferenceUnitTests
{
    [TestClass]
    public class TimeDiffTests
    {
        public readonly int ERROR_INTERVAL = -1;

        [TestMethod]
        public void TimeDifference_TimeDiff_MorningAndEvening()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(3, 45, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(6, 10, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(14, 25);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours are incorrect");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes are incorrect");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_NoDifference()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(3, 45, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(3, 45, false);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(0, 0);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours are incorrect");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes are incorrect");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_12HourDifference()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(2, 0, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(2, 0, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(12, 0);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours are incorrect");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes are incorrect");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_MidnightBoundaryNightToMorning()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(11, 59, true);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, 0, false);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(0, 1);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours are incorrect");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes are incorrect");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_MidnightBoundaryMorningToNight()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, 0, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(11, 59, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(23, 59);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours are incorrect");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes are incorrect");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_MorningBoundaryMorningToAfternoon()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(11, 59, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, 0, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(0, 1);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours are incorrect");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes are incorrect");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_MorningBoundaryAfternoonToMorning()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, 0, true);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(11,59, false);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(23, 59);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours are incorrect");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes are incorrect");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_HourBoundaryOneMinute()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(1, 59, true);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(2, 0, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(0, 1);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours are incorrect");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes are incorrect");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_HourBoundaryWholeDay()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(2, 0, true);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(1, 59, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(23, 59);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours are incorrect");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes are incorrect");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidPositiveEarlyHourPM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(13, 0, true);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, 0, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidPositiveLateHourPM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, 0, true);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(13, 0, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidPositiveEarlyHourAM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(13, 0, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, 0, false);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidPositiveLateHourAM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, 0, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(13, 0, false);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidPositiveEarlyMinutePM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, 60, true);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, 0, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidPositiveLateMinutePM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, 0, true);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, 60, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidPositiveEarlyMinuteAM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, 60, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, 0, false);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidPositiveLateMinuteAM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, 0, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, 60, false);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidNegativeEarlyHourPM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(-1, 0, true);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, 0, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidNegativeLateHourPM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, 0, true);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(-1, 0, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidNegativeEarlyHourAM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(-1, 0, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, 0, false);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidNegativeLateHourAM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, 0, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(-1, 0, false);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidNegativeEarlyMinutePM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, -1, true);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, 0, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidNegativeLateMinutePM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, 0, true);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, -1, true);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidNegativeEarlyMinuteAM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, -1, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, 0, false);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }

        [TestMethod]
        public void TimeDifference_TimeDiff_InvalidNegativeLateMinuteAM()
        {
            TimeDiff.TimeOfDay early = new TimeDiff.TimeOfDay(12, 0, false);
            TimeDiff.TimeOfDay late = new TimeDiff.TimeOfDay(12, -1, false);
            TimeDiff.TimeInterval expected = new TimeDiff.TimeInterval(ERROR_INTERVAL, ERROR_INTERVAL);
            TimeDiff.TimeInterval actual = TimeDiff.computeTimeDiff(early, late);
            Assert.AreEqual(expected.hours, actual.hours, "Hours don't have validation");
            Assert.AreEqual(expected.minutes, actual.minutes, "Minutes don't have validation");
        }
    }
}
