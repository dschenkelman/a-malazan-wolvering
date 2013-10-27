(function () {
    var config = {
        datePicker: { changeMonth: true, changeYear: true, dateFormat: 'yy-mm-dd', gotoCurrent: true }
    };

    function createView() {

        $("#StartDate").datepicker(config.datePicker);
        $("#EndDate").datepicker(config.datePicker);
    }

    $(document).ready(function () {
        createView(document);
    });
})();