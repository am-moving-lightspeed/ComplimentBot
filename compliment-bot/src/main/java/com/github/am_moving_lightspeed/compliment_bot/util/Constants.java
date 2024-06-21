package com.github.am_moving_lightspeed.compliment_bot.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    @UtilityClass
    public static class Errors {

        public static final String FAILED_TO_FETCH_CONTENT = "failed.to.fetch.content";
        public static final String FAILED_TO_READ_FROM_STORAGE = "failed.to.read.from.storage";
        public static final String FAILED_TO_FLUSH_TO_STORAGE = "failed.to.flush.to.storage";
        public static final String FAILED_TO_HANDLE_USER_REQUEST = "failed.to.handle.user.request";
    }

    @UtilityClass
    public static class Messages {

        public static final String BOT_START_COMMAND_RESPONSE = "bot.command.start.response";
        public static final String BOT_HELP_COMMAND_RESPONSE = "bot.command.help.response";
        public static final String BOT_UNSUBSCRIBE_COMMAND_RESPONSE = "bot.command.unsubscribe.response";
    }

    @UtilityClass
    public static class Integration {

        @UtilityClass
        public static class RomanticCollection {

            public static final String CONTENT_CONTAINER_CLASS_NAME = "entry-content";
            public static final String CONTENT_LIST_TAG = "ol";
            public static final String CONTENT_ELEMENT_TAG = "li";
        }

        @UtilityClass
        public static class Infoniac {

            public static final String CONTENT_ELEMENT_TAG = "p";
            public static final String CONTENT_REGEX = "\\d+\\.\\s*([А-яа-я\\s.,—\\-]*)";
        }
    }

    @UtilityClass
    public static class Executors {

        public static final String INTEGRATION_CLIENT_EXECUTOR = "integration-client";
    }

    @UtilityClass
    public static class BotCommands {

        public static final String START = "/start";
        public static final String HELP = "/help";
        public static final String UNSUBSCRIBE = "/unsubscribe";
    }

    @UtilityClass
    public static class Encodings {

        public static final String UTF_8 = "UTF-8";
    }
}
