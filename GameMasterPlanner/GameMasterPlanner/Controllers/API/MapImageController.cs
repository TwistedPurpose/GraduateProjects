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
        private int sideLength = 256;

        public MapImageController()
        {
            mapRepository = new MapRepository();
        }

        public HttpResponseMessage Get(int id)
        {
            Map m = mapRepository.GetMap(id);

            HttpResponseMessage result = new HttpResponseMessage(HttpStatusCode.OK);
            result.Content = new ByteArrayContent(m.Image);
            result.Content.Headers.ContentType = new MediaTypeHeaderValue(m.ImageType);
            return result;
        }

        public HttpResponseMessage Get(int id, int zoom, int x, int y)
        {
            // Add logic for Zoom

            Map m = mapRepository.GetMap(id);

            Bitmap map;
            using (var ms = new MemoryStream(m.Image))
            {
                Bitmap tile;
                map = new Bitmap(ms);

                int numTilesX = (int)Math.Floor(((double)map.Width) / ((double)sideLength));

                int numTilesY = (int)Math.Floor(((double)map.Height) / ((double)sideLength));

                if ((x < 0 || y < 0 || x > numTilesX || y > numTilesY) )
                {
                    tile = CreateBlackTile();
                }
                else
                {
                    Point p = new Point(Math.Abs((x % (numTilesX + 1)) * sideLength), Math.Abs((y % (numTilesY + 1)) * sideLength));
                    Size tileSize;

                    if ((numTilesX == x || numTilesY == y))
                    {
                        int xVariableSize = sideLength;
                        int yVariableSize = sideLength;

                        if (numTilesX == x)
                        {
                            xVariableSize = map.Width % (sideLength);
                        }

                        if (numTilesY == y)
                        {
                            yVariableSize = map.Height % (sideLength);
                        }

                        if(yVariableSize == 0 || xVariableSize == 0)
                        {
                            tile = CreateBlackTile();
                        } else
                        {
                            tileSize = new Size(xVariableSize, yVariableSize);
                            Rectangle rectangleTile = new Rectangle(p, tileSize);

                            tile = new Bitmap(sideLength, sideLength);

                            Bitmap smallTile = map.Clone(rectangleTile, map.PixelFormat);

                            using (Graphics gfx = Graphics.FromImage(tile))
                            {
                                smallTile.MakeTransparent();

                                using (SolidBrush brush = new SolidBrush(Color.FromArgb(0, 0, 0)))
                                {
                                    gfx.FillRectangle(brush, 0, 0, sideLength, sideLength);
                                }

                                gfx.DrawImage(smallTile, 0, 0);
                            }

                        }
                    } 
                    else
                    {
                        tileSize = new Size(sideLength, sideLength);

                        Rectangle rectangleTile = new Rectangle(p, tileSize);

                        tile = map.Clone(rectangleTile, map.PixelFormat);
                    }

                }

                ImageConverter converter = new ImageConverter();
                byte[] returnImage = (byte[])converter.ConvertTo(tile, typeof(byte[]));

                HttpResponseMessage result = new HttpResponseMessage(HttpStatusCode.OK);
                result.Content = new ByteArrayContent(returnImage);
                result.Content.Headers.ContentType = new MediaTypeHeaderValue(m.ImageType);
                return result;
            }
        }

        private Bitmap CreateBlackTile()
        {
            Bitmap tile = new Bitmap(sideLength, sideLength);
            using (Graphics gfx = Graphics.FromImage(tile))
            {

                using (SolidBrush brush = new SolidBrush(Color.FromArgb(0, 0, 0)))
                {
                    gfx.FillRectangle(brush, 0, 0, sideLength, sideLength);
                }
            }
            return tile;
        }
    }
}
