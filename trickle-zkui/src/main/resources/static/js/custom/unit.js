/*
 * need:jQuery V1.11.1
 */
//#region common function
(function (exports) {
    var com = {
        checkFlag:0,
        //获取最顶层的window DOM对象
        getTopWindowDom: function () {
            var obj = window.self;
            var outTimes = 0;
            var upWindow = null;//上一个父级frame
            while (true) {
                //如果取不到，会一直卡下去--默认10次
                try {
                    if (obj.document.getElementById("flag_top_window_ky")) {
                        return obj;
                    }
                } catch (ex) {
                    return upWindow;
                }
                upWindow = obj.window;
                obj = obj.window.parent;
                if (outTimes > 10) {
                    return obj;
                }
                outTimes++;
            }

        },
        //获取url参数
        getParamFromUrl: function (name) {
            name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
            var regexS = "[\\?&]" + name + "=([^&#]*)";
            var regex = new RegExp(regexS);
            var results = regex.exec(window.location.href);
            if (results == null) {
                return null;
            }
            else {
                var data=null;
                var _param=results[1];
                try {
                    data = decodeURIComponent(decodeURIComponent(results[1]));
                }catch (ex){
                    data=decodeURIComponent(_param);
                }

                return data;
            }
        },
        parseUrlParam2Json: function (url) {
            var url = url || window.location.href;
            var reg_url = /^[^\?]+\?([\w\W]+)$/,
                reg_para = /([^&=]+)=([\w\W]*?)(&|$|#)/g,
                arr_url = reg_url.exec(url),
                ret = {};
            if (arr_url && arr_url[1]) {
                var str_para = arr_url[1], result;
                while ((result = reg_para.exec(str_para)) != null) {
                    var _param=result[2];
                    try {
                        ret[result[1]] = decodeURIComponent(decodeURIComponent(result[2]));
                    }catch (ex){
                        ret[result[1]]=decodeURIComponent(_param);
                    }
                }
            }
            return ret;
        },
        //判断是否数字
        isNumber: function () {
            return /^[(-?\d+\.\d+)|(-?\d+)|(-?\.\d+)]+$/.test(val + '');
        },
        //阻止冒泡
        stopBubble: function () {
            //兼容FF
            var e = typeof(event) == "undefined" ? arguments.callee.caller.arguments[0] : event;
            if (e && e.stopPropagation) {
                e.stopPropagation();
            }
            else {
                window.event.cancelBubble = true;
            }
        },

        //消息提醒
        jsmsg: function (msgtitle, msgcss, callback) {
            msgtitle=msgtitle.replace("!","！");
            msgtitle=msgtitle.replace("?","？");
            msgtitle=msgtitle.replace(",","，");
            msgtitle=msgtitle.replace(";","；");
            msgtitle=msgtitle.replace(":","：");
            $("#msgprint").remove();
            var cssname = "";
            switch (msgcss) {
                case "Success":
                    cssname = "pcent success";
                    break;
                case "Error":
                    cssname = "pcent error";
                    break;
                default:
                    cssname = "pcent warning";
                    break;
            }
            var str = "<div id=\"msgprint\" class=\"" + cssname + "\">" + msgtitle + "</div>";
            $("body").append(str);
            var bodyWidth = $("body").innerWidth(), msgprintWidth = $("#msgprint").outerWidth();
            $("#msgprint").css({
                "left": (bodyWidth - msgprintWidth) / 2 - 2,
                width: msgprintWidth,
                "z-index": 999999999
            });
            $("#msgprint").show();
            //1秒后清除提示
            setTimeout(function () {
                $("#msgprint").fadeOut(1000);
                //如果动画结束则删除节点
                if (!$("#msgprint").is(":animated")) {
                    $("#msgprint").remove();
                }
            }, 1200);
            if (typeof (callback) == "function") {
                callback();
            }
            //执行回调函数
        },
        //正确消息提醒
        jsmsgSuccess: function (msgtitle, callback) {
            com.jsmsg(msgtitle, 'Success', callback);
        },
        //错误消息提醒
        jsmsgError: function (msgtitle, callback) {
            com.jsmsg(msgtitle, 'Error', callback);
        },
        //表单验证
        vaildform: function (jqform, vaildHidden) {
            jqform = jqform || $("#baseAttrForm");
            if (com.checkFlag == 0) {
                var checkInput = jqform.find(":input");
                $.each(checkInput, function (index, item) {
                    var dataType = $(item).attr("datatype");
                    var errorMsg = $(item).attr("errormsg");
                    //只有datatype存在且包含"-"(如*1-50)时作处理,另外,datatype为正则表达式时(正则表达式必然包含"/"),也不作处理
                    if (dataType && dataType.indexOf("-") != -1 && dataType.indexOf("/") == -1) {
                        var maxData = dataType.substring(dataType.indexOf("-") + 1);
                        var maxNum = parseInt(maxData);
                        var reg = new RegExp(maxNum, "g");
                        dataType = dataType.replace(reg, maxNum / 2);
                        $(item).attr("datatype", dataType);
                        if (errorMsg) {
                            errorMsg = errorMsg.replace(reg, maxNum / 2);
                            $(item).attr("errormsg", errorMsg);
                        }
                    }
                });
                com.checkFlag++;
            }
            validformObj = $(jqform).Validform({
                ignoreHidden: !vaildHidden ? true : false,
                tiptype: function (msg, o, cssctl) {
                    msg=msg.replace("!","！");
                    msg=msg.replace("?","？");
                    msg=msg.replace(",","，");
                    msg=msg.replace(";","；");
                    msg=msg.replace(":","：");
                    //msg：提示信息;
                    //o:{obj:*,type:*,curform:*}, obj指向的是当前验证的表单元素（或表单对象），type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, curform为当前form对象;
                    //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
                    if (!o.obj.is("form")) {//验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
                        var objtip = o.obj.siblings(".Validform_checktip");
                        var edittype = o.obj.attr("edittype");
                        if (edittype == "date") {
                            if (!objtip[0]) {
                                objtip = o.obj.parent().siblings(".Validform_checktip");
                            }
                            var deteObjTip = $(o.obj).closest(".l-text-date").siblings(".Validform_checktip");
                            if (deteObjTip.length == 0) {
                                objtip = $('<span></span>').addClass("Validform_checktip");
                                var jqParent = $(o.obj).parent(".l-text-date").css({float: "left"})
                                $(objtip).insertAfter(jqParent);
                            }
                            cssctl(objtip, o.type);
                            $(o.obj).data("msg", msg);
                            $(o.obj).focus(function () {
                                if ($(this).hasClass("Validform_error")) {
                                    deteObjTip.removeClass("Validform_right").addClass("Validform_wrong");
                                    var postion = $(this).offset();
                                    var left = postion.left, top = postion.top;
                                    $(".text-up-tips").css({
                                        left: left,
                                        top: top - 26,
                                        "z-index": 999
                                    }).show().find(".msg").text(msg);
                                }
                            });
                            $(o.obj).blur(function () {
                                if ($(this).hasClass("Validform_error")) {
                                    deteObjTip.removeClass("Validform_right").addClass("Validform_wrong");
                                }
                                $(".text-up-tips").hide();
                            });
                        } else {
                            if (objtip.length == 0) {
                                objtip = $('<span></span>').addClass("Validform_checktip");
                                if (($(o.obj)[0].tagName).toLowerCase() === "select") {

                                    /*   $($(o.obj).closest(".simulate-select")).next(".simulate-select").remove();*/
                                    $($(o.obj).closest(".simulate-select")).siblings(".Validform_checktip").remove();
                                    $(objtip).insertAfter(o.obj.closest(".simulate-select"));


                                } else {
                                    $(objtip).insertAfter(o.obj)
                                }
                            }
                            cssctl(objtip, o.type);
                            $(o.obj).data("msg", msg);
                            $(o.obj).focus(function () {
                                if ($(this).hasClass("Validform_error")) {
                                    var postion = $(this).offset();
                                    var left = postion.left, top = postion.top;
                                    $(".text-up-tips").css({
                                        left: left,
                                        top: top - 26,
                                        "z-index": 999
                                    }).show().find(".msg").text(msg);
                                }
                            });
                            $(o.obj).blur(function () {
                                $(".text-up-tips").hide();
                            })
                        }

                    }
                }
            });
            return validformObj;
        },
        _setFormToken: function () {
            return "" + (Math.random() * 1024 | 0) + new Date().getTime();
        },
        //采用iframe下载方式
        makeDownload: function (url) {
            var iframe = $("#downFrame");
            if (!iframe || iframe.length == 0) {
                iframe = $("<iframe id='downFrame' name='downFrame' style='display:none;'></iframe>").appendTo($("body"));
            }
            iframe.attr({src: url});
        },
        //调用方式：数组对象arrayObj.sort(arraySortFun('asc||desc','ws'));其中arrayObj为JSON的数组对象，ws为json中的字段
        arraySortFun: function (order, sortBy) {
            var ordAlpah = (order == "desc") ? '>' : '<';
            var sortFun = new Function('a', 'b', 'return a.' + sortBy + ordAlpah + 'b.' + sortBy + '?1:-1');
            return sortFun;
        },
        //将线性结构转成树形结构的:data为要转换的数据，idKey，为pidKey的关联键， pid是为线性结构标识key，children为转成层级结构后的编码
        pidToChildren: function (data, idKey, pidKey, childrenKey) {
            //筛选出顶级节点的数据
            var result = [];
            $.each(data, function (index, item) {
                var isExist = false;
                $.each(data, function (index1, item1) {
                    //如果这个的父级节点存在，说明它不是顶级节点
                    if (item[pidKey] == item1[idKey]) {
                        isExist = true;
                    } else {
                        item[childrenKey] = [];
                    }
                });
                if (!isExist) {
                    result.push(item);
                }
            });
            //根据顶级节点开始组装子级节点
            $.each(result, function (index, item) {
                $.each(data, function (inedx1, item1) {
                    if (item[idKey] == item1[pidKey]) {
                        if (result[index][childrenKey]) {
                            result[index][childrenKey].push(item1);
                        } else {
                            result[index][childrenKey] = [];
                            result[index][childrenKey].push(item1);
                        }
                    }
                })
            });
            return result;
        },
        //时间戳转换成日期格式
        dateTimeStamp2dateTime: function (dateTimeStamp, format) {
            var dateTime = ""
            if (dateTimeStamp && dateTimeStamp != "null") {
                if (!format) {
                    format = "yyyy-MM-dd";
                }
                var date = new Date(parseInt(dateTimeStamp));

                //夏令时 处理
                /*  var strLocalDateTime =date.toLocaleString();
                 var strLocalTime=date.toLocaleTimeString();
                 if (strLocalTime.indexOf('下午') > -1) {
                 var strLocalDateTime = strLocalDateTime.replace('下午', '');
                 date=new Date(strLocalDateTime);
                 date= date.AddHour(12);
                 } else if(strLocalTime.indexOf('上午') > -1) {
                 var strLocaDatelTime = strLocalDateTime.replace('上午', '');
                 date=new Date(strLocaDatelTime)
                 }else{
                 date=new Date(strLocaDatelTime);
                 }*/


                dateTime = date.Format(format);
            }
            return dateTime;
        },
        //获取操作类型
        getOperaterType: function (type) {
            var name = "";
            switch (type) {
                case 1:
                    name = "查询";
                    break;
                case 2:
                    name = "新增";
                    break;
                case 3:
                    name = "修改";
                    break;
                case 4:
                    name = "删除";
                    break;
                case 5:
                    name = "导入";
                    break;
                case 6:
                    name = "导出";
                    break;
            }
            return name;
        },
        node2elementtree: function (id, pid, nodes, treeData) {
            $(nodes).each(function (index, item) {
                id = id + 1;
                var xmlHtml = $(item).prop("outerHTML") || item.xml || new XMLSerializer().serializeToString(item);
                var pNode = null;
                if ($(xmlHtml).children().length > 0) {
                    pNode = $(xmlHtml).empty();
                } else {
                    pNode = $(xmlHtml);
                }


                var pHtml = "";
                try {
                    pHtml = $(pNode).prop("outerHTML") || pNode.xml || new XMLSerializer().serializeToString(pNode) || "";
                } catch (e) {
                    pHtml = xmlHtml;
                }
                pHtml = pHtml.replace(/</g, "&lt;").replace(/>/g, "&gt;");

                treeData.push({id: id, text: pHtml, pid: pid});
                pid = id;

                $(item).children().each(function (index1, item1) {
                    if ($(item1).children().length > 0) {
                        treeData = com.node2elementtree(id, id, item1, treeData);
                    } else {
                        id = id + 1;
                        var xmlHtml = $(item1).prop("outerHTML") || item1.xml || new XMLSerializer().serializeToString(item1);

                        var cNode = null;
                        if ($(xmlHtml).children().length > 0) {
                            cNode = $(xmlHtml).empty();
                        } else {
                            cNode = $(xmlHtml);
                        }

                        var cHtml = "";
                        try {
                            cHtml = $(cNode).prop("outerHTML") || cNode.xml || new XMLSerializer().serializeToString(cNode);
                        } catch (e) {
                            cHtml = xmlHtml;
                        }

                        cHtml = cHtml.replace(/</g, "&lt;").replace(/>"/g, "&gt;");
                        treeData.push({id: id, text: cHtml, pid: pid});
                    }
                })
            })
            return treeData;
        },
        xml2xmlObject: function (strXml) {
            var xmlObj;
            if (strXml && typeof(strXml) == "string" && $.trim(strXml).length > 0) {
                strXml = strXml.replace(/[\r\n]/g, "");
                xmlObj = strXml.xml2Object();
            } else {
                xmlObj = strXml;
            }
            return xmlObj;
        },
        //xml字符串(对象）转换成tree元素值
        xml2elementtree: function (strXml) {
            var treeData = [];
            var xmlObj = com.xml2xmlObject(strXml);
            var xmlDoc = xmlObj.documentElement;
            var id = 0;
            var pid = 0;
            treeData = com.node2elementtree(id, pid, xmlDoc, treeData);
            return treeData;
        },
        setFormAttr: function (pros, formId) {
            var jqForm = $("#baseAttr");
            jqForm = !formId ? $("#baseAttr") : $("#" + formId);
            $(jqForm).attr(pros);
        },
        /*
         * 目前支持格式：
         * k
         * w
         * ###.##
         */
        numFormat: function (num, format) {
            num = Number(num);
            switch (format) {
                case "k":
                    if (num > 1000) {
                        num = parseInt((num / 1000)) + "千";
                    }
                    break;
                case "w":
                    if (num > 10000) {
                        num = parseInt((num / 10000)) + "万";
                    }
                    break;
            }
            return num;
        },
        getCopyRight: function () {
            $.ajax({
                type: 'get',
                url: (ssoPath || webRoot) + "home/getCopyRight",
                async: false,
                dataType: 'jsonp',
                jsonp: "callback",
                jsonpCallback: "getCopyRight",
                success: function (jsonp) {
                    $("#copyright").html(jsonp.value);
                }

            })

        },
        updatePwd: function (id, oldPwd, newPwd, callback) {

            $.ajax({
                type: 'get',
                url: (ssoPath || webRoot) + "home/updatePwd",
                data: {id: id, oldPwd: encodeURIComponent(oldPwd), newPwd: encodeURIComponent(newPwd)},
                async: false,
                dataType: 'jsonp',
                jsonp: "callback",
                jsonpCallback: "updatePwd",
                success: function (jsonp) {
                    if (typeof(callback) == "function") {
                        callback(jsonp);
                    }
                }
            })
        },
        /**
         * 修改初始密码
         * author by HL on 2017/8/11
         * modify by HL on 2017/8/11
         */
        modifyPwd: function (loginName, oldPwd, newPwd, callback) {
            $.ajax({
                type: 'get',
                url: (ssoPath || webRoot) + "home/modifyPwd",
                data: {loginName: loginName, oldPwd: encodeURIComponent(oldPwd), newPwd: encodeURIComponent(newPwd)},
                async: false,
                dataType: 'jsonp',
                crossDomain: true,
                jsonp: "callback",
                jsonpCallback: "modifyPwd",
                success: function (jsonp) {
                    if (typeof(callback) == "function") {
                        callback(jsonp);
                    }
                }
            })
        },
        getLogoImage: function (code) {
            $("#altLogo").attr("src", (ssoPath || webRoot) + "home/uploadLogo?code=" + code);
        },
        safeExit: function () {
            $("#exit").on("click", function () {
                common.confirm("您确定要退出系统吗？", function () {
                    window.location.href = webRoot + "/frame/loginout";
                });
            })
        },
        getLoginName: function () {
            var req = new Request("frame/getCurUserName");
            req.get({
                isTip: false,
                success: function (userName) {
                    if (userName) {
                        userName = userName.cut(20, 20); //登录名称截取
                        $("#LoginName").text(userName);
                    }
                }
            })
        }

    }
    exports.common = com;
})
(window);
//#endregion
