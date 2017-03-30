using System;
using System.Collections.Generic;
using System.Linq;
using DataAccess.Models;
using DataAccess.Repositories;
using System.Net;
using System.Net.Http;
using System.Web.Http;

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
            return Request.CreateResponse(HttpStatusCode.OK, list);
        }
    }
}
