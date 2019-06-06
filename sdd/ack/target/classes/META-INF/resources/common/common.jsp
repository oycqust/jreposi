<%--
  Created by IntelliJ IDEA.
  User: UTSC0885
  Date: 2017/3/28
  Time: 21:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type="text/javascript">
    /****************************判断是低于IE10***************************************/
    try {
        var browser = navigator.appName
        var version = navigator.appVersion.split(";");
        var trim_Version;
        if (version.length == 2) {
            trim_Version = version[1].replace(/[ ]/g, "")
        } else {
            trim_Version = version[0].replace(/[ ]/g, "");
        }
        var flag = false;
        if (browser == "Microsoft Internet Explorer") {
            if (trim_Version == "MSIE6.0" || trim_Version == "MSIE7.0" || trim_Version == "MSIE7.0" || trim_Version == "MSIE8.0" || trim_Version == "MSIE9.0") {
                flag = true;
            }
        }
        if (flag == true) {
            alert("你使用的浏览器是：" + browser + "(" + trim_Version + ")" + ",会出现界面异常问题,请使用IE11以上,谷歌,火狐路浏览器");
        }
    } catch (err) {
        txt = "此浏览器可能存在兼容性问题。\n\n";
        txt += "错误描述: " + err.description + "\n\n";
        txt += "点击OK继续。\n\n";
        alert(txt);
    }

    /****************************获取项目的根目录***************************************/
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： iptvsmp/login.do
    var pathName = window.document.location.pathname;

    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8080
    var localhostPaht = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/iptvsmp
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);

    /****************************获取项目的sessionInfoName对象***************************************/
    var staffid = "${sessionScope.sessionInfoName.staff.staffid}"
</script>

<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

<!-- jquery-ui -->
<link rel="stylesheet" type="text/css" media="screen"
      href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-ui-1.12.1/jquery-ui.css"/>
<!-- Bootstrap 3.3.6 -->
<link rel="stylesheet"
      href="<%=request.getContextPath()%>/plug-in/superui/content/ui/global/bootstrap/css/bootstrap.css">
<!-- Font Awesome -->
<link href="<%=request.getContextPath()%>/plug-in/superui/content/ui/global/font-awesome/css/font-awesome.css"
      rel="stylesheet"/>
<!-- Theme style -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/plug-in/superui/content/adminlte/dist/css/AdminLTE.css">
<link rel="stylesheet"
      href="<%=request.getContextPath()%>/plug-in/superui/content/adminlte/dist/css/skins/skin-blue.css">
<link href="<%=request.getContextPath()%>/plug-in/superui/content/min/css/supershopui.common.css"
      rel="stylesheet"/>
<link href="<%=request.getContextPath()%>/plug-in/superui/content/supermgr/Base/css/common.css"
      rel="stylesheet"/>
<!-- bootstrap-table -->
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/bootstrap-table/bootstrap-table.css"
      rel="stylesheet"/>
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/bootstrap-table/bootstrap-editable.css"
      rel="stylesheet"/>
<!-- date-range-picker -->
<link rel="stylesheet"
      href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/daterangepicker/daterangepicker.css">
<!-- bootstrap datepicker -->
<link rel="stylesheet"
      href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/datepicker/datepicker3.css">
<!-- Bootstrap time Picker -->
<link rel="stylesheet"
      href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/timepicker/bootstrap-timepicker.min.css">
<!-- bootstrap check -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/iCheck/all.css">
<!-- select2 -->
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/select2/css/select2.min.css"
      rel="stylesheet"/>
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/select2/css/select2-bootstrap.min.css"
      rel="stylesheet"/>
<!-- jstree -->
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jstree/dist/themes/default/style.css"
      rel="stylesheet"/>
<!-- jqgrid -->
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jqgrid/css/ui.jqgrid-bootstrap.css"
      rel="stylesheet" type="text/css" media="screen"/>
<!-- bootstrap-switch -->
<link href="<%=request.getContextPath()%>/plug-in/superui/content/ui/global/bootstrap-switch/css/bootstrap-switch.css"
      rel="stylesheet"/>
<!-- iconfont -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/img/iconfont/iconfont.css">
<!-- bootstrap-datetimepicker -->
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"
      rel="stylesheet" type="text/css"/>
<!-- bootstrap-treeview -->
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/bootstrap-treeview/css/bootstrap-treeview.css"
      rel="stylesheet" type="text/css"/>
<!-- toolbar -->
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/toolbar/jquery.toolbar.css" rel="stylesheet"
      type="text/css"/>
<!-- layout -->
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/layout/layout-default-latest.css"
      rel="stylesheet"
      type="text/css"/>
<!-- jquery-file-upload -->
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/blueimp-gallery/blueimp-gallery.min.css"
      rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/css/jquery.fileupload.css"
      rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/css/jquery.fileupload-ui.css"
      rel="stylesheet" type="text/css"/>


