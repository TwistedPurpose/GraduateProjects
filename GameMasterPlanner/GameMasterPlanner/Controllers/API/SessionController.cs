using System;
using System.Collections.Generic;
using System.Linq;
using DataAccess.Models;
using DataAccess.Repositories;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using DataAccess.EntityFramework;

namespace GameMasterPlanner.Controllers.API
{
    public class SessionController : ApiController
    {
        private SessionRepository sessionRepository = new SessionRepository();

        SessionController()
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
            var list = sessionRepository.GetSessionList(id);

            List<SessionViewModel> sessionList = list.Select(x => new SessionViewModel() {
                Id = x.Id,
                Notes = x.Notes,
                SessionNumber = x.SessionNumber,
                Title = x.Title}).ToList();


            return Request.CreateResponse(HttpStatusCode.OK, sessionList);
        }

        public HttpResponseMessage Post(SessionViewModel session)
        {
            Session dbSession = new Session()
            {
                Id = session.Id,
                CampaignId = session.CampaignId,
                Title = String.IsNullOrWhiteSpace(session.Title) ? String.Empty : session.Title.Trim(),
                Notes = String.IsNullOrWhiteSpace(session.Notes) ? String.Empty : session.Notes.Trim(),
                SessionNumber = session.SessionNumber,
                BaseMapId = session.BaseMapId
            };

            if (session.Id > 0)
            {
                sessionRepository.UpdateSession(dbSession);
            } else
            {
                dbSession = sessionRepository.CreateSession(dbSession);
            }
            
            return Request.CreateResponse(HttpStatusCode.OK, dbSession);
        }

    }
}
