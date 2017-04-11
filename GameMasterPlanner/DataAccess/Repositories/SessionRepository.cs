using DataAccess.EntityFramework;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Runtime.Remoting.Contexts;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess.Repositories
{
    public class SessionRepository
    {
        private GameMasterPlannerDBEntities db;

        public SessionRepository()
        {
            db = new GameMasterPlannerDBEntities();
        }

        /// <summary>
        /// Get all sessions for a campaign for a campagin id
        /// </summary>
        /// <param name="id">id of a campaign</param>
        /// <returns>List of all sessions as view models</returns>
        public List<Session> GetSessionList(int id)
        {
            var list = from sessions in db.Sessions
                       where sessions.CampaignId == id
                       select sessions;

            return list.ToList();
        }

        public void CreateSession(Session newSession)
        {
            db.Sessions.Add(newSession);
            db.SaveChanges();
        }

        public void AssociateCharacterToSession(int characterId, int sessionId)
        {
            Session session = (from sessions in db.Sessions
                          where sessions.Id == sessionId
                          select sessions).FirstOrDefault();

            Character character = (from charcters in db.Characters
                                   where charcters.Id == characterId
                                   select charcters).FirstOrDefault();

            session.Characters.Add(character);
            db.SaveChanges();
        }
    }
}
