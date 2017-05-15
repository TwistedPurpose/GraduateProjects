using DataAccess.EntityFramework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess.Repositories
{
    public class MapRepository : BaseRepository
    {
        /// <summary>
        /// Creates a map in the database using a map entity
        /// </summary>
        /// <param name="map">Map entity for the database</param>
        /// <returns>A saved Map entity with it's new ID</returns>
        public Map CreateMap(Map map)
        {
            db.Maps.Add(map);

            db.SaveChanges();

            return map;
        }

        /// <summary>
        /// Gets the map associated with the session
        /// </summary>
        /// <param name="sessionId">Intiger of session that contains the map</param>
        /// <returns>Map associated with session</returns>
        public Map GetMapInSession(int sessionId)
        {
            Session sessionWithMap = (from session in db.Sessions
                               where session.Id == sessionId
                               select session).FirstOrDefault();

            return sessionWithMap.Map;
        }

        public void UpdateMap(Map map)
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// Gets a map by an integer id
        /// </summary>
        /// <param name="id">Integer id of a map</param>
        /// <returns>Map entity associated with integer id</returns>
        public Map GetMap(int id)
        {
            Map map = (from maps in db.Maps
                       where maps.Id == id
                       select maps).FirstOrDefault();

            return map;
        }

        /// <summary>
        /// Sets the baseMapId of a session, which will display this new map
        /// when the session is viewed
        /// </summary>
        /// <param name="mapId">Intiger of map to be associated</param>
        /// <param name="sessionId">Intiger id of session to be set</param>
        public void AddMapToSession(int mapId, int sessionId)
        {
            Session s = (from session in db.Sessions
                         where session.Id == sessionId
                         select session).FirstOrDefault();

            Map m = (from map in db.Maps
                     where map.Id == mapId
                     select map).FirstOrDefault();

            s.Map = m;

            db.SaveChanges();
        }
    }
}
