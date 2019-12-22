$(document).ready(function() {
    $('.js-select').select2();
    $('.datetimepicker').datetimepicker({
        format: 'YYYY-MM-DD HH',
        daysOfWeekDisabled: [0, 6],
        locale: 'pl',
        disabledHours: [0, 1, 2, 3, 4, 5, 6, 7, 18, 19, 20, 21, 22, 23],
        disabledDates: [
                        moment("12/25/2019"),
                        moment("12/26/2019"),
                        moment("01/01/2020"),
                        moment("05/01/2020"),
                        moment("05/03/2020"),
                        moment("08/15/2020"),
                        moment("11/01/2020"),
                        moment("11/11/2020"),
                        moment("12/25/2020"),
                        moment("12/26/2020"),
                        moment("04/13/2020"),
                        moment("06/11/2020")
                    ]
    });
});