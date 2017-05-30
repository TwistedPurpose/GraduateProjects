using DataAccess.EntityFramework;
using DataAccess.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GameMasterPlanner.Controllers
{
    public class HomeController : Controller
    {
        // GET: Home
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Hub(int campaignId)
        {
            var repro = new CampaignRepository();
            ViewBag.CampaignId = campaignId;
            Campaign campaign = repro.GetCampaign(campaignId);
            ViewBag.CampaignTitle = campaign.Name;
            return View("Hub", new { campaignId });
        }

        public ActionResult CharacterLibrary()
        {
            return View();
        }

        public ActionResult ItemLibrary()
        {
            return View();
        }

        public ActionResult SessionManagment()
        {
            return View();
        }
    }
}