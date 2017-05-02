using DataAccess.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace GameMasterPlanner.Controllers.API
{
    public class SessionCharacterController : ApiController
    {
        private CharacterRepository characterRepro;
        private SessionRepository sessionRepro;

        public SessionCharacterController()
        {
            characterRepro = new CharacterRepository();
            sessionRepro = new SessionRepository();
        }

        /// <summary>
        /// Associates characters with a session
        /// Process is destructive, session will ONLY contain these characters
        /// </summary>
        /// <param name="sessionId">Id of the session to be modified</param>
        /// <param name="characterIds">ids of all the characers to be set to this session</param>
        /// <returns>HTTP response for status of action</returns>
        public HttpResponseMessage PostAssociateCharactersWithSession(SessionWithCharacters modelView)
        {
            characterRepro.AssociateCharactersInSession(modelView.sessionId, modelView.characterIds.ToList());

            return Request.CreateResponse(HttpStatusCode.OK);
        }

        public class SessionWithCharacters
        {
            public int sessionId { get; set; }
            public int[] characterIds { get; set; }
        }
    }
}
