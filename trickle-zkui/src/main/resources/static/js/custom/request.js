//#region Request
(function (exports) {
    jQuery.support.cors = true;
    /**
     * 数据查询基类 Request
     * 根据页面需求进行封装，统一类名Request，同一实体对象可用get和post进行请求，对应ajax的get和post的请求类型，
     * 且同一实体对象中不能并发多个请求，任何新的get/post操作皆会将停止（abort）之前运行的异步请求。
     * exp
     *   构造实体对象：var req = new Request(actionName, 0);
     *   异步请求：req.get([param]); / req.post([param]);
     *   重载：req.reload();
     */
    // 构建类
    var r = exports.Request = new Class();
    r.extend({
        ajaxUrl: "",
        ajaxParam: {
            isTip: true,
            cache: false,
            timeout: 180000,
            success: function (data) {
            },
            complete: function (xhr, status) {

            }
        }
    });
    r.include({
        init: function (ajaxUrl) {
            /// <summary>数据请求基类</summary>
            /// <param name="ajaxUrl" type="String">请求action名称</param>
            // 初始化参数
            this.setAjaxUrl(ajaxUrl);
            this.request = null;
            this.run = false;
        },
        setAjaxUrl: function (ajaxUrl) {
            if (ajaxUrl.indexOf("http") > -1 || ajaxUrl.indexOf("https") > -1) {
                this.ajaxUrl = ajaxUrl;
            } else {
                this.ajaxUrl = ajaxUrl;
            }
        },

        post: function () {
            this.ajax($.extend(true, {type: "POST"}, arguments[0]));
        },
        get: function () {
            this.ajax($.extend(true, {type: "GET"}, arguments[0]));
        },
        put: function () {
            this.ajax($.extend(true, {type: "PUT", contentType: "application/json;charset=UTF-8"}, arguments[0]));
        },
        del: function () {
            this.ajax($.extend(true, {type: "DELETE", contentType: "application/json;charset=UTF-8"}, arguments[0]));
        },
        ajax: function () {

            if (arguments) {
                this.run = true;
                var url = this.ajaxUrl;
                var self = this;
                this.param = $.extend(true, {}, r.ajaxParam, arguments[0]);
                var successObj = $.extend(true, {}, {success: this.param["success"]});
                delete this.param["success"];
                this.param["success"] = function (data) {
                    if (self.param.isTip) {
                        var msgCss = data.success ? "Success" : "Error";
                        var msgContext = "";
                        //网关后台返回格式特殊处理
                        if (data.message && typeof(data.message) == "string") {
                            if (data.message.length > 0) {
                                msgContext = data.message;
                            } else {
                                msgContext = data.success ? "操作成功!" : "操作失败!";
                            }
                        } else {
                            //原先数据格式
                            if (data.message && data.message.length == 0) {
                                msgContext = data.success ? "操作成功!" : "操作失败!";
                            }
                            if (!data.success && data.message && data.message.length > 0) {
                                $.each(data.message, function (index, item) {
                                    msgContext += item["content"]
                                });
                            }
                        }
                        common.getTopWindowDom().common.jsmsg(msgContext, msgCss, function () {
                            if (successObj && (typeof successObj.success == "function")) {
                                successObj.success(data);
                            }
                        })
                    } else {
                        successObj.success(data);
                    }

                }
                var completeObj = $.extend(true, {}, {complete: this.param["complete"]});
                delete this.param["complete"];
                this.param["complete"] = function (xhr, status) {
                    var sessionStatus = xhr.getResponseHeader('sessionstatus');
                    var contextpath = xhr.getResponseHeader('contextpath');
                    //null为手动清空cookie时；timeout为超时session 丢失
                    if (sessionStatus == 'timeout') {
                        var top = common.getTopWindowDom();
                        top.location.href = contextpath
                     //session 冲突，用于异地登陆提示
                    } else if (sessionStatus == 'conflict') {
                        if (confirm(decodeURI(xhr.getResponseHeader('message')))) {
                            top.location.href = contextpath
                        }
                    }
                    completeObj.complete(xhr, status);
                };
                this.param["url"] = url;
                this.request = $.ajax(this.param);

            }
        }
        ,
        // 重载
        reload: function () {
            /// <summary>重载</summary>
            if (this.run) {
                this.abort();
                this.request = $.ajax(this.param);
            }
        },
        // 退出数据请求
        abort: function () {
            /// <summary>退出数据请求</summary>
            if (this.request) {
                this.request.abort();
            }
        }
    });
})(window);
//#endregion