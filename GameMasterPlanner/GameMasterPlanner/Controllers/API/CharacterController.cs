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
        public HttpResponseMessage GetSessionCharacters(int sessionId)
        {
            var repro = new CharacterRepository();
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
            var repro = new CharacterRepository();
            //repro.addNewCharacterToSession();
            return Request.CreateResponse(HttpStatusCode.OK);
        }


    }
}
