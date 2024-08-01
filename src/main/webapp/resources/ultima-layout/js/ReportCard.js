//function ReportCardPrint(no_of_installment, re_payment_period, payable_full_amount, tbl_size, stu_id) {
//
//    var student_name = $('#student_name').html();
//
//    var course_fee = document.getElementById('save-form:total_amount').value;
//    var instalment = document.getElementById('save-form:installment').value;
//    var interest_rate = document.getElementById('save-form:interest_rate').value;
//
//    $.post('../print/PrintLoanCalculator.xhtml', {
//        student_name: student_name,
//        course_fee: course_fee,
//        instalment: instalment,
//        interest_rate: interest_rate,
//        no_of_installment: no_of_installment,
//        re_payment_period: re_payment_period,
//        payable_full_amount: payable_full_amount,
//        stu_id: stu_id
//
//    }, function (data) {
//
//        var centerLeft = parseInt((window.screen.availWidth - 800) / 2);
//        var centerTop = parseInt(((window.screen.availHeight - 650) / 2) - 50);
//        var misc_features = ', status=no, location=no, scrollbars=yes, resizable=yes';
//
//        var windowFeatures = 'width=' + 800 + ',height=' + 850 + ',left=' + centerLeft + ',top=' + centerTop + misc_features;
//
//        var popupWin = window.open('', '_blank', windowFeatures);
//        popupWin.focus();
//        popupWin.document.open();
//        popupWin.document.write('<html><body onload="window.print();">' + data + '</html>');
////        popupWin.document.print();;
////        popupWin.document.close();
////        PaymentChequePrint(po_id, payment_name);
//    });
//
//}
function ReportCardPrint(size) {

    var check_checked = false;

    for (var i = 0; i < size; i++) {

        var checked = document.getElementById("form:datasTable:" + i + ":no_input").checked;
        if (checked == true) {
            check_checked = true;
            break;

        }

    }
    if (check_checked == false) {

        alert("Select Students for Print Report Card !");
    } else {

        var jsn_object = {jsn: []};

        for (var i = 0; i < size; i++) {

            var checked = document.getElementById("form:datasTable:" + i + ":no_input").checked;
            if (checked == true) {
                var sid = document.getElementById("form:datasTable:" + i + ":sid").innerHTML;
                var order = document.getElementById("form:datasTable:" + i + ":order").innerHTML;
                var order1 = document.getElementById("form:datasTable:" + i + ":order1").innerHTML;
                var order2 = document.getElementById("form:datasTable:" + i + ":order2").innerHTML;
                var order3 = document.getElementById("form:datasTable:" + i + ":order3").innerHTML;
                jsn_object.jsn.push({"sid": sid, "order": order, "order1": order1, "order2": order2, "order3": order3});
            }

        }
        var json_array = JSON.stringify(jsn_object);

        var school = document.getElementById('form:school_input').value;
        var grade = document.getElementById('form:grade_input').value;
        var classes = document.getElementById('form:classes_input').value;
        var year = document.getElementById('form:year_input').value;
        var term = document.getElementById('form:term_input').value;

        $.post('../print/PrintReportCard.xhtml', {
            school: school,
            grade: grade,
            classes: classes,
            year: year,
            term: term,
            json_array: json_array,

        }, function (data) {

            var centerLeft = parseInt((window.screen.availWidth - 800) / 2);
            var centerTop = parseInt(((window.screen.availHeight - 650) / 2) - 50);
            var misc_features = ', status=no, location=no, scrollbars=yes, resizable=yes';

            var windowFeatures = 'width=' + 870 + ',height=' + 850 + ',left=' + centerLeft + ',top=' + centerTop + misc_features;

            var popupWin = window.open('', '_blank', windowFeatures);
            popupWin.focus();
            popupWin.document.open();
            popupWin.document.write('<html><body onload="window.print();">' + data + '</html>');
//        popupWin.document.print();;
//        popupWin.document.close();
//        PaymentChequePrint(po_id, payment_name);
        });

    }


}
function DataInputScorePrint() {
    var province = document.getElementById('form:province_input').value;
    var zone = document.getElementById('form:zone_input').value;
    var division = document.getElementById('form:division_input').value;
    var school = document.getElementById('form:school_input').value;

    var jsn_object = {jsn: []};

    for (var i = 0; i < 3; i++) {
//form:panel:0:yearName
        var checked1 = document.getElementById("form:panel:" + i + ":term1_input").checked;
        if (checked1 == true) {
            var yearName = document.getElementById("form:panel:" + i + ":yearName").innerHTML;

            jsn_object.jsn.push({"yearName": yearName, "term": '1'});
        }
        var checked2 = document.getElementById("form:panel:" + i + ":term2_input").checked;
        if (checked2 == true) {
            var yearName = document.getElementById("form:panel:" + i + ":yearName").innerHTML;

            jsn_object.jsn.push({"yearName": yearName, "term": '2'});
        }
        var checked3 = document.getElementById("form:panel:" + i + ":term3_input").checked;
        if (checked3 == true) {
            var yearName = document.getElementById("form:panel:" + i + ":yearName").innerHTML;

            jsn_object.jsn.push({"yearName": yearName, "term": '3'});
        }

    }
    var json_array = JSON.stringify(jsn_object);

    $.post('../print/PrintDataInputScore.xhtml', {
        province: province,
        zone: zone,
        division: division,
        school: school,
        json_array: json_array,
    }, function (data) {

        var centerLeft = parseInt((window.screen.availWidth - 800) / 2);
        var centerTop = parseInt(((window.screen.availHeight - 650) / 2) - 50);
        var misc_features = ', status=no, location=no, scrollbars=yes, resizable=yes';

        var windowFeatures = 'width=' + 870 + ',height=' + 850 + ',left=' + centerLeft + ',top=' + centerTop + misc_features;

        var popupWin = window.open('', '_blank', windowFeatures);
        popupWin.focus();
        popupWin.document.open();
        popupWin.document.write('<html><body onload="window.print();">' + data + '</html>');
//        popupWin.document.print();;
//        popupWin.document.close();
//        PaymentChequePrint(po_id, payment_name);
    });




}
