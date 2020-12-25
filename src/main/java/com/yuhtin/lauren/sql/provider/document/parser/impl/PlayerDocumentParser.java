package com.yuhtin.lauren.sql.provider.document.parser.impl;

import com.yuhtin.lauren.core.player.Player;
import com.yuhtin.lauren.sql.provider.document.Document;
import com.yuhtin.lauren.sql.provider.document.parser.DocumentParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerDocumentParser implements DocumentParser<Player> {

    @Getter private static final PlayerDocumentParser instance = new PlayerDocumentParser();

    @Override
    public Player parse(Document document) {

        if (document.isEmpty()) return null;
        return null;

    }

}
