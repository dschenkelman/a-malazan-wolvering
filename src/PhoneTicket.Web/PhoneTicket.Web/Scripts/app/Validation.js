var validators = (function () {
    function validateRequired(name, value) {
        return {
            isValid: value !== 'undefined' && value !== '',
            message: "El campo '" + name + "' es obligatorio",
        };
    }

    function validateHour(value) {
        return {
            isValid: value >= 0 && value <= 23,
            message: "La hora '" + value + "' no es válida. Ingrese un valor entre 0 y 23"
        };
    }
    
    function validateMinutes(value) {
        return {
            isValid: value >= 0 && value <= 60,
            message: "El minuto '" + value + "' no es válido. Ingrese un valor entre 0 y 60"
        };
    }
    
    function validatePrice(value) {
        return {
            isValid: value >= 0,
            message: "El precio '" + value + "' no es válido. Ingrese un valor mayor o igual a 0"
        };
    }
    
    function validateDates(beginValue, endValue) {
        var beginDate = new Date(beginValue),
            endDate = new Date(endValue);

        return {
            isValid: endDate >= beginDate,
            message: "La fecha de comienzo no puede ser posterior a la fecha de fin"
        };
    }

    return {
        validatePrice: validatePrice,
        validateHour: validateHour,
        validateMinutes: validateMinutes,
        validateDates: validateDates,
        validateRequired: validateRequired
    };
}());