namespace QRSpike.Server
{
    using System;

    class Program
    {
        static void Main(string[] args)
        {
            var guid = Guid.NewGuid();

            var bitmap = new MessagingToolkit.QRCode.Codec.QRCodeEncoder().Encode(guid.ToString());

            bitmap.Save("test.bmp");
        }
    }
}
