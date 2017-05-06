using System.Web;
using System.Web.Optimization;

namespace GameMasterPlanner.App_Start
{
    public class BundleConfig
    {
        // For more information on bundling, visit http://go.microsoft.com/fwlink/?LinkId=301862
        public static void RegisterBundles(BundleCollection bundles)
        {
            bundles.Add(new ScriptBundle("~/bundles/jquery").Include(
                        "~/Scripts/jquery-{version}.js"));

            bundles.Add(new ScriptBundle("~/bundles/jqueryval").Include(
                        "~/Scripts/jquery.validate*"));

            // Use the development version of Modernizr to develop with and learn from. Then, when you're
            // ready for production, use the build tool at http://modernizr.com to pick only the tests you need.
            bundles.Add(new ScriptBundle("~/bundles/modernizr").Include(
                        "~/Scripts/modernizr-*"));

            bundles.Add(new ScriptBundle("~/bundles/bootstrap").Include(
                   "~/Scripts/bootstrap.js",
                 "~/Scripts/respond.js"));

            bundles.Add(new ScriptBundle("~/bundles/knockout").Include(
                "~/Scripts/knockout-3.4.1.js"));

            bundles.Add(new ScriptBundle("~/bundles/common").Include(
                "~/Scripts/Common/Helper.js"));

            bundles.Add(new ScriptBundle("~/bundles/campaign").Include(
                "~/Scripts/ViewModels/CampaignViewModel.js",
                "~/Scripts/ViewModels/CampaignListViewModel.js"));

            bundles.Add(new ScriptBundle("~/bundles/sessionManagement").Include(
                "~/Scripts/ViewModels/CampaignViewModel.js",
                "~/Scripts/ViewModels/SessionViewModel.js",
                "~/Scripts/ViewModels/SessionManagmentViewModel.js"));

            bundles.Add(new ScriptBundle("~/bundles/characterLibrary").Include(
                "~/Scripts/ViewModels/CampaignViewModel.js",
                "~/Scripts/ViewModels/CharacterViewModel.js",
                "~/Scripts/ViewModels/CharacterLibraryViewModel.js"));

            bundles.Add(new ScriptBundle("~/bundles/itemLibrary").Include(
                "~/Scripts/ViewModels/CampaignViewModel.js",
                "~/Scripts/ViewModels/ItemViewModel.js",
                "~/Scripts/ViewModels/ItemLibraryViewModel.js"));

            bundles.Add(new ScriptBundle("~/bundles/character").Include(
                "~/Scripts/ViewModels/CharacterViewModel.js"));

            bundles.Add(new ScriptBundle("~/bundles/item").Include(
                "~/Scripts/ViewModels/ItemViewModel.js"));

            bundles.Add(new ScriptBundle("~/bundles/maps").Include(
                "~/Scripts/gmaps.js"));

            bundles.Add(new ScriptBundle("~/bundles/hub").Include(
                "~/Scripts/gmaps.js",
                "~/Scripts/ViewModels/HubViewModel.js",
                "~/Scripts/ViewModels/SessionViewModel.js"));


            bundles.Add(new StyleBundle("~/Content/css").Include(
                      "~/Content/bootstrap.css",
                      "~/Content/site.css",
                      "~/Content/hub.css"));


        }
    }
}