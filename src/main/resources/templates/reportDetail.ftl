<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>测试报告-${detail.testCasesName}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap -->
    <link href="${projectDomain}/static/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="${projectDomain}/static/css/dashboard.css" rel="stylesheet" media="screen">
</head>
<body>
<#function toLogString logDataList dataIndex=0 depth=0 cardIndex=0 pretitle=''>
<#--函数就算写了其它页面代码也不会显示-->
    <#if !(logDataList??)>
        <#return ''/>
    </#if>
    <#local logStr = ''>
    <#list logDataList as log>
        <#if log.title??>
            <#local showInnerTitle =false>
            <#local thisTitle = ''>
            <#if pretitle!=''>
                <#local thisTitle = pretitle+" —— "+log.title>
            <#else>
                <#local thisTitle = log.title>
            </#if>
            <#if depth==0>
                <#local vindex =dataIndex*100+cardIndex>
                <#local btnClass ="btn-success">
                <#if log.status??>
                    <#if log.status == 1>
                        <#local btnClass ="btn-warning">
                    <#elseif  log.status==2>
                        <#local btnClass ="btn-danger">
                    </#if>
                </#if>
                <#local logStr +="<div class='card'><div class='card-header' id='heading"+vindex+"'><h2 class='mb-0'><button class='btn "+ btnClass +"' type='button' data-toggle='collapse' data-target='#collapse"+vindex+"' aria-expanded='true' aria-controls='collapse"+vindex+"'>"+log.title?html+"</button>">
                <#if log.noticeMsg??>
                    <#list log.noticeMsg?split(",") as x>
                        <#local logStr +="<span class='badge badge-pill badge-secondary align-top' style='font-size:0.5rem'>"+x+"</span>">
                    </#list>
                </#if>
                <#local logStr +="</h2></div><div id='collapse"+vindex+"' class='collapse' aria-labelledby='heading"+vindex+"' data-parent='#accordion'><div class='card-body'>" >
                <#local cardIndex = cardIndex+1 >
            <#elseif depth <= 2 >
                <#local showInnerTitle = true>
                <#local spanClass ="btn-success">
                <#if log.status??>
                    <#if log.status == 1>
                        <#local spanClass ="badge-warning">
                    <#elseif  log.status==2>
                        <#local spanClass ="badge-danger">
                    </#if>
                </#if>
                <#local logStr +="<div class='card'><div class='card-header'><span class='m-1 p-2 badge "+ spanClass +"' style='font-size:0.75rem'>"+thisTitle?html+"</span>">
                <#if log.noticeMsg??>
                    <#list log.noticeMsg?split(",") as x>
                        <#local logStr +="<span class='badge badge-pill badge-secondary align-top' style='font-size:0.5rem'>"+x+"</span>">
                    </#list>
                </#if>
                <#local logStr +="</div><div class='card-body'>" >
            <#else>
                <#local logStr += "<span class='text text-point text-warning' style='padding-left:" + 20*depth +"px'>"+log.title?html+"</span><br/>">
            </#if>
            <#local i = depth+1 >
            <#local logStr += toLogString(log.logDataList, dataIndex, i, cardIndex, thisTitle)>
            <#if depth==0>
                <#local logStr +="</div></div></div>">
            <#elseif showInnerTitle>
                <#local logStr +="</div></div>">
            </#if>
        <#else>
            <#local logStr += "<span class='text text-point text-big' style='padding-left:" + 20*(depth) +"px'>"+log.time?number_to_datetime +"</span>
            <div style='white-space: pre-line;padding-left:" + 20*(depth) +"px'>"+log.text?html+"</div><br/>">
        </#if>
    </#list>
    <#return logStr/>
</#function>

<div class="container-fluid small">
    <div class="row">
        <div class="col-4">
            <div class="card">
                <div class="card-header">
                    <h5>${detail.testCasesName}（${detail.projectName} • ${detail.moduleName}）</h5>
                </div>
                <div id="mainTitle" class="card-body" style="overflow-y:scroll;overflow-x: hidden; ">
                    <table class="table table-striped table-sm"
                           style="word-break:break-all; word-wrap:break-all;">
                        <thead>
                        <tr>
                            <th scope="col">状态</th>
                            <th scope="col">步骤</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list detail.details as dlog>
                            <tr>
                                <td>
                                    <#if dlog.status==2>
                                        <span class="badge badge-danger">程序异常</span>
                                    <#elseif dlog.status==1>
                                        <span class="badge badge-warning">断言失败</span>
                                    <#else>
                                        <span class="badge badge-success">成功</span>
                                    </#if>
                                </td>
                                <td id="func_${dlog_index}" class="text text-primary text-point"
                                    style="cursor: pointer" id-index="${dlog_index}">
                                    ${dlog.title?html}
                                </td>
                                <textarea id="dlog_${dlog_index}"
                                          style="display: none">${toLogString(dlog.logDataList, dlog_index)?html}</textarea>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-8">
            <div class="card">
                <div class="card-header">
                    <h5>详细日志 <span id="log-sub-title" class="badge badge-info">start</span><a
                                href="${projectDomain}/view/tool/freemarker" target="_blank"><span id="s-sub-title"
                                                                                                   class="badge badge-pill badge-primary float-right">Freemarker调式工具</span></a>
                    </h5>
                </div>
                <div class="card-body" id="detailLog"
                     style="overflow-y:scroll;overflow-x: hidden;  word-break:break-all; word-wrap:break-all;border-top: 2px solid #ddd;color: #333;">

                </div>
            </div>
        </div>
    </div>
</div>
<script src="${projectDomain}/static/js/jquery.js"></script>
<script src="${projectDomain}/static/js/bootstrap.min.js"></script>
<script type="text/javascript">

    $(document).ready(function () {
        showDetailLog(0);
        $("td[id^='func_']").click(function () {
            i = $(this).attr("id-index");
            showDetailLog(i);
        }).hover(function () {
            $(this).addClass("table-success");
        }, function () {
            $(this).removeClass("table-success");
        });
    });


    function resizeTextWindow() {
        var screenHeight = document.documentElement.clientHeight;
        var screenWidth = document.documentElement.clientWidth;
        var detailLog = document.getElementById('detailLog');
        detailLog.style.height = screenHeight * 0.85 + "px";
        var mainTitle = document.getElementById('mainTitle');
        mainTitle.style.height = screenHeight * 0.85 + "px";
    }

    function showDetailLog(index) {
        resizeTextWindow();
        $('#detailLog').html("<div class='accordion' id='accordion'>" + $('#dlog_' + index).val() + "</div>");
        $('#log-sub-title').html($('#func_' + index).html());
        $('#detailLog').scrollTop(-10000);
    }
</script>
</body>
</html>