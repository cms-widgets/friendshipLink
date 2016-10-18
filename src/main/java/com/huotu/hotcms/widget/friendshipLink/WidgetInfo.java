/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.friendshipLink;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.common.LinkType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.model.CategoryAndContent;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.ContentService;
import com.huotu.hotcms.service.service.LinkService;
import com.huotu.hotcms.widget.*;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author CJ
 */
public class WidgetInfo implements Widget, PreProcessWidget {
    private static final Log log = LogFactory.getLog(WidgetInfo.class);
    private static final String LINK_CATEGORY_SERIAL = "linkCategorySerial";
    private static final String DATA_LIST = "dataList";

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
        if (locale.equals(Locale.CHINA)) {
            return "友情链接";
        }
        return "Friendship link";
    }

    @Override
    public String description(Locale locale) {
        if (locale.equals(Locale.CHINA)) {
            return "这是一个友情链接，你可以对组件进行自定义修改。";
        }
        return "This is a Friendship link,  you can make custom change the component.";
    }

    @Override
    public String dependVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public Map<String, Resource> publicResources() {
        Map<String, Resource> map = new HashMap<>();
        map.put("thumbnail/friendshipLink1Style.png", new ClassPathResource("thumbnail/friendshipLink1Style.png"
                , getClass().getClassLoader()));
        return map;
    }

    @Override
    public Resource widgetDependencyContent(MediaType mediaType) {
        if (mediaType.isCompatibleWith(Javascript)) {
            return new ClassPathResource("js/friendshipLink.js", getClass().getClassLoader());
        }
        return null;
    }


    @Override
    public WidgetStyle[] styles() {
        return new WidgetStyle[]{new LinkWidgetStyle()};
    }

    @Override
    public void valid(String styleId, ComponentProperties componentProperties) throws IllegalArgumentException {
        WidgetStyle style = WidgetStyle.styleByID(this, styleId);
        String linkCategorySerial = (String) componentProperties.get(LINK_CATEGORY_SERIAL);
        if (linkCategorySerial == null || linkCategorySerial.equals("")) {
            throw new IllegalArgumentException("链接数据源不能为空");
        }
    }

    @Override
    public Class springConfigClass() {
        return null;
    }

    @Override
    public ComponentProperties defaultProperties(ResourceService resourceService) {
        ComponentProperties properties = new ComponentProperties();
        CategoryRepository categoryRepository = getCMSServiceFromCMSContext(CategoryRepository.class);
        List<Category> list = categoryRepository.findBySiteAndContentType(CMSContext.RequestContext().getSite(), ContentType.Link);
        if (list.isEmpty()) {
            Category category = initCategory(null, "链接数据源");
            Category category1 = initCategory(category, "子链接源1");
            Category category2 = initCategory(category, "子链接源2");
            initLink(category1);
            initLink(category2);
            properties.put(LINK_CATEGORY_SERIAL, category.getSerial());
        } else {
            properties.put(LINK_CATEGORY_SERIAL, list.get(0).getSerial());
        }
        return properties;
    }

    @Override
    public void prepareContext(WidgetStyle style, ComponentProperties properties, Map<String, Object> variables
            , Map<String, String> parameters) {
        String categorySerial = (String) properties.get(LINK_CATEGORY_SERIAL);
        LinkRepository linkRepository = getCMSServiceFromCMSContext(LinkRepository.class);
        CategoryRepository categoryRepository = getCMSServiceFromCMSContext(CategoryRepository.class);
        PageInfoRepository pageInfoRepository = getCMSServiceFromCMSContext(PageInfoRepository.class);
        List<Category> list = categoryRepository.findByParent_Serial(categorySerial);
        List<CategoryAndContent<Link>> dataList = new ArrayList<>();
        for (Category category : list) {
            List<Link> links = linkRepository.findByCategory(category);
            for (Link link : links) {
                if (link.getLinkType().isPage()) {
                    PageInfo pageInfo = pageInfoRepository.findOne(link.getPageInfoID());
                    link.setPagePath(pageInfo.getPagePath());
                }
            }
            CategoryAndContent<Link> categoryAndContent = new CategoryAndContent<>(category, links);
            dataList.add(categoryAndContent);
        }
        variables.put(DATA_LIST, dataList);
    }

    /**
     * 从CMSContext中获取CMSService的实现
     *
     * @param cmsService 需要返回的service接口
     * @param <T>        返回的service实现
     * @return
     */
    private <T> T getCMSServiceFromCMSContext(Class<T> cmsService) {
        return CMSContext.RequestContext().
                getWebApplicationContext().getBean(cmsService);
    }

    /**
     * 初始化数据源
     *
     * @param parent
     * @param name
     * @return
     */
    private Category initCategory(Category parent, String name) {
        CategoryService categoryService = getCMSServiceFromCMSContext(CategoryService.class);
        CategoryRepository categoryRepository = getCMSServiceFromCMSContext(CategoryRepository.class);
        Category category = new Category();
        category.setContentType(ContentType.Link);
        category.setName(name);
        categoryService.init(category);
        category.setSite(CMSContext.RequestContext().getSite());
        category.setParent(parent);
        //保存到数据库
        categoryRepository.save(category);
        return category;
    }

    /**
     * 初始化一个图片
     *
     * @param category
     */
    private Link initLink(Category category) {
        ContentService contentService = getCMSServiceFromCMSContext(ContentService.class);
        LinkService linkService = getCMSServiceFromCMSContext(LinkService.class);
        Link link = new Link();
        link.setTitle("link");
        link.setCategory(category);
        link.setDeleted(false);
        link.setCreateTime(LocalDateTime.now());
        link.setLinkUrl("http://www.huobanplus.com/");
        contentService.init(link);
        link.setLinkType(LinkType.Link);
        linkService.saveLink(link);
        return link;
    }
}