<!-- jQuery 2.2.3 -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/ui/global/jQuery/jquery.min.js"></script>
<!-- template.js -->
<script src="<%=request.getContextPath() %>/js/template.js"></script>
<script src="<%=request.getContextPath() %>/js/errcode.js"></script>
<script src="<%=request.getContextPath() %>/js/echarts.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/ui/global/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/ui/global/jquery-blockui/jquery.blockui.min.js"></script>
<!-- util -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/util/webUtil.js"></script>
<!-- validator -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/validator/validator.js"></script>
<!-- common -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/min/js/supershopui.common.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/supermgr/Base/js/common.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/ui/base/scripts/App.Extend.js"></script>
<!-- layer -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/ui/global/layer/layer.js"></script>
<!-- bootstrap-table -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/bootstrap-table/bootstrap-table.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/bootstrap-table/bootstrap-editable.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/bootstrap-table/bootstrap-table-editable.js"></script>
<!-- InputMask -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/input-mask/jquery.inputmask.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/input-mask/jquery.inputmask.date.extensions.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/input-mask/jquery.inputmask.extensions.js"></script>
<!-- date-range-picker -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/daterangepicker/daterangepicker.js"></script>
<!-- bootstrap datepicker -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/datepicker/bootstrap-datepicker.js"></script>
<!-- bootstrap check -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/iCheck/icheck.min.js"></script>
<!-- select2 -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/select2/js/select2.full.min.js"></script>
<!-- jquery-ui -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-ui-1.12.1/jquery-ui.js"></script>
<!-- jstree -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jstree/dist/jstree.min.js"></script>
<!-- jqgrid -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jqgrid/grid.locale-cn.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jqgrid/jquery.jqGrid.js"></script>
<!-- bootstrap-switch -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/ui/global/bootstrap-switch/js/bootstrap-switch.js"></script>
<!-- iconfont -->
<script src="<%=request.getContextPath()%>/img/iconfont/iconfont.js"></script>
<!-- bootstrap-datetimepicker -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<!-- bootstrap-contextmenu -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/contextmenu/bootstrap-contextmenu.js"></script>
<!-- bootstrap-treeview -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/bootstrap-treeview/js/bootstrap-treeview.js"></script>
<!-- toolbar -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/toolbar/jquery.toolbar.js"></script>
<!--laydate-->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/laydate/laydate.js"></script>
<!-- layout -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/layout/jquery.layout-latest.js"></script>
<!-- jquery-file-upload -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/js/vendor/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/js/vendor/tmpl.min.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/js/vendor/load-image.min.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/js/vendor/canvas-to-blob.min.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/blueimp-gallery/jquery.blueimp-gallery.min.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/js/jquery.iframe-transport.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/js/jquery.fileupload.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/js/jquery.fileupload-process.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/js/jquery.fileupload-image.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/js/jquery.fileupload-audio.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/js/jquery.fileupload-video.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/js/jquery.fileupload-validate.js"></script>
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/jquery-file-upload/js/jquery.fileupload-ui.js"></script>
<!-- base64 -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/base64/jquery.base64.js"></script>
<!-- tableDnD -->
<script src="<%=request.getContextPath()%>/plug-in/superui/content/plugins/tablednd/jquery.tablednd.js"></script>

<script>
    //ajax请求发生错误时回调（全局监听）
    $(document).ajaxError(function (e, jqXHR, options, errorMsg) {
        if (jqXHR.status == "401") {
            var top = getTopWinow();
            top.location.href = '<%=request.getContextPath()%>';
        }
    });

    function getTopWinow() {
        var p = window;
        while (p != p.parent) {
            p = p.parent;
        }
        return p;
    }

    //ajax请求前时回调（全局监听）
    var pendingRequests = {};
    jQuery.ajaxPrefilter(function (options, originalOptions, jqXHR) {
        var key = options.url;
        if (!pendingRequests[key]) {
            pendingRequests[key] = jqXHR;
        } else {
            //jqXHR.abort(); //放弃后触发的提交
            pendingRequests[key].abort(); // 放弃先触发的提交
        }
        var complete = options.complete;
        options.complete = function (jqXHR, textStatus) {
            pendingRequests[key] = null;
            if (jQuery.isFunction(complete)) {
                complete.apply(this, arguments);
            }
        };
    });

    $(function () {

        $.jgrid.defaults.styleUI = 'Bootstrap';
        $.fn.select2.defaults.set("theme", "bootstrap");
        var placeholder = "请选择";

        var $ichekbox = $('input[type="checkbox"].flat-red, input[type="radio"].flat-red');
        $ichekbox.iCheck({
            checkboxClass: 'icheckbox_flat-green',
            radioClass: 'iradio_flat-green'
        });
        $ichekbox.val(0);
        $ichekbox.on('ifChecked', function (event) {
            $(this).val(1);
        });
        $ichekbox.on('ifUnchecked', function (event) {
            $(this).val(0);
        });

        var $bootstrapSwitch = $('input[type="checkbox"].check-switch');
        $bootstrapSwitch.val('0');
        $bootstrapSwitch.bootstrapSwitch({
            size: 'small',
            onSwitchChange: function (event, state) {
                if (state == true) {
                    $(this).val('1');
                } else {
                    $(this).val('0');
                }
            }
        });

        $(".select2, .select2-multiple").select2({
            placeholder: placeholder,
            width: null,
            minimumResultsForSearch: -1,
            allowClear: true
        });
        //删除select2验证出现提示
        $('.select2, .select2-multiple').on('select2:select', function (evt) {
            if ($(this).val()) {
                $(this).parent().removeClass('has-error');
                $(this).siblings('.poptip').remove();
                $('.input-error').remove();
            }
        });
    });
</script>

<!-- common 确保该css最后执行，因此放在最后 -->
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/common/common.css"/>
<script src="<%=request.getContextPath()%>/common/common.js"></script>

