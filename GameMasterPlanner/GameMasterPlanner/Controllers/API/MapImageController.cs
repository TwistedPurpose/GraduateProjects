using Newtonsoft.Json;
using Newtonsoft.Json.Bson;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace GameMasterPlanner.Controllers.API
{
    public class MapImageController : ApiController
    {
        public HttpResponseMessage Post(byte[] image)
        {
            MemoryStream ms = new MemoryStream(image);
            using (BsonReader reader = new BsonReader(ms))
            {
                JsonSerializer serializer = new JsonSerializer();

                MapImage imageFromClient = serializer.Deserialize<MapImage>(reader);
            }
            return Request.CreateResponse(HttpStatusCode.OK);
        }

        public class MapImage
        {
            public int Id { get; set; }
            public byte[] Image { get; set; }
        }
    }
}
