
function isNumberKey(evt)
{
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode != 46 && charCode > 31
            && (charCode < 48 || charCode > 57))
        return false;

    return true;
}
function numericFormat(fld, e, extraStrCheck)
{
    var sep = 0;
    var key = '';
    var i = j = 0;
    var len = len2 = 0;
    var strCheck = '0123456789';
    if (extraStrCheck)
        strCheck += extraStrCheck;
    var aux = aux2 = '';
    var whichCode = (window.Event) ? e.which : e.keyCode;

    if (whichCode == 13)
        return true;  // Enter
    if (whichCode == 8)
        return true;  // Backspace
    if (whichCode == 0)
        return true;  // Null
    if (whichCode == 9)
        return true;  // Tab

    key = String.fromCharCode(whichCode);  // Get key value from key code
    if (strCheck.indexOf(key) == -1)
        return false;  // Not a valid key
    var x = new String(fld.value);
    if (key == '.')
    {
        var exp = /\./;
        var a = x.search(exp);
        if (a != -1)
            return false;
    }


}