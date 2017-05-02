using DataAccess.EntityFramework;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess.Repositories
{
    public class ItemRepository : BaseRepository
    {
        /// <summary>
        /// Creates an item for characters
        /// </summary>
        /// <param name="item">Item entity to be saved</param>
        /// <returns>Item with updated ID</returns>
        public Item CreateItem(Item item)
        {
            db.Items.Add(item);
            db.SaveChanges();

            return item;
        }

        /// <summary>
        /// Item to be updated with new data
        /// </summary>
        /// <param name="item">Item inside of game</param>
        public void UpdateItem(Item item)
        {
            db.Items.Attach(item);
            var entry = db.Entry(item);

            entry.State = EntityState.Modified;

            db.SaveChanges();
        }

        /// <summary>
        /// Associates an item with a campaign session
        /// </summary>
        /// <param name="itemId">Integer id of the item to be associated</param>
        /// <param name="sessionId">Integer id of session id for item to be associated to</param>
        public void AddItemToSession(int itemId, int sessionId)
        {
            Session session = (from sessions in db.Sessions
                         where sessions.Id == sessionId
                         select sessions).FirstOrDefault();

            Item item = GetItemById(itemId);

            session.Items.Add(item);
            db.SaveChanges();
        }

        /// <summary>
        /// Gets a single item from the database
        /// </summary>
        /// <param name="itemId">Id of item in the database</param>
        /// <returns>Get the item you want!  Swish swish!</returns>
        public Item GetItem(int itemId)
        {
            return GetItemById(itemId);
        }

        public List<Item> GetAllItemsInSession(int sessionId)
        {
            var list = from session in db.Sessions
                       where session.Id == sessionId
                       select session.Items;

            return list.FirstOrDefault().ToList();
        }

        /// <summary>
        /// Helper method for fetching items by integer id
        /// </summary>
        /// <param name="id">Interger representing the id of a game item</param>
        /// <returns>Item associated with integer</returns>
        private Item GetItemById(int id)
        {
            return (from items in db.Items
                    where items.Id == id
                    select items).FirstOrDefault();
        }
    }
}
