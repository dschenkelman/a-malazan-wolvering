var validators = (function () {
    function handleErrorMessage(isValid, element, message, global) {
        var $errorSpan;
        var $validationSummaryList;
        if (!isValid) {
            if (element) {
                element.addClass("input-validation-error");
            }

            if (global) {
                $validationSummaryList = $("#validationSummary>ul");
                $validationSummaryList.append("<li>" + message + "</li>");
                $validationSummaryList.show();
            } else {
                $errorSpan = $("#" + element.attr("id") + "Error");
                $errorSpan.text(message);
                $errorSpan.show();
            }
        } else {
           
        }
    }

    function validateRequired(name, element) {
        var value = element.val();

        var isValid = value !== 'undefined' && value !== '';
        
        handleErrorMessage(isValid, element, "El campo '" + name + "' es obligatorio", false);

        return isValid;
    }

    function validateHour(element) {
        var value = element.val();
        var isValid = value >= 0 && value <= 23;
       
        handleErrorMessage(isValid, element, "La hora '" + value + "' no es válida. Ingrese un valor entre 0 y 23", false);

        return isValid;
    }
    
    function validateMinutes(element) {
        var value = element.val();
        var isValid = value >= 0 && value <= 60;

        handleErrorMessage(isValid, element, "El minuto '" + value + "' no es válido. Ingrese un valor entre 0 y 60", false);

        return isValid;
    }

    function validateDays(values) {
        var isValid = values.filter(function(index, item) { return item.isChecked; }).length > 0;

        handleErrorMessage(isValid, null, "Al menos un día debe estar seleccionado.", true);

        return isValid;
    }
    
    function validatePrice(element) {
        var value = element.val();
        var isValid = value >= 0;
        
        handleErrorMessage(isValid, element, "El precio '" + value + "' no es válido. Ingrese un valor mayor o igual a 0", false);

        return isValid;
    }
    
    function validateDates(beginElement, endElement) {
        var beginValue = beginElement.val();
        var endValue = endElement.val();

        var beginDate = new Date(beginValue),
            endDate = new Date(endValue);

        var isValid = endDate >= beginDate;
        
        handleErrorMessage(isValid, beginElement, "La fecha de comienzo no puede ser posterior a la fecha de fin", false);

        return isValid;
    }

    return {
        validatePrice: validatePrice,
        validateHour: validateHour,
        validateMinutes: validateMinutes,
        validateDates: validateDates,
        validateRequired: validateRequired,
        validateDays: validateDays
    };
}());