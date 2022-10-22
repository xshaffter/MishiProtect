package com.xshaffter.mishiprotect.tools;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookPage {
    private String componentSeparator = "\n\n";

    public List<BaseComponent> components;

    public BookPage(BaseComponent[] components) {
        this.components = Arrays.asList(components);
        TextComponent firstComponent = (TextComponent) this.components.get(0);
        firstComponent.setText(firstComponent.getText().replace(componentSeparator, ""));
    }

    public BookPage() {
        this.components = new ArrayList<>();
    }
}
