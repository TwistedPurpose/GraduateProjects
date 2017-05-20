using DataAccess.EntityFramework;
using DataAccess.Repositories;
using Newtonsoft.Json;
using Newtonsoft.Json.Bson;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
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

        public HttpResponseMessage Get(int id, int zoom, int x, int y)
        {
            Map m = mapRepository.GetMap(id);

            Bitmap map;
            using (var ms = new MemoryStream(m.Image))
            {
                Bitmap tile;
                map = new Bitmap(ms);

                int sideLength = 256;

                Size tileSize = new Size(sideLength, sideLength);

                int numTilesX = (int)Math.Floor(((double)map.Width) / ((double)sideLength));

                int numTilesY = (int)Math.Floor(((double)map.Height) / ((double)sideLength));

                if (numTilesX == x || numTilesY == y)
                {
                    tile = null;
                } else if (x < 0  ||  y < 0 || x > numTilesX || y > numTilesY)
                {
                    tile = new Bitmap(sideLength, sideLength);
                    using (Graphics gfx = Graphics.FromImage(tile))
                    {

                        using (SolidBrush brush = new SolidBrush(Color.FromArgb(0, 0, 0)))
                        {
                            gfx.FillRectangle(brush, 0, 0, sideLength, sideLength);
                        }
                    }
                } else
                {
                    Point p = new Point(Math.Abs((x % numTilesX) * sideLength), Math.Abs((y % numTilesY) * sideLength));

                    Rectangle rectangleTile = new Rectangle(p, tileSize);

                    tile = map.Clone(rectangleTile, map.PixelFormat);
                }

                ImageConverter converter = new ImageConverter();
                byte[] returnImage = (byte[])converter.ConvertTo(tile, typeof(byte[]));



                HttpResponseMessage result = new HttpResponseMessage(HttpStatusCode.OK);
                result.Content = new ByteArrayContent(returnImage);
                result.Content.Headers.ContentType = new MediaTypeHeaderValue("image/png");
                return result;
            }
            //Bitmap.Clone();

        }
    }
}
