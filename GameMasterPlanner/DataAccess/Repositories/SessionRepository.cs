using DataAccess.EntityFramework;
using DataAccess.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess.Repositories
{
    public class SessionRepository
    {

        /// <summary>
        /// Get all sessions for a campaign for a campagin id
        /// </summary>
        /// <param name="id">id of a campaign</param>
        /// <returns>List of all sessions as view models</returns>
        public List<SessionViewModel> GetSessionList(int id)
        {

            using (var db = new GameMasterPlannerDBEntities())
            {

                var list = from sessions in db.Sessions
                           where sessions.CampaignId == id
                           select new SessionViewModel()
                           {
                               Id = sessions.Id,
                               Title = sessions.Title,
                               Notes = sessions.Notes,
                               SessionNumber = sessions.SessionNumber
                           };


                return list.ToList();
            }
        }
    }
}
