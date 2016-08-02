/**
 * 友情链接
 * 添加可配置的target 默认为_blank by CJ
 * Created by Li Huaixin on 2016/6/23.
 */
CMSWidgets.initWidget({
// 编辑器相关
    editor: {
        properties: null,
        addLink: function () {
            $(".linkbox").on("click", ".addLink", function () {
                var htmlElement = $(".LinkRowHtml").clone();
                htmlElement.attr("display", "block");
                $(".linkbox").append(htmlElement.html());
            });
        },
        removerLinkItem: function () {
            var me = this;
            $(".linkbox").on("click", ".removerLinkItem", function () {
                var itemObj = $(this).parent().parent();
                var title;
                var url;
                var target;
                $.each($(itemObj).find(".linkTitle"), function (i, v) {
                    title = $(v).val();
                });
                $.each($(itemObj).find(".linkUrl"), function (i, v) {
                    url = $(v).val();
                });
                $.each($(itemObj).find(".LinkTarget"), function (i, v) {
                    target = $(v).val();
                });
                itemObj.remove();
                $.grep(me.properties['linkList'], function (obj, i) {
                    if (obj != '' && obj.title == title && obj.url == url && target == obj.target) {
                        me.properties['linkList'].splice(i, 1);
                        return;
                    }
                });
            });
        },
        saveComponent: function (onSuccess, onFailed) {
            var me = this;
            me.properties['linkList'] = [];
            $.each($(".linkbox").find(".row"), function (i, row) {
                var title = 'notitle';
                var url = '#';
                var target = '_blank';
                $.each($(row).find(".linkTitle"), function (i, v) {
                    title = $(v).val();
                });
                $.each($(row).find(".linkUrl"), function (i, v) {
                    url = $(v).val();
                });
                $.each($(row).find(".LinkTarget"), function (i, v) {
                    target = $(v).val();
                });
                var item = {
                    title: title
                    , url: url
                    , target: target
                };
                me.properties['linkList'].push(item);
            });
            if (me.properties['linkList'].length == 0) {
                onFailed("组件数据缺少,未能保存,请完善。");
                return;
            }
            onSuccess(this.properties);
            return this.properties;
        },
        initProperties: function () {
            this.properties['linkList'] = [];
        },
        open: function (globalId) {
            this.properties = widgetProperties(globalId);
            this.initProperties();
            this.removerLinkItem();
            this.addLink();
        },
        close: function (globalId) {
            $('.linkbox').off("click", ".addLink");
            $('.linkbox').off("click", ".removerLinkItem");
        }
    }
});
