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

        /// <summary>
        /// Gets all characters in a single campaign
        /// </summary>
        /// <param name="campaignId">Id of the campaign the characters are in</param>
        /// <returns>List of character model views wrapped in an HTTPResponse</returns>
        public HttpResponseMessage GetAll(int campaignId)
        {
            List<CharacterViewModel> characters = characterRepro.GetAllCharacters(campaignId)
                .Select(x => ModelConverter.ToCharacterViewModel(x)).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, characters);
        }

        /// <summary>
        /// Takes in a character model view and either creates a new entity of a character
        /// or updates an existing character depending on if the ID is populated.
        /// It also associates a character with a session is if the session Id is populated.
        /// </summary>
        /// <param name="character">Character view model to be updated/created/associated</param>
        /// <returns>HTTP Response with modified or created character object</returns>
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

            // This is a hack to cover up for the fact session list is dropping off somewhere?
            dbCharacter = characterRepro.GetCharacter(dbCharacter.Id);

            return Request.CreateResponse(HttpStatusCode.OK, ModelConverter.ToCharacterViewModel(dbCharacter));
        }

        public HttpResponseMessage GetSessionCharacters(int sessionId)
        {
            var list = characterRepro.GetCharactersInSession(sessionId);

            List<CharacterViewModel> charList = list.Select(c => ModelConverter.ToCharacterViewModel(c)).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, charList);
        }


    }
}
