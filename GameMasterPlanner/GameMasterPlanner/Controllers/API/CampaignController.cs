using DataAccess.EntityFramework;
using DataAccess.Models;
using DataAccess.Repositories;
using GameMasterPlanner.Helper;
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
        private SessionRepository sessionRepository;

        public CampaignController()
        {
            sessionRepository = new SessionRepository();
        }

        public HttpResponseMessage Get()
        {
            var repro = new CampaignRepository();
            var list = repro.GetCampaignList();

            List<CampaignViewModel> returnList = list.Select(x => ModelConverter.ToCampaignViewModel(x)).ToList();

            return Request.CreateResponse(HttpStatusCode.OK, returnList);
        }

        public HttpResponseMessage Post(CampaignViewModel campaignVM)
        {
            var repro = new CampaignRepository();
            Campaign campaignDb = ModelConverter.ToDBCampaign(campaignVM);

            if (campaignVM.Id > 0)
            {
                repro.UpdateCampaign(campaignDb);
            }
            else
            {
                campaignDb = repro.CreateCampaign(campaignDb);

                Session firstSession = new Session()
                {
                    CampaignId = campaignDb.Id
                };

                sessionRepository.CreateSession(firstSession);
            }

            //var list = repro.GetCampaignList();
            return Get();
        }
    }
}
