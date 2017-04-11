using DataAccess.EntityFramework;
using DataAccess.Repositories;
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
        private CharacterRepository characterRepro = new CharacterRepository();
        private SessionRepository sessionRepro = new SessionRepository();

        public HttpResponseMessage Get(int characterId)
        {
            Character c = characterRepro.GetCharacter(characterId);

            CharacterViewModel character = new CharacterViewModel()
            {
                Id = c.Id,
                Name = c.Name,
                CharDescription = c.Description,
                Notes = c.Notes
            };

            return Request.CreateResponse(HttpStatusCode.OK, character);
        }

        public HttpResponseMessage Post(CharacterViewModel character)
        {
            Character newCharacter = new Character()
            {
                Name = character.Name,
                Description = character.CharDescription,
                Notes = character.Notes
            };

            newCharacter = characterRepro.CreateCharacter(newCharacter);

            return Get(newCharacter.Id);
        }

        public HttpResponseMessage Put(CharacterViewModel character)
        {
            throw new NotImplementedException();
            return Request.CreateResponse(HttpStatusCode.OK);
        }

        public HttpResponseMessage PostAssociateToSession(int characterId, int sessionId)
        {
            sessionRepro.AssociateCharacterToSession(characterId, sessionId);

            return Request.CreateResponse(HttpStatusCode.OK);
        }

        public HttpResponseMessage GetSessionCharacters(int sessionId)
        {
            var list = characterRepro.GetCharactersInSession(sessionId);


            List<CharacterViewModel> charList = list.Select(c => new CharacterViewModel()
            {
                Id = c.Id,
                CharDescription = c.Description,
                Name = c.Name,
                Notes = c.Notes
            }).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, charList);
        }

    }
}
