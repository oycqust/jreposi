
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-CN">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta charset="UTF-8">
    <title>媒质code管理界面</title>
    <%@ include file="/common/common.jsp" %>
    <script src="<%=request.getContextPath()%>/query/index.js"></script>
    <script src="<%=request.getContextPath()%>/query/tableExport.js"></script>
    <script src="<%=request.getContextPath()%>/query/bootstrap-table-export.js"></script>
    <script src="<%=request.getContextPath()%>/plug-in/bootstrap-fileinput-master/js/fileinput.min.js"></script>
    <script src="<%=request.getContextPath()%>/plug-in/bootstrap-fileinput-master/js/locales/zh.js"></script>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/plug-in/bootstrap-fileinput-master/css/fileinput.min.css"/>

</head>
<body>
<script>
    function operateFormatter(value, row, index) {
        return [
            '<button type="button" class="RoleOfdelete btn btn-primary  btn-sm" style="margin-right:15px;">删除</button>',

            '<button type="button" class="RoleOfedit btn btn-primary  btn-sm" style="margin-right:15px;">修改</button>'
        ].join('');
    }
    $(function () {
        //0.初始化fileinput
        var oFileInput = new FileInput();
        oFileInput.Init("txt_file", projectName+"/index/excelInput.do");
    });

</script>




<section class="content">
    <div class="content-box">
        <%--*************************************************************************************************************--%>
        <div class="search-box">
            <form id="formSearch" class="form-horizontal">
                <div class="form-group" style="font-size:24px;text-align:center" >统一媒资管理</div>
                <input type="text" style="display:none" />

                <!-- 第一行 -->
                <div class="form-group">
                    <label class="control-label col-sm-2" for="medianame">媒质名称</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" id="medianame" name="medianame">
                    </div>

                    <label class="control-label col-sm-1" for="mediacode">媒质code</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" id="mediacode" name="mediacode">
                    </div>

<%--
                    <label class="control-label col-sm-1" for="sysid">运营商类型</label>
--%>
                    <%--<div   class="col-sm-2">
                        <select multiple id="sysid" class="form-control select2" name="sysid">
                            <option></option>
                            <option value="m">移动</option>
                            <option value="t">电信</option>
                            <option value="u">联通</option>
                        </select>
                    </div>--%>
                    <div   class="col-sm-1">
                        <a class="btn-material btn-search" id="btn_query" title="搜索按钮"><i class="iconfont icon-sousuo"></i></a>
                    </div>
                    <div class="col-sm-1">
                        <a id="btn_add" class="btn btn-primary  btn-sm glyphicon glyphicon-plus" style="margin-right:15px;">新增</a>
                    </div>
                    <div class="col-sm-1">
                        <a id="btn_remove" class="btn btn-primary  btn-sm glyphicon glyphicon-remove" style="margin-right:15px;">批量删除</a>
                    </div>
                </div>

            </form>
        </div>


        <%--*************************************************************************************************************--%>

        <%--*************************************************************************************************************--%>

        <div class="table-scrollable">
            <table class="table-hover text-nowrap" id="listViewingLogTable"></table>
        </div>

          <%--  <button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
                批量导入
            </button>--%>
            <!-- 模态框（Modal） -->
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">请选择Excel文件</h4>
                        </div>
                        <div class="modal-body">
                            <a href="/acp/excelTemplate/example.xls" download=""   class="form-control" style="border:none;">下载导入模板</a>
                            <input type="file" name="txt_file" id="txt_file" multiple class="file-loading" />
                        </div></div>
                </div>
            </div>

            <div class="modal fade" id="updatemyModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog modal-full" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="updatemyModalLabel">修改媒资</h4>
                        </div>
                        <div class="modal-body">
                            <input type="text" name="update_key" style="display:none"  class="form-control" id="update_key" >
                            <div class="form-group">
                                <label for="update_medianame">媒资名称</label>
                                <input type="text" name="update_medianame" class="form-control" id="update_medianame" placeholder="媒资名称">
                            </div>
                            <div class="form-group">
                                <label for="update_mediacode">媒资code</label>
                                <input type="text" name="update_mediacode" class="form-control" id="update_mediacode" placeholder="媒资code">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
                            <button type="button" id="btn_submit" class="btn btn-primary" data-dismiss="modal"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
                        </div>
                    </div>
                </div>
            </div>
    </div>
</section>


</body>
</html>

