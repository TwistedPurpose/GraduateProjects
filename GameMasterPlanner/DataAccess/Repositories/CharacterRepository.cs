using DataAccess.EntityFramework;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess.Repositories
{
    public class CharacterRepository : BaseRepository
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="id">Campaign Id</param>
        /// <returns></returns>
        public List<Character> GetAllCharacters(int campaignId)
        {
            var list = (from characters in db.Characters
                       where characters.CampaignId == campaignId
                       select characters).Include(s => s.Sessions);

            return list.ToList();
        }

        public List<Character> GetCharactersInSession(int sessionId)
        {
            var list = from session in db.Sessions
                       where session.Id == sessionId
                       select session.Characters;

            return list.FirstOrDefault().ToList();
        }

        public Character GetCharacter(int characterId)
        {
            return (from character in db.Characters
                    where character.Id == characterId
                    select character).Include(s => s.Sessions).FirstOrDefault();
        }

        public Character CreateCharacter(Character character)
        {
            db.Characters.Add(character);
            db.SaveChanges();
            return character;
        }

        public void AddCharacterToSession(int characterId, int sessionId)
        {
            Session s = (from session in db.Sessions
                          where session.Id == sessionId
                         select session).FirstOrDefault();

            Character c = GetCharacterById(sessionId);

            s.Characters.Add(c);

            db.SaveChanges();
        }

        public void UpdateCharacter(Character dbCharacter)
        {
            db.Characters.Attach(dbCharacter);
            var entry = db.Entry(dbCharacter);

            entry.State = EntityState.Modified;

            db.SaveChanges();
        }

        /// <summary>
        /// Takes a list of character ids and associates with a session
        /// This is destructive, the session will only have the
        /// characters in the characterIds list.
        /// </summary>
        /// <param name="sessionId"></param>
        /// <param name="characterIds"></param>
        public void AssociateCharactersInSession(int sessionId, List<int> characterIds)
        {
            Session session = (from s in db.Sessions
                               where s.Id == sessionId
                               select s).FirstOrDefault();

            session.Characters.Clear();

            foreach(int characterId in characterIds)
            {
                Character c = GetCharacterById(characterId);

                session.Characters.Add(c);
            }

            db.SaveChanges();
        }

        /// <summary>
        /// Gets the first instance of a character by ID
        /// </summary>
        /// <param name="characterId">Id of the character</param>
        /// <returns>Character that matches id in input</returns>
        private Character GetCharacterById (int characterId)
        {
            return (from character in db.Characters
                           where character.Id == characterId
                           select character).FirstOrDefault();
        }
    }
}
