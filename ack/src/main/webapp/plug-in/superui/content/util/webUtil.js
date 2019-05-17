/**
 *
 * Created by UTSC0885 on 2017/3/30.
 */
var webUtil = {};

/**
 *  建议使用getQueryString方法
 * @param name
 * @deprecated
 * @returns {*}
 */
webUtil.getUrlParam = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return decodeURIComponent(r[2]);
    return null;
};


