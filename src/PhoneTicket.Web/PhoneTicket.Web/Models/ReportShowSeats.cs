namespace PhoneTicket.Web.Models
{
    using System;

    public class ReportShowSeats
    {
        private readonly ReportSeatState[][] seats;

        public ReportShowSeats()
        {
            this.Free = 0;
            this.Reserved = 0;
            this.Purchased = 0;
            this.seats = new ReportSeatState[17][];

            for (int i = 0; i < 17; i++)
            {
                this.seats[i] = new ReportSeatState[22];

                for (int j = 0; j < 22; j++)
                {
                    this.seats[i][j] = ReportSeatState.NoSeat;
                }
            }
        }

        public ReportSeatState[][] Seats
        {
            get
            {
                return this.seats;
            }
        }

        public int Free { get; private set; }

        public int Reserved { get; private set; }

        public int Purchased { get; private set; }

        public void MarkFree(int row, int column)
        {
            this.MarkSeat(row, column, ReportSeatState.Free);
        }

        public void MarkPurchased(int row, int column)
        {
            this.MarkSeat(row, column, ReportSeatState.Purchased);
        }

        public void MarkReserved(int row, int column)
        {
            this.MarkSeat(row, column, ReportSeatState.Reserved);
        }

        private void MarkSeat(int row, int column, ReportSeatState state)
        {
            if (row < 1 || row > 17)
            {
                throw new ArgumentOutOfRangeException("row");
            }

            if (column < 1 || column > 22)
            {
                throw new ArgumentOutOfRangeException("row");
            }

            if (this.seats[row - 1][column - 1] == ReportSeatState.NoSeat)
            {
                switch (state)
                {
                    case ReportSeatState.NoSeat:
                        break;
                    case ReportSeatState.Reserved:
                        this.Reserved++;
                        break;
                    case ReportSeatState.Purchased:
                        this.Purchased++;
                        break;
                    case ReportSeatState.Free:
                        this.Free++;
                        break;
                    default:
                        throw new ArgumentOutOfRangeException("state");
                }
            }
            else if (this.seats[row - 1][column - 1] == ReportSeatState.Free)
            {
                switch (state)
                {
                    case ReportSeatState.NoSeat:
                        break;
                    case ReportSeatState.Reserved:
                        this.Reserved++;
                        this.Free--;
                        break;
                    case ReportSeatState.Purchased:
                        this.Purchased++;
                        this.Free--;
                        break;
                    case ReportSeatState.Free:
                        break;
                    default:
                        throw new ArgumentOutOfRangeException("state");
                }
            }

            this.seats[row - 1][column - 1] = state;
        }
    }

    public enum ReportSeatState
    {
        NoSeat,
        Reserved,
        Purchased,
        Free,
    }
}