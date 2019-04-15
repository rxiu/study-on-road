//need unit common.js
//
//param参数
//{
//     winName:"",
//     winCalbackName:"",
//     title:title,
//     width:100,
//     height:100,
//     url:webRoot+"",
//     buttons:[{text:"",onclick:function(){}}]
//}
(function (exports) {
    var com = {
        dialog: function (param) {
            var top = com.getTopWindowDom();
            param["url"] = webRoot + param["url"];
            var dialogObj = top.$.ligerDialog.open(param);
            return dialogObj;
        },
        //表格
        grid: function (jqGrid, param) {
            $(jqGrid).ligerGrid(param);
        },
        //询问框
        confirm: function (text, callback,cancelCallback) {
            $.ligerDialog.confirm(text, function (yes) {
                if (yes && typeof (callback) == "function") {
                    callback();
                }else
                if (!yes && typeof (cancelCallback) == "function") {
                    cancelCallback();
                }

            })
        },
        tree: function (jqTree, param) {
            $(jqTree).ligerTree(param);
        },
        //获取ligerui的插件对象
        getGrid: function (gridId) {
            var ligerObj = liger.get(gridId);
            return ligerObj;
        }
    }
    com = $.extend(true, com, common);
    exports.common = com;
})(window);