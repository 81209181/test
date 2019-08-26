$(document).ready(function () {
    getTreeData();
});

function getTreeData() {
   $.ajax({
       type: 'GET',
       url: '',
       success: function (tree) {
           loadTree(tree);
       }
   });
}

function loadTree(tree) {
    $('roleTree').jstree({
        'core': {
            'data': tree
        },
        'plugin': ['search', 'checkbox']
    })
}

