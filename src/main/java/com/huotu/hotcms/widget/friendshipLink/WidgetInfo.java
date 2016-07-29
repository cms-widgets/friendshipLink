/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.friendshipLink;

import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.http.entity.ContentType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author CJ
 */
public class WidgetInfo implements Widget{
    public static final String VALID_LINK_LIST = "linkList";
    /*
     * 指定风格的模板类型 如：html,text等
     */
    public static final String VALID_STYLE_TEMPLATE = "styleTemplate";

    @Override
    public String groupId() {
        return "com.huotu.hotcms.widget.friendshipLink";
    }

    @Override
    public String widgetId() {
        return "friendshipLink";
    }

    @Override
    public String name(Locale locale) {
        if (locale.equals(Locale.CHINESE)) {
            return "友情链接";
        }
        return "Friendship link";
    }

    @Override
    public String description() {
        return "这是一个友情链接，你可以对组件进行自定义修改。";
    }

    @Override
    public String description(Locale locale) {
        if (locale.equals(Locale.CHINESE)) {
            return "这是一个友情链接，你可以对组件进行自定义修改。";
        }
        return "This is a Friendship link,  you can make custom change the component.";
    }

    @Override
    public int dependBuild() {
        return 0;
    }

    @Override
    public Map<String, Resource> publicResources() {
        Map<String, Resource> map = new HashMap<>();
        map.put("thumbnail/friendshipLink1Style.png",new ClassPathResource("thumbnail/friendshipLink1Style.png"
                ,getClass().getClassLoader()));
        return map;
    }

    @Override
    public Resource widgetDependencyContent(ContentType contentType) {
        return null;
    }

    @Override
    public Resource widgetJs() {
        return new ClassPathResource("js/friendshipLink.js",getClass().getClassLoader());
    }

    @Override
    public WidgetStyle[] styles() {
        return new WidgetStyle[]{new LinkWidgetStyle()};
    }

    @Override
    public void valid(String styleId, ComponentProperties componentProperties) throws IllegalArgumentException {
        WidgetStyle[] widgetStyles = styles();
        boolean flag = false;
        if (widgetStyles == null || widgetStyles.length < 1) {
            throw new IllegalArgumentException();
        }
        for (WidgetStyle ws : widgetStyles) {
            if ((flag = ws.id().equals(styleId))) {
                break;
            }
        }
        if (!flag) {
            throw new IllegalArgumentException();
        }
        List<LinkItem> linkList = (List<LinkItem>) componentProperties.get(VALID_LINK_LIST);
        String styleTemplate = (String) componentProperties.get(VALID_STYLE_TEMPLATE);
        if (linkList==null ||linkList.size()<=0 || styleTemplate==null || !"html".equals(styleTemplate)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Class springConfigClass() {
        return null;
    }

    @Override
    public ComponentProperties defaultProperties(ResourceService resourceService) {
        ComponentProperties properties = new ComponentProperties();
        List<LinkItem> linkItems = new ArrayList<>();
        LinkItem item1 = new LinkItem();
        item1.setTitle("火图科技");
        item1.setUrl("http://www.huobanplus.com");

        linkItems.add(item1);
        linkItems.add(item1);
        linkItems.add(item1);
        linkItems.add(item1);
        linkItems.add(item1);

        properties.put(VALID_LINK_LIST,item1);
        return properties;
    }

}