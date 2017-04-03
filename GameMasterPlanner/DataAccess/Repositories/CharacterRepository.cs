using DataAccess.EntityFramework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess.Repositories
{
    public class CharacterRepository
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="id">Campaign Id</param>
        /// <returns></returns>
        public List<Character> GetAllCharacters(int campaignId)
        {
            return new List<Character>();
        }

        public List<Character> GetCharactersInSession(int sessionId)
        {
            using (var db = new GameMasterPlannerDBEntities())
            {
                var list = from session in db.Sessions
                           where session.Id == sessionId
                           select session.Characters;


                return list.First().ToList();
            }
        }
    }
}
