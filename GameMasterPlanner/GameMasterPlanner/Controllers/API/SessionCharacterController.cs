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

        public SessionCharacterController()
        {
            characterRepro = new CharacterRepository();
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
            if (modelView != null)
            {
                if(modelView.characterIds != null)
                {
                    characterRepro.AssociateCharactersInSession(modelView.sessionId, modelView.characterIds.ToList());
                } else
                {
                    characterRepro.AssociateCharactersInSession(modelView.sessionId, new List<int>());
                }
            }
            
            return Request.CreateResponse(HttpStatusCode.OK);
        }

        public class SessionWithCharacters
        {
            public int sessionId { get; set; }
            public int[] characterIds { get; set; }
        }
    }
}
