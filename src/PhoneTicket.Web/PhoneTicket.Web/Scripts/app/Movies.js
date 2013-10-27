(function () {
    function createMovieController(textBox,imageBox) {
        
        function updateImageSource() {
            var urlSrc = textBox.val();
            imageBox.attr("src", urlSrc);
            imageBox.css("display", "block");
        }

        return {
            updateImage: updateImageSource
        };
    }

    $(document).ready(function () {
        var textBox = $("#ImageUrl");
        var imageBox = $("#MovieImg");
        var movieController = createMovieController(textBox,imageBox);
        
        $("#refreshImage").on("click", movieController.updateImage);
    });
}());