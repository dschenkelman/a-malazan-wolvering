namespace PhoneTicket.Web.ViewModels.Api
{
    using PhoneTicket.Web.Models;
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    public class ArmChairViewModel
    {
        public int Row { get; set; }

        public int Column { get; set; }

        public static ArmChairViewModel FromOccupiedSeat(OccupiedSeat seat)
        {
            return new ArmChairViewModel { Row = seat.Row, Column = seat.Column };
        }
    }
}