/**
 * Created by Patrik on 15/01/2017.
 */
var HTML_LOADER_CONTENT = "<div id='loadingDialog' class='modal fade' data-backdrop='static' data-keyboard='false' role='dialog'><div class='modal-dialog'>"
+ "<div class='modal-content'><div class='modal-header'> <h4 class='modal-title'>Please wait...</h4></div>"
+ "<div class='modal-body'><div class='thecube'><div class='cube c1'></div><div class='cube c2'></div>"
+ "<div class='cube c4'></div><div class='cube c3'></div></div></div>"
+ "</div> </div> </div>";

function showLoading(){
    if($("#loadingDialog").length == 0) {
        $(HTML_LOADER_CONTENT).appendTo("body");
    }
    $('#loadingDialog').modal('show');

}

function hideLoading(){
    $('#loadingDialog').modal('hide');
}

$(document).ready(function() {
    $('#resultsTable').DataTable({
        "bLengthChange": false,
        "searching": false,
        "iDisplayLength": 10
    } );
} );