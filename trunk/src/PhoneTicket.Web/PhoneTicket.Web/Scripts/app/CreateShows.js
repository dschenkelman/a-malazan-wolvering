(function () {
    var config = {
        hourSpinner: { min: 0, max: 23, numberFormat: "n" },
        minutesSpinner: { min: 0, max: 60, numberFormat: "n", page: 10 },
        datePicker: { changeMonth: true, changeYear: true, dateFormat: 'yy-mm-dd', gotoCurrent: true },
        priceSpinner: { min: 0, numberFormat: "C" }
    };

    function createView() {
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

        var validationTemplate = "<div class=\"alert alert-danger alert-dismissable\">\
                                    <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button>\
                                    <strong>Error:</strong> {message}.</div>.";

        var timesPanel = $("#shows");
        var validationPanel = $("#validation");

        var beginDate = $("#beginDate");
        var endDate = $("#endDate");
        var price = $("#price");
        beginDate.datepicker(config.datePicker);
        endDate.datepicker(config.datePicker);
        price.spinner(config.priceSpinner);
        $(".hour").spinner(config.hourSpinner);
        $(".minutes").spinner(config.minutesSpinner);

        var validate = function (data) {
            var result = [];
            var i, current;
            result.push(validators.validateRequired('precio', data.price));
            result.push(validators.validatePrice(data.price));
            result.push(validators.validateRequired('Fecha comienzo', data.beginDate));
            result.push(validators.validateRequired('Fecha fin', data.endDate));
            result.push(validators.validateDates(data.beginDate, data.endDate));

            for (i = 0; i < data.timesAndRooms.length; i++) {
                current = data.timesAndRooms[i];
                result.push(validators.validateRequired('Hora', current.hour));
                result.push(validators.validateHour(current.hour));
                result.push(validators.validateRequired('Minutos', current.minutes));
                result.push(validators.validateMinutes(current.minutes));
            }

            return result;
        };

        $("#create").click(function () {
            var timesAndRooms = timesPanel.find(".row").map(function () {
                var element = $(this);
                return {
                    hour: element.find(".hour").val(),
                    minutes: element.find(".minutes").val(),
                    room: element.find(".rooms").val()
                };
            });

            var data = {
                price: price.val(),
                beginDate: beginDate.val(),
                endDate: endDate.val(),
                timesAndRooms: timesAndRooms
            };

            var i;

            var result = validate(data);

            var errors = result.filter(function (item) {
                return !item.isValid;
            });

            validationPanel.empty();

            if (errors.length > 0) {
                // show errors
                for (i = 0; i < errors.length; i++) {
                    validationPanel.append(validationTemplate.replace("{message}", errors[i].message));
                }
            } else {
                // post data
            }
        });

        $("#add").click(function () {
            timesPanel.append(timeAndRoomTemplate);
            timesPanel.find(".hour:last").spinner(config.hourSpinner);
            timesPanel.find(".minutes:last").spinner(config.minutesSpinner);
        });

        $("#remove").click(function () {
            timesPanel.find("div.row:last").remove();
        });
    }

    $(document).ready(function () {
        createView(document);
    });
})();