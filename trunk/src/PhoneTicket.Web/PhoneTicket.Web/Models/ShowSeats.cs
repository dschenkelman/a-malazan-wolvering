namespace PhoneTicket.Web.Models
{
    using System;

    public class ShowSeats
    {
        private readonly SeatState[][] seats;

        public ShowSeats()
        {
            this.Capacity = 0;
            this.seats = new SeatState[17][];

            for (int i = 0; i < 17; i++)
            {
                this.seats[i] = new SeatState[22];

                for (int j = 0; j < 22; j++)
                {
                    this.seats[i][j] = SeatState.NoSeat;
                }
            }
        }

        public SeatState[][] Seats
        {
            get
            {
                return this.seats;
            }
        }

        public int Capacity { get; private set; }

        public void MarkFree(int row, int column)
        {
            this.MarkSeat(row, column, SeatState.Free);
        }

        public void MarkTaken(int row, int column)
        {
            this.MarkSeat(row, column, SeatState.Taken);
        }

        public ReportShowSeats ToReport()
        {
            var reportShowSeats = new ReportShowSeats();

            for (int i = 0; i < 17; i++)
            {
                for (int j = 0; j < 22; j++)
                {
                    if (this.seats[i][j] == SeatState.Free)
                    {
                        reportShowSeats.MarkFree(i + 1, j + 1);
                    }
                }
            }

            return reportShowSeats;
        }

        private void MarkSeat(int row, int column, SeatState state)
        {
            if (row < 1 || row > 17)
            {
                throw new ArgumentOutOfRangeException("row");
            }

            if (column < 1 || column > 22)
            {
                throw new ArgumentOutOfRangeException("row");
            }

            if (this.seats[row - 1][column - 1] == SeatState.NoSeat)
            {
                this.Capacity++;
            }

            this.seats[row - 1][column - 1] = state;
        }
    }

    public enum SeatState
    {
        NoSeat,
        Taken,
        Free,
    }
}