<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>测试报告-汇总</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap -->
    <link href="${projectDomain}/static/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="${projectDomain}/static/css/dashboard.css" rel="stylesheet" media="screen">
</head>
<body>
<div class="container small">
    <h2 class="sub-header">汇总</h2>
    <table class="table table-striped table-sm">
        <thead>
        <tr>
            <th>任务总数</th>
            <th>成功数</th>
            <th>失败数</th>
            <th>总时长(秒)</th>
            <th>通过率</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><span class="badge badge-info">${report.totalCount}</span></td>
            <td><span class="badge badge-success">${report.successCount}</span></td>
            <td><span class="badge badge-danger">${report.failCount}<span</td>
            <td><span class="text-point text-big">${(report.duration/1000.0)?string("#.##")}</span></td>
            <td>
                <span class="badge badge-pill badge-success text-point text-big">${(report.successCount*100/report.totalCount)?string("#.##")}%</span>
            </td>
        </tr>
        </tbody>
    </table>
    <h2 class="sub-header mt-4">测试任务统计</h2>
    <table class="table table-striped table-sm">
        <thead>
        <tr>
            <th>ID</th>
            <th>任务名称</th>
            <th>项目</th>
            <th>模块</th>
            <th>责任人</th>
            <th>开始时间</th>
            <th>总数</th>
            <th>成功数</th>
            <th>失败数</th>
            <th>总时长(秒)</th>
            <th>通过率</th>
            <th>失败原因</th>
        </tr>
        </thead>
        <tbody>
        <#list list as o>
            <tr>
                <td>${o.testCasesId}</td>
                <td><a id="${o.testCasesId}" name="testCasesName" href="#">${o.testCasesName}</a></td>
                <td>${o.projectName}</td>
                <td>${o.moduleName}</td>
                <td>${o.ownerName}</td>
                <td>${o.startTime?datetime}</td>
                <td>${o.totalCount}</td>
                <td>${o.successCount}</td>
                <td>${o.failCount}</td>
                <td>${(o.duration/1000.0)?string("#.##")}</td>
                <td><#if o.totalCount gt 0>${(o.successCount*100/o.totalCount)?string("#.##")}<#else>0</#if>%
                </td>
                <td><#if o.runState==1>
                        <span class="badge badge-success">成功</span>
                    <#elseif o.hasExceptions>
                        <span class="badge badge-danger">程序异常</span>
                    <#else>
                        <span class="badge badge-warning">断言失败</span>
                    </#if></td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>

<script src="${projectDomain}/static/js/jquery.js"></script>
<script src="${projectDomain}/static/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function () {
        $("a[name=testCasesName]").click(function () {
            var id = this.getAttribute("id");
            var url = "${projectDomain}/view/report/details/${report.reportUuid}/" + id;
            window.open(url, "_self");
        });
    });
</script>
</body>
</html>