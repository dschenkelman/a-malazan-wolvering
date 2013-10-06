(function() {
    $(document).ready(function() {
        var datePickerConfig = {
            changeMonth: true,
            changeYear: true,
            dateFormat: 'yyyy-mm-dd',
            gotoCurrent: true
        };

        var hourSpinnerConfig = { min: 0, max: 23, numberFormat: "n" };

        var minutesSpinnerConfig = { min: 0, max: 60, numberFormat: "n", page: 10 };

        var timeAndRoomTemplate = "<div class=\"row\">\
                            <div class =\"col-md-6\">\
                                <label>Hora:</label>\
                                <input class=\"hour spinner\" type=\"text\"/>\
                                <span>:</span>\
                                <input class=\"minutes spinner\" type=\"text\"/>\
                            </div>\
                            <div class=\"col-md-6\">\
                                <label>Sala:</label>\
                                <select class=\"rooms\">\
                                    <option>Sala 1</option>\
                                    <option>Sala 2</option>\
                                </select>\
                            </div>\
                        </div>";

        var timesPanel;

        $("#beginDate").datepicker(datePickerConfig);
        $("#endDate").datepicker(datePickerConfig);
        $("#price").spinner({ min: 0, numberFormat: "C" });
        $(".hour").spinner(hourSpinnerConfig);
        $(".minutes").spinner(minutesSpinnerConfig);

        timesPanel = $("#shows");

        $("#add").click(function() {
            timesPanel.append(timeAndRoomTemplate);
            timesPanel.find(".hour:last").spinner(hourSpinnerConfig);
            timesPanel.find(".minutes:last").spinner(minutesSpinnerConfig);
        });
        
        $("#remove").click(function () {
            timesPanel.find("div.row:last").remove();
        });
    });
})();