﻿using DataAccess.EntityFramework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess.Repositories
{
    public class BaseRepository
    {
        protected GameMasterPlannerDBEntities db;

        public BaseRepository()
        {
            db = new GameMasterPlannerDBEntities();
        }
    }
}