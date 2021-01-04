<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>工具-FreeMarker调试</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap -->
    <link href="${projectDomain}/static/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="${projectDomain}/static/css/dashboard.css" rel="stylesheet" media="screen">
    <style>
        .form-control::-moz-placeholder {
            color: #c3c3c3;
            font-size: 0.5rem;
            opacity: 1;
        }

        .form-control:-ms-input-placeholder {
            color: #c3c3c3;
            font-size: 0.5rem;
        }

        .form-control::-webkit-input-placeholder {
            color: #c3c3c3;
            font-size: 0.5rem;
        }
    </style>
</head>
<body>

<div class="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom shadow-sm">
    <h3 class="my-0 mr-md-auto font-weight-normal"><span class="badge badge-pill badge-primary mr-1">FreeMarker</span>调试工具</h3>
    <nav class="my-2 my-md-0 mr-md-3">
        <a class="p-2 text-point"  href="https://git.winbaoxian.com/wy_lab/freemarker-functions" target="_blank" >常用语法</a>
        <a class="p-2 text-point" href="http://freemarker.foofun.cn/" target="_blank" >freemarker官网</a>
    </nav>
</div>
<div class="container-fluid small">
    <div class="input-group">
        <div class="input-group-prepend">
            <span class="input-group-text text-point text-success align-self-auto">全局数据（JSON）</span>
        </div>
        <textarea id="input-data" class="form-control" aria-label="全局数据（JSON）" rows="8"
                  placeholder="JSON对象，kv格式（非必填） &#13;&#10;例如：{&quot;code&quot;:200,&quot;msg&quot;:null,&quot;data&quot;:{&quot;pageNum&quot;:1,&quot;list&quot;:[{&quot;id&quot;:16,&quot;key&quot;:&quot;brokerage_toa_server&quot;,&quot;description&quot;:&quot;微易经纪 to APP 接口server&quot;,&quot;deleted&quot;:false}],&quot;startRow&quot;:0}}&#13;&#10;常用函数：&#13;&#10;-转json对象 ＄{toJSON('jsonStr')} 返回JSON对象&#13;&#10;-转字符串 ＄{toJSONString(jsonObj)}&#13;&#10;-当前时间 ＄{.now} 2019-11-18 15:02:53&#13;&#10;-根据年龄获取生日 ＄{getBirthday(31)} 1988-02-11&#13;&#10;-根据当前时间获取时间 ＄{getDateByDelta(1, 1, 1)} 2020-01-11&#13;&#10;-生成数字列表 ＄{toJSONString(range(1,4))}  [1,2,3,4]&#13;&#10;-提取URL数据 ＄{urlExtract('url').getPath()} ＄{urlExtract('url').getParameter('searchKey')}&#13;&#10;-随机数 ＄{random()}&#13;&#10;-笛卡尔函数 ＄{toJSONString(descartes('a',[1,2],'b',[5,6]))}  [{'a':1,'b':5},{'a':1,'b':6},{'a':2,'b':5},{'a':2,'b':6}]&#13;&#10;-获取身份证号 ＄{generateIdNum('1987-09-12',1)} 330521198709121121&#13;&#10;-列表截断 ＄{toJSONString(list[0..2])}&#13;&#10;-jsonPath提取数据 ＄{jsonPathExtract(data, '＄.info')}&#13;&#10;-提取list数据 ＄{listByKey(listObject, 'keyName')}&#13;&#10;-数字四舍五入 ＄{round(number, scale)}&#13;&#10;-对象特征值 ＄{signature(object)}"></textarea>
    </div>
    <div class="input-group mt-1">
        <select id="select-func-name" class="custom-select" id="inputGroupSelect04"
                aria-label="Example select with button addon">
            <option selected value="" class="badge">--请选择--</option>
            <option value="toJSON('jsonStr')" class="badge text-success">toJSON(jsonStr) 返回json对象</option>
            <option value="toJSONString(jsonObject)" class="badge text-success">toJSONString(jsonObject) 转字符串
            </option>
            <option value="toJSONString(jsonObject, 1)" class="badge text-success">toJSONString(jsonObject,1)
                转字符串(格式化输出)
            </option>
            <option value=".now" class="badge text-primary">.now 当前时间</option>
            <option value="getBirthday(age)" class="badge text-primary">getBirthday(age) 根据年龄获取生日</option>
            <option value="getDateByDelta(yearDelta, monthDelta, dateDelta)" class="badge text-primary">
                getDateByDelta(yearDelta,monthDelta,dateDelta) 根据当前时间获取时间
            </option>
            <option value="range(start, end)" class="badge text-warning">range(start, end) 生成数字列表，返回list对象</option>
            <option value="urlExtract('url').getPath()" class="badge text-warning">urlExtract('url').getPath()
                提取URL数据-path
            </option>
            <option value="urlExtract('url').getParameter('keyName')" class="badge text-warning">
                urlExtract('url').getParameter('keyName') 提取URL数据-参数
            </option>
            <option value="random()" class="badge text-danger">random() 随机数</option>
            <option value="descartes('name1',list,'name2',list ..)" class="badge text-danger">
                descartes('name1',list,'name2',list ..)
                笛卡尔函数
            </option>
            <option value="generateIdNum('birthDayStr',sex)" class="badge text-danger">generateIdNum('birthDayStr',sex)
                获取身份证号
            </option>
            <option value="jsonPathExtract(data, 'jsonPathRegex')" class="badge text-success">jsonPathExtract(data,
                'jsonPathRegex') jsonPath提取数据
            </option>
            <option value="listByKey(listObject, 'keyName')" class="badge text-success">listByKey(listObject, 'keyName')
                list中提取名字为key的数据
            </option>
            <option value="round(number, scale)" class="badge text-success">round(number, scale) 数字四舍五入</option>
            <option value="signature(object)" class="badge text-primary">signature(object) 对象特征值</option>
        </select>
        <div class="input-group-append">
            <button id="insert-func-button" class="btn btn-outline-success" type="button">插入函数</button>
        </div>
    </div>
    <div class="input-group mt-3">
        <div class="input-group-prepend">
            <span class="input-group-text text-point text-success">FreeMarker 表达式</span>
        </div>
        <textarea id="input-expression" class="form-control" aria-label="FreeMarker表达式" rows="6"></textarea>
    </div>
    <div class="btn-group btn-block mt-2">
        <button id="escape-button" class="btn btn-outline-success rounded btn-sm" type="button">ESCAPE</button>
        <button id="unescape-button" class="btn btn-outline-success rounded btn-sm" type="button">UNESCAPE</button>
        <button id="run-button" class="btn btn-primary rounded text-point pl-3" type="button">执 行</button>
    </div>
    <div class="card mt-3">
        <div class="card-header">
            <span class="text-point text-big">结 果</span><span class="badge badge-success ml-3">成功</span><span
                    class="badge badge-warning ml-1">失败</span><span class="badge badge-danger ml-1">服务器错误</span>
        </div>
        <div class="card-body">
            <p id="result" class="card-text" style='white-space: pre-line; word-break:break-all; word-wrap:break-all;'>show result in this content.</p>
        </div>
    </div>
