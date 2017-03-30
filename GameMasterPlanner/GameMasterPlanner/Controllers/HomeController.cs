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
            ViewBag.CampaignId = campaignId;
            return View("Hub", new { campaignId });
        }
    }
}