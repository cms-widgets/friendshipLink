package com.huotu.hotcms.widget.friendshipLink;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.model.CategoryAndContent;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import com.huotu.widget.test.Editor;
import com.huotu.widget.test.WidgetTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class TestWidgetInfo extends WidgetTest {
    @Override
    protected boolean printPageSource() {
        return true;
    }

    @Override
    protected void editorWork(Widget widget, Editor editor, Supplier<Map<String, Object>> currentWidgetProperties) throws IOException {
        if (widget instanceof WidgetInfo) {
            WidgetInfo widgetInfo = (WidgetInfo) widget;
            Category category = widgetInfo.initCategory(null, "");
            editor.chooseCategory(WidgetInfo.LINK_CATEGORY_SERIAL, category);
            Map<String, Object> map = currentWidgetProperties.get();
            assertThat(category.getSerial()).isEqualTo(map.get(WidgetInfo.LINK_CATEGORY_SERIAL));
        }
    }

    @Override
    protected void browseWork(Widget widget, WidgetStyle style, Function<ComponentProperties, WebElement> uiChanger)
            throws IOException {
        ComponentProperties properties = widget.defaultProperties(resourceService);
        WebElement webElement = uiChanger.apply(properties);
        String categorySerial = (String) properties.get(WidgetInfo.LINK_CATEGORY_SERIAL);
        LinkRepository linkRepository = widget.getCMSServiceFromCMSContext(LinkRepository.class);
        CategoryRepository categoryRepository = widget.getCMSServiceFromCMSContext(CategoryRepository.class);
        PageInfoRepository pageInfoRepository = widget.getCMSServiceFromCMSContext(PageInfoRepository.class);
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
        assertThat(webElement.findElements(By.tagName("dt")).size()).isEqualTo(dataList.size());
    }

    @Override
    protected void editorBrowseWork(Widget widget, Function<ComponentProperties, WebElement> uiChanger
            , Supplier<Map<String, Object>> currentWidgetProperties) throws IOException {

    }


}
