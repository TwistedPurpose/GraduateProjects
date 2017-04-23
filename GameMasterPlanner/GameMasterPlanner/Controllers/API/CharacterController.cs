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

            return Get(dbCharacter.Id);
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

    }
}
