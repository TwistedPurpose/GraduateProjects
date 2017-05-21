using DataAccess.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace GameMasterPlanner.Controllers.API
{
    public class SessionMapController : ApiController
    {
        private MapRepository mapRepository;

        public SessionMapController()
        {
            mapRepository = new MapRepository();
        }

        public HttpResponseMessage Post(int mapId, int sessionId)
        {
            mapRepository.AddMapToSession(mapId, sessionId);
            return Request.CreateResponse(HttpStatusCode.OK);
        }
    }
}
