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
        public HttpResponseMessage Get()
        {
            var repro = new SessionRepository();
            var list = repro.GetSessionList();
            return Request.CreateResponse(HttpStatusCode.OK, list);
        }
    }
}
