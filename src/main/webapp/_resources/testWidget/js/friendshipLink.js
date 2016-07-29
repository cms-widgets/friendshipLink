/**
 * Created by admin on 2016/6/23.
 */
var friendshipLink = {
    properties:null,
    addLink: function () {
        $(".addLink").click(function () {
            $(".linkbox").append(
                '<div class="row">'
                + '<div class="col-sm-3">'
                + '<input type="text" class="form-control linkTitle" value="." placeholder="链接名称">'
                + '</div>'
                + '<div class="col-sm-6">'
                + '<input type="url" class="form-control linkUrl" value="." placeholder="链接地址">'
                + '</div>'
                + '<div class="col-sm-3">'
                + '<input type="button" class="btn btn-danger removerLinkItem" value="删除">'
                + '</div>'
                + '</div>'
            );
        });
    },
    removerLinkItem: function () {
        var me = this;
        $(".linkbox").on("click", ".removerLinkItem", function () {
            var itemObj = $(this).parent().parent();
            var title;
            var url;
            $.each($(itemObj).find(".linkTitle"), function (i, v) {
                title = $(v).val();
            })
            $.each($(itemObj).find(".linkUrl"), function (i, v) {
                url = $(v).val();
            })

            $.grep(me.properties['linkList'], function (obj, i) {
                if (obj!= '' && obj.title == title && obj.url == url) {
                    me.properties['linkList'].splice(i, 1);
                    itemObj.remove();
                    return;
                }
            });
        });
    },
    saveComponent: function () {
        var me = this;
        me.properties['linkList']=[]
        $.each($(".linkbox").find(".row"), function (i, row) {
            var title;
            var url;
            $.each($(row).find(".linkTitle"), function (i, v) {
                title = $(v).val();
            });
            $.each($(row).find(".linkUrl"), function (i, v) {
                url = $(v).val();
            });
            var item = {
                title: title
                , url: url
            }
            me.properties['linkList'].push(item);
        });
        if(me.properties['linkList'].length==0){
            console.error('[FRIEND_LINK] get bad linklist');
            layer.msg("组件数据缺少,未能保存,请完善。");
            return null;
        }

        return me.properties;
    },
    initProperties:function(){
        this.properties['linkList'] = [];
    },
    init: function (globalId) {
        this.properties = widgetProperties(globalId);
        this.initProperties();
        this.removerLinkItem();
        this.addLink();
    }
};
