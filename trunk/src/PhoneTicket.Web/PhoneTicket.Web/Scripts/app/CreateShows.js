(function () {
    var config = {
        hourSpinner: { min: 0, max: 23, numberFormat: "n" },
        minutesSpinner: { min: 0, max: 60, numberFormat: "n", page: 10 },
        datePicker: { changeMonth: true, changeYear: true, dateFormat: 'yy-mm-dd', gotoCurrent: true },
        priceSpinner: { min: 0, numberFormat: "C" }
    };

    function createView() {
        var showItems = 1;

        var timeAndRoomTemplate = "<div class=\"row\">\
                                    <div class=\"col-md-6\">\
                                        <div class=\"row\">\
                                            <div class=\"col-md-12\">\
                                                <label>Hora:</label>\
                                                <input id=\"hour{showItems}\" class=\"hour spinner\" type=\"text\" />\
                                                <span>:</span>\
                                                <input id=\"minutes{showItems}\" class=\"minutes spinner\" type=\"text\" />\
                                            </div>\
                                        </div>\
                                        <div class=\"row\">\
                                            <div class=\"col-md-12\">\
                                                <span id=\"hour{showItems}Error\" class=\"field-validation-error\" style=\"display: none\"></span>\
                                                <span id=\"minutes{showItems}Error\" class=\"field-validation-error\" style=\"display: none\"></span>\
                                            </div>\
                                        </div>\
                                    </div>\
                                    <div class=\"col-md-6\">\
                                        <label>Sala:</label>\
                                        <select class=\"rooms\"></select>\
                                    </div>\
                                </div>";

        var $timesPanel = $("#shows");
        var $complexCombo = $("#complex");
        var $moviesCombo = $("#movie");
        var $daysCheckboxes = $("#days input");
        var roomsPerComplex = {};
        var selectedComplex = 0;

        var $beginDate = $("#beginDate");
        var $endDate = $("#endDate");
        var $price = $("#price");
        $beginDate.datepicker(config.datePicker);
        $endDate.datepicker(config.datePicker);
        $price.spinner(config.priceSpinner);
        $(".hour").spinner(config.hourSpinner);
        $(".minutes").spinner(config.minutesSpinner);

        var loadCombo = function(items, displayProperty, $comboBox, itemAction) {
            $comboBox.empty();
            for (var i = 0; i < items.length; i++) {
                $comboBox.append('<option value="' + items[i].id + '">' + items[i][displayProperty] + '</option>');
                 
                if (itemAction) {
                    itemAction(i, items[i]);
                }
            }
        };
        
        // load complexes
        $.get("/api/complexes", function (data) { loadCombo(data, "name", $complexCombo,
                function (j, complex) {
                    if (j === 0) {
                        selectedComplex = complex.id;
                    }

                    $.get("/api/complexes/" + complex.id + "/rooms", function (roomsData) {
                        roomsPerComplex[complex.id] = roomsData;

                        if (complex.id === selectedComplex) {
                            $(".rooms").each(function (index, combo) {
                                loadCombo(roomsData, "name", $(combo));
                            });
                        }
                    }, "json");
                }
            ); }, "json");
        
        // load movies
        $.get("/api/movies", function (data) { loadCombo(data, "title", $moviesCombo); }, "json");

        var validate = function (data) {
            var result = [];
            var i, current;

            $("input.input-validation-error").removeClass("input-validation-error");
            $("span.field-validation-error").hide();

            result.push(validators.validateRequired('precio', data.price));
            result.push(validators.validatePrice(data.price));
            result.push(validators.validateRequired('Fecha comienzo', data.beginDate));
            result.push(validators.validateRequired('Fecha fin', data.endDate));
            result.push(validators.validateDates(data.beginDate, data.endDate));

            result.push(validators.validateDays(data.days));

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
            var timesAndRooms = $timesPanel.find(".row").map(function () {
                var element = $(this);
                return {
                    hour: element.find(".hour"),
                    minutes: element.find(".minutes"),
                    room: element.find(".rooms")
                };
            });

            var days = $daysCheckboxes.map(function() {
                var element = $(this);

                return {
                    isChecked: element.is(":checked"),
                    day: element.attr("id")
                };
            });

            var data = {
                price: $price,
                beginDate: $beginDate,
                endDate: $endDate,
                timesAndRooms: timesAndRooms,
                days: days
            };

            var result = validate(data);

            var errors = result.filter(function (item) {
                return !item.isValid;
            });

            if (errors.length === 0) {
                // post data
            }
        });

        $("#add").click(function () {
            showItems++;
            $timesPanel.append(timeAndRoomTemplate.replace(/{showItems}/g, showItems));
            $timesPanel.find(".hour:last").spinner(config.hourSpinner);
            $timesPanel.find(".minutes:last").spinner(config.minutesSpinner);
            loadCombo(roomsPerComplex[selectedComplex], "name", $timesPanel.find(".rooms:last"));
        });

        $("#remove").click(function () {
            $timesPanel.find("div.row:last").remove();
            showItems--;
        });

        $complexCombo.change(function() {
            selectedComplex = Number($complexCombo.val());
            $(".rooms").each(function (index, combo) {
                loadCombo(roomsPerComplex[selectedComplex], "name", $(combo));
            });
        });
    }

    $(document).ready(function () {
        createView(document);
    });
})();