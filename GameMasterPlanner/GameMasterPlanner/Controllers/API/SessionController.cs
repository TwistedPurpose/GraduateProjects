using System;
using System.Collections.Generic;
using System.Linq;
using DataAccess.Models;
using DataAccess.Repositories;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using DataAccess.EntityFramework;
using GameMasterPlanner.Helper;

namespace GameMasterPlanner.Controllers.API
{
    public class SessionController : ApiController
    {
        private SessionRepository sessionRepository = new SessionRepository();

        public SessionController()
        {
            sessionRepository = new SessionRepository();
        }

        /// <summary>
        /// Get all sessions based on campaign ID
        /// </summary>
        /// <param name="id">Get all sessions with the following camaign ID</param>
        /// <returns></returns>
        public HttpResponseMessage Get(int id)
        {
            //Switch this to get one session later
            var list = sessionRepository.GetSessionList(id);

            List<SessionViewModel> sessionList = list.Select(x => ModelConverter.ToSessionViewModel(x)).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, sessionList);
        }

        public HttpResponseMessage Post(List<SessionViewModel> sessionList)
        {
            List<Session> dbSessions = sessionList.Select(x => ModelConverter.ToDbSessionModel(x)).ToList();

            if (dbSessions.Count == 1)
            {
                Session session = dbSessions.First();
                if (session.Id > 0)
                {
                    sessionRepository.UpdateSession(session);
                }
                else
                {
                    session = sessionRepository.CreateSession(session);
                }
            } else
            {
                sessionRepository.BulkSessionUpdate(dbSessions);
            }

            // OMG FIX THIS LATER
            sessionList = sessionRepository.GetSessionList(dbSessions.First().CampaignId).Select(x => ModelConverter.ToSessionViewModel(x)).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, dbSessions);
        }


        ///Should bring in character and item associations into here to consolidate controllers
    }
}
