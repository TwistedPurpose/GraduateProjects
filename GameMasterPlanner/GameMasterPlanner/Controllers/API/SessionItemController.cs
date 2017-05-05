using DataAccess.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace GameMasterPlanner.Controllers.API
{
    public class SessionItemController : ApiController
    {

        private ItemRepository itemRepository;

        public SessionItemController()
        {
            itemRepository = new ItemRepository();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="sessionItems"></param>
        /// <returns></returns>
        public HttpResponseMessage Post(SessionWithItems sessionItems)
        {
            if (sessionItems != null)
            {
                if(sessionItems.itemIds != null)
                {
                    itemRepository.AssociateItemsInSession(sessionItems.sessionId, sessionItems.itemIds.ToList());
                } else
                {
                    itemRepository.AssociateItemsInSession(sessionItems.sessionId, new List<int>());
                }
                
            }
            

            return Request.CreateResponse(HttpStatusCode.OK);
        }

        public class SessionWithItems
        {
            public int sessionId { get; set; }
            public int[] itemIds { get; set; }
        }
    }
}
