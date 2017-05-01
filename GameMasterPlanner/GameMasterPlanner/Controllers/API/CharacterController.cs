using DataAccess.EntityFramework;
using DataAccess.Repositories;
using GameMasterPlanner.Helper;
using GameMasterPlanner.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace GameMasterPlanner.Controllers.API
{
    public class CharacterController : ApiController
    {
        private CharacterRepository characterRepro;
        private SessionRepository sessionRepro;

        public CharacterController()
        {
            characterRepro = new CharacterRepository();
            sessionRepro = new SessionRepository();
        }

        /// <summary>
        /// Gets a single character by ID
        /// </summary>
        /// <param name="characterId">Id of character</param>
        /// <returns>HTTP response with character view model object</returns>
        public HttpResponseMessage Get(int characterId)
        {
            Character c = characterRepro.GetCharacter(characterId);

            CharacterViewModel character = ModelConverter.ToCharacterViewModel(c);

            return Request.CreateResponse(HttpStatusCode.OK, character);
        }

        public HttpResponseMessage GetAll(int campaignId)
        {
            List<CharacterViewModel> characters = characterRepro.GetAllCharacters(campaignId)
                .Select(x => ModelConverter.ToCharacterViewModel(x)).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, characters);
        }

        public HttpResponseMessage Post(CharacterViewModel character)
        {
            Character dbCharacter = ModelConverter.ToDbCharacterModel(character);

            if (dbCharacter.Id > 0)
            {
                characterRepro.UpdateCharacter(dbCharacter);
            }
            else
            {
                dbCharacter = characterRepro.CreateCharacter(dbCharacter);
            }

            if(character.SessionId > 0)
            {
                sessionRepro.AssociateCharacterToSession(dbCharacter.Id, character.SessionId);
            }

            return Request.CreateResponse(HttpStatusCode.OK, ModelConverter.ToCharacterViewModel(dbCharacter));
        }

        public HttpResponseMessage PostAssociateToSession(int characterId, int sessionId)
        {
            sessionRepro.AssociateCharacterToSession(characterId, sessionId);

            return Request.CreateResponse(HttpStatusCode.OK);
        }

        public HttpResponseMessage GetSessionCharacters(int sessionId)
        {
            var list = characterRepro.GetCharactersInSession(sessionId);

            List<CharacterViewModel> charList = list.Select(c => ModelConverter.ToCharacterViewModel(c)).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, charList);
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
