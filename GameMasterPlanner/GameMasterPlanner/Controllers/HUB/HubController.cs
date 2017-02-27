using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GameMasterPlanner.Controllers.HUB
{
    public class HubController : Controller
    {
        // GET: Hub
        //public ActionResult Index()
        //{
        //    return View();
        //}
        public ActionResult Index(int id)
        {
            return View();
        }


    }
}