</div>


<script src="${projectDomain}/static/js/jquery.js"></script>
<script src="${projectDomain}/static/js/bootstrap.min.js"></script>
<script src="${projectDomain}/static/js/bootstrap.bundle.min.js"></script>
<script>
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
        $("#escape-button,#unescape-button,#run-button").click(function () {
            var base_badge_css = "text-left card-text badge ";
            var operationType = this.id.replace('-button', '');
            var data = $("#input-data").val();
            var expression = $("#input-expression").val();
            if (expression == '') {
                $("#result").attr("class", base_badge_css + "badge-warning");
                $("#result").text("请输入表达式");
            } else {
                $.ajax({
                    type: "post",
                    url: "${projectDomain}/tool/freemarker/getResult",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify({data: data, tpl: expression, operationType: operationType}),
                    success: function (result) {
                        if (result.code == 200) {
                            $("#result").attr("class", base_badge_css + "badge-success");
                            $("#result").text(result.data.result);
                        } else if (result.msg != null) {
                            $("#result").attr("class", base_badge_css + "badge-warning");
                            $("#result").text(result.msg);
                        } else {
                            $("#result").attr("class", base_badge_css + "badge-danger");
                            $("#result").text("服务器异常");
                        }
                    },
                    error: function (xhr) {
                        if (xhr.status == 401) {
                            $("#result").attr("class", base_badge_css + "badge-warning");
                            $("#result").text("请登录");
                        } else if (('responseJSON' in xhr) && ('msg' in xhr.responseJSON)) {
                            $("#result").attr("class", base_badge_css + "badge-warning");
                            $("#result").text(xhr.responseJSON.msg);
                        } else {
                            $("#result").attr("class", base_badge_css + "badge-danger");
                            $("#result").text("服务器异常");
                        }
                    }
                });
            }
        });
        $("#insert-func-button").click(function () {
            selFuncName = $("#select-func-name").val();
            if (selFuncName != "") {
                $("#input-expression").val($("#input-expression").val() + selFuncName);
            }
        });
    });
</script>
</body>
</html>