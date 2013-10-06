(function() {
    $(document).ready(function () {
        var datePickerConfig = {
            changeMonth: true,
            changeYear: true,
            dateFormat: 'yyyy-mm-dd',
            gotoCurrent: true
        };

        $("#beginDate").datepicker(datePickerConfig);
        $("#endDate").datepicker(datePickerConfig);
        $("#price").spinner({min: 0, numberFormat: "C"});
    });
})()