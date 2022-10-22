package com.xshaffter.mishiprotect.tools;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class BookComponentBuilder {

    private List<BookPage> pages;

    public BookComponentBuilder() {
        this.pages = new ArrayList<>();
    }
    private static void createTooltip(TextComponent button, String text) {
        if (text != null) {
            button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(text)));
        }
    }

    private TextComponent prepareText(String text) {
        TextComponent component;
        component = new TextComponent("\n\n" + text);
        return component;
    }

    public TextComponent createButton(String text, String clickCommand, String hoverText) {

        TextComponent button = prepareText(text);
        button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickCommand));
        createTooltip(button, hoverText);
        return button;
    }

    public TextComponent createButton(String text, ClickEvent clickCommand, String hoverText) {

        TextComponent button = prepareText(text);
        button.setClickEvent(clickCommand);
        createTooltip(button, hoverText);
        return button;
    }

    public void createPage(TextComponent ... components) {
        this.pages.add(new BookPage(components));


    }

    public List<BaseComponent[]> compile() {
        return this.pages.stream().map((it) -> {
            return it.components.toArray(new BaseComponent[0]);
        }).collect(Collectors.toList());
    }


}
