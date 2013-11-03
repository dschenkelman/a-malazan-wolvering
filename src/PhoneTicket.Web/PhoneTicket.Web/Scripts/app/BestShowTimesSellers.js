(function () {
    var config = {
        datePicker: { changeMonth: true, changeYear: true, dateFormat: 'yy-mm-dd', gotoCurrent: true }
    };

    function createView() {
        var $form = $("#search");

        $("#From").datepicker(config.datePicker);
        $("#To").datepicker(config.datePicker);
        $("#pdfView").click(function () {
            var originalValue = $form.prop("action");

            var newValue = originalValue.indexOf("Pdf") !== -1 ?
                originalValue :
                originalValue.replace("BestShowTimesSellers", "BestShowTimesSellersPdf");

            $form.prop("action", newValue);

            $form.submit();
        });
        
        $("#generate").click(function () {
            var originalValue = $form.prop("action");

            var newValue = originalValue.indexOf("Pdf") !== -1 ?
                originalValue.replace("Pdf", "") :
                originalValue;

            $form.prop("action", newValue);
        });
    }

    $(document).ready(function () {
        createView(document);
    });
})();