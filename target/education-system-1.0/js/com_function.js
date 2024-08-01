
function isNumberKey(evt)
{
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}


function makeInitialTextReadOnly(input) {
    var readOnlyLength = input.value.length;
    field.addEventListener('keydown', function (event) {
        var which = event.which;
        if (((which == 8) && (input.selectionStart <= readOnlyLength)) ||
                ((which == 46) && (input.selectionStart < readOnlyLength))) {
            event.preventDefault();
        }
    });
    field.addEventListener('keypress', function (event) {
        var which = event.which;
        if ((event.which != 0) && (input.selectionStart < readOnlyLength)) {
            event.preventDefault();
        }
    });
}