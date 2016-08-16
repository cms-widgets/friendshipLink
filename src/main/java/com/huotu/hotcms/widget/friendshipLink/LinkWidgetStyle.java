package com.huotu.hotcms.widget.friendshipLink;

import com.huotu.hotcms.widget.WidgetStyle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Locale;

/**
 * Created by lhx on 2016/6/22.
 */

public class LinkWidgetStyle implements WidgetStyle {
    @Override
    public String id() {
        return "friendshipLink1";
    }

    @Override
    public String name() {
        return "bootstrap 风格";
    }

    @Override
    public String name(Locale locale) {
        if (locale.equals(Locale.CHINESE)) {
            return "bootstrap 风格";
        }
        return "bootstrap style";
    }

    @Override
    public String description() {
        return "基于bootstrap样式的友情链接";
    }

    @Override
    public String description(Locale locale) {
        if (locale.equals(Locale.CHINESE)) {
            return "基于bootstrap样式的友情链接";
        }
        return "Based on the bootstrap style by Friendship link";
    }

    @Override
    public Resource thumbnail() {
        return new ClassPathResource("/thumbnail/friendshipLink1Style.png", getClass().getClassLoader());
    }

    @Override
    public Resource previewTemplate() {
        return null;
    }

    @Override
    public Resource browseTemplate() {
        return new ClassPathResource("/template/friendshipLink1BrowseTemplate.html", getClass().getClassLoader());
    }
}
