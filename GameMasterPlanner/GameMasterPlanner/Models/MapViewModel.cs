﻿namespace GameMasterPlanner.Models
{
    public class MapViewModel
    {
        public int Id { get; set; }
        public int ParentMapId { get; set; }
        public string Name { get; set; }
        public byte[] Image { get; set; }
        public string ImageType { get; set; }
        public int SessionId { get; set; }
    }
}