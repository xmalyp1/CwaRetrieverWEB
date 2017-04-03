/**
 * Created by Patrik on 12/01/2017.
 */
var actualOntology;

function selectOntology(oid){
    actualOntology = oid;
}

function removeOntology(ontologyId){
    return $.ajax({
        url: "/app/ontology?oid="+actualOntology,
        type: "DELETE",
        success: function (data) {
            $("#ontology"+actualOntology).remove();
            $("#deleteModal").modal("hide");
        },
        error: function (xhr) {
           console.log("unable to remove the element")
        }
    });
}