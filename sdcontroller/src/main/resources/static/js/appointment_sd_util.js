/*
 *To build up the external call service from other system
 */
function Appointment() {
}

Appointment.prototype.apptObj = "";
Appointment.prototype.cancelObj = "";
Appointment.prototype.hiddenForm = "";
Appointment.prototype.make = function(obj, url) {
    var data = obj.data;
    var targetUrl = url + "/sd/JobAppointmentDisplayPublic.action";
    /*
     *
     * Open the window with post method (POST METHOD)
     * http://taswar.zeytinsoft.com/javascript-http-post-data-to-new-window-or-pop-up/
     */
    hiddenForm = document.createElement("form");
    hiddenForm.setAttribute("method", "POST");
    hiddenForm.setAttribute("action", targetUrl);
    hiddenForm.setAttribute("target", "Testing"); // set the window name
    /*
     * If it use the native javascript, use this one
     * https://stackoverflow.com/questions/1714786/query-string-encoding-of-a-javascript-object
     */
    serialize = function(form, obj, prefix, isArray) {
        var p = 0;
        for (p in obj) {
            if (obj.hasOwnProperty(p)) {
                var k, v;
                if (isArray)
                    k = prefix ? prefix + "[" + p + "]" : p, v = obj[p];
                else
                    k = prefix ? prefix + "." + p + "" : p, v = obj[p];

                if (v !== null && typeof v === "object") {
                    if (Array.isArray(v)) {
                        serialize(form,v, k, true);
                    } else {
                        serialize(form,v, k, false);
                    }
                } else {
                    var query = k + "=" + v;
                    var hiddenField = document.createElement("input");
                    hiddenField.setAttribute("type", "hidden");
                    hiddenField.setAttribute("name", k);
                    hiddenField.setAttribute("value", v);
                    form.appendChild(hiddenField);
                }
            }
        }
        return hiddenForm;
    };

    hiddenForm = serialize(hiddenForm, data, "sdSystemParam", false);

    document.body.appendChild(hiddenForm);
    newWindow = window
        .open(
            "",
            "Testing", // set the window name
            "toolbar=no,location=no,menubar=no,resize=no, status=no, width=1400, height=400");
    hiddenForm.submit();
    this.apptObj = obj;
    return newWindow;
};

Appointment.prototype.cancel = function(obj) {
    // TODO : to implement the cancel function
};

Appointment.prototype.query = function(obj) {
    // TODO : to implement the query function
};


var AppointmentSDObj = new Appointment();