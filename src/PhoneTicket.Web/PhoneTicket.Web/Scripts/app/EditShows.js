(function () {
    var config = {
        datePicker: { changeMonth: true, changeYear: true, dateFormat: 'yy-mm-dd', gotoCurrent: true }
    };

    function createView() {
        
        var $date = $("#Date");
        $date.datepicker(config.datePicker);

    }

    $(document).ready(function () {
        createView(document);
    });
})();