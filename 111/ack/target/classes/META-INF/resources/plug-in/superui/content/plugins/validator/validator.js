/**
 * 数据验证完整性
 * 多重校验的方式以空格分开,例如Price NumOrNull
 * @returns {boolean}
 * @constructor
 */
$.fn.Validform = function () {
    var Validatemsg = "";
    var Validateflag = true;
    var focusflag = false;
    $(this).find("[isvalid=yes]").each(function () {
        var checkexpession = $(this).attr("checkexpession");
        var errormsg = $(this).attr("errormsg");
        if (checkexpession != undefined) {
            if (errormsg == undefined) {
                errormsg = "";
            }
            var value = $(this).val();
            if ($(this).hasClass('ui-select')) {
                value = $(this).attr('data-value');
            }
            var checkexpessions = checkexpession.split(' ');
            var control = $(this);
            $.each(checkexpessions, function (idx, item) {
                var flag = isValidator(control, item, value, errormsg);
                if (!flag)
                    return false;
            });
        }
    });
    if ($(this).find("[fieldexist=yes]").length > 0) {
        return false;
    }
    return Validateflag;

    /**
     * 验证
     * @param obj
     * @param checkexpession
     * @param value
     * @param errormsg
     * @returns {boolean}
     */
    function isValidator(obj, checkexpession, value, errormsg) {
        switch (checkexpession) {
            case "NotNull": {
                if (isNotNull(value)) {
                    Validatemsg = errormsg + "不能为空！";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "NotContainsOneChar": {
                var notContainsOneChar = $(obj).attr("notContainsOneChar");
                if (isContainsOneChar(value, notContainsOneChar)) {
                    Validatemsg = errormsg + "不能包含“" + notContainsOneChar + "”特殊字符！";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "Num": {
                if (!isInteger(value)) {
                    Validatemsg = errormsg + "必须为数字！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "isLenEqualNum": {
                var lenEqualNum = $(obj).attr("lenEqualNum");
                if (!isInteger(value, lenEqualNum)) {
                    Validatemsg = errormsg + "必须为" + lenEqualNum + "位数字！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "isEqualFirstNum": {
                var lenEqualNum = $(obj).attr("firstNum");
                if (!isInteger(value)) {
                    Validatemsg = errormsg + "必须为" + lenEqualNum + "开头的数字！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "equalEightNum": {
                if (!isEqualEightNum(value)) {
                    Validatemsg = errormsg + "必须为8开头的8位数字！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "Price": {
                if (!isPrice(value)) {
                    Validatemsg = errormsg + "必须为数字，且小数位不能超过2位！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                }
                break;
            }
            case "NumOrNull": {
                if (!isIntegerOrNull(value)) {
                    Validatemsg = errormsg + "必须为数字！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "Email": {
                if (!isEmail(value)) {
                    Validatemsg = errormsg + "必须为E-mail格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "EmailOrNull": {
                if (!isEmailOrNull(value)) {
                    Validatemsg = errormsg + "必须为E-mail格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "EnglishStr": {
                if (!isEnglishStr(value)) {
                    Validatemsg = errormsg + "必须为字符串！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "EnglishStrOrNull": {
                if (!isEnglishStrOrNull(value)) {
                    Validatemsg = errormsg + "必须为字符串！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "lessThan": {
                var lessThanValue = $(obj).attr("lessThanValue");
                if (!isLessThan(value, lessThanValue)) {
                    Validatemsg = errormsg + "必须小于" + lessThanValue + "的数字！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "greaterThan": {
                var greaterThanValue = $(obj).attr("greaterThanValue");
                if (!isGreaterThan(value, greaterThanValue)) {
                    Validatemsg = errormsg + "必须大于" + greaterThanValue + "的整数字！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "LenNum": {
                if (!isLenNum(value, $(obj).attr("length"))) {
                    Validatemsg = errormsg + "必须少于" + $(obj).attr("length") + "位数字！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "LenNumOrNull": {
                if (!isLenNumOrNull(value, $(obj).attr("length"))) {
                    Validatemsg = errormsg + "必须少于" + $(obj).attr("length") + "位数字！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "minLenNum" : {
                if (!isMinLenNum(value, $(obj).attr("minLength"))) {
                    Validatemsg = errormsg + "必须多于" + $(obj).attr("minLength") + "位数字！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "minLenNum1" : {
                if (!isMinLenNum(value, $(obj).attr("minLength"))) {
                    Validatemsg = errormsg + "必须等于" + $(obj).attr("minLength") + "位数字！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "minLenNumOrNull" : {
                if (!isMinLenNumOrNull(value, $(obj).attr("minLength"))) {
                    Validatemsg = errormsg + "必须多于" + $(obj).attr("minLength") + "位数字！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }

            case "LenStr": {
                if (!isLenStr(value, obj.attr("length"))) {
                    Validatemsg = errormsg + "必须少于" + $(obj).attr("length") + "位字符！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "LenStrOrNull": {
                if (!isLenStrOrNull(value, $(obj).attr("length"))) {
                    Validatemsg = errormsg + "必须小于" + $(obj).attr("length") + "位字符！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "minLenStr": {
                if (!isMinLenStr(value, obj.attr("minLength"))) {
                    Validatemsg = errormsg + "必须多于" + $(obj).attr("minLength") + "位字符！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "minLenStrOrNull": {
                if (!isMinLenStrOrNull(value, obj.attr("minLength"))) {
                    Validatemsg = errormsg + "必须多于" + $(obj).attr("minLength") + "位字符！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }

            case "Phone": {
                if (!isTelephone(value)) {
                    Validatemsg = errormsg + "必须电话格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "PhoneOrNull": {
                if (!isTelephoneOrNull(value)) {
                    Validatemsg = errormsg + "必须电话格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "Fax": {
                if (!isTelephoneOrNull(value)) {
                    Validatemsg = errormsg + "必须为传真格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "Mobile": {
                if (!isMobile(value)) {
                    Validatemsg = errormsg + "必须为手机格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "MobileOrNull": {
                if (!isMobileOrnull(value)) {
                    Validatemsg = errormsg + "必须为手机格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "MobileOrPhone": {
                if (!isMobileOrPhone(value)) {
                    Validatemsg = errormsg + "必须为电话格式或手机格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "MobileOrPhoneOrNull": {
                if (!isMobileOrPhoneOrNull(value)) {
                    Validatemsg = errormsg + "必须为电话格式或手机格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "Uri": {
                if (!isUri(value)) {
                    Validatemsg = errormsg + "必须为网址格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "UriOrNull": {
                if (!isUriOrnull(value)) {
                    Validatemsg = errormsg + "必须为网址格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "Equal": {
                if (!isEqual(value, $(this).attr("eqvalue"))) {
                    Validatemsg = errormsg + "不相等！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "Date": {
                if (!isDate(value, $(this).attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为日期格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "DateOrNull": {
                if (!isDateOrNull(value, $(obj).attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为日期格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "DateTime": {
                if (!isDateTime(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为日期时间格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "DateTimeOrNull": {
                if (!isDateTimeOrNull(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为日期时间格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "Time": {
                if (!isTime(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为时间格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "TimeOrNull": {
                if (!isTimeOrNull(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为时间格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "ChineseStr": {
                if (!isChinese(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为中文！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "ChineseStrOrNull": {
                if (!isChineseOrNull(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为中文！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "Zip": {
                if (!isZip(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为邮编格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "ZipOrNull": {
                if (!isZipOrNull(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为邮编格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "Double": {
                if (!isDouble(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为小数！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "DoubleOrNull": {
                if (!isDoubleOrNull(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为小数！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "IDCard": {
                if (!isIDCard(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为身份证格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "IDCardOrNull": {
                if (!isIDCardOrNull(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为身份证格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "IsIP": {
                if (!isIP(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为IP格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "IPOrNull": {
                if (!isIPOrNullOrNull(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "必须为IP格式！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "MoneyOrNull": {
                if (!isMoneyOrNull(value, obj.attr("eqvalue"))) {
                    Validatemsg = errormsg + "请输入正确的数字格式，如:1.25！\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            case "MoneyLenOrNull": {
                if (!isMoneyLenOrNull(value, obj.attr("length"))) {
                    Validatemsg = errormsg + "必须少于"+obj.attr("length")+"位字符\n";
                    Validateflag = false;
                    ValidationMessage(obj, Validatemsg);
                    return false;
                }
                return true;
            }
            default:
                return true;
        }
    }

    //验证不为空 notnull
    function isNotNull(obj) {
        obj = $.trim(obj);
        if (obj.length == 0 || obj == null || obj == undefined) {
            return true;
        }
        else
            return false;
    }

    //验证是否包含某个特殊字符
    function isContainsOneChar(obj, value) {
        var str = $.trim(obj);
        if (str.indexOf(value) == -1) {
            return false;//不包含
        } else {
            return true;//包含
        }
    }

    //验证价格，最多有两位小数
    function isPrice(obj) {
        //var reg = /^[\+]?\d+(\.\d{1,2})?$/;
        var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证数字 num
    function isInteger(obj) {
        reg = /^[-+]?\d+$/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证数字 num  或者null,空
    function isIntegerOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        reg = /^[-+]?\d+$/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //Email验证 email
    function isEmail(obj) {
        reg = /^\w{3,}@\w+(\.\w+)+$/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //Email验证 email   或者null,空
    function isEmailOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        reg = /^\w{3,}@\w+(\.\w+)+$/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证只能输入英文字符串 echar
    function isEnglishStr(obj) {
        reg = /^[a-z,A-Z]+$/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证只能输入英文字符串 echar 或者null,空
    function isEnglishStrOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        reg = /^[a-z,A-Z]+$/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证整数小于对应值
    function isLessThan(obj, value) {
        var controlObj = $.trim(obj);
        if (!isIntegerOrNull(controlObj)) {
            return false;
        }
        if (parseInt($.trim(obj)) >= parseInt(value)) {
            return false;
        } else {
            return true;
        }
    }

    //验证整数大于对应值
    function isGreaterThan(obj, value) {
        var controlObj = $.trim(obj);

        if (!isIntegerOrNull(controlObj)) {
            return false;
        }
        if (parseInt($.trim(obj)) <= parseInt(value)) {
            return false;
        } else {
            return true;
        }
    }

    //验证是否是n位数字字符串编号 nnum
    function isLenNum(obj, n) {
        reg = /^[0-9]+$/;
        obj = $.trim(obj);
        if (obj.length > n)
            return false;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证长度是否为n位数字字符串编号 nnum
    function isLenEqualNum(obj, n) {
        reg = /^[0-9]+$/;
        obj = $.trim(obj);
        if (!reg.test(obj)) {
            return false;
        } else if (obj.length != n) {
            return false;
        } else {
            return true;
        }
    }

    //验证数字最高位是否等于n
    function isEqualFirstNum(obj, n) {
        reg = /^[0-9]+$/;
        obj = $.trim(obj);
        if (!reg.test(obj)) {
            return false;
        }
        var first = obj.substring(0, 1);
        if (obj.length != n) {
            return false;
        } else {
            return true;
        }
    }

    //验证8位数，且以8开头
    function isEqualEightNum(obj) {
        reg = /^[0-9]+$/;
        obj = $.trim(obj);
        if (!reg.test(obj)) {
            return false;
        } else if (obj.length != 8) {
            return false;
        }
        var first = obj.substring(0, 1);
        if (first != 8) {
            return false;
        } else {
            return true;
        }
    }

    //验证是否大于n位数字字符串编号 nnum
    function isMinLenNum(obj, n) {
        reg = /^[0-9]+$/;
        obj = $.trim(obj);
        if (obj.length < n)
            return false;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证是否大于n位数字字符串编号 nnum或者null,空
    function isMinLenNumOrNull(obj, n) {
        reg = /^[0-9]+$/;
        obj = $.trim(obj);
        if (obj.length == 0 || obj == null || obj == undefined) {
            return true;
        }
        if (obj.length < n)
            return false;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证是否是n位数字字符串编号 nnum或者null,空
    function isLenNumOrNull(obj, n) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        reg = /^[0-9]+$/;
        obj = $.trim(obj);
        if (obj.length > n)
            return false;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证是否小于等于n位数的字符串 nchar
    function isLenStr(obj, n) {
        obj = $.trim(obj);
        if (obj.length == 0 || obj.length > n)
            return false;
        else
            return true;
    }

    //验证是否多于等于n位数的字符串 nchar
    function isMinLenStr(obj, n) {
        obj = $.trim(obj);
        if (obj.length == 0 || obj.length < n)
            return false;
        else
            return true;
    }

    //验证是否小于等于n位数的字符串 nchar或者null,空
    function isLenStrOrNull(obj, n) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        obj = $.trim(obj);
        if (obj.length > n)
            return false;
        else
            return true;
    }

    //验证是否多于等于n位数的字符串 nchar或者null,空
    function isMinLenStrOrNull(obj, n) {
        obj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        if (obj.length < n)
            return false;
        else
            return true;
    }

    //验证是否电话号码 phone
    function isTelephone(obj) {
        reg = /^(\d{3,4}\-)?[1-9]\d{6,7}$/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证是否电话号码 phone或者null,空
    function isTelephoneOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        reg = /^(\d{3,4}\-)?[1-9]\d{6,7}$/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证是否手机号 mobile
    function isMobile(obj) {
        reg = /^(\+\d{2,3}\-)?\d{11}$/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证是否手机号 mobile或者null,空
    function isMobileOrnull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        reg = /^(\+\d{2,3}\-)?\d{11}$/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证是否手机号或电话号码 mobile phone
    function isMobileOrPhone(obj) {
        reg_mobile = /^(\+\d{2,3}\-)?\d{11}$/;
        reg_phone = /^(\d{3,4}\-)?[1-9]\d{6,7}$/;
        if (!reg_mobile.test(obj) && !reg_phone.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证是否手机号或电话号码 mobile phone或者null,空
    function isMobileOrPhoneOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        reg = /^(\+\d{2,3}\-)?\d{11}$/;
        reg2 = /^(\d{3,4}\-)?[1-9]\d{6,7}$/;
        if (!reg.test(obj) && !reg2.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证网址 uri
    function isUri(obj) {
        reg = /^http:\/\/[a-zA-Z0-9]+\.[a-zA-Z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证网址 uri或者null,空
    function isUriOrnull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        reg = /^http:\/\/[a-zA-Z0-9]+\.[a-zA-Z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
        if (!reg.test(obj)) {
            return false;
        } else {
            return true;
        }
    }

    //验证两个值是否相等 equals
    function isEqual(obj1, controlObj) {
        if (obj1.length != 0 && controlObj.length != 0) {
            if (obj1 == controlObj)
                return true;
            else
                return false;
        }
        else
            return false;
    }

    //判断日期类型是否为YYYY-MM-DD格式的类型 date
    function isDate(obj) {
        if (obj.length != 0) {
            reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
            if (!reg.test(obj)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    //判断日期类型是否为YYYY-MM-DD格式的类型 date或者null,空
    function isDateOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        if (obj.length != 0) {
            reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
            if (!reg.test(obj)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    //判断日期类型是否为YYYY-MM-DD hh:mm:ss格式的类型 datetime
    function isDateTime(obj) {
        if (obj.length != 0) {
            reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
            if (!reg.test(obj)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    //判断日期类型是否为YYYY-MM-DD hh:mm:ss格式的类型 datetime或者null,空
    function isDateTimeOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        if (obj.length != 0) {
            reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
            if (!reg.test(obj)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    //判断日期类型是否为hh:mm:ss格式的类型 time
    function isTime(obj) {
        if (obj.length != 0) {
            reg = /^((20|21|22|23|[0-1]\d)\:[0-5][0-9])(\:[0-5][0-9])?$/;
            if (!reg.test(obj)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    //判断日期类型是否为hh:mm:ss格式的类型 time或者null,空
    function isTimeOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        if (obj.length != 0) {
            reg = /^((20|21|22|23|[0-1]\d)\:[0-5][0-9])(\:[0-5][0-9])?$/;
            if (!reg.test(obj)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    //判断输入的字符是否为中文 cchar
    function isChinese(obj) {
        if (obj.length != 0) {
            reg = /^[\u0391-\uFFE5]+$/;
            if (!reg.test(str)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    //判断输入的字符是否为中文 cchar或者null,空
    function isChineseOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        if (obj.length != 0) {
            reg = /^[\u0391-\uFFE5]+$/;
            if (!reg.test(str)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    //判断输入的邮编(只能为六位)是否正确 zip
    function isZip(obj) {
        if (obj.length != 0) {
            reg = /^\d{6}$/;
            if (!reg.test(str)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    //判断输入的邮编(只能为六位)是否正确 zip或者null,空
    function isZipOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        if (obj.length != 0) {
            reg = /^\d{6}$/;
            if (!reg.test(str)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    //判断输入的字符是否为双精度 double
    function isDouble(obj) {
        if (obj.length != 0) {
            reg = /^[-\+]?\d+(\.\d+)?$/;
            if (!reg.test(obj)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    //判断输入的字符是否为双精度 double或者null,空
    function isDoubleOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        if (obj.length != 0) {
            reg = /^[-\+]?\d+(\.\d+)?$/;
            if (!reg.test(obj)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    //判断是否为身份证 idcard
    function isIDCard(obj) {
        if (obj.length != 0) {
            reg = /^\d{15}(\d{2}[A-Za-z0-9;])?$/;
            if (!reg.test(obj))
                return false;
            else
                return true;
        }
    }

    //判断是否为身份证 idcard或者null,空
    function isIDCardOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        if (obj.length != 0) {
            reg = /^\d{15}(\d{2}[A-Za-z0-9;])?$/;
            if (!reg.test(obj))
                return false;
            else
                return true;
        }
    }

    //判断是否为IP地址格式
    function isIP(obj) {
        var re = /^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g //匹配IP地址的正则表达式 
        if (re.test(obj)) {
            if (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256) return true;
        }
        return false;
    }

    //判断是否为IP地址格式 或者null,空
    function isIPOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        var re = /^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g //匹配IP地址的正则表达式 
        if (re.test(obj)) {
            if (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256) return true;
        }
        return false;
    }

    //验证价格
    function isMoneyOrNull(obj) {
        var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
        if (reg.test(obj)) {
            return true;
        }
        return false;
    }
    
    function isMoneyLenOrNull(obj,n){
    	var controlObj = $.trim(obj);
        if (controlObj.length == 0 || controlObj == null || controlObj == undefined) {
            return true;
        }
        var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
        if(obj.length > n){
        	return false;
        }
        if (reg.test(obj)) {
            return true;
        }
        return false;
    }

    //提示信息
    function ValidationMessage(obj, Validatemsg) {
        try {
            removeMessage(obj);
            if (focusflag == false) {
                obj.focus();
                focusflag = true;
            }
            // var $poptip_error = $('<div class="poptip"><span class="poptip-arrow poptip-arrow-top"><em>◆</em></span>' + Validatemsg + '</div>').css("left", obj.offset().left + 'px').css("top", obj.offset().top + obj.parent().height() + 5 + 'px');
            var $poptip_error = $('<div class="poptip"><span class="poptip-arrow poptip-arrow-top"><em>◆</em></span>' + Validatemsg + '</div>').css("top", 31 + 'px');
            obj.parent().append($poptip_error);
            if (obj.hasClass('form-control') || obj.hasClass('ui-select')) {
                obj.parent().addClass('has-error');
            }
            if (obj.hasClass('ui-select')) {
                $('.input-error').remove();
            }

            obj.bind('focus', function () {
                removeMessage(obj);
            });
            obj.bind('input propertychange', function () {
                if (obj.val()) {
                    removeMessage(obj);
                }
            });
            if (obj.hasClass('ui-select')) {
                $(document).click(function (e) {
                    if (obj.attr('data-value')) {
                        removeMessage(obj);
                    }
                    e.stopPropagation();
                });
            }
            return false;
        } catch (e) {
            alert(e)
        }
    }

//移除提示
    function removeMessage(obj) {
        obj.parent().removeClass('has-error');
        obj.siblings('.poptip').remove();
        // $('.poptip').remove();
        $('.input-error').remove();
    }
}
