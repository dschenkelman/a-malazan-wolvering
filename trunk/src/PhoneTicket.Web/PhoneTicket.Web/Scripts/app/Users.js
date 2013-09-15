(function () {
    function createUserController() {
        function changeUserValidity(button, isValid, revert) {
            button.prop("disabled", "disabled");
            var userId = button.data("userId");

            $.ajax({
                type: "PUT",
                url: "/api/users/" + userId,
                contentType: "application/json",
                data: JSON.stringify({ isValid: isValid }),
                success: function () {
                    button.prop("value", isValid ? "Invalidar" : "Validar");
                    button.removeProp("disabled");
                    button.one('click', revert);

                    var old = isValid ? "invalid" : "valid";
                    var replaceWith = isValid ? "valid" : "invalid";

                    var parentRow = button.closest('tr.' + old);
                    parentRow.removeClass(old);
                    parentRow.addClass(replaceWith);
                }
            });
        }

        var invalidateUser = function() {
            changeUserValidity($(this), false, validateUser);
        };
        
        var validateUser = function () {
            changeUserValidity($(this), true, invalidateUser);
        };

        return {
            invalidateUser: invalidateUser,
            validateUser: validateUser
        };
    }

    $(document).ready(function() {
        var controller = createUserController();
        $(".invalidate").one('click', controller.invalidateUser);
        $(".validate").one('click', controller.validateUser);
    });
}());