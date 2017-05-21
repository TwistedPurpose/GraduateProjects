using DataAccess.EntityFramework;
using DataAccess.Repositories;
using GameMasterPlanner.Helper;
using GameMasterPlanner.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace GameMasterPlanner.Controllers.API
{
    public class MapController : ApiController
    {
        private MapRepository mapRepository;

        public MapController()
        {
            mapRepository = new MapRepository();
        }

        // POST api/<controller>
        public HttpResponseMessage Post(MapViewModel mapVm)
        {

            Map map = ModelConverter.ToDbMapModel(mapVm);

            // If the ID is not zero, then edit the map
            if (map.Id > 0)
            {
                mapRepository.UpdateMap(map);
            }
            else // If the map has a zero id, then create a new map
            {
                map = mapRepository.CreateMap(map);
            }

            if (mapVm.SessionId > 0)
            {
                mapRepository.AddMapToSession(map.Id, mapVm.SessionId);
            }

            mapVm = ModelConverter.ToMapViewModel(mapRepository.GetMap(map.Id));

            return Request.CreateResponse(HttpStatusCode.OK, mapVm);
        }

        public HttpResponseMessage GetForSession(int sessionId)
        {
            return Request.CreateResponse(HttpStatusCode.OK, ModelConverter.ToMapViewModel(mapRepository.GetMapInSession(sessionId)));
        }

        public HttpResponseMessage Get(int id)
        {
            List<MapViewModel> mapList = mapRepository.GetMapList(id).Select(x => { Map m  = x; m.Image = null; return ModelConverter.ToMapViewModel(m); }).ToList();
            return Request.CreateResponse(HttpStatusCode.OK, mapList);
        }
    }
}