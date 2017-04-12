using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess.Models
{
    public class SessionViewModel
    {
        public int Id { get; set; }
        public string Title { get; set; }
        public int SessionNumber { get; set; }
        public string Notes { get; set; }
        public int? BaseMapId { get; internal set; }
    }
}
