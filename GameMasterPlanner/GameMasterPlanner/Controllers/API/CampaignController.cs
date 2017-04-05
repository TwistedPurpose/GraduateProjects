using DataAccess.EntityFramework;
using DataAccess.Models;
using DataAccess.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace GameMasterPlanner.Controllers.API
{
    public class CampaignController : ApiController
    {

        public HttpResponseMessage Get()
        {
            var repro = new CampaignRepository();
            var list = repro.GetCampaignList();

            List<CampaignViewModel> returnList = list.Select(x => new CampaignViewModel() { Id = x.Id,
                History = x.History == null ? "" : x.History.Description,
                Name = x.Name }).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, returnList);
        }

        public HttpResponseMessage Post(CampaignViewModel newCampaign)
        {
            var repro = new CampaignRepository();

            Campaign campaignDb = new Campaign()
            {
                Name = newCampaign.Name
            };

            if (!String.IsNullOrWhiteSpace(newCampaign.History))
            {

                var campaignHistory = new History()
                {
                    Description = newCampaign.History
                };
                campaignDb.History = campaignHistory;

                repro.CreateHistory(campaignHistory);
            }

            repro.CreateCampaign(campaignDb);

            var list = repro.GetCampaignList();
            return Get();
        }
    }
}
