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
        public CharacterController()
        {
            characterRepro = new CharacterRepository();
            sessionRepro = new SessionRepository();
        }

        private CharacterRepository characterRepro;
        private SessionRepository sessionRepro;

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

        public HttpResponseMessage GetAll(int campaignId)
        {
            List<CharacterViewModel> characters = characterRepro.GetAllCharacters(campaignId)
                .Select(x => new CharacterViewModel()
                {
                    Id = x.Id,
                    CharDescription = x.Description,
                    Notes = x.Notes,
                    Name = x.Name
                }).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, characters);
        }

        public HttpResponseMessage Post(CharacterViewModel character)
        {
            Character dbCharacter = new Character()
            {
                Id = character.Id,
                CampaignId = character.CampaignId,
                Name = String.IsNullOrWhiteSpace(character.Name) ? String.Empty : character.Name.Trim(),
                Description = String.IsNullOrWhiteSpace(character.CharDescription) ? String.Empty : character.CharDescription.Trim(),
                Notes = String.IsNullOrWhiteSpace(character.Notes) ? String.Empty : character.Notes.Trim()
            };

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

            List<CharacterViewModel> charList = list.Select(c => new CharacterViewModel()
            {
                Id = c.Id,
                CampaignId = c.CampaignId,
                CharDescription = c.Description,
                Name = c.Name,
                Notes = c.Notes
            }).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, charList);
        }

    }
}
