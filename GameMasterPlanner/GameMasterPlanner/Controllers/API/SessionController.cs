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
        /// <summary>
        /// Get all sessions based on campaign ID
        /// </summary>
        /// <param name="id">Get all sessions with the following camaign ID</param>
        /// <returns></returns>
        public HttpResponseMessage Get(int id)
        {
            var repro = new SessionRepository();
            var list = repro.GetSessionList(id);

            List<SessionViewModel> sessionList = list.Select(x => new SessionViewModel() {
                Id = x.Id,
                Notes = x.Notes,
                SessionNumber = x.SessionNumber,
                Title = x.Title}).ToList();


            return Request.CreateResponse(HttpStatusCode.OK, sessionList);
        }

        public HttpResponseMessage Post(SessionViewModel session)
        {
            throw new NotImplementedException();
            return Request.CreateResponse(HttpStatusCode.OK);
        }
    }
}
