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
        private CharacterRepository repro = new CharacterRepository();

        public HttpResponseMessage GetSessionCharacters(int sessionId)
        {
            var list = repro.GetCharactersInSession(sessionId);


            List<CharacterViewModel> charList = list.Select(c => new CharacterViewModel()
            { Id = c.Id, Description = "",
                History = "",
                Name = c.Name,
                Notes = "" }).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, charList);
        }

        public HttpResponseMessage Post(CharacterViewModel character)
        {
            Character newCharacter = new Character()
            {

            };
            repro.AddNewCharacter(newCharacter);
            return Request.CreateResponse(HttpStatusCode.OK);
        }

        public HttpResponseMessage PostAssociateToSession(int characterId, int sessionId)
        {
            var repro = new CharacterRepository();
            return Request.CreateResponse(HttpStatusCode.OK);
        }

    }
}
