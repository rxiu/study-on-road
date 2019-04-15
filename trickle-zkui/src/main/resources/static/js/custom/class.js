/**
  * 基础类Class
  * 1.新建类
  *   var Func = new Class();
  *
  * 2.添加类的属性
  *   Func.extend({ run: function() { /-- doing sth. --/ } });
  *   Func.run();
  *
  * 3.添加类实例的属性
  *   Func.include({ run: function() { /-- doing sth. --/ } });
  *   var func = new Func();
  *   func.run();
  *
  * 4.继承类
  *   var Animal = new Class();
  *   Animal.include({ breath: function() { /-- doing sth. --/ } });
  *   var Cat = new Class(Animal);
  *   var Tom = new Cat();
  *   Tom.breath();
  *
  * 5.方法代理(proxy)
  *   var Button = new Class();
  *   Button.include({
  *      init: function(element) {
  *         this.element = jQuery(element);
  *         this.element.click(this.proxy(this.click));
  *      },
  *      click: function() { /-- doing sth. --/ } }
  *   });
  *
  */
(function (exports) {
    //#region Class
    var Class = function (parent) {
        var _class = function () {
            this.init.apply(this, arguments);
        };
        _class.prototype.init = function () { };

        // 改变类的原型
        if (parent) {
            var subClass = function () { };
            subClass.prototype = parent.prototype;
            _class.prototype = new subClass;
        };

        // 定义prototype别名
        _class.fn = _class.prototype;

        // 定义类的别名
        _class.fn.parent = _class;

        _class._super = _class.__proto__;


        // 给类添加属性
        _class.extend = function (obj) {
            var extended = obj.extended;
            for (var i in obj) {
                _class[i] = obj[i];
            }
            if (extended) extended(_class);
        };

        // 给类的实例添加属性
        _class.include = function (obj) {
            var included = obj.included;
            for (var i in obj) {
                _class.fn[i] = obj[i];
            }
            if (included) included(_class);
        };

        // 代理函数
        _class.proxy = function (func) {
            var self = this;
            return (function () {
                return func.apply(self, arguments);
            });
        };
        // 于实例中添加代理
        _class.fn.proxy = _class.proxy;

        return _class;
    };
    exports.Class = Class;
    //#endregion

})(window);