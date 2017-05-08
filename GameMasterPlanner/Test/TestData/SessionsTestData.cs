using DataAccess.EntityFramework;
using DataAccess.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Test.TestData
{
    public static class SessionsTestData
    {
        private static List<Session> _dbSessionList = new List<Session>()
        {
            new Session()
            {
                Id = 1,
                BaseMapId = 1,
                CampaignId = 1,
                SessionNumber = 1,
                Title = "King's Day",
                Notes = "The day the king drinks water from every river and lake in the kingdom, but the Wild Hunt has something different to say about this!"
            },
            new Session()
            {
                Id = 1,
                BaseMapId = 2,
                CampaignId = 1,
                SessionNumber = 2,
                Title = "Wish Hunters",
                Notes = "The four unlikely souls unite to push those who evoked the Wild Hunt to destory Crystala, but they are really fucking bad at travel"
            },
            new Session()
            {
                Id = 1,
                BaseMapId = 3,
                CampaignId = 1,
                SessionNumber = 4,
                Title = "The Day the Music Died",
                Notes = "The group of adventurers arrive at Breeman to enjoy the music festival, but the event is cancelled in more ways than one"
            }
        };

        private static List<SessionViewModel> _sessionVMs = new List<SessionViewModel>()
        {
            new SessionViewModel()
            {
                Id = 1,
                BaseMapId = 1,
                CampaignId = 1,
                Title = "King's Day",
                Notes = "The day the king drinks water from every river and lake in the kingdom, but the Wild Hunt has something different to say about this!"
            },
            new SessionViewModel()
            {
                Id = 1,
                BaseMapId = 2,
                CampaignId = 1,
                SessionNumber = 2,
                Title = "Wish Hunters",
                Notes = "The four unlikely souls unite to push those who evoked the Wild Hunt to destory Crystala, but they are really fucking bad at travel"
            },
            new SessionViewModel()
            {
                Id = 1,
                BaseMapId = 3,
                CampaignId = 1,
                SessionNumber = 4,
                Title = "The Day the Music Died",
                Notes = "The group of adventurers arrive at Breeman to enjoy the music festival, but the event is cancelled in more ways than one"
            }
        };

        public static List<SessionViewModel> getSessionVMs() { return _sessionVMs; }
        public static List<Session> getDbSessions() { return _dbSessionList; }

    }
}
