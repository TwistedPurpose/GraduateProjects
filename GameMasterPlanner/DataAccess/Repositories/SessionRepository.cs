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

        public List<SessionViewModel> GetSessionList()
        {

            using (var db = new GameMasterPlannerDBEntities())
            {

                var list = from sessions in db.Sessions
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
