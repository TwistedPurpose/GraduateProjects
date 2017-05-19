using DataAccess.EntityFramework;
using DataAccess.Repositories;
using Newtonsoft.Json;
using Newtonsoft.Json.Bson;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Web.Http;

namespace GameMasterPlanner.Controllers.API
{
    public class MapImageController : ApiController
    {
        private MapRepository mapRepository;

        public MapImageController()
        {
            mapRepository = new MapRepository();
        }

        public HttpResponseMessage Get(int id)
        {
            Map m = mapRepository.GetMap(id);


            HttpResponseMessage result = new HttpResponseMessage(HttpStatusCode.OK);
            result.Content = new ByteArrayContent(m.Image);
            result.Content.Headers.ContentType = new MediaTypeHeaderValue("image/png");
            return result;

            //Bitmap.Clone();

        }
    }
}
