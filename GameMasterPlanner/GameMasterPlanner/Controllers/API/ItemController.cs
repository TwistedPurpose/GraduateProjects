using DataAccess.EntityFramework;
using DataAccess.Repositories;
using GameMasterPlanner.Helper;
using GameMasterPlanner.Models;
using System;
using System.Linq;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace GameMasterPlanner.Controllers.API
{
    public class ItemController : ApiController
    {
        //Data repository for items, controls database interactions
        private ItemRepository itemRepository;

        /// <summary>
        /// Constructor for item controller, setups up repostory for db interactions
        /// </summary>
        public ItemController()
        {
            itemRepository = new ItemRepository();
        }

        /// <summary>
        /// Updates or creates a new item, will associate with a session if a
        /// SessionId is included in the VM
        /// </summary>
        /// <param name="itemVM">View Model containing data for a character item</param>
        /// <returns></returns>
        public HttpResponseMessage Post(ItemViewModel itemVM)
        {
            Item dbItem = ModelConverter.ToDbItemModel(itemVM);

            if (dbItem.Id > 0)
            {
                itemRepository.UpdateItem(dbItem);

            } else
            {
                dbItem = itemRepository.CreateItem(dbItem);
            }

            if(itemVM.SessionId > 0)
            {
                itemRepository.AddItemToSession(dbItem.Id, itemVM.SessionId);
            }

            //Another hack, fix disappearing session lists from item library after grad school
            dbItem = itemRepository.GetItem(dbItem.Id);

            return Request.CreateResponse(HttpStatusCode.OK, ModelConverter.ToItemViewModel(dbItem));
        }

        /// <summary>
        /// Get a single item, I'm not sure when I'll use this though
        /// </summary>
        /// <param name="itemId"></param>
        /// <returns></returns>
        public HttpResponseMessage Get(int itemId)
        {
            Item dbItem = itemRepository.GetItem(itemId);

            return Request.CreateResponse(HttpStatusCode.OK, ModelConverter.ToItemViewModel(dbItem));
        }

        /// <summary>
        /// Returns all items that appear in the session
        /// </summary>
        /// <param name="sessionId">Integer id of session to get all items from</param>
        /// <returns>List of all items in the session wrapped in HTTP Response</returns>
        public HttpResponseMessage GetAllInSession(int sessionId)
        {
            List<ItemViewModel> itemList = itemRepository.GetAllItemsInSession(sessionId)
                .Select(x => ModelConverter.ToItemViewModel(x)).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, itemList);
        }

        /// <summary>
        /// Gets all items in a campaign
        /// </summary>
        /// <param name="campaignId">Integer id of campaign to get all items</param>
        /// <returns>HTTP Response wrapped list of items in campaign</returns>
        public HttpResponseMessage GetAll(int campaignId)
        {
            List<ItemViewModel> itemList = itemRepository.GetAll(campaignId)
                .Select(x => ModelConverter.ToItemViewModel(x)).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, itemList);
        }


    }
}